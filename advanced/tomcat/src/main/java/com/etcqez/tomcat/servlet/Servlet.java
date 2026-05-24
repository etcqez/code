package com.etcqez.tomcat.servlet;

import com.etcqez.tomcat.http.Request;
import com.etcqez.tomcat.http.Response;

import java.io.IOException;

public interface Servlet {

    void init() throws Exception;

    void service(Request request, Response response) throws IOException;

    void destroy();
}
