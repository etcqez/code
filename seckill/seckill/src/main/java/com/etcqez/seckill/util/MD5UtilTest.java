package com.etcqez.seckill.util;

import org.junit.jupiter.api.Test;

public class MD5UtilTest {

    @Test
    public void f1() {

        String midPass = MD5Util.inputPassToMidPass("12345");
        System.out.println(midPass);
        System.out.println(MD5Util.midPassToDBPass(midPass, "hYLLSQ4x"));
        System.out.println(MD5Util.inputPassToDBPass("12345", "hYLLSQ4x"));
    }
}
