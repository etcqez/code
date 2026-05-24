package com.etcqez.tomcat.servlet;

import com.etcqez.tomcat.http.Request;
import com.etcqez.tomcat.http.Response;

import java.io.IOException;

public abstract class HttpServlet implements Servlet {

    @Override
    public void service(Request request, Response response) throws IOException {

        if ("GET".equalsIgnoreCase(request.getMethod())) {
            this.doGet(request, response);
        } else if ("POST".equalsIgnoreCase(request.getMethod())) {
            this.doPost(request, response);
        }
    }

    public abstract void doGet(Request request, Response response);

    public abstract void doPost(Request request, Response response);
}
