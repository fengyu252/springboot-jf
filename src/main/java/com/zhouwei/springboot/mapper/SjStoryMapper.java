package com.zhouwei.springboot.mapper;

import com.zhouwei.springboot.entity.SjStory;

import java.util.List;
import java.util.Map;

public interface SjStoryMapper {

    int addSjStory(SjStory sjStory);

    List<SjStory> findImgUrl(Map map);

    int updateImgUrlR2(Map map);
}
