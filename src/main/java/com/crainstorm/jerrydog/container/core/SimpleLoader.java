package com.crainstorm.jerrydog.container.core;

import org.apache.catalina.*;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

/**
 * Created by chen on 7/8/17.
 */
public class SimpleLoader implements Loader, Lifecycle {

    public static final String WEB_ROOT = System.getenv("JERRYDOG_HOME") + File.separator + "webroot";

    ClassLoader classLoader = null;
    Container container = null;

    public SimpleLoader() {
        try {
            URL[] urls = new URL[1];
            URLStreamHandler streamHandler = null;
            File classPath = new File(WEB_ROOT);
            String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString();
            System.out.println(classPath);
            System.out.println(classPath.getCanonicalPath());
            System.out.println(repository);
            urls[0] = new URL(null, repository, streamHandler);
            // why URLClassLoader
            // 双亲委派呢？ tomcat 的类加载机制？
            classLoader = new URLClassLoader(urls);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public DefaultContext getDefaultContext() {
        return null;
    }

    @Override
    public void setDefaultContext(DefaultContext defaultContext) {

    }

    @Override
    public boolean getDelegate() {
        return false;
    }

    @Override
    public void setDelegate(boolean b) {

    }

    @Override
    public String getInfo() {
        return "A simple loader";
    }

    @Override
    public boolean getReloadable() {
        return false;
    }

    @Override
    public void setReloadable(boolean b) {

    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {

    }

    @Override
    public void addRepository(String s) {

    }

    @Override
    public String[] findRepositories() {
        return new String[0];
    }

    @Override
    public boolean modified() {
        return false;
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {

    }

    //---------------------------- LifeCycle --------------------------
    @Override
    public void addLifecycleListener(LifecycleListener lifecycleListener) {

    }

    @Override
    public LifecycleListener[] findLifecycleListeners() {
        return new LifecycleListener[0];
    }

    @Override
    public void removeLifecycleListener(LifecycleListener lifecycleListener) {

    }

    @Override
    public void start() throws LifecycleException {
        System.out.println("Starting SimpleLoader");
    }

    @Override
    public void stop() throws LifecycleException {

    }
}
