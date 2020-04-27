package com.angel.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.angel.common.utils.PageUtils;
import com.angel.product.entity.SkuInfoEntity;

import java.util.Map;

/**
 * sku信息
 *
 * @author archangel
 * @email sunlightcs@gmail.com
 * @date 2020-04-16 16:28:59
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageByConditon(Map<String, Object> params);
}

