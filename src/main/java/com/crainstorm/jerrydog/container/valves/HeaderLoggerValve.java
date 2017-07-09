package com.crainstorm.jerrydog.container.valves;

import org.apache.catalina.*;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by chen on 7/8/17.
 */
public class HeaderLoggerValve implements Valve, Contained{

    protected Container container;

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public void invoke(Request request, Response response, ValveContext valveContext) throws IOException, ServletException {
        valveContext.invokeNext(request, response);

        System.out.println("Header Logger Valve:");
        ServletRequest sreq = request.getRequest();
        if(sreq instanceof HttpServletRequest){
            HttpServletRequest hreq = (HttpServletRequest) sreq;
            Enumeration<String> headerNames = hreq.getHeaderNames();
            while (headerNames.hasMoreElements()){
                String headerName = headerNames.nextElement().toString();
                String headerValue = hreq.getHeader(headerName);
                System.out.println(headerName + ": " + headerValue);
            }
        }else {
            System.out.println("Not an HTTP Request");
        }
        System.out.println("-------------------------------------");
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
