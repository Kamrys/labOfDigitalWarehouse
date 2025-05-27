package com.wms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Storage;
import com.wms.service.StorageService;
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
 * @since 2024-11-22
 */
@RestController
@RequestMapping("/storage")
public class StorageController {

    @Autowired  // 注入
    private StorageService storageService;

    // TODO 新增
    @PostMapping("/save")
    public Result save(@RequestBody Storage storage){
        return storageService.save(storage)?Result.suc():Result.fail();
    }
    // TODO 更新
    @PostMapping("/update")
    public Result update(@RequestBody Storage storage){
        return storageService.updateById(storage)?Result.suc():Result.fail();
    }
    // TODO 删除
    @GetMapping("/delete")
    public Result del(@RequestParam String id){
        return storageService.removeById(id)?Result.suc():Result.fail();
    }

    // TODO 多删除1
    @GetMapping("/deletes")
    public Result deletes(@RequestParam List<Integer> ids) {
        return storageService.removeByIds(ids) ? Result.suc() : Result.fail();
    }

    // TODO 查询 + 分页
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
        String name = (String)param.get("name");

        Page<Storage> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Storage> lambdaQueryWrapper = new LambdaQueryWrapper();
        if(StringUtils.isNotBlank(name) && !"null".equals(name)){
            lambdaQueryWrapper.like(Storage::getName,name);
        }

        IPage result = storageService.pageCK(page,lambdaQueryWrapper);
        return Result.suc(result.getRecords(),result.getTotal());
    }

    // TODO 列表
    @GetMapping("/list")
    public Result list(){
        List list = storageService.list();
        return Result.suc(list);
    }
}
