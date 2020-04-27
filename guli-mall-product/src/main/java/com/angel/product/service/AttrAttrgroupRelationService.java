package com.angel.product.service;

import com.angel.product.vo.AttrGroupVo;
import com.angel.product.vo.RelationVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.angel.common.utils.PageUtils;
import com.angel.product.entity.AttrAttrgroupRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author archangel
 * @email sunlightcs@gmail.com
 * @date 2020-04-16 16:28:59
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void deleteBatchRelation(List<RelationVO> relationVOS);

    List<AttrGroupVo> getAttrGroupVosByCatId(Long catelogId);
}

