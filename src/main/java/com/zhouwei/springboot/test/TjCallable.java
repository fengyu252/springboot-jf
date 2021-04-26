package com.zhouwei.springboot.test;

import java.util.concurrent.Callable;

public class TjCallable implements Callable {

    public TjCallable(){

    }

    @Override
    public String call() throws Exception {
        String str="";
        for(int i=0;i<100;i++){
            str+=i+",";
        }
        return str;
    }
}
