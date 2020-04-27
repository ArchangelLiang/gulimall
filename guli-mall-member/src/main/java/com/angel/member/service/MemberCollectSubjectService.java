package com.angel.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.angel.common.utils.PageUtils;
import com.angel.member.entity.MemberCollectSubjectEntity;

import java.util.Map;

/**
 * ??Ա?ղص?ר???
 *
 * @author archangel
 * @email sunlightcs@gmail.com
 * @date 2020-04-13 16:51:11
 */
public interface MemberCollectSubjectService extends IService<MemberCollectSubjectEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

