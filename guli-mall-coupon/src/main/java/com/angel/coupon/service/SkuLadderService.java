package com.angel.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.angel.common.utils.PageUtils;
import com.angel.coupon.entity.SkuLadderEntity;

import java.util.Map;

/**
 * 商品阶梯价格
 *
 * @author archangel
 * @email sunlightcs@gmail.com
 * @date 2020-04-18 21:51:53
 */
public interface SkuLadderService extends IService<SkuLadderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

