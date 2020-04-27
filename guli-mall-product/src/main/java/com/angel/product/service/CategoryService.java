package com.angel.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.angel.common.utils.PageUtils;
import com.angel.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * ??Ʒ???????
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-04-13 15:39:43
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    void deleteByIds(Long[] catIds);

    List<Long> getCatelogPath(Long catelogId);
}

