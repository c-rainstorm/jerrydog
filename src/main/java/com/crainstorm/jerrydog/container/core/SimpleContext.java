package com.crainstorm.jerrydog.container.core;

import org.apache.catalina.*;
import org.apache.catalina.deploy.*;
import org.apache.catalina.util.CharsetMapper;

import javax.naming.directory.DirContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.HashMap;

/**
 * 一个 SimpleContext 代表一个 application, 使用 pipeline 来处理请求并生成相应的 response。
 *
 */
public class SimpleContext implements Context, Pipeline {

    public SimpleContext() {
        pipeline.setBasic(new SimpleContextValve(this));
    }

    // parent container
    protected Container parent = null;
    // children containers
    protected HashMap<String, Container> children = new HashMap<>();
    // application class loader
    protected Loader loader = null;
    // pipeline to process request and response
    protected SimplePipeline pipeline = new SimplePipeline(this);
    // application servlet mapping
    protected HashMap<String, String> servletMappings = new HashMap<>();
    // default mapper
    protected Mapper mapper = null;
    // todo why different protocol need different mapper
    protected HashMap<String, Mapper> mappers = new HashMap<>();


    @Override
    public Object[] getApplicationListeners() {
        return new Object[0];
    }

    @Override
    public void setApplicationListeners(Object[] objects) {

    }

    @Override
    public boolean getAvailable() {
        return false;
    }

    @Override
    public void setAvailable(boolean b) {

    }

    @Override
    public CharsetMapper getCharsetMapper() {
        return null;
    }

    @Override
    public void setCharsetMapper(CharsetMapper charsetMapper) {

    }

    @Override
    public boolean getConfigured() {
        return false;
    }

    @Override
    public void setConfigured(boolean b) {

    }

    @Override
    public boolean getCookies() {
        return false;
    }

    @Override
    public void setCookies(boolean b) {

    }

    @Override
    public boolean getCrossContext() {
        return false;
    }

