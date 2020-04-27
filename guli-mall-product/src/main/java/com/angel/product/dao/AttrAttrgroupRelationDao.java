package com.angel.product.dao;

import com.angel.product.entity.AttrAttrgroupRelationEntity;
import com.angel.product.vo.RelationVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author archangel
 * @email sunlightcs@gmail.com
 * @date 2020-04-16 16:28:59
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    void deleteBathRelation(@Param("relationVOS") List<RelationVO> relationVOS);
}
