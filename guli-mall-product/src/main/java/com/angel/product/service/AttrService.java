package com.angel.product.service;

import com.angel.product.vo.AttrVo;
import com.angel.product.vo.RespAttrVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.angel.common.utils.PageUtils;
import com.angel.product.entity.AttrEntity;

import java.util.Map;

/**
 * 商品属性
 *
 * @author archangel
 * @email sunlightcs@gmail.com
 * @date 2020-04-14 22:19:28
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Long catelogId,String attrType);

    RespAttrVo getRespVo(AttrEntity attr);

    void cascadeUpdate(AttrVo attr);

    PageUtils getNoRelationList(Long attrgroupId, Map<String, Object> params);
}

