package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Goods;
import com.wms.entity.Record;
import com.wms.service.GoodsService;
import com.wms.service.RecordService;
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
@RequestMapping("/record")
public class RecordController {
    @Autowired  // 注入
    private RecordService recordService;

    @Autowired  // 注入
    private GoodsService goodsService;

    // TODO 多删除1
    @GetMapping("/deletes")
    public Result deletes(@RequestParam List<Integer> ids) {
        return recordService.removeByIds(ids) ? Result.suc() : Result.fail();
    }

    // TODO 查询 + 分页
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
        String name = (String)param.get("name");
        String goodstype = (String)param.get("goodstype");
        String storage = (String)param.get("storage");
        String roleId = (String)param.get("roleId");
        String userId = (String)param.get("userId");

        Page<Record> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        QueryWrapper<Record> queryWrapper = new QueryWrapper();
        queryWrapper.apply(" a.goods=b.id and b.storage=c.id and b.goodsType=d.id ");

        // 在记录管理中，用户权限的只能自己查到自己的
        if("2".equals(roleId)){
            // queryWrapper.eq(Record::getUserid,userId);
            queryWrapper.apply(" a.userId= " + userId);
        }

        if(StringUtils.isNotBlank(name) && !"null".equals(name)){
            queryWrapper.like("b.name",name);
        }
        if(StringUtils.isNotBlank(goodstype) && !"null".equals(goodstype)){
            queryWrapper.eq("d.id",goodstype);
        }
        if(StringUtils.isNotBlank(storage) && !"null".equals(storage)){
            queryWrapper.eq("c.id",storage);
        }

        IPage result = recordService.pageJL(page,queryWrapper);
        return Result.suc(result.getRecords(),result.getTotal());
    }

    // TODO 新增
    @PostMapping("/save")
    public Result save(@RequestBody Record record){

        Goods goods = goodsService.getById(record.getGoods());
        int n = record.getCount();

        // 出库
        if("2".equals(record.getAction())){
            n = -n;
            record.setCount(n);
        }

        int num = goods.getCount() + n;

        // 检查最终库存数量不能为负数
        if (num < 0) {
            return Result.fail("操作会导致库存数量为负数");
        }

        goods.setCount(num);
        goodsService.updateById(goods);

        return recordService.save(record)?Result.suc():Result.fail();
    }

    // TODO 列表
    @GetMapping("/list")
    public Result list(){
        List list = recordService.list();
        return Result.suc(list);
    }
}
