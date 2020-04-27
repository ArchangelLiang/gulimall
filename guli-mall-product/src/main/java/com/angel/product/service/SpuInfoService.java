package com.angel.product.service;

import com.angel.product.vo.SpuSaveVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.angel.common.utils.PageUtils;
import com.angel.product.entity.SpuInfoEntity;

import java.util.Map;

/**
 * spu??Ϣ
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-04-13 15:39:43
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVo spuSaveVo);

    PageUtils queryPageByCondition(Map<String, Object> params);
}

