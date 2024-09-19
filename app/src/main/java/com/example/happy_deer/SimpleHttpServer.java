package com.example.happy_deer;

import fi.iki.elonen.NanoHTTPD;

public class SimpleHttpServer extends NanoHTTPD {

    public SimpleHttpServer(int port) {
        super(port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String htmlResponse = "<html>" +
                "<head><title>Welcome</title></head>" +
                "<body>" +
                "<h1>Hello, World!</h1>" +
                "<p>This is a simple web page served from NanoHttpd!</p>" +
                "</body>" +
                "</html>";

        return newFixedLengthResponse(Response.Status.OK, "text/html", htmlResponse);
    }

}
