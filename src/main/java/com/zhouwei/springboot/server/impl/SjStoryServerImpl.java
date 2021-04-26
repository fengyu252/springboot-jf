package com.zhouwei.springboot.server.impl;

import com.zhouwei.springboot.entity.SjStory;
import com.zhouwei.springboot.mapper.SjStoryMapper;
import com.zhouwei.springboot.server.SjStoryServer;
import com.zhouwei.springboot.utils.ImgUrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class SjStoryServerImpl implements SjStoryServer {

    @Autowired
    SjStoryMapper sjStoryMapper;

    @Transactional
    @Override
    public int addSjStory(List<SjStory> sjStory) {
        for(int i=0,len=sjStory.size();i<len;i++){
            sjStoryMapper.addSjStory(sjStory.get(i));
        }
        return 1;
    }
    //@Transactional
    @Override
    public int downImgUpateR2(Map map) {
        try {
            List<SjStory> list=sjStoryMapper.findImgUrl(map);
            for(int i=0,len=list.size();i<len;i++){
                SjStory sjStory=list.get(i);
               boolean b= ImgUrlUtil.downImgByUrl(sjStory.getR1(),sjStory.getImgUrl());
               if(b){
                   map.put("id",sjStory.getId());
                   sjStoryMapper.updateImgUrlR2(map);
               }
               Thread.sleep(2000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
}
