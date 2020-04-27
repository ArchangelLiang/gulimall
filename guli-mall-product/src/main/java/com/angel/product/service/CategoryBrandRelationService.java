package com.angel.product.service;

import com.angel.product.entity.BrandEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.angel.common.utils.PageUtils;
import com.angel.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * Ʒ?Ʒ???????
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-04-13 15:39:43
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateCategoryName(Long catId, String name);

    void updateBrandName(Long brandId, String name);

    List<BrandEntity> getBrandsByCatId(Long catId);
}

