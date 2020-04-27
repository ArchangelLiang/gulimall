package com.angel.ware.service;

import com.angel.ware.vo.PurchaseDoneVo;
import com.angel.ware.vo.PurchaseMergeVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.angel.common.utils.PageUtils;
import com.angel.ware.entity.PurchaseEntity;

import java.util.List;
import java.util.Map;

/**
 * ?ɹ???Ϣ
 *
 * @author archangel
 * @email sunlightcs@gmail.com
 * @date 2020-04-13 17:04:35
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryUnReceiveList(Map<String, Object> params);

    void merge(PurchaseMergeVo purchaseMergeVo);

    void receive(List<Long> purchaseIds);

    void done(PurchaseDoneVo purchaseDoneVo);
}

