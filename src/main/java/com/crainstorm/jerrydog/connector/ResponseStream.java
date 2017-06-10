package com.crainstorm.jerrydog.connector;

import com.crainstorm.jerrydog.connector.http.HttpResponse;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by chen on 6/10/17.
 */
public class ResponseStream extends ServletOutputStream {
    protected boolean closed = false;

    protected boolean commit = false;

    // the number of bytes which have already been written to this stream
    protected int count = 0;

    // content length
    protected int length = -1;

    protected HttpResponse response = null;

    protected OutputStream stream = null;

    public ResponseStream(HttpResponse response) {
        super();
        closed = false;
        commit = false;
        count = 0;
        this.response = response;
    }

    public boolean getCommit() {
        return this.commit;
    }

    public void setCommit(boolean commit) {
        this.commit = commit;
    }

    public void close() throws IOException {
        if (closed) {
            throw new IOException("responseStream.close.closed");
        }

        response.flushBuffer();
        closed = true;
    }

    public void flush() throws IOException {
        if (closed) {
            throw new IOException("responseStream.flush.closed");
        }
        if (!commit) {
            response.flushBuffer();
        }
    }

    public void write(int b) throws IOException {
        if (closed) {
            throw new IOException("responseStream.write.closed");
        }
        if (length > 0 && count >= length) {
            throw new IOException("responseStream.write.count");
        }

        response.write(b);
        count++;
    }

    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        if (closed) {
            throw new IOException("responseStream.write.closed");
        }
        int actual = len;
        // todo find out where to setContentLength
//        if(len > 0 && (count + len >= length)){
//            actual = length - count;
//        }
        response.write(b, off, actual);
        if (actual < len) {
            throw new IOException("responseSteam.write.count");
        }
    }

    boolean isClosed() {
        return closed;
    }

    void reset() {
        count = 0;
    }


}
