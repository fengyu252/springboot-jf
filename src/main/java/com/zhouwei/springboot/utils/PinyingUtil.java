package com.zhouwei.springboot.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *author:  zhouwei
 *time:  2020/5/20
 *function: 本类提供
 *  1：汉字转换为拼音加音调toPinyinYd
 *  2：汉字转换为拼音toPinyin
 *  3：汉字转换为拼音并且获取首字符toFirstChar
 *  4：提供判断字符是否是汉字 checkcountname
 *  5：提供一串字符是否全部为汉字checkname
 */
public class PinyingUtil {

    public static void main(String[] args) {
        System.out.println(toPinyinYd(changOtherHanZi("江南可采莲，莲叶何田田.")));
        System.out.println(toPinyin("江南可采莲"));
        System.out.println(toFirstChar("白日依山尽"));
        System.out.println(checkname("江南可采莲，莲叶何田田"));
        System.out.println(changOtherHanZi("江南可采莲，莲叶何田田."));

    }
    /**
     *author:  zhouwei
     *time:  2020/5/14
     *function: 汉子转换为拼音 加上音调
     */
    public static String toPinyinYd(String chinese){
        String pinyinYdStr = "";
        char[] newChar = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        //defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);

        for (int i = 0; i < newChar.length; i++) {
            if (newChar[i] > 128) {
                try {
                  String [] s=  PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat);
                  //System.out.println(s[0]);
                  pinyinYdStr += s[0]+" ";
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            }else{
                pinyinYdStr += newChar[i];
            }
        }
        return pinyinYdStr;
    }

    /**
     *author:  zhouwei
     *time:  2020/5/20
     *function: 返回汉字拼音
     */
    public static String toPinyin(String chinese){
        String pinyinStr = "";
        char[] newChar = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();

        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);


        for (int i = 0; i < newChar.length; i++) {
            if (newChar[i] > 128) {
                try {
                    pinyinStr += PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat)[0]+" ";
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            }else{
                pinyinStr += newChar[i];
            }
        }
        return pinyinStr;
    }
    /**
     *author:  zhouwei
     *time:  2020/5/14
     *function: 一串汉字返回第一个汉子的字母
     */
    public static String toFirstChar(String chinese){
        String pinyinStr = "";
        char[] newChar = chinese.toCharArray();  //转为单个字符
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < newChar.length; i++) {
            if (newChar[i] > 128) {
                try {
                    pinyinStr += PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat)[0].charAt(0);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            }else{
                pinyinStr += newChar[i];
            }
        }
        return pinyinStr;
    }

    /**
     *author:  zhouwei
     *time:  2020/5/20
     *function: 判断字符是否汉字
     */
    public static boolean checkcountname(String countname)
    {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(countname);
        if (m.find()) {
            return true;
        }
        return false;
    }
    /**
     *author:  zhouwei
     *time:  2020/5/20
     *function:  判断整个字符串都由汉字组成
     */
    public static boolean checkname(String name)
    {
        int n = 0;
        for(int i = 0; i < name.length(); i++) {
            n = (int)name.charAt(i);
            if(!(19968 <= n && n <40869)) {
                return false;
            }
        }
        return true;
    }
    /**
     *author:  zhouwei
     *time:  2020/5/22
     *function: 把一串字符中不是汉字的去掉
     */
    public static String changOtherHanZi(String name){
        int n = 0;
        for(int i = 0; i < name.length(); i++) {
            n = (int)name.charAt(i);
            if(!(19968 <= n && n <40869)) {
                //System.out.println("==="+name.charAt(i));
               name =name.replace(name.charAt(i)+""," ");

            }
        }

        return name;
    }


}
