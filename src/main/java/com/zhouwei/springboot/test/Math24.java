package com.zhouwei.springboot.test;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Math24 {


    /**
     *author:  zhouwei
     *time:  2020/3/4
     *function: 计算结果是24的
     */
    public  String res(int a,int b,int c,int d){
        int r=0;
        try {
           return  jisuan(a,b,c,d,0,0,0);
//           for(int n=0;n<=3;n++){
//               for(int nn=0;nn<=3;nn++){
//                   for(int nnn=0;nnn<=3;nnn++){
//                      int result= jisuan(a,b,c,d,n,nn,nnn);
//
//                   }
//               }
//           }
        }catch (Exception e){
            e.printStackTrace();
        }
      return null;

    }
    public  String jisuan(double a,double b,double c,double d,int n,int nn,int nnn){
        String f [] ={"+","-","*","/"};
        //int ressg=Integer.parseInt(a+f[n]+b+f[nn]+c+f[nnn]+d);

        // a+b+c+d  a+b-c-d a+b-c+d a+b+c-d
        // a-b+c+d  a-b-c+d a-b+c-d
        // a*b+c+d  a*b-c+d a*b-c-d a*b+c-d
        // a*b/c+d  a*b/c-d a*b+c/d a*b-c/d
        // a*b*c+d  a*b*c-d a*b-c*d a*b+c*d
        // a/b+c+d  a/b-c-d a/b+c-d a/b-c+d
        // a/b/c+d  a/b/c-d
        // a/b*c+d  a/b*c-d a/b+c*d a/b-c*d
        // a/b*c*d

        double ress=((a/b)+c)+d;
        if(ress==24){
            String s= ""+(int)a+"-"+(int)b+"+"+(int)c+"+"+(int)d+"";
            System.out.println(s);
            return s;
        }

        return null;
    }

    public List backMath24(){
        List list=new ArrayList();
        String f [] ={"+","-","*","/"};
        for(int i=1;i<=13;i++){
            for(int k=1;k<=13;k++) {
                for(int l=1;l<=13;l++) {
                    for(int m=1;m<=13;m++) {
                        String ress= res(i,k,l,m);
                        if(ress!=null){
                            list.add(ress);
                        }
                    }
                }
            }
        }
        System.out.println(" 总数:"+list.size());
        return list;
    }


    public static void main(String[] args) {
       Math24 math24=new Math24();
        List list=new ArrayList();
        String f [] ={"+","-","*","/"};
        for(int i=1;i<=13;i++){
            for(int k=1;k<=13;k++) {
                for(int l=1;l<=13;l++) {
                    for(int m=1;m<=13;m++) {
                       String ress= math24.res(i,k,l,m);
                       if(ress!=null){
                           list.add(ress);
                       }
                    }
                }
            }
        }
        System.out.println(" 总数:"+list.size());
    }
}
