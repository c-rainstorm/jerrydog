package com.crainstorm.jerrydog.connector;

import com.crainstorm.jerrydog.connector.http.Constants;
import com.crainstorm.jerrydog.connector.http.HttpRequest;
import org.apache.catalina.util.StringManager;

import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chen on 6/9/17.
 */
public class RequestStream extends ServletInputStream {

    protected static StringManager sm = StringManager.getManager(Constants.PACKAGE);
    protected boolean closed = false;
    // the byte num read from inputStream
    protected int count = 0;
    // contentLength
    protected int length = -1;
    protected InputStream stream = null;

    public RequestStream(HttpRequest httpRequest) {
        super();
        // todo why we need init again?
        closed = false;
        count = 0;
        length = httpRequest.getContentLength();
        stream = httpRequest.getStream();
    }

    @Override
    public void close() throws IOException {
        if (closed) {
            throw new IOException(sm.getString("requestStream.close.closed"));
        }

        // todo why we need do this
        if (length > 0) {
            while (count < length) {
                int b = read();
                if (b < 0) {
                    break;
                }
            }
        }

        closed = true;
    }

    // read and return a single byte from stream, or -1 if end of file has encountered.
    @Override
    public int read() throws IOException {

        if (closed) {
            throw new IOException(sm.getString("requestStream.read.closed"));
        }

        if ((length >= 0) && (count >= length)) {
            return -1;
        }

        int b = stream.read();
        if (b >= 0) {
            count++;
        }

        return b;
    }

    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int toRead = len;
        if (length > 0) {
            if (count >= length) {
                return -1;
            }
            if ((count + len) > length) {
                toRead = length - count;
            }
        }
        int actuallyRead = super.read(b, off, toRead);
        return actuallyRead;
    }

}
