package com.zhouwei.springboot.server;

import com.zhouwei.springboot.entity.SjStory;

import java.util.List;
import java.util.Map;

public interface SjStoryServer {

    int addSjStory(List<SjStory> sjStory);

    int downImgUpateR2(Map map);
}
