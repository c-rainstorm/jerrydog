package com.crainstorm.jerrydog.connector.http;

import com.crainstorm.jerrydog.connector.ResponseStream;
import com.crainstorm.jerrydog.connector.ResponseWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by chen on 6/2/17.
 */
public class HttpResponse implements HttpServletResponse {

    private static final int BUFFER_SIZE = 1024;
    protected final SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
    // todo why protected
    protected byte[] buffer = new byte[BUFFER_SIZE];
    protected int bufferCount = 0;
    protected boolean committed = false;
    // bytes write to response
    protected int contentCount = 0;
    protected int contentLength = -1;
    protected String contentType = null;
    protected String encoding = null;
    protected ArrayList<Cookie> cookies = new ArrayList<>();
    protected HashMap<String, ArrayList<String>> headers = new HashMap<>();
    protected String message = getStatusMessage(HttpServletResponse.SC_OK);
    protected int status = HttpServletResponse.SC_OK;
    // todo why default
    HttpRequest request;
    OutputStream output;
    PrintWriter writer;

    public HttpResponse(OutputStream output) {
        this.output = output;
    }

    public void finishResponse() {
        if (writer != null) {
            writer.flush();
            writer.close();
        }
    }

    protected String getStatusMessage(int status) {
        switch (status) {
            case SC_OK:
                return ("OK");
            case SC_ACCEPTED:
                return ("Accepted");
            case SC_BAD_GATEWAY:
                return ("Bad Gateway");
            case SC_BAD_REQUEST:
                return ("Bad Request");
            case SC_CONFLICT:
                return ("Conflict");
            case SC_CONTINUE:
                return ("Continue");
            case SC_CREATED:
                return ("Created");
            case SC_EXPECTATION_FAILED:
                return ("Expectation Failed");
            case SC_FORBIDDEN:
                return ("Forbidden");
            case SC_GATEWAY_TIMEOUT:
                return ("Gateway Timeout");
            case SC_GONE:
                return ("Gone");
            case SC_HTTP_VERSION_NOT_SUPPORTED:
                return ("HTTP Version Not Supported");
            case SC_INTERNAL_SERVER_ERROR:
                return ("Internal Server Error");
            case SC_LENGTH_REQUIRED:
                return ("Length Required");
            case SC_METHOD_NOT_ALLOWED:
                return ("Method Not Allowed");
            case SC_MOVED_PERMANENTLY:
                return ("Moved Permanently");
            case SC_MOVED_TEMPORARILY:
                return ("Moved Temporarily");
            case SC_MULTIPLE_CHOICES:
                return ("Multiple Choices");
            case SC_NO_CONTENT:
                return ("No Content");
            case SC_NON_AUTHORITATIVE_INFORMATION:
                return ("Non-Authoritative Information");
            case SC_NOT_ACCEPTABLE:
                return ("Not Acceptable");
            case SC_NOT_FOUND:
                return ("Not Found");
            case SC_NOT_IMPLEMENTED:
                return ("Not Implemented");
            case SC_NOT_MODIFIED:
                return ("Not Modified");
            case SC_PARTIAL_CONTENT:
                return ("Partial Content");
            case SC_PAYMENT_REQUIRED:
                return ("Payment Required");
            case SC_PRECONDITION_FAILED:
                return ("Precondition Failed");
            case SC_PROXY_AUTHENTICATION_REQUIRED:
                return ("Proxy Authentication Required");
            case SC_REQUEST_ENTITY_TOO_LARGE:
                return ("Request Entity Too Large");
            case SC_REQUEST_TIMEOUT:
                return ("Request Timeout");
            case SC_REQUEST_URI_TOO_LONG:
                return ("Request URI Too Long");
            case SC_REQUESTED_RANGE_NOT_SATISFIABLE:
                return ("Requested Range Not Satisfiable");
            case SC_RESET_CONTENT:
                return ("Reset Content");
            case SC_SEE_OTHER:
                return ("See Other");
            case SC_SERVICE_UNAVAILABLE:
                return ("Service Unavailable");
            case SC_SWITCHING_PROTOCOLS:
                return ("Switching Protocols");
            case SC_UNAUTHORIZED:
                return ("Unauthorized");
            case SC_UNSUPPORTED_MEDIA_TYPE:
                return ("Unsupported Media Type");
            case SC_USE_PROXY:
                return ("Use Proxy");
            case 207:       // WebDAV
                return ("Multi-Status");
            case 422:       // WebDAV
                return ("Unprocessable Entity");
            case 423:       // WebDAV
                return ("Locked");
            case 507:       // WebDAV
                return ("Insufficient Storage");
            default:
                return ("HTTP Response Status " + status);
        }
    }

