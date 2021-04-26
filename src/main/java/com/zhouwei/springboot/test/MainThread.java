package com.zhouwei.springboot.test;

import java.util.concurrent.*;

public class MainThread {

    public static void main(String[] args) {
//        //卖票系统runnable 实现
//        Thread1 runnable=new Thread1(100,"runnable-1");
//        Thread t=new Thread(runnable,"run-1");
//        Thread t2=new Thread(runnable,"run-2");
//        t.start();
//        t2.start();
//        //卖票系统thread 实现
//        Thread2 threads1=new Thread2(100,"thread-1");
//        Thread ttd1=new Thread(threads1,"trun-1");
//        Thread ttd2=new Thread(threads1,"trun-2");
//        ttd1.start();
//        ttd2.start();

        /**
         * 表示线程池有返回信息的线程
         */
        //设置五个线程
        ExecutorService executorService= Executors.newFixedThreadPool(3);
        ThredCallable tt1=new ThredCallable("中国",1000);
        ThredCallable tt2=new ThredCallable("美国",500);

        try {

        if(0==0){
            Thread2 t2=new Thread2();
            executorService.submit(t2);

        }else if(0>1) {
                TjCallable tjCallable=new TjCallable();
                FutureTask<String> futureTask=new FutureTask<String>(tjCallable);
                executorService.submit(futureTask);
                String str=futureTask.get();
                System.out.println("=="+str);
                executorService.shutdown();
            }else if(0>1) {
                FutureTask<Integer> ft1 = new FutureTask<>(tt1);
                FutureTask<Integer> ft2 = new FutureTask<>(tt2);
                executorService.submit(ft1);
                executorService.submit(ft2);
                Thread.sleep(30000);
                tt1.setStop(false);
                tt2.setStop(false);
                int count1=ft1.get();
                int count2=ft2.get();
                System.out.println(tt1.getThreadName()+"=="+count1);
                System.out.println(tt2.getThreadName()+"=="+count2);
                executorService.shutdown();
            }else{
                Future<Integer> ft1=executorService.submit(tt1);
                Future<Integer> ft2=executorService.submit(tt2);
                Thread.sleep(5000);
                tt1.setStop(false);
                tt2.setStop(false);
                int count1=ft1.get();
                int count2=ft2.get();
                System.out.println(tt1.getThreadName()+"=="+count1);
                System.out.println(tt2.getThreadName()+"=="+count2);
                executorService.shutdown();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
