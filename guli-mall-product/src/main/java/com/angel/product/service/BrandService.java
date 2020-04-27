package com.angel.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.angel.common.utils.PageUtils;
import com.angel.product.entity.BrandEntity;

import java.util.Map;

/**
 * Ʒ?
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-04-13 15:39:43
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

