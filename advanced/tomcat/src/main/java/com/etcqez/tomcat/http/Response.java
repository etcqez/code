package com.etcqez.tomcat.http;

import java.io.OutputStream;
import java.util.HashMap;

public class Response {

    private OutputStream outputStream = null;

    public static final String respHeader = "HTTP/1.1 200\n" +
            "Content-Type: text/html;charset=utf-8\n\n";

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }
}
