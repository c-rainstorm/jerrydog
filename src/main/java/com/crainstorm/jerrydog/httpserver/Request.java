package com.crainstorm.jerrydog.httpserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chen on 5/28/17.
 */
public class Request {
    private static final Logger logger = LogManager.getLogger(Request.class);
    private RequestMethod method = null;
    private String uri = null;
    private String protocolVersion = null;
    private Map<String, String> headers = new HashMap<>(32);
    private String body = null;


    public Request(InputStream inputStream) {
        this.parse(inputStream);
    }

    private void parse(InputStream inputStream) {
        if (logger.isDebugEnabled()) {
            logger.debug("start parse socket...");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            // 1. parse request line

            String[] requestLine = reader.readLine().split(" ");
            this.method = RequestMethod.valueOf(requestLine[0].toUpperCase());
            this.uri = requestLine[1];
            this.protocolVersion = requestLine[2];
            if (logger.isDebugEnabled()) {
                logger.debug("request line done!");
            }
            // 2. parse request header

            String header;
            while (true) {
                header = reader.readLine();
                if (header.length() == 0) {
                    break;
                }
                int index = header.indexOf(':');
                headers.put(header.substring(0, index), header.substring(index + 1));
            }
            if (logger.isDebugEnabled()) {
                logger.debug("request header done!");
            }

            // 3. parse request body
            final int BUFFER_SIZE = 4096;
            StringBuilder builder = new StringBuilder(BUFFER_SIZE);
            char[] buff = new char[BUFFER_SIZE];
            int readedNum = 0;
            while (reader.ready() &&
                    ((readedNum = reader.read(buff, 0, BUFFER_SIZE)) != -1)) {
                builder.append(buff, 0, readedNum);
            }
            this.body = builder.toString();
            if (logger.isDebugEnabled()) {
                logger.debug("request body done!");
                logger.debug("request parse finish...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public RequestMethod getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public Map<String, String> getHeaders() {
        Map<String, String> copy = (Map<String, String>) ((HashMap) this.headers).clone();
        return copy;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        StringBuilder request = new StringBuilder(512);
        // request line
        request.append(this.method).append(" ").append(this.uri).append(" ").append(this.protocolVersion).append("\r\n");

        // request header

        for (String header : headers.keySet()) {
            request.append(header).append(": ").append(headers.get(header)).append("\r\n");
        }
        request.append("\r\n");

        // request body
        request.append(this.body);

        return request.toString();
    }

    public static void main(String[] args) {
        try {
            Request request = new Request(
                    new FileInputStream("/home/chen/workspace/git/jerrydog/src/main/resources/request-test.txt"));

//            Map<String, String> headers = request.getHeaders();
//            headers.put("test","test");
//            System.out.println(request.getHeaders().get("test"));

            System.out.println(request);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
