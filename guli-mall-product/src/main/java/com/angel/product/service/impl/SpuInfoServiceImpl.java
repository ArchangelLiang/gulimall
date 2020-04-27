package com.angel.product.service.impl;

import com.angel.common.to.SkuReductionTo;
import com.angel.common.to.SpuBoundsTo;
import com.angel.common.utils.R;
import com.angel.product.entity.*;
import com.angel.product.feign.CouponFeignService;
import com.angel.product.service.*;
import com.angel.product.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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

import com.angel.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoDescService spuInfoDescService;

    @Autowired
    private SpuImagesService spuImagesService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private CouponFeignService couponFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    //有条件的查询商品
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {

        QueryWrapper<SpuInfoEntity> queryWrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.and(query -> {
                query.eq("id",key).or().like("spu_name",key).or()
                        .like("spu_description",key);
            });
        }

        String catelogId = (String) params.get("catelogId");
        if (StringUtils.isNotBlank(catelogId) && !catelogId.trim().equalsIgnoreCase("0")) {
            queryWrapper.eq("catalog_id",catelogId);
        }

        String brandId = (String) params.get("brandId");
        if (StringUtils.isNotBlank(brandId) && !brandId.trim().equalsIgnoreCase("0")) {
            queryWrapper.eq("brand_id",brandId);
        }

        String status = (String) params.get("status");
        if (StringUtils.isNotBlank(status)) {
            queryWrapper.eq("publish_status",status);
        }

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    /**
     * 保存发布的商品信息
     *
     * @param spuSaveVo 封装页面传递过来的Spu以及所有Sku信息
     */
    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo spuSaveVo) {
        //保存Spu基本信息，对应的表：pms_sku_info
        Long spuId = saveBaseSpuInfo(spuSaveVo);

        //保存Spu的图片描述信息，对应的表：pms_spu_info_desc
        saveSpuImgDesc(spuId, spuSaveVo);

        //保存Spu图集，对应的表：pms_sku_images
        saveSpuImages(spuId, spuSaveVo);

        //保存Spu的规格参数，对应的表：pms_product_attr_value
        saveSpuBaseAttr(spuId, spuSaveVo);

        //通过feign远程保存Spu的积分信息：对应的库：gulimall_sms，积分表：sms_spu_bounds
        saveSpuBounds(spuId, spuSaveVo);

        //保存当前Spu对应的所有Sku信息，
        /*
         *  保存Sku信息，对应的表：pms_sku_info
         *  保存Sku的图片信息，对应的表：pms_sku_images,
         *  保存Sku的销售属性信息，对应的表：pms_sku_sale_attr_value
         * */
        saveSkuBaseInfo(spuId, spuSaveVo);
    }

    //通过feign远程保存Spu的积分信息
    private void saveSpuBounds(Long spuId, SpuSaveVo spuSaveVo) {
        Bounds bounds = spuSaveVo.getBounds();
        SpuBoundsTo spuBoundsTo = new SpuBoundsTo();
        BeanUtils.copyProperties(bounds, spuBoundsTo);
        spuBoundsTo.setSpuId(spuId);
        R r = couponFeignService.saveSpuBounds(spuBoundsTo);
        Integer code = r.getCode();
        if (code != 0) {
            log.error("远程保存spu积分信息失败");
        }
    }

    //保存Spu的基本信息
    private void saveSkuBaseInfo(Long spuId, SpuSaveVo spuSaveVo) {
        List<Skus> skus = spuSaveVo.getSkus();
        if (skus != null && skus.size() > 0) {
            skus.forEach(sku -> {
                //保存sku基本信息
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                skuInfoEntity.setSpuId(spuId);
                skuInfoEntity.setSaleCount(0L);
                sku.getImages().forEach(images -> {
                    if (images.getDefaultImg() == 1) {
                        skuInfoEntity.setSkuDefaultImg(images.getImgUrl());
                    }
                });
                skuInfoEntity.setSkuTitle(sku.getSkuTitle());
                skuInfoEntity.setSkuSubtitle(sku.getSkuSubtitle());
                skuInfoEntity.setBrandId(spuSaveVo.getBrandId());
                skuInfoEntity.setCatalogId(spuSaveVo.getCatalogId());
                skuInfoEntity.setPrice(sku.getPrice());

                skuInfoService.save(skuInfoEntity);
                Long skuId = skuInfoEntity.getSkuId();
                //保存sku图片信息
                saveSkuImages(skuId, sku);
                //保存sku的销售属性信息
                saveSkuSaleAttr(skuId, sku);
                //保存Sku的优惠满减信息，对应的库：gulimall_sms,对应的表：打折表：sms_sku_ladder、满减表：sms_sku_full_reduction、会员价格表：sms_member_price
                saveSkuReduction(skuId, sku);
            });
        }
    }

    //通过feign远程保存Sku的优惠满减信息
    private void saveSkuReduction(Long skuId, Skus sku) {
        SkuReductionTo skuReductionTo = new SkuReductionTo();
        BeanUtils.copyProperties(sku, skuReductionTo);
        skuReductionTo.setSkuId(skuId);
        R r = couponFeignService.saveSkuReduction(skuReductionTo);
        Integer code = r.getCode();
        if (code != 0) {
            log.error("远程保存sku优惠信息失败");
        }
    }

    //保存sku的销售属性信息
    private void saveSkuSaleAttr(Long skuId, Skus sku) {
        List<Attr> attrs = sku.getAttr();
        List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attrs.stream().map(attr -> {
            SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
            skuSaleAttrValueEntity.setAttrId(attr.getAttrId());
            skuSaleAttrValueEntity.setAttrName(attr.getAttrName());
            skuSaleAttrValueEntity.setAttrValue(attr.getAttrValue());
            skuSaleAttrValueEntity.setSkuId(skuId);
            return skuSaleAttrValueEntity;
        }).collect(Collectors.toList());

        skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);
    }

    //保存sku的图片信息
    private void saveSkuImages(Long skuId, Skus sku) {
        List<SkuImagesEntity> skuImagesEntities = sku.getImages().stream().filter(images ->
                StringUtils.isNotBlank(images.getImgUrl())).map(images -> {
            SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
            skuImagesEntity.setImgUrl(images.getImgUrl());
            skuImagesEntity.setSkuId(skuId);
            skuImagesEntity.setDefaultImg(images.getDefaultImg());
            return skuImagesEntity;
        }).collect(Collectors.toList());

        skuImagesService.saveBatch(skuImagesEntities);
    }

    //保存Spu的规格参数
    private void saveSpuBaseAttr(Long spuId, SpuSaveVo spuSaveVo) {
        List<BaseAttrs> baseAttrs = spuSaveVo.getBaseAttrs();
        if (baseAttrs != null && baseAttrs.size() > 0) {
            List<ProductAttrValueEntity> productAttrValueEntities = baseAttrs.stream().map(baseAttr -> {
                ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
                productAttrValueEntity.setAttrId(baseAttr.getAttrId());

                AttrEntity attr = attrService.getById(baseAttr.getAttrId());
                String attrName = attr.getAttrName();
                productAttrValueEntity.setAttrName(attrName);
                productAttrValueEntity.setAttrValue(baseAttr.getAttrValues());
                productAttrValueEntity.setQuickShow(baseAttr.getShowDesc());
                productAttrValueEntity.setSpuId(spuId);
                return productAttrValueEntity;
            }).collect(Collectors.toList());

            productAttrValueService.saveBatch(productAttrValueEntities);
        }
    }

    //保存Spu的图片集
    private void saveSpuImages(Long spuId, SpuSaveVo spuSaveVo) {
        List<String> images = spuSaveVo.getImages();
        if (images != null && images.size() > 0) {
            List<SpuImagesEntity> imagesEntities = images.stream().map(img -> {
                SpuImagesEntity imagesEntity = new SpuImagesEntity();
                imagesEntity.setImgUrl(img);
                imagesEntity.setSpuId(spuId);
                return imagesEntity;
            }).collect(Collectors.toList());

            spuImagesService.saveBatch(imagesEntities);
        }
    }

    //保存Spu的描述图片
    private void saveSpuImgDesc(Long spuId, SpuSaveVo spuSaveVo) {
        List<String> spuImgDescriptions = spuSaveVo.getDecript();
        if (spuImgDescriptions != null && spuImgDescriptions.size() > 0) {
            SpuInfoDescEntity spuImgDescEntity = new SpuInfoDescEntity();
            //将集合中的所有用来描述Spu的图片Url以逗号拼接起来
            String ImgUrlDescriptions = String.join(",", spuImgDescriptions);
            spuImgDescEntity.setDecript(ImgUrlDescriptions);
            spuImgDescEntity.setSpuId(spuId);

            spuInfoDescService.save(spuImgDescEntity);
        }
    }

    //保存Spu的基本信息
    private Long saveBaseSpuInfo(SpuSaveVo spuSaveVo) {
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVo, spuInfoEntity);
        Date date = new Date();
        spuInfoEntity.setCreateTime(date);
        spuInfoEntity.setUpdateTime(date);
        baseMapper.insert(spuInfoEntity);
        return spuInfoEntity.getId();
    }
}