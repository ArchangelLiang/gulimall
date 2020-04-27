package com.angel.product.dao;

import com.angel.product.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品属性
 * 
 * @author archangel
 * @email sunlightcs@gmail.com
 * @date 2020-04-14 22:19:28
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {
	
}
