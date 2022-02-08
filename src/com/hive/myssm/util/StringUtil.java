package com.hive.myssm.util;

/**
 * @author Hive
 * Description: 字符串快捷判空的工具类
 * Date: 2022/2/8 10:30
 */

public class StringUtil {
    //判断字符串是否为null或者""
    public static boolean isEmpty(String str){
        return str==null || "".equals(str);
    }

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }
}