    @Override
    public void addCookie(Cookie cookie) {
        if (isCommitted()) {
            return;
        }

        synchronized (cookies) {
            cookies.add(cookie);
        }
    }

    @Override
    public boolean containsHeader(String name) {
        synchronized (headers) {
            return headers.get(name) != null;
        }
    }

    @Override
    public String encodeURL(String url) {
        return null;
    }

    @Override
    public String encodeRedirectURL(String url) {
        return null;
    }

    @Override
    public String encodeUrl(String url) {
        return encodeURL(url);
    }

    @Override
    public String encodeRedirectUrl(String url) {
        return encodeRedirectURL(url);
    }

    protected void sendHeaders() {
        if (isCommitted()) {
            return;
        }

        OutputStreamWriter osr = null;
        try {
            osr = new OutputStreamWriter(getStream(), getCharacterEncoding());
        } catch (UnsupportedEncodingException e) {
            osr = new OutputStreamWriter(getStream());
        }

        final PrintWriter outputWrite = new PrintWriter(osr);

        // status line
        outputWrite.print(this.getProtocol());
        outputWrite.print(" ");
        outputWrite.print(status);
        if (message != null) {
            outputWrite.print(" ");
            outputWrite.print(message);
        }
        outputWrite.print("\r\n");

        if (getContentType() != null) {
            outputWrite.print("Content-Type: " + getContentType() + "\r\n");
        }
        if (getContentLength() >= 0) {
            outputWrite.print("Content-Length: " + getContentLength() + "\r\n");
        }

        // todo why we need synchronized
        synchronized (headers) {
            Iterator<String> names = headers.keySet().iterator();
            while (names.hasNext()) {
                String name = names.next();
                ArrayList<String> values = headers.get(name);
                Iterator<String> items = values.iterator();
                while (items.hasNext()) {
                    String value = items.next();
                    outputWrite.print(name);
                    outputWrite.print(": ");
                    outputWrite.print(value);
                    outputWrite.print("; ");
                }
                outputWrite.print("\r\n");
            }
        }

        synchronized (cookies) {
            Iterator<Cookie> items = cookies.iterator();
            while (items.hasNext()) {
                Cookie cookie = items.next();
                outputWrite.print(CookieTools.getCookieHeaderName(cookie));
                outputWrite.print(": ");
                outputWrite.print(CookieTools.getCookieHeaderValue(cookie));
                outputWrite.print(";");
            }
            outputWrite.print("\r\n");
        }

        outputWrite.print("\r\n");
        outputWrite.flush();

        committed = true;
    }


    public void sendStaticResource() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;

