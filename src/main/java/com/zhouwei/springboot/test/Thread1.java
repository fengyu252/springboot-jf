package com.zhouwei.springboot.test;


public class Thread1 implements Runnable {

    private Integer num=0;
    private String threadName="";
    public Thread1() {
    }

    public Thread1(Integer num,String threadName) {
        this.num = num;
        this.threadName=threadName;
    }

    @Override
    public void run() {

            while(num>0)
                synchronized (this) {
                    System.out.println(Thread.currentThread().getName()+"==="+num--);
                }
    }


}
