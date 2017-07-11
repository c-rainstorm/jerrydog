package com.crainstorm.jerrydog.loader.startup;

import com.crainstorm.jerrydog.loader.core.SimpleContextConfig;
import com.crainstorm.jerrydog.loader.core.SimpleWrapper;
import org.apache.catalina.*;
import org.apache.catalina.connector.http.HttpConnector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.loader.WebappClassLoader;
import org.apache.catalina.loader.WebappLoader;
import org.apache.naming.resources.ProxyDirContext;

import java.io.IOException;

/**
 * Created by chen on 7/10/17.
 */
public class Bootstrap {
    public static void main(String[] args) {

        // StandardContext need this property
        System.setProperty("catalina.base", System.getenv("JERRYDOG_HOME"));
        Connector connector = new HttpConnector();
        Wrapper wrapper1 = new SimpleWrapper();
        wrapper1.setName("Primitive");
        wrapper1.setServletClass("PrimitiveServlet");
        Wrapper wrapper2 = new SimpleWrapper();
        wrapper2.setName("Modern");
        wrapper2.setServletClass("ModernServlet");

        Context context = new StandardContext();
        // what does it mean?
        context.setPath("/myApp");
        context.setDocBase("myApp");

        context.addChild(wrapper1);
        context.addChild(wrapper2);

        context.addServletMapping("/Primitive", "Primitive");
        context.addServletMapping("/Modern", "Modern");

        LifecycleListener listener = new SimpleContextConfig();
        ((Lifecycle) context).addLifecycleListener(listener);

        // what's the relationship between WebappClassLoader and System class loader?
        Loader loader = new WebappLoader();
        context.setLoader(loader);

        connector.setContainer(context);

        try {
            connector.initialize();
            ((Lifecycle) connector).start();
            ((Lifecycle) context).start();

            WebappClassLoader classLoader = (WebappClassLoader) loader.getClassLoader();
            System.out.println("Resources' docBase: " + ((ProxyDirContext)classLoader.getResources()).getDocBase());
            String[] repositories = classLoader.findRepositories();
            for(int i = 0; i < repositories.length; ++i){
                System.out.println("  repository: " + repositories[i]);
            }
            System.in.read();
            ((Lifecycle) context).stop();
        } catch (LifecycleException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
