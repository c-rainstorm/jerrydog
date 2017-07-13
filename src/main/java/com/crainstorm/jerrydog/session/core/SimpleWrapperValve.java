package com.crainstorm.jerrydog.session.core;

import org.apache.catalina.*;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * basic valve of wrapper
 */
public class SimpleWrapperValve implements Valve, Contained {

    private Container container;

    public SimpleWrapperValve(SimpleWrapper simpleWrapper) {
        this.container = simpleWrapper;
    }


    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public void invoke(Request request, Response response, ValveContext valveContext) throws IOException, ServletException {
        SimpleWrapper wrapper = (SimpleWrapper) getContainer();
        ServletRequest sreq = request.getRequest();
        ServletResponse sres = response.getResponse();
        HttpServletRequest hreq = null;
        HttpServletResponse hres = null;
        if (sreq instanceof HttpServletRequest) {
            hreq = (HttpServletRequest) sreq;
        }
        if (sres instanceof HttpServletResponse) {
            hres = (HttpServletResponse) sres;
        }

        request.setContext((Context) wrapper.getParent());

        Servlet servlet = null;

        servlet = wrapper.allocate();
        if (hreq != null && hres != null) {
            servlet.service(hreq, hres);
        } else {
            servlet.service(sreq, sres);
        }
    }

    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }
}
