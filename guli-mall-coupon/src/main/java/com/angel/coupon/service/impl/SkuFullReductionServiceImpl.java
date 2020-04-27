package com.angel.coupon.service.impl;

import com.angel.common.to.MemberPrice;
import com.angel.common.to.SkuReductionTo;
import com.angel.coupon.entity.MemberPriceEntity;
import com.angel.coupon.entity.SkuLadderEntity;
import com.angel.coupon.service.MemberPriceService;
import com.angel.coupon.service.SkuLadderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.angel.common.utils.PageUtils;
import com.angel.common.utils.Query;

import com.angel.coupon.dao.SkuFullReductionDao;
import com.angel.coupon.entity.SkuFullReductionEntity;
import com.angel.coupon.service.SkuFullReductionService;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    private SkuLadderService skuLadderService;

    @Autowired
    private SkuFullReductionService skuFullReductionService;

    @Autowired
    private MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuReduction(SkuReductionTo skuReductionTo) {
        //保存阶梯价格信息，就是折扣信息
        if (skuReductionTo.getFullCount() > 0) {
            SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
            skuLadderEntity.setFullCount(skuReductionTo.getFullCount());
            skuLadderEntity.setDiscount(skuReductionTo.getDiscount());
            skuLadderEntity.setSkuId(skuReductionTo.getSkuId());
            skuLadderEntity.setAddOther(1);

            skuLadderService.save(skuLadderEntity);
        }

        //保存慢减信息
        if (skuReductionTo.getFullPrice().compareTo(new BigDecimal(0L)) > 0) {
            SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
            skuFullReductionEntity.setFullPrice(skuReductionTo.getFullPrice());
            skuFullReductionEntity.setReducePrice(skuReductionTo.getReducePrice());
            skuFullReductionEntity.setSkuId(skuReductionTo.getSkuId());
            skuFullReductionEntity.setAddOther(1);

            skuFullReductionService.save(skuFullReductionEntity);
        }

        //保存会员价格信息
        List<MemberPrice> memberPrices = skuReductionTo.getMemberPrice();
        if (memberPrices != null && memberPrices.size() > 0) {
            List<MemberPriceEntity> memberPriceEntities = memberPrices.stream().map(memberPrice -> {
                MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
                memberPriceEntity.setMemberLevelId(memberPrice.getId());
                memberPriceEntity.setMemberLevelName(memberPrice.getName());
                memberPriceEntity.setMemberPrice(memberPrice.getPrice());
                memberPriceEntity.setSkuId(skuReductionTo.getSkuId());
                memberPriceEntity.setAddOther(1);

                return memberPriceEntity;
            }).collect(Collectors.toList());

            memberPriceService.saveBatch(memberPriceEntities);
        }
    }

}