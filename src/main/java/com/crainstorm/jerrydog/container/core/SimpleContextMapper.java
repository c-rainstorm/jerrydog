package com.crainstorm.jerrydog.container.core;

import org.apache.catalina.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Mapper 提供了从 url 地址到处理请求的 Servlet 之间的映射
 */
public class SimpleContextMapper implements Mapper {

    private String protocol = null;
    private SimpleContext context = null;

    @Override
    public Container getContainer() {
        return context;
    }

    @Override
    public void setContainer(Container container) {
        if(!(container instanceof SimpleContext)){
            throw new IllegalArgumentException("Illegal type of container");
        }
        this.context = (SimpleContext) container;
    }

    @Override
    public String getProtocol() {
        return this.protocol;
    }

    @Override
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public Container map(Request request, boolean b) {
        String contextPath = ((HttpServletRequest)request.getRequest()).getContextPath();
        String requestURI = ((HttpRequest)request).getDecodedRequestURI();
        String relativeURI = requestURI.substring(contextPath.length());
        Wrapper wrapper = null;
        String servletPath = relativeURI;
        String name = context.findServletMapping(relativeURI);
        if(name != null){
            wrapper = (Wrapper) context.findChild(name);
        }
        return wrapper;
    }
}
