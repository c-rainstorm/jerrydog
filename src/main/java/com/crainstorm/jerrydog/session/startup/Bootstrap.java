package com.crainstorm.jerrydog.session.startup;

import com.crainstorm.jerrydog.session.core.SimpleWrapper;
import com.crainstorm.jerrydog.session.core.SimpleContextConfig;
import org.apache.catalina.*;
import org.apache.catalina.connector.http.HttpConnector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.loader.WebappLoader;
import org.apache.catalina.session.StandardManager;

import java.io.IOException;

/**
 * Created by chen on 7/11/17.
 */
public class Bootstrap {
    public static void main(String[] args) {
        System.setProperty("catalina.base", System.getenv("JERRYDOG_HOME"));
        Connector connector = new HttpConnector();

        Wrapper sessionWrapper = new SimpleWrapper();
        sessionWrapper.setName("Session");
        sessionWrapper.setServletClass("SessionServlet");

        Context context = new StandardContext();
        context.setPath("/myApp");
        context.setDocBase("myApp");

        context.addChild(sessionWrapper);

        context.addServletMapping("/myApp/Session","Session");

        LifecycleListener listener = new SimpleContextConfig();
        ((Lifecycle) context).addLifecycleListener(listener);

        Loader loader = new WebappLoader();
        context.setLoader(loader);

        Manager manager = new StandardManager();
        context.setManager(manager);

        connector.setContainer(context);

        try {
            connector.initialize();
            ((Lifecycle) connector).start();

            ((Lifecycle) context).start();

            // make the application wait until we press a key.
            System.in.read();
            ((Lifecycle) context).stop();
        } catch (LifecycleException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
