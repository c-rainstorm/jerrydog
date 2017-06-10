package com.crainstorm.jerrydog.connector.http;

/**
 * Created by chen on 6/2/17.
 */
public class HttpRequestLine {

    public static final int INITIAL_METHOD_SIZE = 8;
    public static final int INITIAL_URI_SIZE = 64;
    public static final int INITIAL_PROTOCOL_SIZE = 8;
    public static final int MAX_METHOD_SIZE = 1024;
    public static final int MAX_URI_SIZE = 32768;
    public static final int MAX_PROTOCOL_SIZE = 1024;


    public char[] method;
    public int methodEnd;
    public char[] uri;
    public int uriEnd;
    public char[] protocol;
    public int protocolEnd;

    public HttpRequestLine() {
        this(new char[INITIAL_METHOD_SIZE], 0,
                new char[INITIAL_URI_SIZE], 0,
                new char[INITIAL_PROTOCOL_SIZE], 0);
    }

    public HttpRequestLine(char[] method, int methodEnd,
                           char[] uri, int uriEnd,
                           char[] protocol, int protocolEnd) {
        this.method = method;
        this.methodEnd = methodEnd;
        this.uri = uri;
        this.uriEnd = uriEnd;
        this.protocol = protocol;
        this.protocolEnd = protocolEnd;
    }

    public void recycle() {
        this.methodEnd = 0;
        this.uriEnd = 0;
        this.protocolEnd = 0;
    }

    public int indexof(char[] buf) {
        return this.indexof(buf, buf.length);
    }

    public int indexof(String str) {
        return this.indexof(str.toCharArray(), str.length());
    }

    public int indexof(char[] buf, int end) {
        char firstChar = buf[0];
        int pos = 0;
        while (pos < uriEnd) {
            pos = indexof(firstChar, pos);
            if (pos == -1 || pos + end > uriEnd) {
                return -1;
            }
            for (int i = 0; i < end; ++i) {
                if (uri[pos + i] != buf[i]) {
                    break;
                }
                if (i == end - 1) {
                    return pos;
                }
            }
            pos++;
        }
        return -1;
    }

    public int indexof(char ch, int start) {
        for (int i = start; i < uriEnd; ++i) {
            if (uri[i] == ch) {
                return i;
            }
        }
        return -1;
    }
}
