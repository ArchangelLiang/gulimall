package com.angel.product.service.impl;

import com.angel.product.entity.AttrEntity;
import com.angel.product.entity.AttrGroupEntity;
import com.angel.product.entity.CategoryEntity;
import com.angel.product.service.AttrGroupService;
import com.angel.product.service.AttrService;
import com.angel.product.service.CategoryService;
import com.angel.product.vo.AttrGroupVo;
import com.angel.product.vo.RelationVO;
import org.springframework.beans.BeanUtils;
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

import com.angel.product.dao.AttrAttrgroupRelationDao;
import com.angel.product.entity.AttrAttrgroupRelationEntity;
import com.angel.product.service.AttrAttrgroupRelationService;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {


    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void deleteBatchRelation(List<RelationVO> relationVOS) {

        baseMapper.deleteBathRelation(relationVOS);

    }

    /**
     * 根据分类 id 获取 该分类下面的所有属性分组以及属性分组下的所有属性
     * @param catelogId 三级分类id
     * @return 该分类下面的所有属性分组以及属性分组下的所有属性组成的VO集合
     */
    @Override
    public List<AttrGroupVo> getAttrGroupVosByCatId(Long catelogId) {

        //根据三级分类id获取所有属性分组
        List<AttrGroupEntity> attrGroupEntities = attrGroupService.list(new QueryWrapper<AttrGroupEntity>()
                .eq("catelog_id", catelogId));

        if (attrGroupEntities != null && attrGroupEntities.size() > 0) {
            return attrGroupEntities.stream().map(attrGroupEntity -> {
                AttrGroupVo attrGroupVo = new AttrGroupVo();
                BeanUtils.copyProperties(attrGroupEntity, attrGroupVo);

                Long attrGroupId = attrGroupVo.getAttrGroupId();
                //根据属性分组 id 获取其与属性的关系对象集合
                List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities = baseMapper.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>()
                        .eq("attr_group_id", attrGroupId));

                if (attrAttrgroupRelationEntities != null && attrAttrgroupRelationEntities.size() > 0) {
                    //通过属性分组与属性的关系对象拿到属性集合拿到所有属性id
                    List<Long> ids = attrAttrgroupRelationEntities.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());

                    //根据属性id的集合查询属性
                    List<AttrEntity> attrEntities = attrService.listByIds(ids);
                    attrGroupVo.setAttrs(attrEntities);
                }

                return attrGroupVo;
            }).collect(Collectors.toList());
        }

        return null;
    }

}