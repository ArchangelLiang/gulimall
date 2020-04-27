package com.angel.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.angel.common.utils.PageUtils;
import com.angel.common.utils.Query;

import com.angel.product.dao.CategoryDao;
import com.angel.product.entity.CategoryEntity;
import com.angel.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {

        List<CategoryEntity> entities = this.baseMapper.selectList(null);

        List<CategoryEntity> level1 = entities.stream().filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .sorted(Comparator.comparingInt(CategoryEntity::getSort)).collect(Collectors.toList());
        level1.forEach(categoryEntity -> categoryEntity.setChildren(getChildren(categoryEntity,entities)));

        return level1;
    }

    private List<CategoryEntity> getChildren(CategoryEntity parent,List<CategoryEntity> list){

        return list.stream().filter(categoryEntity -> categoryEntity.getParentCid().equals(parent.getCatId()))
                .peek(categoryEntity -> categoryEntity.setChildren(getChildren(categoryEntity,list)))
                .sorted(Comparator.comparingInt(CategoryEntity::getSort))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByIds(Long[] catIds) {
        //TODO 首先要检察这些数据是否被关联
        baseMapper.deleteBatchIds(Arrays.asList(catIds));
    }

    @Override
    public List<Long> getCatelogPath(Long catelogId) {
        ArrayList<Long> catelogPath = new ArrayList<>();
        getCatelogPath(catelogId,catelogPath);
        Collections.reverse(catelogPath);
        return catelogPath;
    }

    private void getCatelogPath(Long catelogId,List<Long> catelogList){
        CategoryEntity categoryEntity = baseMapper.selectById(catelogId);
        catelogList.add(categoryEntity.getCatId());
        if (categoryEntity.getParentCid() != 0){
            getCatelogPath(categoryEntity.getParentCid(),catelogList);
        }
    }

}