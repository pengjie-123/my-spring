package com.my.spring.tomcat;

public class ResponseUtil {

    public static String get404() {
        return "HTTP/1.1 404 NotFound\n"
            + "Server:Apache Tomcat/8.0.12\n"
            + "Date:Mon,6Oct2021 13:23:42 GMT\n"
            + "Content-Length:100\n"
            + "\n"
            + "<!doctype html><html lang=\"en\"><head><title>Tomcat</title></head><body><h1>404 - Not Found</h1></body></html>";
    }

    public static String get200(String body) {
        return "HTTP/1.1 200 OK\n"
            + "Content-Type:text/html\n"
            + "Content-Length:100\n"
            + "\n"
            + "<!doctype html><html lang=\"en\"><head><title>Tomcat</title></head><body><h1>" + body + "</h1></body></html>";
    }

    public static String get500(Exception e) {
        return "HTTP/1.1 500 InternalError\n"
            + "Server:Apache Tomcat/8.0.12\n"
            + "Date:Mon,6Oct2021 13:23:42 GMT\n"
            + "Content-Length:100\n"
            + "\n"
            + "<!doctype html><html lang=\"en\"><head><title>Tomcat</title></head><body><h1>500 - " + e.getMessage() + "</h1></body></html>";
    }
}
