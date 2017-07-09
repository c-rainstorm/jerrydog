package com.crainstorm.jerrydog.container.valves;

import org.apache.catalina.*;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by chen on 7/8/17.
 */
public class ClientIPLoggerValve implements Valve, Contained {

    protected Container container;

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public void invoke(Request request, Response response, ValveContext valveContext) throws IOException, ServletException {
        valveContext.invokeNext(request, response);

        System.out.println("Client IP Logger Valve");
        System.out.println(request.getRequest().getRemoteAddr());
        System.out.println("--------------------------------------");
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
