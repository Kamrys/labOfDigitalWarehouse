package com.wms.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 小林ikun
 * @since 2024-11-08
 */
public interface UserService extends IService<User> {

    IPage<User> pageC(Page<User> page);

    IPage<User> pageCc(Page<User> page, Wrapper wrapper);
}
