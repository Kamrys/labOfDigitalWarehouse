package com.wms.service.impl;

import com.wms.entity.Menu;
import com.wms.mapper.MenuMapper;
import com.wms.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 小林ikun
 * @since 2024-11-21
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

}
