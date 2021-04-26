package com.zhouwei.springboot.controller;

import com.zhouwei.springboot.entity.SjTaskDetail;
import com.zhouwei.springboot.server.SjTaskServer;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 *author:  zhouwei
 *time:  2020/4/23
 *function: 跑单Exl 导出
 */
@Controller
public class ExlDownLoadController {

    @Autowired
    SjTaskServer sjTaskServer;

    /**
     *author:  zhouwei
     *time:  2020/4/20
     *function:
     */
    @RequestMapping("/downTaskExl/{taskId}/{bType}/{sjNumber}")
    public void downTaskDetail(HttpServletResponse response,
                               @PathVariable("taskId") Integer taskId,
                               @PathVariable("bType") Integer bType,
                               @PathVariable("sjNumber") Integer sjNumber) throws IOException {
        //表头数据
        String[] header = {"IDFA", "时间","appstoreId", "IP", "BundlId", "昵称","model","oss","关键词","回调时间"};

       List<SjTaskDetail> list=sjTaskServer.findSjDetailMess(taskId,bType,0,0);

        //声明一个工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();

        //生成一个表格，设置表格名称为"学生表"
        HSSFSheet sheet = workbook.createSheet("xq");

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
        String name="";
        for(int i=0;i<list.size();i++){
            SjTaskDetail ts=list.get(i);
            HSSFRow row1 = sheet.createRow(1+i);
            name=ts.getNames();
            row1.createCell(0).setCellValue(ts.getIdfa());
            row1.createCell(1).setCellValue(ts.getChangtime());
            row1.createCell(2).setCellValue(ts.getAppstoreid());
            row1.createCell(3).setCellValue(ts.getIp());
            row1.createCell(4).setCellValue(ts.getBundleid());
            row1.createCell(5).setCellValue(ts.getNames());
            row1.createCell(6).setCellValue(ts.getModel());
            row1.createCell(7).setCellValue(ts.getOss());
            row1.createCell(8).setCellValue(ts.getR1());
            row1.createCell(9).setCellValue(ts.getCallbacktime());
        }



        //准备将Excel的输出流通过response输出到页面下载
        //八进制输出流
        response.setContentType("application/octet-stream");

        //这后面可以设置导出Excel的名称，此例中名为student.xls

        String fileName= URLEncoder.encode(name+"("+sjNumber+")","utf-8");
        response.setHeader("Content-disposition", "attachment;filename="+fileName+".xlsx");

        //刷新缓冲
        response.flushBuffer();

        //workbook将Excel写入到response的输出流中，供页面下载
        workbook.write(response.getOutputStream());
    }

}
