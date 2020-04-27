package com.angel.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.angel.product.constant.AttrType;
import com.angel.product.entity.AttrAttrgroupRelationEntity;
import com.angel.product.entity.AttrGroupEntity;
import com.angel.product.service.AttrAttrgroupRelationService;
import com.angel.product.service.AttrGroupService;
import com.angel.product.service.CategoryService;
import com.angel.product.vo.AttrVo;
import com.angel.product.vo.RespAttrVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.angel.product.entity.AttrEntity;
import com.angel.product.service.AttrService;
import com.angel.common.utils.PageUtils;
import com.angel.common.utils.R;



/**
 * 商品属性
 *
 * @author archangel
 * @email sunlightcs@gmail.com
 * @date 2020-04-14 22:19:28
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    /**
     * 查询属性列表，基本属性于销售属性通用
     * @param params 封装查询参数
     * @param catelogId 属性所属分类Id
     * @param attrType 属性类型，是基本属性（base）还是销售属性（sale）
     * @return 属性列表
     */
    @GetMapping("{attrType}/list/{catelogId}")
    private R baseAttrList(Map<String,Object> params,@PathVariable("catelogId") Long catelogId,
                           @PathVariable("attrType") String attrType){

        PageUtils page = attrService.queryPage(params,catelogId,attrType);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
		AttrEntity attr = attrService.getById(attrId);

		RespAttrVo respAttrVo = attrService.getRespVo(attr);

        return R.ok().put("attr", respAttrVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrVo attrVo){

        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo,attrEntity);
        attrService.save(attrEntity);

        // 如果是基本属性才保存分组信息
        if (attrVo.getAttrType().equals(AttrType.ATTR_TYPE_BASE.getCode())) {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            this.attrAttrgroupRelationService.save(attrAttrgroupRelationEntity);
        }

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrVo attr){
//		attrService.updateById(attr);
        attrService.cascadeUpdate(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
