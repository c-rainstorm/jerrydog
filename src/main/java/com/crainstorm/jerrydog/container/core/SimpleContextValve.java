package com.crainstorm.jerrydog.container.core;

import org.apache.catalina.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * application context basic valve
 * invoke wrapper to process request
 */
public class SimpleContextValve implements Valve, Contained {

    protected Container container;

    public SimpleContextValve(SimpleContext simpleContext) {
        setContainer(simpleContext);
    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public void invoke(Request request, Response response, ValveContext valveContext) throws IOException, ServletException {
        if (!(request.getRequest() instanceof HttpServletRequest) ||
                !(response.getResponse() instanceof HttpServletResponse)) {
            return;
        }

        HttpServletRequest hreq = (HttpServletRequest) request.getRequest();
        String contextPath = hreq.getContextPath();
        String requestURI = ((HttpRequest) request).getDecodedRequestURI();

        Context context = (Context) getContainer();

        Wrapper wrapper = (Wrapper) context.map(request, true);
        if (wrapper == null) {
            notFound(requestURI, (HttpServletResponse) response.getResponse());
            return;
        }
        response.setContext(context);
        wrapper.invoke(request, response);
    }

    private void notFound(String requestURI, HttpServletResponse response) {
        try {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, requestURI);
        } catch (IOException e) {
            e.printStackTrace();
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
