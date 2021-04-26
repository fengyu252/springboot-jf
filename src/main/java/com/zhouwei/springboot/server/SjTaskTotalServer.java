package com.zhouwei.springboot.server;

import java.util.List;
import java.util.Map;

public interface SjTaskTotalServer {

    /**
     * 查询统计信息
     */
    List findSjTotal(Map map);
}
