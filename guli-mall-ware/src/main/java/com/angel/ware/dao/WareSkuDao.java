package com.angel.ware.dao;

import com.angel.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * ??Ʒ???
 * 
 * @author archangel
 * @email sunlightcs@gmail.com
 * @date 2020-04-13 17:04:35
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    void addStock(@Param("wareId") Long wareId, @Param("skuId") Long skuId, @Param("skuNum") Integer skuNum);
}
