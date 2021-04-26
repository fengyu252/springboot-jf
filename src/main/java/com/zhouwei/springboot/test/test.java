package com.zhouwei.springboot.test;

import org.apache.commons.collections4.IterableMap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class test {

    static int num=0;//计算总共有多少种情况
    public static void main(String[] args) {
        /*
         * 4层for循环获取4张扑克牌所有的可能组合
         * 将组合存入数组poker
         */
        for(int i=1;i<=13;i++){
            for(int j=1;j<=13;j++){
                for(int k=1;k<=13;k++){
                    for(int m=1;m<=13;m++){
                        int[] poker=new int[]{i,j,k,m};
                        opertor(poker);
                    }
                }
            }
        }
        System.out.println("总共有"+num+"种情况");
    }
    /*
     * 使用3层for循环来获取运算符的所有组合存入symbolNum数组
     * 调用symbol方法获得与数字对应的运算符输出
     */
    public static void opertor(int[] poker){
        double sum=0;
        for(int i=0;i<4;i++){
            double sum1=code(poker[0],poker[1],i);
            for(int j=0;j<4;j++){
                double sum2=code(sum1,poker[2],j);
                for(int k=0;k<4;k++){
                    sum=code(sum2,poker[3],k);
                    int[] symbolNum={i,j,k};
                    String[] symbol=new String[4];
                    symbol=symbol(symbolNum);
                    if(sum==24) {
                        num++;

//                        if (poker[0] == poker[1] && poker[2] == poker[3]) {
//                            System.out.println("[" + "(" + poker[0] + " " + symbol[0] + " " + poker[1] + ")" + " " + symbol[1] + " " + poker[2] + "]" + " " + symbol[2] + " " + poker[3]);
//
//                        } else if (poker[0] == poker[2] && poker[1] == poker[3]) {
//                            System.out.println("[" + "(" + poker[0] + " " + symbol[0] + " " + poker[1] + ")" + " " + symbol[1] + " " + poker[2] + "]" + " " + symbol[2] + " " + poker[3]);
//
//                        } else if (poker[0] == poker[3] && poker[1] == poker[2]) {
//                            //System.out.println("["+"("+poker[0]+" "+symbol[0]+" "+poker[1]+")"+" "+symbol[1]+" "+poker[2]+"]"+" "+symbol[2]+" "+poker[3]);
//                            System.out.println("[" + "(" + poker[0] + " " + symbol[0] + " " + poker[1] + ")" + " " + symbol[1] + " " + poker[2] + "]" + " " + symbol[2] + " " + poker[3]);
//
//                        }
                        if (poker[0] ==5 && poker[1]==5 && poker[2] ==5&& poker[3]==1) {
                            System.out.println("[" + "(" + poker[0] + " " + symbol[0] + " " + poker[1] + ")" + " " + symbol[1] + " " + poker[2] + "]" + " " + symbol[2] + " " + poker[3]);
//
                        }else if(poker[0] ==5 && poker[1]==5 && poker[2] ==1&& poker[3]==5){
                            System.out.println("[" + "(" + poker[0] + " " + symbol[0] + " " + poker[1] + ")" + " " + symbol[1] + " " + poker[2] + "]" + " " + symbol[2] + " " + poker[3]);
//
                        }else if(poker[0] ==5 && poker[1]==1 && poker[2] ==5&& poker[3]==5){
                            System.out.println("[" + "(" + poker[0] + " " + symbol[0] + " " + poker[1] + ")" + " " + symbol[1] + " " + poker[2] + "]" + " " + symbol[2] + " " + poker[3]);
//
                        }else if(poker[0] ==1 && poker[1]==5 && poker[2] ==5&& poker[3]==5){
                            System.out.println("[" + "(" + poker[0] + " " + symbol[0] + " " + poker[1] + ")" + " " + symbol[1] + " " + poker[2] + "]" + " " + symbol[2] + " " + poker[3]);
//
                        }
                        //System.out.println("[" + "(" + poker[0] + " " + symbol[0] + " " + poker[1] + ")" + " " + symbol[1] + " " + poker[2] + "]" + " " + symbol[2] + " " + poker[3]);
//
                    }
                }
            }
        }
    }
    /*
     * 通过符号来计算两个数并返回
     */
    public static double code(double num1,double num2,int num){
        double sum=0.0;
        if(num==0){
            sum=num1+num2;
        }else if(num==1){
            sum=num1-num2;
        }else if(num==2){
            sum=num1*num2;
        }else{
            sum=num1/num2;
        }
        return sum;
    }
    /*
     * 将代表计算符号的数字转换成字符存入Sring数组并返回
     */
    public static String[] symbol(int[] symbolNum){
        String[] symbol=new String[4];
        for(int i=0;i<3;i++){
            int sym=symbolNum[i];
            switch (sym) {
                case 0:
                    symbol[i]="+";
                    break;
                case 1:
                    symbol[i]="-";
                    break;
                case 2:
                    symbol[i]="x";
                    break;
                case 3:
                    symbol[i]="÷";
                    break;
                default:
                    break;
            }
        }
        return symbol;
    }
}
