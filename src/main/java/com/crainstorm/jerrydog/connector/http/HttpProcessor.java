package com.crainstorm.jerrydog.connector.http;

import com.crainstorm.jerrydog.ServletProcessor;
import com.crainstorm.jerrydog.StaticResourceProcessor;
import org.apache.catalina.util.RequestUtil;
import org.apache.catalina.util.StringManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by chen on 6/2/17.
 */
public class HttpProcessor {

    private static final Logger logger = LogManager.getLogger(HttpProcessor.class);
    // todo why protected, why put it here
    protected String method = null;
    protected String queryString = null;
    protected StringManager sm = StringManager.getManager("com.crainstorm.jerrydog.connector.http");
    private HttpConnector connector = null;
    private HttpRequest request;
    // todo find out why we need this class
    private HttpRequestLine requestLine = new HttpRequestLine();
    private HttpResponse response;

    public HttpProcessor(HttpConnector httpConnector) {
        this.connector = httpConnector;
    }

    public void process(Socket client) {
        SocketInputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new SocketInputStream(client.getInputStream(), 2048);
            outputStream = client.getOutputStream();

            request = new HttpRequest(inputStream);
            response = new HttpResponse(outputStream);
            response.setRequest(request);

            logger.trace("start parse request line...");
            parseRequest(inputStream, outputStream);
            logger.trace("parse request line done!");
            logger.trace("start parse header...");
            parseHeaders(inputStream);
            logger.trace("parse header done!");

            logger.debug("requestURI: " + request.getRequestURI());
            if (request.getRequestURI().startsWith("/servlet/")) {
                ServletProcessor processor = new ServletProcessor();
                processor.process(request, response);
            } else {
                StaticResourceProcessor processor = new StaticResourceProcessor();
                processor.process(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void parseRequest(SocketInputStream input, OutputStream output) throws IOException, ServletException {
        input.readRequestLine(requestLine);
        String method = new String(requestLine.method, 0, requestLine.methodEnd);
        String uri = null;
        String protocol = new String(requestLine.protocol, 0, requestLine.protocolEnd);

        if (method.length() < 1) {
            throw new ServletException("Missing HTTP request method!");
        } else if (requestLine.uriEnd < 1) {
            throw new ServletException("Missing HTTP request URI");
        }

        int question = requestLine.indexof("?");
        if (question >= 0) {
            // has parameter
            request.setQueryString(new String(requestLine.uri, question + 1, requestLine.uriEnd - question - 1));
            uri = new String(requestLine.uri, 0, question);
        } else {
            request.setQueryString(null);
            uri = new String(requestLine.uri, 0, requestLine.uriEnd);
        }

        if (!uri.startsWith("/")) {
            // TODO check for an absolute URI
            // after deep learn http
        }

        // TODO parse requested session ID from REQUEST URI

        // TODO clean uri to normal


        request.setMethod(method);
        request.setProtocol(protocol);
        request.setRequestURI(uri);

    }

    private void parseHeaders(SocketInputStream input) throws IOException, ServletException {
        while (true) {
            HttpHeader header = new HttpHeader();

            input.readHeader(header);
            if (header.nameEnd == 0) {
                if (header.valueEnd == 0) {
                    return;
                } else {
                    throw new ServletException(sm.getString("httpProcessor.parseHeaders.colon"));
                }
            }

            String name = new String(header.name, 0, header.nameEnd);
            String value = new String(header.value, 0, header.valueEnd);
            request.addHeader(name, value);

            if (name.equals("cookie")) {
                Cookie[] cookies = RequestUtil.parseCookieHeader(value);
                for (int i = 0; i < cookies.length; ++i) {
                    if (cookies[i].getName().equals("jsessionid")) {
                        if (!request.isRequestedSessionIdFromCookie()) {
                            request.setRequestedSessionId(cookies[i].getValue());
                            request.setRequestedSessionCookie(true);
                            request.setRequestedSessionURL(false);
                        }
                    }
                    request.addCookie(cookies[i]);
                }
            } else if (name.equals("content-length")) {
                int n = -1;
                try {
                    n = Integer.parseInt(value);
                } catch (Exception e) {
                    throw new ServletException(sm.getString("httpProcessor.parseHeaders.contentLength"));
                }
                request.setContentLength(n);
            } else if (name.equals("content-type")) {
                request.setContentType(value);
            }
        }
    }
}