        try {
            File file = new File(Constants.WEB_ROOT, request.getRequestURI());
            fis = new FileInputStream(file);

            int ch = fis.read(bytes, 0, BUFFER_SIZE);
            while (ch != -1) {
                output.write(bytes, 0, ch);
                ch = fis.read(bytes, 0, BUFFER_SIZE);
            }
        } catch (FileNotFoundException e) {
            StringBuilder builder = new StringBuilder(64);
            builder.append("HTTP/1.1 404 File Not Found\r\n")
                    .append("Content-Type: text/html\r\n")
                    .append("Content-Length: 23\r\n")
                    .append("\r\n")
                    .append("<h1>File Not Found</h1>");
            output.write(builder.toString().getBytes());
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    public void write(int b) throws IOException {
        if (bufferCount >= buffer.length) {
            flushBuffer();
        }
        buffer[bufferCount++] = (byte) b;
        contentCount++;
    }

    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        if (len == 0) {
            return;
        }

        if (len <= (buffer.length - bufferCount)) {
            System.arraycopy(b, off, buffer, bufferCount, len);
            bufferCount += len;
            contentCount += len;
            return;
        }

        flushBuffer();
        int iterations = len / buffer.length;
        int leftoverStart = iterations * buffer.length;
        int leftoverLen = len - leftoverStart;
        for (int i = 0; i < iterations; ++i) {
            write(b, off + (i * buffer.length), buffer.length);
        }
        if (leftoverLen > 0) {
            write(b, off + leftoverStart, leftoverLen);
        }
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {

    }

    @Override
    public void sendError(int sc) throws IOException {

    }

    @Override
    public void sendRedirect(String location) throws IOException {

    }

    @Override
    public void setDateHeader(String name, long date) {

    }

    @Override
    public void addDateHeader(String name, long date) {
        if (isCommitted()) {
            return;
        }
        addHeader(name, format.format(new Date(date)));
    }

    @Override
    public void setHeader(String name, String value) {
        if (isCommitted()) {
            return;
        }
        ArrayList<String> values = new ArrayList<>();
        values.add(value);
        synchronized (headers) {
            headers.put(name, values);
        }
        String match = name.toLowerCase();
        if (match.equals("content-length")) {
            int contentLength = -1;
            contentLength = Integer.parseInt(value);
            if (contentLength >= 0) {
                setContentLength(contentLength);
            }
        } else if (match.equals("content-type")) {
            setContentType(value);
        }
    }

    @Override
    public void addHeader(String name, String value) {
        if (isCommitted()) {
            return;
        }

        synchronized (headers) {
            ArrayList<String> values = headers.get(name);
            if (values == null) {
                values = new ArrayList<>();
                headers.put(name, values);
            }
            values.add(value);
        }
    }

    @Override
    public void setIntHeader(String name, int value) {

    }

    @Override
    public void addIntHeader(String name, int value) {
        if (isCommitted()) {
            return;
        }

        addHeader(name, Integer.toString(value));
    }

    @Override
    public void setStatus(int sc) {

    }

    @Override
    public void setStatus(int sc, String sm) {

    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    @Override
    public String getCharacterEncoding() {
        if (encoding == null) {
            return "UTF-8";
        }
        return encoding;
    }

    @Override
    public void setCharacterEncoding(String charset) {

    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public void setContentType(String type) {

    }

    public int getContentLength() {
        return contentLength;
    }

    @Override
    public void setContentLength(int len) {
        if (isCommitted()) {
            return;
        }
        this.contentLength = len;
    }

    protected String getProtocol() {
        return request.getProtocol();
    }

    public OutputStream getStream() {
        return this.output;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        ResponseStream newStream = new ResponseStream(this);
        newStream.setCommit(false);
        OutputStreamWriter osw = new OutputStreamWriter(newStream, getCharacterEncoding());
        writer = new ResponseWriter(osw);
        return writer;
    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void setBufferSize(int size) {

    }

    @Override
    public void flushBuffer() throws IOException {
        if (bufferCount > 0) {
            try {
                output.write(buffer, 0, bufferCount);
            } finally {
                bufferCount = 0;
            }
        }
    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return committed;
    }

    @Override
    public void reset() {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public void setLocale(Locale loc) {
        if (isCommitted()) {
            return;
        }

        String language = loc.getLanguage();
        if (language != null && language.length() > 0) {
            String country = loc.getCountry();
            StringBuffer value = new StringBuffer(language);
            if (country != null && country.length() > 0) {
                value.append('-');
                value.append(country);
            }
            setHeader("Content-Language", value.toString());
        }
    }
}
