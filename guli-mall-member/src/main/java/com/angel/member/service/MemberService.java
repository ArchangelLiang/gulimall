package com.angel.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.angel.common.utils.PageUtils;
import com.angel.member.entity.MemberEntity;

import java.util.Map;

/**
 * ??Ա
 *
 * @author archangel
 * @email sunlightcs@gmail.com
 * @date 2020-04-13 16:51:11
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

