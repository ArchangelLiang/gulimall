package com.angel.product.service.impl;

import com.angel.product.entity.BrandEntity;
import com.angel.product.service.BrandService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.angel.common.utils.PageUtils;
import com.angel.common.utils.Query;

import com.angel.product.dao.CategoryBrandRelationDao;
import com.angel.product.entity.CategoryBrandRelationEntity;
import com.angel.product.service.CategoryBrandRelationService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    private BrandService brandService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void updateCategoryName(Long catId, String name) {
        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setCatelogId(catId);
        categoryBrandRelationEntity.setCatelogName(name);
        baseMapper.update(categoryBrandRelationEntity,new UpdateWrapper<CategoryBrandRelationEntity>()
                .eq("catelog_id",catId));
    }

    @Transactional
    @Override
    public void updateBrandName(Long brandId, String name) {
        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setBrandId(brandId);
        categoryBrandRelationEntity.setBrandName(name);

        baseMapper.update(categoryBrandRelationEntity,new UpdateWrapper<CategoryBrandRelationEntity>()
        .eq("brand_id",brandId));
    }

    /**
     * 获取指定分类下的所有品牌
     * @param catId 分类id
     * @return 指定分类下的所有品牌集合
     */
    @Override
    public List<BrandEntity> getBrandsByCatId(Long catId) {

        List<CategoryBrandRelationEntity> categoryBrandRelationEntityList = baseMapper
                .selectList(new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catId));

        return categoryBrandRelationEntityList.stream().map(categoryBrandRelationEntity -> {
            Long brandId = categoryBrandRelationEntity.getBrandId();
            return brandService.getById(brandId);
        }).collect(Collectors.toList());
    }

}