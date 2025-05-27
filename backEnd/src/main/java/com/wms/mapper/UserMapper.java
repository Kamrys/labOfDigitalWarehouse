package com.wms.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 小林ikun
 * @since 2024-11-08
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    IPage<User> pageC(Page<User> page);

    /**
     * @Param 注解用于指定方法参数在 SQL 语句中的占位符,
     * Constants.WRAPPER 是一个常量，表示查询条件包装器
     */
    IPage<User> pageCc(Page<User> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
