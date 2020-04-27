package com.angel.product.service.impl;

import com.angel.product.constant.AttrType;
import com.angel.product.dao.AttrAttrgroupRelationDao;
import com.angel.product.dao.AttrGroupDao;
import com.angel.product.dao.CategoryDao;
import com.angel.product.entity.AttrAttrgroupRelationEntity;
import com.angel.product.entity.AttrGroupEntity;
import com.angel.product.entity.CategoryEntity;
import com.angel.product.service.AttrAttrgroupRelationService;
import com.angel.product.service.AttrGroupService;
import com.angel.product.service.CategoryService;
import com.angel.product.vo.AttrVo;
import com.angel.product.vo.RespAttrVo;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.angel.common.utils.PageUtils;
import com.angel.common.utils.Query;

import com.angel.product.dao.AttrDao;
import com.angel.product.entity.AttrEntity;
import com.angel.product.service.AttrService;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    private AttrGroupDao attrGroupDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    // 组装规格参数页面列表数据
    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId, String attrType) {

        Integer attrTypeCode = attrType.equalsIgnoreCase(AttrType.ATTR_TYPE_BASE.getText()) ?
                AttrType.ATTR_TYPE_BASE.getCode() : AttrType.ATTR_TYPE_SALE.getCode();

        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("attr_type", attrTypeCode);

        if (catelogId != 0) {
            queryWrapper.eq("catelog_id", catelogId);
        }
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.and(wrapper -> {
                wrapper.eq("attr_id", key)
                        .or().like("attr_name", key);
            });
        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );

        PageUtils pageUtils = new PageUtils(page);

        List<AttrEntity> records = page.getRecords();

        List<RespAttrVo> respAttrVos = records.stream().map(attrEntity -> {

            RespAttrVo respAttrVo = new RespAttrVo();
            BeanUtils.copyProperties(attrEntity, respAttrVo);

            Long attrId = respAttrVo.getAttrId();
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));

            // 如果是基本属性才查询分组信息
            if (attrType.equalsIgnoreCase(AttrType.ATTR_TYPE_BASE.getText())) {

                if (attrAttrgroupRelationEntity != null) {
                    Long attrGroupId = attrAttrgroupRelationEntity.getAttrGroupId();
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);

                    respAttrVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }

            }

            CategoryEntity categoryEntity = categoryDao.selectById(respAttrVo.getCatelogId());

            respAttrVo.setCatelogName(categoryEntity.getName());

            return respAttrVo;
        }).collect(Collectors.toList());

        pageUtils.setList(respAttrVos);

        return pageUtils;
    }

    //组装 Attr 的VO 对象
    @Override
    public RespAttrVo getRespVo(AttrEntity attr) {

        RespAttrVo respAttrVo = new RespAttrVo();
        BeanUtils.copyProperties(attr, respAttrVo);

        // 如果是基本属性才查询分组信息
        if (attr.getAttrType().equals(AttrType.ATTR_TYPE_BASE.getCode())) {
            AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationService.getOne(new QueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq("attr_id", attr.getAttrId()));

            if (relationEntity != null) {
                Long attrGroupId = relationEntity.getAttrGroupId();
                AttrGroupEntity groupEntity = this.attrGroupService.getById(attrGroupId);

                respAttrVo.setAttrGroupId(attrGroupId);
                respAttrVo.setGroupName(groupEntity.getAttrGroupName());
            }

        }
        List<Long> catelogPath = this.categoryService.getCatelogPath(respAttrVo.getCatelogId());
        respAttrVo.setCatelogPath(catelogPath);

        return respAttrVo;
    }

    // 更新属性的同时更新其所属分组
    @Override
    public void cascadeUpdate(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        updateById(attrEntity);

        // 如果是基本属性才更新分组信息
        if (attr.getAttrType().equals(AttrType.ATTR_TYPE_BASE.getCode())) {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrId(attr.getAttrId());
            attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());

            Integer count = attrAttrgroupRelationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));

            if (count != null && count > 0) {
                attrAttrgroupRelationDao.update(attrAttrgroupRelationEntity, new UpdateWrapper<AttrAttrgroupRelationEntity>()
                        .eq("attr_id", attr.getAttrId()));
            } else {
                attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
            }
        }

    }

    /**
     * 获取指定分组没有关联的属性集合
     * @param attrgroupId 属性分组id
     * @param params 请求参数
     * @return 指定分组没有关联的属性集合
     */
    @Override
    public PageUtils getNoRelationList(Long attrgroupId, Map<String, Object> params) {

        List<AttrAttrgroupRelationEntity> relationEntities = this.attrAttrgroupRelationDao.selectList(null);
        List<Long> ids = relationEntities.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());

        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_type",AttrType.ATTR_TYPE_BASE.getCode());
        if (ids.size() > 0) {
            queryWrapper.notIn("attr_id", ids);
        }

        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.eq("attr_id",key).or().like("attr_name",key);
        }

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

}