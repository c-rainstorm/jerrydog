package com.crainstorm.jerrydog.httpserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by chen on 5/28/17.
 */
public class Request implements ServletRequest {
    private static final Logger logger = LogManager.getLogger(Request.class);

    InputStream inputStream = null;

    private RequestMethod method = null;
    private String uri = null;
    private String protocolVersion = null;
    private Map<String, String> headers = new HashMap<>(32);
    private String body = null;

    public Request(InputStream inputStream) {
        this.inputStream = inputStream;
        this.parse();
    }

    private void parse() {
        if (logger.isDebugEnabled()) {
            logger.debug("start parse socket...");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.inputStream));

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

    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @Override
    public Enumeration getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {

    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public String getParameter(String name) {
        return null;
    }

    @Override
    public Enumeration getParameterNames() {
        return null;
    }

    @Override
    public String[] getParameterValues(String name) {
        return new String[0];
    }

    @Override
    public Map getParameterMap() {
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public void setAttribute(String name, Object o) {

    }

    @Override
    public void removeAttribute(String name) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    @Deprecated
    @Override
    public String getRealPath(String path) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }
}
