package com.angel.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.angel.common.utils.PageUtils;
import com.angel.coupon.entity.CouponSpuCategoryRelationEntity;

import java.util.Map;

/**
 * 优惠券分类关联
 *
 * @author archangel
 * @email sunlightcs@gmail.com
 * @date 2020-04-18 21:51:53
 */
public interface CouponSpuCategoryRelationService extends IService<CouponSpuCategoryRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

