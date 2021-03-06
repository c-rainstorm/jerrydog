package com.crainstorm.jerrydog.container.startup;

import com.crainstorm.jerrydog.container.core.*;
import com.crainstorm.jerrydog.container.valves.ClientIPLoggerValve;
import com.crainstorm.jerrydog.container.valves.HeaderLoggerValve;
import org.apache.catalina.*;
import org.apache.catalina.connector.http.HttpConnector;
import org.apache.catalina.logger.FileLogger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;

/**
 * 1. 初始化连接器
 * 2. 初始化 Wrapper 并关联到 Context
 * 3. 初始化 Valve 并关联到 Context
 * 4. 初始化 Mapper 并关联到 Context
 * 5. 初始化 Loader 并关联到 Context
 * 6. 添加 ServletMapping 到 Context
 * 7. 关联 Context 到 Connector
 */
public class Bootstrap {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(Bootstrap.class);

    public static void main(String[] args) {
        HttpConnector connector = new HttpConnector();

        // servlet context primitive
        Wrapper primitive = new SimpleWrapper();
        primitive.setName("Primitive");
        primitive.setServletClass("PrimitiveServlet");
        logger.debug("init primitive Servlet");

        //servlet context modern
        Wrapper modern = new SimpleWrapper();
        modern.setName("Modern");
        modern.setServletClass("ModernServlet");
        logger.debug("init modern Servlet");

        // application context
        Context context = new SimpleContext();
        context.addChild(primitive);
        context.addChild(modern);

        Valve headerLogger = new HeaderLoggerValve();
        Valve clientIPLogger = new ClientIPLoggerValve();

        ((Pipeline) context).addValve(headerLogger);
        ((Pipeline) context).addValve(clientIPLogger);

        logger.debug("add Valve..");

        Mapper mapper = new SimpleContextMapper();
        mapper.setProtocol("http");
        context.addMapper(mapper);

        logger.debug("add mapper");

        // add lifecycle listener
        LifecycleListener listener = new SimpleContextLifecycleListener();
        ((Lifecycle) context).addLifecycleListener(listener);

        Loader loader = new SimpleLoader();
        context.setLoader(loader);

        logger.debug("set loader");

        context.addServletMapping("/Primitive", "Primitive");
        context.addServletMapping("/Modern", "Modern");
        logger.debug("add servlet mapping...");

        System.setProperty("catalina.base", System.getenv("JERRYDOG_HOME"));
        FileLogger logger = new FileLogger();
        logger.setPrefix("FileLog_");
        logger.setTimestamp(true);
        context.setLogger(logger);

        connector.setContainer(context);

        try {
            connector.initialize();
            ((Lifecycle) connector).start();
            ((Lifecycle) context).start();

            System.in.read();
            ((Lifecycle) context).stop();
        } catch (LifecycleException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
