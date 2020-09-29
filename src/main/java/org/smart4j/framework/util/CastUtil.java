package org.smart4j.framework.util;

public final class CastUtil {

    /**
     * 转为String型
     * @param obj
     * @return
     */
    public static String castString(Object obj){
        return CastUtil.castString(obj,"");
    }

    /**
     * 转为String型（提供默认值）
     * @param obj
     * @param defaultValue
     * @return
     */
    public static String castString(Object obj, String defaultValue) {
        return obj != null ? String.valueOf(obj) : defaultValue;
    }

    /**
     * 转为double型
     * @param obj
     * @return
     */
    public static double castDouble(Object obj){
        return castDouble(obj,0);
    }

    /**
     * 转为double型（提供默认值）
     * @param obj
     * @param defaultValue
     * @return
     */
    public static double castDouble(Object obj, double defaultValue) {
        double doubleValue=defaultValue;
        if (obj != null){
            String strValue=castString(obj);
            if (StringUtil.isNotEmpty(strValue)){
                try{
                    doubleValue=Double.parseDouble(strValue);
                } catch (NumberFormatException e){
                    doubleValue=defaultValue;
                }
            }
        }
        return doubleValue;
    }

    /**
     * 转为 long 型
     * @param obj
     * @return
     */
    public static long castLong(Object obj){
        return CastUtil.castLong(obj,0);
    }

    /**
     * 转为 long 型 (提供默认值)
     * @param obj
     * @param defaultValue
     * @return
     */
    public static long castLong(Object obj, long defaultValue) {
        long longValue=defaultValue;
        if (obj != null) {
            String strValue=castString(obj);
            if (StringUtil.isNotEmpty(strValue)){
                try{
                    longValue=Long.parseLong(strValue);
                }catch (NumberFormatException e){
                    longValue=defaultValue;
                }
            }
        }
        return longValue;
    }

    /**
     * 转为 int 型
     * @param obj
     * @return
     */
    public static int castInt(Object obj){
        return CastUtil.castInt(obj,0);
    }

    /**
     * 转为 int 型 （提供默认值)
     * @param obj
     * @param defaultValue
     * @return
     */
    public static int castInt(Object obj, int defaultValue) {
        int intValue=defaultValue;
        if (obj != null) {
            String strValue=castString(obj);
            if (StringUtil.isNotEmpty(strValue)){
                try {
                    intValue=Integer.parseInt(strValue);
                }catch (NumberFormatException e){
                    intValue=defaultValue;
                }
            }
        }
        return intValue;
    }

}
