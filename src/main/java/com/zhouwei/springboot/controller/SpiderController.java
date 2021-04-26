package com.zhouwei.springboot.controller;


import com.zhouwei.springboot.entity.Cy;
import com.zhouwei.springboot.entity.ShuZi24;
import com.zhouwei.springboot.entity.SjStory;
import com.zhouwei.springboot.entity.TangShi;
import com.zhouwei.springboot.server.SjStoryServer;
import com.zhouwei.springboot.test.All24;
import com.zhouwei.springboot.utils.ReadTxtCyUtil;
import com.zhouwei.springboot.utils.SpiderHtml;
import com.zhouwei.springboot.utils.SpiderJk1Html;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/spider")
public class SpiderController {


    @Autowired
    SjStoryServer sjStoryServer;






    @GetMapping("/spiderStory/{types}")
    public String spiderStory(@PathVariable("types") Integer types){
        String url="";

        try {
            for(int i=1;i<=10;i++) {
                if(i>1) {
                    //url = "http://www.gushi365.com/Dadmom/index_"+i+".html";
                    url="https://tiba.jsyks.com/kmstk_2004_"+i;
                    //url="";
                }else{
                    //url = "http://www.gushi365.com/Dadmom/index.html";
                    url="https://tiba.jsyks.com/kmstk_2004";
                }
                System.out.println(url);
                List<SjStory> list = SpiderJk1Html.findJkTkDetail(url);
                sjStoryServer.addSjStory(list);
                Thread.sleep(3000);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return "ok";
    }

    @GetMapping(value={"/downImgMess"})
    public String downImgMess(){
        try {
            String name = "交通信号";
            Map mp = new HashMap();
            mp.put("name", name);
            mp.put("ltype",1);
            sjStoryServer.downImgUpateR2(mp);
            return "success";
        }catch (Exception e){
            e.printStackTrace();
        }
        return "okk";
    }


    /**
     * Excel表格导出接口
     * http://localhost/spider/ExcelDownload
     * @param response response对象
     * @throws IOException 抛IO异常
     */
    @RequestMapping("/ExcelDownload3/{type}")
    public void excelDownload3(HttpServletResponse response, @PathVariable("type") Integer type) throws IOException {
        //表头数据
        String[] header = {"昵称", "拼音", "释义", "出处", "示例",};

        String url="https://so.gushiwen.org/wenyan/gaowen.aspx";
        List<Cy> list= ReadTxtCyUtil.redTextCy();

        //  SpiderHtml.getTanshiAuthor(); 作者
        //声明一个工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();

        //生成一个表格，设置表格名称为"学生表"
        HSSFSheet sheet = workbook.createSheet("学生表");

        //设置表格列宽度为10个字节
        sheet.setDefaultColumnWidth(10);

        //创建第一行表头
        HSSFRow headrow = sheet.createRow(0);

        //遍历添加表头(下面模拟遍历学生，也是同样的操作过程)
        for (int i = 0; i < header.length; i++) {
            //创建一个单元格
            HSSFCell cell = headrow.createCell(i);

            //创建一个内容对象
            HSSFRichTextString text = new HSSFRichTextString(header[i]);

            //将内容对象的文字内容写入到单元格中
            cell.setCellValue(text);
        }

        //模拟遍历结果集，把内容加入表格
        //模拟遍历第一个学生
//        HSSFRow row1 = sheet.createRow(1);
//        for (int i = 0; i < student1.length; i++) {
//            HSSFCell cell = row1.createCell(i);
//            HSSFRichTextString text = new HSSFRichTextString(student1[i]);
//            cell.setCellValue(text);
//        }

        for(int i=0;i<list.size();i++){
            Cy ts=list.get(i);
            HSSFRow row1 = sheet.createRow(1+i);
            row1.createCell(0).setCellValue(ts.getName());
            row1.createCell(1).setCellValue(ts.getPy());
            row1.createCell(2).setCellValue(ts.getSy());
            row1.createCell(3).setCellValue(ts.getCc());
            row1.createCell(4).setCellValue(ts.getSl());
        }

//        //模拟遍历第三个学生
//        HSSFRow row3 = sheet.createRow(3);
//        for (int i = 0; i < student3.length; i++) {
//            HSSFCell cell = row3.createCell(i);
//            HSSFRichTextString text = new HSSFRichTextString(student3[i]);
//            cell.setCellValue(text);
//        }

        //准备将Excel的输出流通过response输出到页面下载
        //八进制输出流
        response.setContentType("application/octet-stream");

        //这后面可以设置导出Excel的名称，此例中名为student.xls
        response.setHeader("Content-disposition", "attachment;filename=tangshi300.xls");

        //刷新缓冲
        response.flushBuffer();

        //workbook将Excel写入到response的输出流中，供页面下载
        workbook.write(response.getOutputStream());
    }


    /**
     * Excel表格导出接口
     * http://localhost/spider/ExcelDownload
     * @param response response对象
     * @throws IOException 抛IO异常
     */
    @RequestMapping("/ExcelDownload2/{type}")
    public void excelDownload2(HttpServletResponse response, @PathVariable("type") Integer type) throws IOException {
        //表头数据
        String[] header = {"年级", "名字", "内容", "解释", "注释",};

        String url="https://so.gushiwen.org/wenyan/gaowen.aspx";
       // List<TangShi> list=SpiderHtml.getTanshi(url);//宋词精选
        List<TangShi> list = SpiderHtml.getTanshiAuthor(); //作者
        //声明一个工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();

        //生成一个表格，设置表格名称为"学生表"
        HSSFSheet sheet = workbook.createSheet("学生表");

        //设置表格列宽度为10个字节
        sheet.setDefaultColumnWidth(10);

        //创建第一行表头
        HSSFRow headrow = sheet.createRow(0);

        //遍历添加表头(下面模拟遍历学生，也是同样的操作过程)
        for (int i = 0; i < header.length; i++) {
            //创建一个单元格
            HSSFCell cell = headrow.createCell(i);

            //创建一个内容对象
            HSSFRichTextString text = new HSSFRichTextString(header[i]);

            //将内容对象的文字内容写入到单元格中
            cell.setCellValue(text);
        }

        //模拟遍历结果集，把内容加入表格
        //模拟遍历第一个学生
//        HSSFRow row1 = sheet.createRow(1);
//        for (int i = 0; i < student1.length; i++) {
//            HSSFCell cell = row1.createCell(i);
//            HSSFRichTextString text = new HSSFRichTextString(student1[i]);
//            cell.setCellValue(text);
//        }

        for(int i=0;i<list.size();i++){
            TangShi ts=list.get(i);
            HSSFRow row1 = sheet.createRow(1+i);
            row1.createCell(0).setCellValue(ts.getNj());
            row1.createCell(1).setCellValue(ts.getName());
            row1.createCell(2).setCellValue(ts.getContent());
            row1.createCell(3).setCellValue(ts.getYw());
            row1.createCell(4).setCellValue(ts.getZs());
        }

//        //模拟遍历第三个学生
//        HSSFRow row3 = sheet.createRow(3);
//        for (int i = 0; i < student3.length; i++) {
//            HSSFCell cell = row3.createCell(i);
//            HSSFRichTextString text = new HSSFRichTextString(student3[i]);
//            cell.setCellValue(text);
//        }

        //准备将Excel的输出流通过response输出到页面下载
        //八进制输出流
        response.setContentType("application/octet-stream");

        //这后面可以设置导出Excel的名称，此例中名为student.xls
        response.setHeader("Content-disposition", "attachment;filename=aa.xls");

        //刷新缓冲
        response.flushBuffer();

        //workbook将Excel写入到response的输出流中，供页面下载
        workbook.write(response.getOutputStream());
    }


    /**
     * Excel表格导出接口
     * http://localhost/spider/ExcelDownload2
     * @param response response对象
     * @throws IOException 抛IO异常
     */
    @RequestMapping("/ExcelDownload/{type}")
    public void excelDownload(HttpServletResponse response, @PathVariable("type") Integer type) throws IOException {
        //表头数据
        String[] header = {"公式", "A", "B", "C", "D","NUM"};

        String url="https://so.gushiwen.org/wenyan/gaowen.aspx";
        List<ShuZi24> list= All24.findAll24();//宋词精选
        //list.stream().filter(b -> !b.getAbcd().isEmpty()).distinct().collect(Collectors.toList());
       // list=list.stream().filter(Objects::nonNull).collect(Collectors.toList());

        //  SpiderHtml.getTanshiAuthor(); 作者

            //声明一个工作簿
            HSSFWorkbook workbook = new HSSFWorkbook();


            //生成一个表格，设置表格名称为"学生表"
            HSSFSheet sheet = workbook.createSheet("数据");

            //设置表格列宽度为10个字节
            sheet.setDefaultColumnWidth(10);

            //创建第一行表头
            HSSFRow headrow = sheet.createRow(0);

            //遍历添加表头(下面模拟遍历学生，也是同样的操作过程)
            for (int i = 0; i < header.length; i++) {
                //创建一个单元格
                HSSFCell cell = headrow.createCell(i);

                //创建一个内容对象
                HSSFRichTextString text = new HSSFRichTextString(header[i]);

                //将内容对象的文字内容写入到单元格中
                cell.setCellValue(text);
            }

            //模拟遍历结果集，把内容加入表格
            //模拟遍历第一个学生
//        HSSFRow row1 = sheet.createRow(1);
//        for (int i = 0; i < student1.length; i++) {
//            HSSFCell cell = row1.createCell(i);
//            HSSFRichTextString text = new HSSFRichTextString(student1[i]);
//            cell.setCellValue(text);
//        }
//            int startNum=65555*(type-1);
//            int endNum=65555*type;
            for (int i = 0; i < list.size(); i++) {

                    ShuZi24 ts = list.get(i);
                    HSSFRow row1 = sheet.createRow(1 + i);
                    row1.createCell(0).setCellValue(ts.getGs());
                    row1.createCell(1).setCellValue(ts.getA());
                    row1.createCell(2).setCellValue(ts.getB());
                    row1.createCell(3).setCellValue(ts.getC());
                    row1.createCell(4).setCellValue(ts.getD());
                    row1.createCell(5).setCellValue(ts.getAbcd());

            }

//        //模拟遍历第三个学生
//        HSSFRow row3 = sheet.createRow(3);
//        for (int i = 0; i < student3.length; i++) {
//            HSSFCell cell = row3.createCell(i);
//            HSSFRichTextString text = new HSSFRichTextString(student3[i]);
//            cell.setCellValue(text);
//        }
        //准备将Excel的输出流通过response输出到页面下载
        //八进制输出流
        response.setContentType("application/octet-stream");

        //这后面可以设置导出Excel的名称，此例中名为student.xls
        response.setHeader("Content-disposition", "attachment;filename=ABCD("+type+").xlsx");

        //刷新缓冲
        response.flushBuffer();

        //workbook将Excel写入到response的输出流中，供页面下载
        workbook.write(response.getOutputStream());
    }


    /**
     *author:  zhouwei
     *time:  2020/4/20
     *function:
     */
    @RequestMapping("/ExcelDownloadDetail/{type}")
    public void downTaskDetail(HttpServletResponse response, @PathVariable("type") Integer type) throws IOException {
        //表头数据
        String[] header = {"IDFA", "时间", "IP", "BundlId", "昵称","model","oss","关键词","回调时间"};

        String url="https://so.gushiwen.org/wenyan/gaowen.aspx";
//        List<ShuZi24> list= All24.findAll24();//宋词精选
        List<ShuZi24> list=null;
        //  SpiderHtml.getTanshiAuthor(); 作者
        //声明一个工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();

        //生成一个表格，设置表格名称为"学生表"
        HSSFSheet sheet = workbook.createSheet("学生表");

        //设置表格列宽度为10个字节
        sheet.setDefaultColumnWidth(10);

        //创建第一行表头
        HSSFRow headrow = sheet.createRow(0);

        //遍历添加表头(下面模拟遍历学生，也是同样的操作过程)
        for (int i = 0; i < header.length; i++) {
            //创建一个单元格
            HSSFCell cell = headrow.createCell(i);

            //创建一个内容对象
            HSSFRichTextString text = new HSSFRichTextString(header[i]);

            //将内容对象的文字内容写入到单元格中
            cell.setCellValue(text);
        }

        //模拟遍历结果集，把内容加入表格
        //模拟遍历第一个学生
//        HSSFRow row1 = sheet.createRow(1);
//        for (int i = 0; i < student1.length; i++) {
//            HSSFCell cell = row1.createCell(i);
//            HSSFRichTextString text = new HSSFRichTextString(student1[i]);
//            cell.setCellValue(text);
//        }

        for(int i=0;i<list.size();i++){
            ShuZi24 ts=list.get(i);
            HSSFRow row1 = sheet.createRow(1+i);
            row1.createCell(0).setCellValue(ts.getGs());
            row1.createCell(1).setCellValue(ts.getA());
            row1.createCell(2).setCellValue(ts.getB());
            row1.createCell(3).setCellValue(ts.getC());
            row1.createCell(4).setCellValue(ts.getD());
            row1.createCell(5).setCellValue(ts.getAbcd());
        }

//        //模拟遍历第三个学生
//        HSSFRow row3 = sheet.createRow(3);
//        for (int i = 0; i < student3.length; i++) {
//            HSSFCell cell = row3.createCell(i);
//            HSSFRichTextString text = new HSSFRichTextString(student3[i]);
//            cell.setCellValue(text);
//        }

        //准备将Excel的输出流通过response输出到页面下载
        //八进制输出流
        response.setContentType("application/octet-stream");

        //这后面可以设置导出Excel的名称，此例中名为student.xls
        response.setHeader("Content-disposition", "attachment;filename=tangshi300.xlsx");

        //刷新缓冲
        response.flushBuffer();

        //workbook将Excel写入到response的输出流中，供页面下载
        workbook.write(response.getOutputStream());
    }

}
