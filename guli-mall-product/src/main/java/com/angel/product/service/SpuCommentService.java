package com.angel.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.angel.common.utils.PageUtils;
import com.angel.product.entity.SpuCommentEntity;

import java.util.Map;

/**
 * 商品评价
 *
 * @author archangel
 * @email sunlightcs@gmail.com
 * @date 2020-04-16 16:28:59
 */
public interface SpuCommentService extends IService<SpuCommentEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

