package com.etcqez.tomcat.handler;

import com.etcqez.tomcat.http.Request;
import com.etcqez.tomcat.http.Response;
import com.etcqez.tomcat.servlet.CalServlet;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {

    private Socket socket = null;

    public RequestHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
//
//            System.out.println("当前线程" + Thread.currentThread().getName());
//
//            String mes = null;
//            while ((mes = bufferedReader.readLine()) != null) {
//                if (mes.length() == 0) {
//                    break;
//                }
//                System.out.println("mes = " + mes);
//            }

            Request request = new Request(inputStream);
//            String num1 = request.getParameter("num1");
//            String num2 = request.getParameter("num2");
//            String uri = request.getUri();
//            System.out.println("num1 = " + num1);
//            System.out.println("num2 = " + num2);
//            System.out.println("uri = " + uri);
//            System.out.println("request = " + request);

            OutputStream out = socket.getOutputStream();
            Response response = new Response(out);
            CalServlet calServlet = new CalServlet();
            calServlet.doGet(request,response);
//            String resp = Response.respHeader + "<h1> Response 返回的信息 hi 你好</h1>";
//            OutputStream outputStream = response.getOutputStream();
//            outputStream.write(resp.getBytes());
//            outputStream.flush();
//            outputStream.close();

//            String respHeader = "HTTP/1.1 200\n" +
//                    "Content-Type: text/html;charset=utf-8\n\n";
//
//            String resp = respHeader + "hi, etcqez";
//            System.out.println("resp = " + resp);
//            outputStream.write(resp.getBytes());
//
//            outputStream.flush();
//            outputStream.close();
            inputStream.close();
            socket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
