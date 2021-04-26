package com.zhouwei.springboot.server.impl;

import com.zhouwei.springboot.mapper.SjTaskTotalMapper;
import com.zhouwei.springboot.server.SjTaskTotalServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class SjTaskTotalServerImpl implements SjTaskTotalServer {

    @Autowired
    SjTaskTotalMapper sjTaskTotalMapper;

    @Override
    public List findSjTotal(Map mp) {
        int page=Integer.parseInt(mp.get("page").toString());
        int pageNum=Integer.parseInt(mp.get("pageNum").toString());
        mp.put("startNum",(page-1)*pageNum);
        mp.put("pageNum",pageNum);
        return sjTaskTotalMapper.findSjTotal(mp);
    }
}
