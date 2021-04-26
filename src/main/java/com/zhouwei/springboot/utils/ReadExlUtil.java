package com.zhouwei.springboot.utils;

import com.zhouwei.springboot.entity.Cy;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *author:  zhouwei
 *time:  2020/5/25
 *function: exl 读取数据
 */
public class ReadExlUtil {

    Logger logger= LoggerFactory.getLogger(getClass());


    public static List readExl(InputStream inputStream,String fileName){
        List list=new ArrayList();
        Workbook workbook=null;
        try{
            //判断什么类型文件
            if (fileName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (fileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            }
            if(workbook == null){
                return null;
            }else{
                //获取所有的工作表的的数量
                int numOfSheet = workbook.getNumberOfSheets();
                System.out.println(numOfSheet+"--->numOfSheet");
                //遍历表
                for (int i = 0; i < numOfSheet; i++) {
                    //获取一个sheet也就是一个工作本。
                    Sheet sheet = workbook.getSheetAt(i);
                    if (sheet == null) continue;
                    //获取一个sheet有多少Row
                    int lastRowNum = sheet.getLastRowNum();
                    if (lastRowNum == 0) continue;
                    Row row;
                    for (int j = 1; j <= lastRowNum; j++) {
                        row = sheet.getRow(j);
                        if (row == null) {
                            continue;
                        }
                        //获取一个Row有多少Cell
                        short lastCellNum = row.getLastCellNum();
                        for (int k = 0; k <= lastCellNum; k++) {
                            if (row.getCell(k) == null) {
                                continue;
                            }
                            row.getCell(k).setCellType(CellType.STRING);
                            //row.getCell(k).setCellType(Cell.CELL_TYPE_STRING);
                            String res = row.getCell(k).getStringCellValue().trim();
                            //打印出cell(单元格的内容)


                            String str= PinyingUtil.changOtherHanZi(res);

                            String py=PinyingUtil.toPinyinYd(str);
                            System.out.println(py);
                            System.out.println(res.trim());
                        }
                        System.out.println();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<Map<Object,String>> readExlTanshi(InputStream inputStream, String fileName){
        List<Map<Object,String>> list=new ArrayList<Map<Object,String>>();
        Workbook workbook=null;
        try{
            //判断什么类型文件
            if (fileName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (fileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            }
            if(workbook == null){
                return null;
            }else{
                //获取所有的工作表的的数量
                int numOfSheet = workbook.getNumberOfSheets();
                System.out.println("有几个工作簿："+numOfSheet+"");
                //遍历表
                for (int i = 0; i < numOfSheet; i++) {
                    //获取一个sheet也就是一个工作本。
                    Sheet sheet = workbook.getSheetAt(i);
                    if (sheet == null) continue;
                    //获取一个sheet有多少Row
                    int lastRowNum = sheet.getLastRowNum();
                    if (lastRowNum == 0) continue;
                    Row row;
                    for (int j = 1; j <= lastRowNum; j++) {
                        row = sheet.getRow(j);
                        if (row == null) {
                            continue;
                        }
                        //获取一个Row有多少Cell
                        short lastCellNum = row.getLastCellNum();
                        if(lastCellNum>=0){
                           Map mp=new HashMap();
                           mp.put("name",row.getCell(0).getStringCellValue().trim());
                            String names=row.getCell(0).getStringCellValue().trim();
                            names= PinyingUtil.changOtherHanZi(names);
                            names=PinyingUtil.toPinyinYd(names);
                            mp.put("names",names);
                           mp.put("author",row.getCell(1).getStringCellValue().trim());
                           String authorpy=row.getCell(1).getStringCellValue().trim();
                            authorpy= PinyingUtil.changOtherHanZi(authorpy);
                            authorpy=PinyingUtil.toPinyinYd(authorpy);
                           mp.put("authorpy",authorpy);
                           mp.put("contents",row.getCell(2).getStringCellValue().trim());
                            String contentspy=row.getCell(2).getStringCellValue().trim();
                            contentspy= PinyingUtil.changOtherHanZi(contentspy);
                            contentspy=PinyingUtil.toPinyinYd(contentspy);
                           mp.put("contentspy",contentspy);
                           mp.put("js",row.getCell(3).getStringCellValue().trim());
                           mp.put("zs",row.getCell(4).getStringCellValue().trim());
                           mp.put("amess",row.getCell(5).getStringCellValue().trim());
                           mp.put("class",row.getCell(6).getStringCellValue().trim());
                           mp.put("type","0");
                           mp.put("py","0");
                           mp.put("r1",row.getCell(9).getStringCellValue().trim());
                           list.add(mp);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list.size()>0?list:null;
    }
    /**
     *author:  zhouwei
     *time:  2020/5/26
     *function: 写入本地文件exl
     */
    public static String writeExl( List<Map<Object,String>> list){
        try{
            String [] header={"昵称","昵称拼音","作者","内容","内容拼音",
                    "解释","注释","作者简介","类型","type","py","音频地址"};
            //声明一个工作簿
            XSSFWorkbook xssfWorkbook=new XSSFWorkbook();
            //生成一个表格，设置表格名称为
            XSSFSheet sheet=xssfWorkbook.createSheet("gushi");
            //设置表格列宽度为10个字节
            sheet.setDefaultColumnWidth(10);
            //创建第一行表头
            XSSFRow headrow = sheet.createRow(0);

            //遍历添加表头(下面模拟遍历学生，也是同样的操作过程)
            for (int i = 0; i < header.length; i++) {
                //创建一个单元格
                XSSFCell cell = headrow.createCell(i);
                //创建一个内容对象

                XSSFRichTextString text = new XSSFRichTextString(header[i]);
                //将内容对象的文字内容写入到单元格中
                cell.setCellValue(text);
            }

            for(int i=0;i<list.size();i++){
                Map<Object,String> mp=list.get(i);
                XSSFRow row1 = sheet.createRow(1+i);
                row1.createCell(0).setCellValue(mp.get("name"));
                row1.createCell(1).setCellValue(mp.get("names"));
                row1.createCell(2).setCellValue(mp.get("author"));
                row1.createCell(3).setCellValue(mp.get("contents"));
                row1.createCell(4).setCellValue(mp.get("contentspy"));
                row1.createCell(5).setCellValue(mp.get("js"));
                row1.createCell(6).setCellValue(mp.get("zs"));
                row1.createCell(7).setCellValue(mp.get("amess"));
                row1.createCell(8).setCellValue(mp.get("class"));
                row1.createCell(9).setCellValue(mp.get("type"));
                row1.createCell(10).setCellValue(mp.get("py"));
                row1.createCell(11).setCellValue(mp.get("r1"));
            }
            LocalDateTime localDateTime=LocalDateTime.now();
            File file=new File("E:\\"+localDateTime.getSecond()+".xlsx");
            if(!file.exists()){
               // file.mkdirs(); 创建文件夹
                file.createNewFile();
            }
            FileOutputStream os=new FileOutputStream(file);
            xssfWorkbook.write(os);
            return "success";
        }catch (Exception e){
            e.printStackTrace();
        }
        return "faile";
    }

    public static void main(String[] args) {
        File file=new File("E:\\1-6.xlsx");
        try {
            InputStream inputStream = new FileInputStream(file);
            //readExl(inputStream,file.getName());
            List<Map<Object,String>> list=readExlTanshi(inputStream,file.getName());
            if(!list.isEmpty()){
                System.out.println(writeExl(list));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