    @Override
    public void setCrossContext(boolean b) {

    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public void setDisplayName(String s) {

    }

    @Override
    public boolean getDistributable() {
        return false;
    }

    @Override
    public void setDistributable(boolean b) {

    }

    @Override
    public String getDocBase() {
        return null;
    }

    @Override
    public void setDocBase(String s) {

    }

    @Override
    public LoginConfig getLoginConfig() {
        return null;
    }

    @Override
    public void setLoginConfig(LoginConfig loginConfig) {

    }

    @Override
    public NamingResources getNamingResources() {
        return null;
    }

    @Override
    public void setNamingResources(NamingResources namingResources) {

    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public void setPath(String s) {

    }

    @Override
    public String getPublicId() {
        return null;
    }

    @Override
    public void setPublicId(String s) {

    }

    @Override
    public boolean getReloadable() {
        return false;
    }

    @Override
    public void setReloadable(boolean b) {

    }

    @Override
    public boolean getOverride() {
        return false;
    }

    @Override
    public void setOverride(boolean b) {

    }

    @Override
    public boolean getPrivileged() {
        return false;
    }

    @Override
    public void setPrivileged(boolean b) {

    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public int getSessionTimeout() {
        return 0;
    }

    @Override
    public void setSessionTimeout(int i) {

    }

    @Override
    public String getWrapperClass() {
        return null;
    }

    @Override
    public void setWrapperClass(String s) {

    }

    @Override
    public void addApplicationListener(String s) {

    }

    @Override
    public void addApplicationParameter(ApplicationParameter applicationParameter) {

    }

    @Override
    public void addConstraint(SecurityConstraint securityConstraint) {

    }

    @Override
    public void addEjb(ContextEjb contextEjb) {

    }

    @Override
    public void addEnvironment(ContextEnvironment contextEnvironment) {

    }

    @Override
    public void addErrorPage(ErrorPage errorPage) {

    }

    @Override
    public void addFilterDef(FilterDef filterDef) {

    }

    @Override
    public void addFilterMap(FilterMap filterMap) {

    }

    @Override
    public void addInstanceListener(String s) {

    }

    @Override
    public void addLocalEjb(ContextLocalEjb contextLocalEjb) {

    }

    @Override
    public void addMimeMapping(String s, String s1) {

    }

    @Override
    public void addParameter(String s, String s1) {

    }

    @Override
    public void addResource(ContextResource contextResource) {

    }

    @Override
    public void addResourceEnvRef(String s, String s1) {

    }

    @Override
    public void addResourceLink(ContextResourceLink contextResourceLink) {

    }

    @Override
    public void addRoleMapping(String s, String s1) {

    }

    @Override
    public void addSecurityRole(String s) {

    }

    @Override
    public void addServletMapping(String pattern, String name) {
        synchronized (servletMappings) {
            servletMappings.put(pattern, name);
        }
    }

    @Override
    public void addTaglib(String s, String s1) {

    }

    @Override
    public void addWelcomeFile(String s) {

    }

    @Override
    public void addWrapperLifecycle(String s) {

    }

    @Override
    public void addWrapperListener(String s) {

    }

    @Override
    public Wrapper createWrapper() {
        return null;
    }

    @Override
    public String[] findApplicationListeners() {
        return new String[0];
    }

    @Override
    public ApplicationParameter[] findApplicationParameters() {
        return new ApplicationParameter[0];
    }

    @Override
    public SecurityConstraint[] findConstraints() {
        return new SecurityConstraint[0];
    }

    @Override
    public ContextEjb findEjb(String s) {
        return null;
    }

    @Override
    public ContextEjb[] findEjbs() {
        return new ContextEjb[0];
    }

    @Override
    public ContextEnvironment findEnvironment(String s) {
        return null;
    }

    @Override
    public ContextEnvironment[] findEnvironments() {
        return new ContextEnvironment[0];
    }

    @Override
    public ErrorPage findErrorPage(int i) {
        return null;
    }

    @Override
    public ErrorPage findErrorPage(String s) {
        return null;
    }

    @Override
    public ErrorPage[] findErrorPages() {
        return new ErrorPage[0];
    }

    @Override
    public FilterDef findFilterDef(String s) {
        return null;
    }

    @Override
    public FilterDef[] findFilterDefs() {
        return new FilterDef[0];
    }

    @Override
    public FilterMap[] findFilterMaps() {
        return new FilterMap[0];
    }

    @Override
    public String[] findInstanceListeners() {
        return new String[0];
    }

    @Override
    public ContextLocalEjb findLocalEjb(String s) {
        return null;
    }

    @Override
    public ContextLocalEjb[] findLocalEjbs() {
        return new ContextLocalEjb[0];
    }

    @Override
    public String findMimeMapping(String s) {
        return null;
    }

    @Override
    public String[] findMimeMappings() {
        return new String[0];
    }

    @Override
    public String findParameter(String s) {
        return null;
    }

    @Override
    public String[] findParameters() {
        return new String[0];
    }

    @Override
    public ContextResource findResource(String s) {
        return null;
    }

    @Override
    public String findResourceEnvRef(String s) {
        return null;
    }

    @Override
    public String[] findResourceEnvRefs() {
        return new String[0];
    }

    @Override
    public ContextResourceLink findResourceLink(String s) {
        return null;
    }

    @Override
    public ContextResourceLink[] findResourceLinks() {
        return new ContextResourceLink[0];
    }

    @Override
    public ContextResource[] findResources() {
        return new ContextResource[0];
    }

    @Override
    public String findRoleMapping(String s) {
        return null;
    }

    @Override
    public boolean findSecurityRole(String s) {
        return false;
    }

    @Override
    public String[] findSecurityRoles() {
        return new String[0];
    }

    @Override
    public String findServletMapping(String pattern) {
        synchronized (servletMappings) {
            return servletMappings.get(pattern);
        }
    }

    @Override
    public String[] findServletMappings() {
        return new String[0];
    }

    @Override
    public String findStatusPage(int i) {
        return null;
    }

    @Override
    public int[] findStatusPages() {
        return new int[0];
    }

    @Override
    public String findTaglib(String s) {
        return null;
    }

    @Override
    public String[] findTaglibs() {
        return new String[0];
    }

    @Override
    public boolean findWelcomeFile(String s) {
        return false;
    }

    @Override
    public String[] findWelcomeFiles() {
        return new String[0];
    }

    @Override
    public String[] findWrapperLifecycles() {
        return new String[0];
    }

    @Override
    public String[] findWrapperListeners() {
        return new String[0];
    }

    @Override
    public void reload() {

    }

    @Override
    public void removeApplicationListener(String s) {

    }

    @Override
    public void removeApplicationParameter(String s) {

    }

    @Override
    public void removeConstraint(SecurityConstraint securityConstraint) {

    }

    @Override
    public void removeEjb(String s) {

    }

    @Override
    public void removeEnvironment(String s) {

    }

    @Override
    public void removeErrorPage(ErrorPage errorPage) {

    }

    @Override
    public void removeFilterDef(FilterDef filterDef) {

    }

    @Override
    public void removeFilterMap(FilterMap filterMap) {

    }

    @Override
    public void removeInstanceListener(String s) {

    }

    @Override
    public void removeLocalEjb(String s) {

    }

    @Override
    public void removeMimeMapping(String s) {

    }

    @Override
    public void removeParameter(String s) {

    }

    @Override
    public void removeResource(String s) {

    }

    @Override
    public void removeResourceEnvRef(String s) {

    }

    @Override
    public void removeResourceLink(String s) {

    }

    @Override
    public void removeRoleMapping(String s) {

    }

    @Override
    public void removeSecurityRole(String s) {

    }

    @Override
    public void removeServletMapping(String s) {

    }

    @Override
    public void removeTaglib(String s) {

    }

    @Override
    public void removeWelcomeFile(String s) {

    }

    @Override
    public void removeWrapperLifecycle(String s) {

    }

    @Override
    public void removeWrapperListener(String s) {

    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public Loader getLoader() {
        if (loader != null) {
            return loader;
        } else if (parent != null) {
            return parent.getLoader();
        } else {
            return null;
        }
    }

    @Override
    public void setLoader(Loader loader) {
        this.loader = loader;
    }

    @Override
    public Logger getLogger() {
        return null;
    }

    @Override
    public void setLogger(Logger logger) {

    }

    @Override
    public Manager getManager() {
        return null;
    }

    @Override
    public void setManager(Manager manager) {

    }

    @Override
    public Cluster getCluster() {
        return null;
    }

    @Override
    public void setCluster(Cluster cluster) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String s) {

    }

    @Override
    public Container getParent() {
        return null;
    }

    @Override
    public void setParent(Container container) {

    }

    @Override
    public ClassLoader getParentClassLoader() {
        return null;
    }

    @Override
    public void setParentClassLoader(ClassLoader classLoader) {

    }

    @Override
    public Realm getRealm() {
        return null;
    }

    @Override
    public void setRealm(Realm realm) {

    }

    @Override
    public DirContext getResources() {
        return null;
    }

    @Override
    public void setResources(DirContext dirContext) {

    }

    @Override
    public void addChild(Container child) {
        child.setParent(this);
        children.put(child.getName(), child);
    }

    @Override
    public void addContainerListener(ContainerListener containerListener) {

    }

    @Override
    public void addMapper(Mapper mapper) {
        mapper.setContainer(this);
        synchronized (mappers) {
            if (mappers.get(mapper.getProtocol()) != null) {
                throw new IllegalArgumentException("addMapper: Protocol '" +
                        mapper.getProtocol() + "' is not unique");
            }
            mappers.put(mapper.getProtocol(), mapper);
            if (mappers.size() == 1) {
                this.mapper = mapper;
            } else {
                this.mapper = null;
            }
        }
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {

    }

    @Override
    public Container findChild(String name) {
        if (name == null) {
            return null;
        }
        synchronized (children) {
            return children.get(name);
        }
    }

    @Override
    public Container[] findChildren() {
        synchronized (children) {
            Container[] result = new Container[children.size()];
            return children.values().toArray(result);
        }
    }

    @Override
    public ContainerListener[] findContainerListeners() {
        return new ContainerListener[0];
    }

    @Override
    public Mapper findMapper(String protocol) {
        if (mapper != null) {
            return mapper;
        } else {
            synchronized (mappers) {
                return mappers.get(protocol);
            }
        }
    }

    @Override
    public Mapper[] findMappers() {
        return new Mapper[0];
    }

    @Override
    public Valve getBasic() {
        return pipeline.getBasic();
    }

    @Override
    public void setBasic(Valve valve) {
        pipeline.setBasic(valve);
    }

    @Override
    public void addValve(Valve valve) {
        pipeline.addValve(valve);
    }

    @Override
    public Valve[] getValves() {
        return pipeline.getValves();
    }

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        pipeline.invoke(request, response);
    }

    @Override
    public void removeValve(Valve valve) {
        pipeline.removeValve(valve);
    }

    @Override
    public Container map(Request request, boolean update) {
        Mapper mapper = findMapper(request.getRequest().getProtocol());
        if (mapper == null) {
            return null;
        }

        return mapper.map(request, update);
    }

    @Override
    public void removeChild(Container container) {

    }

    @Override
    public void removeContainerListener(ContainerListener containerListener) {

    }

    @Override
    public void removeMapper(Mapper mapper) {

    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {

    }
}
