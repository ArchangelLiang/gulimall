package com.angel.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.angel.product.entity.BrandEntity;
import com.angel.product.entity.CategoryEntity;
import com.angel.product.service.BrandService;
import com.angel.product.service.CategoryService;
import com.angel.product.vo.BrandVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.omg.CORBA.FREE_MEM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.angel.product.entity.CategoryBrandRelationEntity;
import com.angel.product.service.CategoryBrandRelationService;
import com.angel.common.utils.PageUtils;
import com.angel.common.utils.R;



/**
 * Ʒ?Ʒ???????
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-04-13 15:39:43
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @GetMapping("/brands/list")
    private R relationList(@RequestParam("catId")Long catId){

        List<BrandEntity> brandEntityList = this.categoryBrandRelationService.getBrandsByCatId(catId);

        List<BrandVO> brandVOList = brandEntityList.stream().map(brandEntity -> {
            BrandVO brandVO = new BrandVO();
            brandVO.setBrandId(brandEntity.getBrandId());
            brandVO.setBrandName(brandEntity.getName());
            return brandVO;
        }).collect(Collectors.toList());

        return R.ok().put("data",brandVOList);

    }

    /**
     * 获取当前品牌关联的所有分类列表
     * @param brandId 品牌id
     * @return 当前品牌关联的所有分类列表
     */
    @GetMapping("catelog/list")
    public R list(@RequestParam("brandId") Long brandId){
        List<CategoryBrandRelationEntity> list = this.categoryBrandRelationService.list(new QueryWrapper<CategoryBrandRelationEntity>()
                .eq("brand_id", brandId));
        return R.ok().put("data",list);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){

        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();

        CategoryEntity categoryEntity = this.categoryService.getById(catelogId);
        categoryBrandRelation.setCatelogName(categoryEntity.getName());

        BrandEntity brandEntity = this.brandService.getById(brandId);
        categoryBrandRelation.setBrandName(brandEntity.getName());

        categoryBrandRelationService.save(categoryBrandRelation);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
