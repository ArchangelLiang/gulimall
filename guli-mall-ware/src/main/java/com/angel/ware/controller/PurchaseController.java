package com.angel.ware.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.angel.ware.vo.PurchaseDoneVo;
import com.angel.ware.vo.PurchaseMergeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.angel.ware.entity.PurchaseEntity;
import com.angel.ware.service.PurchaseService;
import com.angel.common.utils.PageUtils;
import com.angel.common.utils.R;



/**
 * ?ɹ???Ϣ
 *
 * @author archangel
 * @email sunlightcs@gmail.com
 * @date 2020-04-13 17:04:35
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    //完成采购单
    @PostMapping("/done")
    public R done(@RequestBody PurchaseDoneVo purchaseDoneVo){

        purchaseService.done(purchaseDoneVo);

        return R.ok();
    }

    // 领取采购单
    @PostMapping("received")
    public R receive(@RequestBody List<Long> purchaseIds){
        purchaseService.receive(purchaseIds);

        return R.ok();
    }

    //合并采购单
    @RequestMapping("/merge")
    public R merge(@RequestBody PurchaseMergeVo purchaseMergeVo){
        purchaseService.merge(purchaseMergeVo);

        return R.ok();
    }


    //获取为领取的采购单
    @RequestMapping("/unreceive/list")
    public R unReceiveList(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryUnReceiveList(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PurchaseEntity purchase){
        Date date = new Date();
        purchase.setCreateTime(date);
        purchase.setUpdateTime(date);
		purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
