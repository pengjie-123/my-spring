package com.my.spring.tomcat;

import com.my.spring.annotation.MyAutowired;
import com.my.spring.annotation.MyComponent;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

@MyComponent("tomcat")
public class Tomcat {
    private String ip;
    private Integer port = 8080;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @MyAutowired UrlMappingHandler urlMappingHandler;

    public Tomcat() {}

    public void startServer () {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Tomcat server started...");
            while (true) {
                Socket       socket       = null;
                InputStream  inputStream  = null;
                OutputStream outputStream = null;
                long         x1           = 0L;
                try {
                    socket = serverSocket.accept();

                    System.out.println("-----Receive a request from server-----");
                    x1 = System.currentTimeMillis();

                    inputStream = socket.getInputStream();
//                int available = 0;
//                while (available == 0) {
//                    available = inputStream.available();
//                }
                    byte[] request = new byte[1024];
                    inputStream.read(request);
                    String   input    = new String(request);
                    String[] mappings = getUrlMapping(input);
                    Object   resp     = urlMappingHandler.invoke(mappings);
                    outputStream = socket.getOutputStream();
                    outputStream.write(resp.toString().getBytes());
                    outputStream.close();
                    inputStream.close();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (outputStream != null)
                        outputStream.close();
                    if (inputStream != null)
                        inputStream.close();
                    if (socket != null)
                        socket.close();
                    long time = System.currentTimeMillis() - x1;
                    System.out.println("------ End process request, total use: " + time + " ms");
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private static String[] getUrlMapping(String input) {
        if (input == null) {
            throw new RuntimeException();
        }
        String firstLine = input.split("\\n")[0].trim();
        System.out.println("firstLine: " + firstLine + ", length: " + firstLine.length());
        return firstLine.split(" ");
    }
}
