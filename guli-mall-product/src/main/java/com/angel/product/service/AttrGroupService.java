package com.angel.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.angel.common.utils.PageUtils;
import com.angel.product.entity.AttrGroupEntity;

import java.util.Map;

/**
 * ???Է??
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-04-13 15:39:43
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Long catId);
}

