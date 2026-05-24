package com.etcqez.tomcat;

import com.etcqez.tomcat.handler.RequestHandler;
import com.etcqez.tomcat.servlet.HttpServlet;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Tomcat {

    public static final ConcurrentHashMap<String, HttpServlet> servletMapping = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, String> servletUrlMapping = new ConcurrentHashMap<>();

    public void init() {

    }
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("serverSocket = " + serverSocket);

        while (!serverSocket.isClosed()) {

            Socket accept = serverSocket.accept();

            new Thread(new RequestHandler(accept)).start();
        }
    }
}
