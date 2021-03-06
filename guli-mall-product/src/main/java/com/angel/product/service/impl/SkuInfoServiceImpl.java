package com.angel.product.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.angel.common.utils.PageUtils;
import com.angel.common.utils.Query;

import com.angel.product.dao.SkuInfoDao;
import com.angel.product.entity.SkuInfoEntity;
import com.angel.product.service.SkuInfoService;


@Slf4j
@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByConditon(Map<String, Object> params) {

        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.and(query -> {
                query.eq("sku_id",key).or().like("sku_name",key);
            });
        }

        String catelogId = (String) params.get("catelogId");
        if (StringUtils.isNotBlank(catelogId) && !catelogId.equalsIgnoreCase("0")) {
            queryWrapper.eq("catalog_id",catelogId);
        }

        String brandId = (String) params.get("brandId");
        if (StringUtils.isNotBlank(brandId) && !brandId.equalsIgnoreCase("0")) {
            queryWrapper.eq("brand_id",brandId);
        }

        String max = (String) params.get("max");
        if (StringUtils.isNotBlank(max)) {
            try {
                BigDecimal maxBigDecimal = new BigDecimal(max);
                if (maxBigDecimal.compareTo(new BigDecimal(0L)) > 0) {
                    queryWrapper.le("price",max);
                }
            } catch (Exception e) {
                log.error("客户端传递的最大价格数据有误");
                e.printStackTrace();
            }
        }

        String min = (String) params.get("min");
        if (StringUtils.isNotBlank(min)) {
            try {
                BigDecimal minBigDecimal = new BigDecimal(min);
                if (minBigDecimal.compareTo(new BigDecimal(0L)) >= 0) {
                    queryWrapper.ge("price",min);
                }
            } catch (Exception e) {
                log.error("客户端传递的最小价格数据有误");
                e.printStackTrace();
            }
        }

        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

}