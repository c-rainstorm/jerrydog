package com.crainstorm.jerrydog.httpserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.Servlet;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chen on 6/1/17.
 */
public class ServletProcessor {

    private static final Logger logger = LogManager.getLogger(ServletProcessor.class);

    private URLClassLoader classLoader = null;
    private Map<String, Servlet> servlets = new HashMap<>(32);

    public ServletProcessor() {
        // construct a classloader to load servlet
        try {
            // scan directories
            URL[] urls = new URL[1];
            URLStreamHandler streamHandler = null;
            File classPath = new File(HttpServer.WEB_ROOT);
            String repository = null;
            repository = (new URL("file",
                    null, classPath.getCanonicalPath() + File.separator))
                    .toString();
            urls[0] = new URL(null, repository, streamHandler);
            this.classLoader = new URLClassLoader(urls);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void process(Request request, Response response) {
        String uri = request.getUri();
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);

        if (logger.isDebugEnabled()) {
            logger.debug("start load servlet [" + servletName + "]...");
        }

        try {
            Servlet servlet = null;
            if (this.servlets.containsKey(servletName)) {
                // get servlet from cached HashMap
                servlet = servlets.get(servletName);
            } else {
                // load servlet
                Class servletClass = classLoader.loadClass(servletName);
                servlet = (Servlet) servletClass.newInstance();
                servlets.put(servletName, servlet);
            }

            RequestFacade req = new RequestFacade(request);
            ResponseFacade res = new ResponseFacade(response);
            // call servlet's service method
            servlet.service(req, res);

            if (logger.isDebugEnabled()) {
                logger.debug("load servlet [" + servletName + "] done!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
