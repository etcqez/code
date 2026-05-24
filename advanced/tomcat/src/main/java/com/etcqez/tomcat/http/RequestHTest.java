package com.etcqez.tomcat.http;

import org.junit.Test;

public class RequestHTest {

    @Test
    public void f1 () {
        String s1 = "GET /disk/main?key=test&value=test HTTP/1.1";
        String uri = s1.split(" ")[1];
        System.out.println("uri = " + uri);
    }

    @Test
    public void f2() {
        String[] split = "video=".split("=");
        System.out.println(split.length);
        for (String s : split) {
            System.out.println(s);
        }
    }
}
