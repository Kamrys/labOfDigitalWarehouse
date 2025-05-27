package com.wms.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.entity.Goodstype;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wms.entity.Storage;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 小林ikun
 * @since 2024-11-22
 */
public interface GoodstypeService extends IService<Goodstype> {
    IPage<Goodstype> pageFL(Page<Goodstype> page, Wrapper wrapper);
}
