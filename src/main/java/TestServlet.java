//package com.crainstorm.jerrydog.httpserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by chen on 6/1/17.
 */
public class TestServlet implements Servlet {

    private static final Logger logger = LogManager.getLogger(TestServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        logger.debug("init...");
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        logger.debug("service...");
        res.getWriter().println("Hello world!");
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {
        logger.debug("destroy...");
    }
}
