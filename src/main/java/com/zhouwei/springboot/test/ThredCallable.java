package com.zhouwei.springboot.test;

import java.util.concurrent.Callable;

public class ThredCallable implements Callable {

    private Integer num=0;
    private String threadName=null;
    private boolean stop=true;
    private Integer sleepTime=0;
    public ThredCallable(String threadName,Integer sleepTime) {
        this.threadName=threadName;
        this.sleepTime=sleepTime;
    }

    @Override
    public Integer call() throws Exception {
        while (stop) {
            Thread.sleep(sleepTime);
            System.out.println(Thread.currentThread()+"=="+num);
            num++;
        }
        return num;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }
}
