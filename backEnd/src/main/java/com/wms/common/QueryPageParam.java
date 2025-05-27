package com.wms.common;

import lombok.Data;

import java.util.HashMap;

/**
 * TODO 分页封装
 */
@Data       // 等同于手动编写了所有getter、setter、toString、equals和hashCode方法
public class QueryPageParam {
    // 默认   一页有多少条数据
    private static int PAGE_SIZE = 20;
    private static int PAGE_NUM = 1;

    private int pageSize = PAGE_SIZE;
    private int pageNum = PAGE_NUM;

    private HashMap param = new HashMap();
}
