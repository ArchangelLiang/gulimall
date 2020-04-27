package com.angel.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.angel.product.service.CategoryBrandRelationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.angel.product.entity.CategoryEntity;
import com.angel.product.service.CategoryService;
import com.angel.common.utils.PageUtils;
import com.angel.common.utils.R;



/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-04-13 15:39:43
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     *  分类列表
     * @return 组装好父子分类的集合
     */
    @GetMapping("/list/tree")
    public R listTree(){
        List<CategoryEntity> entities = this.categoryService.listWithTree();
        return R.ok().put("data",entities);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("data", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return R.ok();
    }

    @PostMapping("/batchUpdate")
    public R batchUpdate(@RequestBody List<CategoryEntity> categoryEntities){
        categoryService.updateBatchById(categoryEntities);
        return R.ok();
    }

    /**
     * 修改
     */
    @Transactional
    @RequestMapping("/update")
    public R update(@RequestBody CategoryEntity category){
		categoryService.updateById(category);

        String name = category.getName();
        if (StringUtils.isNotBlank(name)) {
            // 更新分类与品牌的关联表中的分类名称
            categoryBrandRelationService.updateCategoryName(category.getCatId(),name);
        }

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] catIds){
//		categoryService.removeByIds(Arrays.asList(catIds));

        categoryService.deleteByIds(catIds);

        return R.ok();
    }

}
