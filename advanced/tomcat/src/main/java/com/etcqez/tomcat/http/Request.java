package com.etcqez.tomcat.http;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class Request {

    private String method;
    private String uri;
    private HashMap<String, String> parametersMapping = new HashMap<>();

    public Request(InputStream inputStream) {

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            String requestLine = bufferedReader.readLine();
            String[] requestLineArr = requestLine.split(" ");

            method = requestLineArr[0];

            int index = requestLineArr[1].indexOf("?");
            if (index == -1) {
                uri = requestLineArr[1];
            } else {
                uri = requestLineArr[1].substring(0, index);
                String parameters = requestLineArr[1].substring(index + 1);
                String[] parameterPairs = parameters.split("&");
                if (null != parameterPairs && !"".equals(parameterPairs)) {
                    for (String parameterPair : parameterPairs) {

                        String[] keyValue = parameterPair.split("=");
                        if (keyValue.length == 2) {
                            parametersMapping.put(keyValue[0], keyValue[1]);
                        }
                    }
                }
            }

//            inputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getParameter(String name) {
        if (parametersMapping.containsKey(name)) {
            return parametersMapping.get(name);
        } else {
            return "";
        }
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
