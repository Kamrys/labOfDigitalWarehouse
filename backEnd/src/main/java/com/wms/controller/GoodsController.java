package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Goods;
import com.wms.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 小林ikun
 * @since 2024-11-23
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired  // 注入
    private GoodsService goodsService;

    // TODO 新增
    @PostMapping("/save")
    public Result save(@RequestBody Goods goods){
        return goodsService.save(goods)?Result.suc():Result.fail();
    }
    // TODO 更新
    @PostMapping("/update")
    public Result update(@RequestBody Goods goods){
        return goodsService.updateById(goods)?Result.suc():Result.fail();
    }
    // TODO 删除
    @GetMapping("/delete")
    public Result del(@RequestParam String id){
        return goodsService.removeById(id)?Result.suc():Result.fail();
    }

    // TODO 多删除1
    @GetMapping("/deletes")
    public Result deletes(@RequestParam List<Integer> ids) {
        return goodsService.removeByIds(ids) ? Result.suc() : Result.fail();
    }

    // TODO 查询 + 分页
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
        String name = (String)param.get("name");
        String storage = (String)param.get("storage");
        String goodstype = (String)param.get("goodstype");

        Page<Goods> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Goods> lambdaQueryWrapper = new LambdaQueryWrapper();
        if(StringUtils.isNotBlank(name) && !"null".equals(name)){
            lambdaQueryWrapper.like(Goods::getName,name);
        }

        if(StringUtils.isNotBlank(storage) && !"null".equals(storage)){
            lambdaQueryWrapper.like(Goods::getStorage,storage);
        }

        if(StringUtils.isNotBlank(goodstype) && !"null".equals(goodstype)){
            lambdaQueryWrapper.like(Goods::getGoodstype,goodstype);
        }

        IPage result = goodsService.pageGL(page,lambdaQueryWrapper);
        return Result.suc(result.getRecords(),result.getTotal());
    }

    // TODO 列表
    @GetMapping("/list")
    public Result list(){
        List list = goodsService.list();
        return Result.suc(list);
    }
}
