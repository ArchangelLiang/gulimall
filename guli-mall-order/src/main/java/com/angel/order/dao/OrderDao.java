package com.angel.order.dao;

import com.angel.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * ????
 * 
 * @author archangel
 * @email sunlightcs@gmail.com
 * @date 2020-04-13 16:57:57
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
