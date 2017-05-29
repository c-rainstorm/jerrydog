package com.crainstorm.jerrydog.httpserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeTypes;

import java.io.*;
import java.util.Date;

/**
 * Created by chen on 5/28/17.
 */
public class Response {
    private static final Logger logger = LogManager.getLogger(Response.class);
    private Request request = null;

    public Response(Request request) {
        this.request = request;

    }

    public void sendStaticResource(OutputStream outputStream) {
        File file = new File(HttpServer.WEB_ROOT, this.request.getUri());
        if (logger.isDebugEnabled()) {
            logger.debug("File path: " + file.getAbsolutePath());
        }
        try {
            if (file.exists()) {
                writeFileToSocket(file, outputStream);
            } else {
                writeNotFound(outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeNotFound(OutputStream outputStream) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("static resource not found!");
        }
        StringBuilder builder = new StringBuilder(64);

        builder.append("HTTP/1.1 404 File Not Found\r\n")
                .append("Content-Type: text/html\r\n")
                .append("Content-Length: 23\r\n")
                .append("\r\n")
                .append("<h1>File Not Found</h1>");

        outputStream.write(builder.toString().getBytes());
    }

    private void writeFileToSocket(File file, OutputStream outputStream) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("start write resource to socket...");
        }
        StringBuilder builder = new StringBuilder(64);
        InputStream inputStream = null;

        // 1. status line
        builder.append(request.getProtocolVersion()).append(" ").append("200 OK\r\n");

        // 2. response headers
        builder.append("Date: ")
                .append(new Date().toString()).append("\r\n");
        if (file.getName().matches(".*\\.html")) {
            builder.append("Content-type: text/html\r\n");
        } else {
            inputStream = new BufferedInputStream(new FileInputStream(file));
            builder.append("Content-type: ")
                    .append((new MimeTypes()).detect(inputStream, new Metadata()))
                    .append("\r\n");
            inputStream.close();
        }
        builder.append("Last-Modified: ")
                .append(file.lastModified()).append("\r\n");
        builder.append("Content-Length: ")
                .append(file.length()).append("\r\n");

        builder.append("\r\n");
        outputStream.write(builder.toString().getBytes());

        // 3. file content
        inputStream = new FileInputStream(file);
        final int BUFFER_SIZE = 4096;

        byte[] buff = new byte[BUFFER_SIZE];
        int readedNum = 0;
        while ((readedNum = inputStream.read(buff, 0, BUFFER_SIZE)) != -1) {
            outputStream.write(buff, 0, readedNum);
        }
        inputStream.close();
        if (logger.isDebugEnabled()) {
            logger.debug("file write success!");
        }
    }

    public static void main(String[] args) {
    }
}
