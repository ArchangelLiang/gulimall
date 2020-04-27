package com.angel.ware.service.impl;

import com.angel.common.constant.WareConstant;
import com.angel.ware.entity.PurchaseDetailEntity;
import com.angel.ware.service.PurchaseDetailService;
import com.angel.ware.service.WareSkuService;
import com.angel.ware.vo.PurchaseDetailVo;
import com.angel.ware.vo.PurchaseDoneVo;
import com.angel.ware.vo.PurchaseMergeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.angel.common.utils.PageUtils;
import com.angel.common.utils.Query;

import com.angel.ware.dao.PurchaseDao;
import com.angel.ware.entity.PurchaseEntity;
import com.angel.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Autowired
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryUnReceiveList(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status",0).or().eq("status",1)
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void merge(PurchaseMergeVo purchaseMergeVo) {
        Long purchaseId = purchaseMergeVo.getPurchaseId();
        Date date = new Date();
        if (purchaseId == null) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setCreateTime(date);
            purchaseEntity.setUpdateTime(date);
            purchaseEntity.setStatus(WareConstant.PurchaseStatus.CREATED.getCode());
            //临时设置的默认优先级
            purchaseEntity.setPriority(1);

            purchaseService.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        } else {
            PurchaseEntity purchaseEntity = this.purchaseService.getById(purchaseId);
            purchaseEntity.setUpdateTime(date);
            purchaseService.updateById(purchaseEntity);
        }

        List<Long> ids = purchaseMergeVo.getItems();

        //TODO 在合并之前应该先校验每一个采购需求的状态，只有处于新建已分配状态的才可以进行合并，否则就是已经合并过的

        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> purchaseDetailEntities = ids.stream().map(id -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(id);
            purchaseDetailEntity.setPurchaseId(finalPurchaseId);
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatus.ASSIGNED.getCode());
            return purchaseDetailEntity;
        }).collect(Collectors.toList());

        purchaseDetailService.updateBatchById(purchaseDetailEntities);
    }

    //领取采购单
    @Transactional
    @Override
    public void receive(List<Long> purchaseIds) {
        //改变所有采购单的状态为已领取
        List<PurchaseEntity> purchaseEntities = purchaseIds.stream().map(id -> purchaseService.getById(id)).filter(purchaseEntity -> {
            //过滤：只有采购单状态为新建的采购单或已分配的才可以领取
            return purchaseEntity.getStatus() == WareConstant.PurchaseStatus.CREATED.getCode() || purchaseEntity.getStatus() == WareConstant.PurchaseStatus.ASSIGNED.getCode();
        }).map(purchaseEntity -> {
            //因为是更新，只保留需要更新的字段
            PurchaseEntity pu = new PurchaseEntity();
            pu.setId(purchaseEntity.getId());
            pu.setUpdateTime(new Date());
            pu.setStatus(WareConstant.PurchaseStatus.RECEIVED.getCode());
            return pu;
        }).collect(Collectors.toList());
        // 更新 purchaseEntities
        purchaseService.updateBatchById(purchaseEntities);

        //改变采购单内的采购需求状态为采购中
        purchaseEntities.stream().map(purchaseEntity -> {
            Long id = purchaseEntity.getId();
            //根据采购订单的 id 获取采购需求集合
            List<PurchaseDetailEntity> detailEntities = purchaseDetailService.list(new QueryWrapper<PurchaseDetailEntity>().eq("purchase_id", id));
            //对采购需求集合进行过滤
            return detailEntities.stream().filter(detailEntity -> {
                Integer status = detailEntity.getStatus();
                //只保留采购需求状态为 新建 或 已领取的
                return status == WareConstant.PurchaseDetailStatus.CREATED.getCode() || status == WareConstant.PurchaseDetailStatus.ASSIGNED.getCode();
            }).map(detailEntity -> {
                //因为是更新，只保留需要更新的字段
                PurchaseDetailEntity entity = new PurchaseDetailEntity();
                entity.setId(detailEntity.getId());
                entity.setStatus(WareConstant.PurchaseDetailStatus.BUYING.getCode());

                return entity;
            }).collect(Collectors.toList());
        }).forEach(purchaseDetailEntities -> {
            //更新
            purchaseDetailService.updateBatchById(purchaseDetailEntities);
        });
    }

    @Transactional
    @Override
    public void done(PurchaseDoneVo purchaseDoneVo) {
        //拿到采购单 id
        Long id = purchaseDoneVo.getId();

        //根据采购单id获取采购单对象
        PurchaseEntity purchaseEntity = purchaseService.getById(id);

        //拿到所有的采购需求
        List<PurchaseDetailVo> items = purchaseDoneVo.getItems();
        List<PurchaseDetailEntity> purchaseDetailEntities = items.stream().map(item -> {
            //创建一个采购需求对象，更新时用
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            Long itemId = item.getItemId();
            purchaseDetailEntity.setId(itemId);
            Long status = item.getStatus();
            //一个标识位，如果为false那就证明本次采购有异常
            boolean flag = true;
            if (status == WareConstant.PurchaseDetailStatus.FINISH.getCode()) {
                //采购完成
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatus.FINISH.getCode());
            } else {
                //采购失败
                flag = false;
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatus.FAILED.getCode());
            }
            return purchaseDetailEntity;
        }).collect(Collectors.toList());

        //更新采购需求的状态
        purchaseDetailService.updateBatchById(purchaseDetailEntities);
        //更新采购单的状态
        purchaseEntity.setStatus(WareConstant.PurchaseStatus.ERROR.getCode());
        purchaseEntity.setUpdateTime(new Date());
        purchaseService.updateById(purchaseEntity);

        //获取所有采购需求的详情，更新sku的库存
        List<Long> ids = purchaseDetailEntities.stream().filter(purchaseDetail ->
                purchaseDetail.getStatus() == WareConstant.PurchaseDetailStatus.FINISH.getCode())
                .map(PurchaseDetailEntity::getId).collect(Collectors.toList());
        List<PurchaseDetailEntity> purchaseDetailList = purchaseDetailService.listByIds(ids);
        purchaseDetailList.forEach(purchaseDetail -> {
            Long skuId = purchaseDetail.getSkuId();
            Integer skuNum = purchaseDetail.getSkuNum();
            Long wareId = purchaseDetail.getWareId();
            //增加库存
            wareSkuService.addStock(wareId,skuId,skuNum);
        });
    }

}