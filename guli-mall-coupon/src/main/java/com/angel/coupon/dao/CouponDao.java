package com.angel.coupon.dao;

import com.angel.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author archangel
 * @email sunlightcs@gmail.com
 * @date 2020-04-18 21:51:53
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
