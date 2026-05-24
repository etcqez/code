package com.etcqez.utils;

public class WebUtils {
    public static int parseInt(String strNum, int defaultValue) {
        try {
            return Integer.parseInt(strNum);
        } catch (NumberFormatException e) {
            System.out.println("格式不对，转换失败");
        }
        return defaultValue;
    }
}
