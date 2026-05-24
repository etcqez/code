package com.etcqez.seckill.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    //准备一个salt
    private static final String SALT = "4tIY5VcX";

    //加密加盐, 完成的任务是 md5(password明文 + salt1)
    public static String inputPassToMidPass(String inputPass) {
        String str = SALT.charAt(0) + inputPass + SALT.charAt(6);
        return md5(str);
    }

    //加密加盐, 完成的任务就是把MidPass -> DB中的密码
    //md5(md5(password明文 + salt1) + salt2)
    public static String midPassToDBPass(String midPass, String salt) {

        String str = salt.charAt(1) + midPass + salt.charAt(5);
        return md5(str);
    }

    //password明文 -> DB中的密码
    public static String inputPassToDBPass(String inputPass, String salt) {
        String midPass = inputPassToMidPass(inputPass);
        return midPassToDBPass(midPass, salt);
    }
}
