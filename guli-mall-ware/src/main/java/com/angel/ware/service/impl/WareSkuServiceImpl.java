package com.angel.ware.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.angel.common.utils.PageUtils;
import com.angel.common.utils.Query;

import com.angel.ware.dao.WareSkuDao;
import com.angel.ware.entity.WareSkuEntity;
import com.angel.ware.service.WareSkuService;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    private WareSkuDao wareSkuDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();

        String wareId = (String) params.get("wareId");
        if (StringUtils.isNotBlank(wareId)) {
            queryWrapper.eq("ware_id",wareId);
        }

        String skuId = (String) params.get("skuId");
        if (StringUtils.isNotBlank(skuId)) {
            queryWrapper.eq("sku_id",skuId);
        }

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(Long wareId, Long skuId, Integer skuNum) {
        wareSkuDao.addStock(wareId,skuId,skuNum);
    }

}