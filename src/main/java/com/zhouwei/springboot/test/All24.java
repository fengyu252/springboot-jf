package com.zhouwei.springboot.test;

import com.zhouwei.springboot.entity.ShuZi24;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class All24 {
    static int num=0;//计算总共有多少种情况
    public static void main(String[] args) {
        int type=101;
        /*
         * 4层for循环获取4张扑克牌所有的可能组合
         * 将组合存入数组poker
         */
        List<ShuZi24> list=new ArrayList<ShuZi24>();


        suiji(4,5,7,6);
//        ShuZi24 s4=  suanzhongjianleft24(9, 6, 6, 7, 303);
//        ShuZi24 s44=  suanzhongjianright24(6, 7 ,6, 9, 303);
//        ShuZi24 s444=  suanLeft24(6, 7, 6, 9, 303);
//        ShuZi24 s4444=  suanRight24(6, 7, 6, 9, 303);
        /*
         * 4层for循环获取4张扑克牌所有的可能组合
         * 将组合存入数组poker
         */
//        for(int i=1;i<=15;i++){
//            for(int j=1;j<=15;j++){
//                for(int k=1;k<=15;k++){
//                    for(int m=1;m<=15;m++){
//                        int[] poker=new int[]{i,j,k,m};
//                        ShuZi24 s1=  suanzhongjianleft24(i,j,k,m,105);
//                        if(s1==null) {
//                            ShuZi24 s2=  suanzhongjianright24(i, j, k, m, 405);
//                            if(s2==null) {
//                                ShuZi24 s3= suanLeft24(i, j, k, m, 205);
//                                if(s3==null) {
//                                    ShuZi24 s4=  suanRight24(i, j, k, m, 305);
//                                    if(s4!=null)
//                                        list.add(s4);
//                                }else{
//                                    list.add(s3);
//                                }
//
//
//                            }else{
//                                list.add(s2);
//                            }
//
//                        }else{
//                            list.add(s1);
//                        }
//
//
//
//
//                    }
//                }
//            }
//        }
        System.out.println("总共有"+num+"种情况"+ list.size());

    }

    public static void  suiji(int a,int b,int c,int d){
            int [] num = {a,b,c,d};
            int aa,bb,cc,dd;
            for(int i=0;i<100;i++) {
               int  aa1 = (int) (Math.random() * num.length);

               int bb1 = (int) (Math.random() * num.length);

                int cc1 = (int) (Math.random() * num.length);

               int dd1 = (int) (Math.random() * num.length);
               if(aa1!=bb1&&aa1!=cc1&&aa1!=dd1&&bb1!=cc1&&bb1!=dd1&&cc1!=dd1) {
                   cc = num[cc1];
                   bb = num[bb1];
                   aa = num[aa1];
                   dd = num[dd1];
                   ShuZi24 s4 = suanzhongjianleft24(aa, bb, cc, dd, 0);
                   ShuZi24 s44 = suanzhongjianright24(aa, bb, cc, dd, 0);
                   ShuZi24 s444 = suanLeft24(aa, bb, cc, dd, 0);
                   ShuZi24 s4444 = suanRight24(aa, bb, cc, dd, 0);
               }
            }
    }

    public static List findAll24(){
        List<ShuZi24> list=new ArrayList<ShuZi24>();
        int type=101;
        /*
         * 4层for循环获取4张扑克牌所有的可能组合
         * 将组合存入数组poker
         */
        for(int i=1;i<=24;i++){
            for(int j=1;j<=24;j++){
                for(int k=1;k<=24;k++){
                    for(int m=1;m<=24;m++){
                        int[] poker=new int[]{i,j,k,m};
                        ShuZi24 s1=  suanzhongjianleft24(i,j,k,m,105);
                        if(s1==null) {
                            ShuZi24 s2=  suanzhongjianright24(i, j, k, m, 405);
                            if(s2==null) {
                                ShuZi24 s3= suanLeft24(i, j, k, m, 205);
                                if(s3==null) {
                                    ShuZi24 s4=  suanRight24(i, j, k, m, 305);
                                    if(s4!=null)
                                        list.add(s4);
                                }else{
                                    list.add(s3);
                                }


                            }else{
                                list.add(s2);
                            }

                        }else{
                            list.add(s1);
                        }




                    }
                }
            }
        }
        System.out.println("总共有"+num+"种情况"+ (float)(1/5));
        return list;
    }


    public static ShuZi24 suanzhongjianleft24(int a,int b,int c,int d,int type){
        ShuZi24 sz=null;
        try {

            String fh[] = {"+", "-", "*", "÷"};
            for (int i = 0; i < 4; i++) {
                double  sum1 = jiSuan(a, b, i);
                for (int j = 0; j < 4; j++) {
                   double sum2 = jiSuan(c,  sum1, j);
                    for (int k = 0; k < 4; k++) {
                        double   sum = jiSuan( sum2, d, k);
                        //sum=Math.ceil(sum);
                        if (sum == 24) {
                           // System.out.print( sum1+"=="+sum2+"   ");
                            num++;

                     System.out.println("(" + c + "" + fh[j] + "(" + a + "" + fh[i] + "" + b + "))" + fh[k] + d+"="+sum);

                           return  sendMess(a,b,c,d,fh,i,j,k,type);

                        }

                    }

                }

            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static ShuZi24 suanzhongjianright24(int a,int b,int c,int d,int type){

        try {

            String fh[] = {"+", "-", "*", "÷"};
            for (int i = 0; i < 4; i++) {
                double  sum1 = jiSuan(a, b, i);
                for (int j = 0; j < 4; j++) {
                    double sum2 = jiSuan(  sum1,c, j);
                    for (int k = 0; k < 4; k++) {
                        double   sum = jiSuan( d,sum2, k);
                        //sum=Math.ceil(sum);
                        if (sum == 24) {
                            //System.out.print( sum1+"=="+sum2+"   ");
                            num++;
                        System.out.println(d+fh[k]+"(("+a+fh[i]+b+")"+fh[j]+c+")");

                          return sendMess(a,b,c,d,fh,i,j,k,type);

                        }

                    }

                }

            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static ShuZi24 suanLeft24(int a,int b,int c,int d,int type){
        String fh [] ={"+","-","*","÷"};
        double sum=0;
        for(int i=0;i<4;i++){
            double sum1=jiSuan(a,b,i);
            for(int j=0;j<4;j++){
               double sum2=jiSuan(sum1,c,j);
                for(int k=0;k<4;k++){
                    sum=jiSuan(sum2,d,k);
                    //sum=Math.ceil(sum);
                    if(sum==24){
                        num++;
                         System.out.println("(("+a+fh[i]+b+")"+fh[j]+c+"))"+fh[k]+d);
                       return sendMess(a,b,c,d,fh,i,j,k,type);

                    }
                }
            }
        }
        return null;
    }

    public static ShuZi24 suanRight24(int a,int b,int c,int d,int type){
        String fh [] ={"+","-","*","÷"};
        double sum=0;
        for(int i=0;i<4;i++){
            double  sum1=jiSuan(a,b,i);
            for(int j=0;j<4;j++){
                double  sum2=jiSuan(c,sum1,j);
                for(int k=0;k<4;k++){
                    sum=jiSuan(d,sum2,k);
                   //sum=Math.ceil(sum);
                  //  System.out.println(d+fh[k]+"("+c+fh[j]+"("+a+fh[i]+b+"))="+Math.ceil(sum));
                    if(sum==24){
                        num++;
                      System.out.println(d+fh[k]+"("+c+fh[j]+"("+a+fh[i]+b+"))="+sum);
                       return  sendMess(a,b,c,d,fh,i,j,k,type);
                    }
                }
            }
        }
        return null;
    }

    public static double   jiSuan(double num1,double num2,int type){
        double sum=0;
        if(type==0){
            sum=num1+num2;

        }else if(type==1){
            sum=num1-num2;
        }else if(type==2){
            sum=num1*num2;
        }else{
           if(num2==0){
               return sum=0;
           }
            sum=num1/num2;
        }
        return sum;
    }

    public static ShuZi24 sendMess(int a, int b, int c, int d, String fh [], int i, int j, int k, int type){
            List<Integer> ltt= Arrays.asList(a,b,c,d);
            ltt=ltt.stream().sorted().collect(Collectors.toList());
//            a=ltt.get(0);
//            b=ltt.get(1);
//            c=ltt.get(2);
//            d=ltt.get(3);
            ShuZi24 shuZi24=new ShuZi24();
            shuZi24.setA(ltt.get(0)+"");
            shuZi24.setB(ltt.get(1)+"");
            shuZi24.setC(ltt.get(2)+"");
            shuZi24.setD(ltt.get(3)+"");

            shuZi24.setAbcd(ltt.get(0)+"-"+ltt.get(1)+"-"+ltt.get(2)+"-"+ltt.get(3));
            if(type==101) {
                if (a == b && c == d) {
                    System.out.println("(" + "(" + a + fh[i] + b + ")" + fh[j] + c + ")" + fh[k] + d);
                } else if (a == c && b == d) {
                    System.out.println("(" + "(" + a + fh[i] + b + ")" + fh[j] + c + ")" + fh[k] + d);
                } else if (a == d && c == d) {
                    System.out.println("(" + "(" + a + fh[i] + b + ")" + fh[j] + c + ")" + fh[k] + d);
                }
            }else if(type==102) {
                if (a == b && b == c && d == 1) {
                    System.out.println("(" + c + "" + fh[j] + "(" + a + "" + fh[i] + "" + b + "))" + fh[k] + d);

                } else if (a == d && d == b && c == 1) {
                    System.out.println("(" + c + "" + fh[j] + "(" + a + "" + fh[i] + "" + b + "))" + fh[k] + d);

                } else if (a == d && d == c && b == 1) {
                    System.out.println("(" + c + "" + fh[j] + "(" + a + "" + fh[i] + "" + b + "))" + fh[k] + d);

                } else if (d == b && c == b && a == 1) {
                    System.out.println("(" + c + "" + fh[j] + "(" + a + "" + fh[i] + "" + b + "))" + fh[k] + d);

                }
            }else if(type==103){//表示中间

                String gs="[" + c + "" + fh[j] + "(" + a + "" + fh[i] + "" + b + ")]" + fh[k] + d+"";
                System.out.println(gs);
                shuZi24.setGs(gs);
            }else if(type==104){
                if(!"+".equals(fh[i])&&fh[j].equals(fh[k])) {
                    String gs = "[" + c + "" + fh[j] + "(" + a + "" + fh[i] + "" + b + ")]" + fh[k] + d + "";
                    System.out.println(gs);
                    shuZi24.setGs(gs);
                }else{
                    return null;
                }
            }else if(type==105){
                //"*","÷"
                if("*".equals(fh[i])||"*".equals(fh[j])||"*".equals(fh[k])) {
                    String gs = "[" + c + "" + fh[j] + "(" + a + "" + fh[i] + "" + b + ")]" + fh[k] + d + "";
                    System.out.println(gs);
                    shuZi24.setGs(gs);
                }else if("÷".equals(fh[i])||"÷".equals(fh[j])||"÷".equals(fh[k])){
                    String gs = "[" + c + "" + fh[j] + "(" + a + "" + fh[i] + "" + b + ")]" + fh[k] + d + "";
                    System.out.println(gs);
                    shuZi24.setGs(gs);
                }else{
                    return null;
                }

            }else if(type==201){

                if (a == b && c == d) {
                    System.out.println("(("+a+fh[i]+b+")"+fh[j]+c+"))"+fh[k]+d);
                } else if (a == c && b == d) {
                    System.out.println("(("+a+fh[i]+b+")"+fh[j]+c+"))"+fh[k]+d);
                } else if (a == d && c == d) {
                    System.out.println("(("+a+fh[i]+b+")"+fh[j]+c+"))"+fh[k]+d);
                }
            }else if(type==202){
                if (a == b && b == c && d == 1) {
                    System.out.println("(("+a+fh[i]+b+")"+fh[j]+c+"))"+fh[k]+d);

                } else if (a == d && d == b && c == 1) {
                    System.out.println("(("+a+fh[i]+b+")"+fh[j]+c+"))"+fh[k]+d);

                } else if (a == d && d == c && b == 1) {
                    System.out.println("(("+a+fh[i]+b+")"+fh[j]+c+"))"+fh[k]+d);

                } else if (d == b && c == b && a == 1) {
                    System.out.println("(("+a+fh[i]+b+")"+fh[j]+c+"))"+fh[k]+d);

                }
            }else if(type==203){

                //System.out.println("(("+a+fh[i]+b+")"+fh[j]+c+"))"+fh[k]+d);
                String gs="[("+a+fh[i]+b+")"+fh[j]+c+")]"+fh[k]+d+"";
                System.out.println(gs);
                shuZi24.setGs(gs);
            }else if(type==204) {
                if (!"+".equals(fh[i]) && fh[j].equals(fh[k])) {
                    String gs = "[(" + a + fh[i] + b + ")" + fh[j] + c + ")]" + fh[k] + d + "";
                    System.out.println(gs);
                    shuZi24.setGs(gs);
                } else {
                    return null;
                }
            }else if(type==205){
                if("*".equals(fh[i])||"*".equals(fh[j])||"*".equals(fh[k])) {
                    String gs = "[(" + a + fh[i] + b + ")" + fh[j] + c + ")]" + fh[k] + d + "";
                    System.out.println(gs);
                    shuZi24.setGs(gs);
                }else if("÷".equals(fh[i])||"÷".equals(fh[j])||"÷".equals(fh[k])){
                    String gs = "[(" + a + fh[i] + b + ")" + fh[j] + c + ")]" + fh[k] + d + "";
                    System.out.println(gs);
                    shuZi24.setGs(gs);
                }else{
                    return null;
                }
            } else if(type==301){
                if (a == b && c == d) {
                    System.out.println(d+fh[k]+"("+c+fh[j]+"("+a+fh[i]+b+"))");
                } else if (a == c && b == d) {
                    System.out.println(d+fh[k]+"("+c+fh[j]+"("+a+fh[i]+b+"))");
                } else if (a == d && c == d) {
                    System.out.println(d+fh[k]+"("+c+fh[j]+"("+a+fh[i]+b+"))");
                }
            }else if(type==302){
                if (a == b && b == c && d == 1) {
                    System.out.println(d+fh[k]+"("+c+fh[j]+"("+a+fh[i]+b+"))");

                } else if (a == d && d == b && c == 1) {
                    System.out.println(d+fh[k]+"("+c+fh[j]+"("+a+fh[i]+b+"))");

                } else if (a == d && d == c && b == 1) {
                    System.out.println(d+fh[k]+"("+c+fh[j]+"("+a+fh[i]+b+"))");

                } else if (d == b && c == b && a == 1) {
                    System.out.println(d+fh[k]+"("+c+fh[j]+"("+a+fh[i]+b+"))");

                }
            }else if(type==303){
               // System.out.println(d+fh[k]+"("+c+fh[j]+"("+a+fh[i]+b+"))");
                String gs=d+fh[k]+"["+c+fh[j]+"("+a+fh[i]+b+")]";
                System.out.println(gs);
                shuZi24.setGs(gs);
            }else if(type==304) {
                if (!"+".equals(fh[i]) && fh[j].equals(fh[k])) {
                    // System.out.println(d+fh[k]+"("+c+fh[j]+"("+a+fh[i]+b+"))");
                    String gs = d + fh[k] + "[" + c + fh[j] + "(" + a + fh[i] + b + ")]";
                    System.out.println(gs);
                    shuZi24.setGs(gs);
                } else {
                    return null;
                }
            }else if(type==305){
                if("*".equals(fh[i])||"*".equals(fh[j])||"*".equals(fh[k])) {
                    String gs = d + fh[k] + "[" + c + fh[j] + "(" + a + fh[i] + b + ")]";
                    System.out.println(gs);
                    shuZi24.setGs(gs);
                }else if("÷".equals(fh[i])||"÷".equals(fh[j])||"÷".equals(fh[k])){
                    String gs = d + fh[k] + "[" + c + fh[j] + "(" + a + fh[i] + b + ")]";
                    System.out.println(gs);
                    shuZi24.setGs(gs);
                }else{
                    return null;
                }
            } else if(type==401){
                if (a == b && c == d) {
                    System.out.println(d+fh[k]+"(("+a+fh[i]+b+")"+fh[j]+c+")");
                } else if (a == c && b == d) {
                    System.out.println(d+fh[k]+"(("+a+fh[i]+b+")"+fh[j]+c+")");
                } else if (a == d && c == d) {
                    System.out.println(d+fh[k]+"(("+a+fh[i]+b+")"+fh[j]+c+")");
                }
            }else if(type==402){
                if (a == b && b == c && d == 1) {
                    System.out.println(d+fh[k]+"(("+a+fh[i]+b+")"+fh[j]+c+")");

                } else if (a == d && d == b && c == 1) {
                    System.out.println(d+fh[k]+"(("+a+fh[i]+b+")"+fh[j]+c+")");

                } else if (a == d && d == c && b == 1) {
                    System.out.println(d+fh[k]+"(("+a+fh[i]+b+")"+fh[j]+c+")");

                } else if (d == b && c == b && a == 1) {
                    System.out.println(d+fh[k]+"(("+a+fh[i]+b+")"+fh[j]+c+")");

                }
            }else if(type==403){
               // System.out.println(d+fh[k]+"(("+a+fh[i]+b+")"+fh[j]+c+")");
                String gs=d+fh[k]+"[("+a+fh[i]+b+")"+fh[j]+c+"]"+"";
                System.out.println(gs);
                shuZi24.setGs(gs);
             }else if(type==404){
                if(!"+".equals(fh[i])&&fh[j].equals(fh[k])) {
                    // System.out.println(d+fh[k]+"(("+a+fh[i]+b+")"+fh[j]+c+")");
                    String gs = d + fh[k] + "[(" + a + fh[i] + b + ")" + fh[j] + c + "]" + "";
                    System.out.println(gs);
                    shuZi24.setGs(gs);
                }else{
                    return null;
                }
            }else if(type==405){
                if("*".equals(fh[i])||"*".equals(fh[j])||"*".equals(fh[k])) {
                    String gs = d + fh[k] + "[(" + a + fh[i] + b + ")" + fh[j] + c + "]" + "";
                    System.out.println(gs);
                    shuZi24.setGs(gs);
                }else if("÷".equals(fh[i])||"÷".equals(fh[j])||"÷".equals(fh[k])){
                    String gs = d + fh[k] + "[(" + a + fh[i] + b + ")" + fh[j] + c + "]" + "";
                    System.out.println(gs);
                    shuZi24.setGs(gs);
                }else{
                    return null;
                }
            }
            return shuZi24;
    }


}
