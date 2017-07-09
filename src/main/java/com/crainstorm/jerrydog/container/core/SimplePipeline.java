package com.crainstorm.jerrydog.container.core;

import org.apache.catalina.*;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by chen on 7/8/17.
 */
public class SimplePipeline implements Pipeline {
    public SimplePipeline(Container container) {
        setContainer(container);
    }

    protected Valve basic = null;
    protected Container container = null;
    protected Valve[] valves = new Valve[0];

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public Valve getBasic() {
        return basic;
    }

    @Override
    public void setBasic(Valve valve) {
        this.basic = valve;
    }

    @Override
    public void addValve(Valve valve) {
        if (valve instanceof Contained) {
            ((Contained) valve).setContainer(this.container);
        }
        // todo can be better
        synchronized (valves) {
            Valve[] result = new Valve[valves.length + 1];
            System.arraycopy(valves, 0, result, 0, valves.length);
            result[valves.length] = valve;
            valves = result;
        }
    }

    @Override
    public Valve[] getValves() {
        return valves;
    }

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        ((new SimplePipelineValveContext())).invokeNext(request, response);
    }

    @Override
    public void removeValve(Valve valve) {

    }

    protected class SimplePipelineValveContext implements ValveContext {

        protected int stage = 0;

        @Override
        public String getInfo() {
            return null;
        }

        @Override
        public void invokeNext(Request request, Response response) throws IOException, ServletException {
            int subscript = stage;
            stage = stage + 1;
            if (subscript < valves.length) {
                valves[subscript].invoke(request, response, this);
            } else if ((subscript == valves.length) && basic != null) {
                basic.invoke(request, response, this);
            } else {
                throw new ServletException("No valve");
            }
        }
    }
}
