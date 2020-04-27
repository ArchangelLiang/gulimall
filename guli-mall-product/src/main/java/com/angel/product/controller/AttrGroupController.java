package com.angel.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.angel.product.entity.AttrAttrgroupRelationEntity;
import com.angel.product.entity.AttrEntity;
import com.angel.product.service.AttrAttrgroupRelationService;
import com.angel.product.service.AttrService;
import com.angel.product.service.CategoryService;
import com.angel.product.vo.AttrGroupVo;
import com.angel.product.vo.RelationVO;
import com.angel.product.vo.RespAttrVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.angel.product.entity.AttrGroupEntity;
import com.angel.product.service.AttrGroupService;
import com.angel.common.utils.PageUtils;
import com.angel.common.utils.R;



/**
 * ???Է??
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-04-13 15:39:43
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    @Autowired
    private AttrService attrService;


    @GetMapping("/{catelogId}/withattr")
    private R getAttyHGroupWithAttr(@PathVariable("catelogId")Long catelogId){

        List<AttrGroupVo> attrGroupVos = attrAttrgroupRelationService.getAttrGroupVosByCatId(catelogId);
        return R.ok().put("data",attrGroupVos);

    }

    @PostMapping("/attr/relation")
    private R addRelation(@RequestBody List<RelationVO> relationVos){

        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities = relationVos.stream().map(relationVO -> {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrGroupId(relationVO.getAttrGroupId());
            attrAttrgroupRelationEntity.setAttrId(relationVO.getAttrId());
            return attrAttrgroupRelationEntity;
        }).collect(Collectors.toList());

        this.attrAttrgroupRelationService.saveBatch(attrAttrgroupRelationEntities);

        return R.ok();
    }

    /**
     * 获取指定分组没有关联的属性集合
     * @param params 请求参数
     * @param attrgroupId 属性分组id
     * @return 指定分组没有关联的属性集合
     */
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R noRelation(Map<String,Object> params,@PathVariable("attrgroupId") Long attrgroupId){
        PageUtils pageUtils = attrService.getNoRelationList(attrgroupId,params);

        return R.ok().put("page",pageUtils);
    }

    /**
     * 删除属性于属性分组的关联关系，支持批量删除
     * @param relationVOS 需要删除的关联关系的属性id与属性分组id组成的集合
     * @return
     */
    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody List<RelationVO> relationVOS){

        attrAttrgroupRelationService.deleteBatchRelation(relationVOS);

        return R.ok();
    }

    /**
     * 获取指定分组关联的所有属性
     * @param attrgroupId 分组id
     * @return 指定分组关联的所有属性集合
     */
    @GetMapping("/{attrgroupId}/attr/relation")
    public R relationAttr(@PathVariable("attrgroupId") Long attrgroupId){

        List<AttrAttrgroupRelationEntity> relationEntities = attrAttrgroupRelationService.list(new QueryWrapper<AttrAttrgroupRelationEntity>()
                .eq("attr_group_id", attrgroupId));

        List<Long> attrIds = relationEntities.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
        List<AttrEntity> attrEntities = null;
        if (attrIds.size() > 0){
            attrEntities = this.attrService.listByIds(attrIds);
        }

        return R.ok().put("data",attrEntities);
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{catId}")
    public R list(@RequestParam Map<String, Object> params,@PathVariable("catId") Long catId){
//        PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPage(params,catId);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        List<Long> catelogPath = categoryService.getCatelogPath(attrGroup.getCatelogId());
		attrGroup.setCatelogPath(catelogPath);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
