package com.etcqez.tomcat.servlet;

import com.etcqez.tomcat.http.Request;
import com.etcqez.tomcat.http.Response;
import com.etcqez.utils.WebUtils;

import java.io.IOException;
import java.io.OutputStream;

public class CalServlet extends HttpServlet {
    @Override
    public void doGet(Request request, Response response) {

        System.out.println(request.getUri());
        int num1 = WebUtils.parseInt(request.getParameter("num1"), 0);
        int num2 = WebUtils.parseInt(request.getParameter("num2"), 0);

        int sum = num1 + num2;
        OutputStream outputStream = response.getOutputStream();
        String respMes = Response.respHeader + "<h1>" + num1 + "+" + num2 + "=" + sum + "</h1>";

        try {
            outputStream.write(respMes.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doPost(Request request, Response response) {
        this.doGet(request, response);
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void destroy() {

    }
}

