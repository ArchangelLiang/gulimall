package com.angel.product.service.impl;

import com.angel.product.dao.AttrAttrgroupRelationDao;
import com.angel.product.dao.AttrDao;
import com.angel.product.entity.AttrAttrgroupRelationEntity;
import com.angel.product.entity.AttrEntity;
import com.angel.product.service.AttrService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
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

import com.angel.product.dao.AttrGroupDao;
import com.angel.product.entity.AttrGroupEntity;
import com.angel.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catId) {
        IPage<AttrGroupEntity> page;
        String key = (String) params.get("key");
        QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();
        if (catId == 0) {
            if (key != null && !key.trim().equals("")) {
                queryWrapper.eq("attr_group_id", key).or().like("attr_group_name", key).or()
                        .like("descript", key);
            }
            page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    queryWrapper
            );
        } else {
            queryWrapper.eq("catelog_id", catId);
            if (key != null && !key.trim().equals("")) {
                queryWrapper.and(wrapper -> {
                    wrapper.eq("attr_group_id", key).or().like("attr_group_name", key).or()
                            .like("descript", key);
                });
            }
            page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    queryWrapper
            );
        }

        return new PageUtils(page);
    }


}