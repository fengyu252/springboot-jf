package com.zhouwei.springboot.utils;

import com.zhouwei.springboot.entity.Cy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ReadTxtCyUtil {

    public static void main(String[] args) {
        String cyy="樽酒论文  & 拼音：zūn jiǔ lùn wén     & 释义：一边喝酒，一边议论文章。     " +
                "& 出处：唐·杜甫《春日忆李白》诗何时一樽酒，重与细论文。”     " +
                "& 示例：连年客里度初度，～第一遭。★陈世宜《上巳社集是日值余初度》诗\n";
//        String [] s=cyy.split("&");
//        System.out.println(s[0]+"****");
        redTextCy();
    }

    public static List<Cy> redTextCy(){
        List<Cy> list=new ArrayList<Cy>();
        String content=null;
        try {
            File file = new File("E://cy.txt");
            String encoding = "UTF-8";
            InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file), encoding);//考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            Cy cy=null;

            while ((lineTxt = bufferedReader.readLine()) != null) {
                cy=new Cy();
                content=lineTxt
                        .replaceAll("\uFEFF","")
                        .replaceAll("-","")
                        .toString().trim();
                if(!content.trim().isEmpty()) {
                    String[] s = content.split("&");
                    // System.out.println(content);


                    if (s.length > 1) {
//                        System.out.println(content);
                      // System.out.println(s[0]+"*****"+s[1]+"****"+s[2]+"****");
//                        System.out.println(s.length);
                        cy.setName(s[0].trim());
                        cy.setPy(s[1].trim());
                        if(s.length>=3)
                            cy.setSy(s[2].trim());
                        if(s.length>=4)
                            cy.setCc(s[3].trim());
                        if(s.length>=5) {
                            cy.setSl(s[4].trim());
                        }

                        System.out.println(cy.getName() + " = " + cy.getPy() + " = " + cy.getSy() + " = " + cy.getCc() + " = " + cy.getSl());
                        list.add(cy);
                    }
                    //break;
                }
            }
            read.close();
        }catch (Exception e){

            e.printStackTrace();
            System.out.println(content);

        }
        return list;
    }

}
