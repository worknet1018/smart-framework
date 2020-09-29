package org.smart4j.framework.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符转换工具
 */
public final class StringUtil {

    /**
     * 字符串分隔符
     */
    public static final String SEPARATOR =String.valueOf((char) 29);

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        if (str != null) {
            str=str.trim();
        }
        return StringUtils.isEmpty(str);
    }

    /**
     * 判断字符串是否非空
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }

    public static String[] splitString(String body, String s) {
        return StringUtils.split(body, s);
    }
}
