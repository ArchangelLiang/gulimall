package com.angel.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.angel.common.utils.PageUtils;
import com.angel.ware.entity.WareSkuEntity;

import java.util.Map;

/**
 * ??Ʒ???
 *
 * @author archangel
 * @email sunlightcs@gmail.com
 * @date 2020-04-13 17:04:35
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long wareId, Long skuId, Integer skuNum);
}

