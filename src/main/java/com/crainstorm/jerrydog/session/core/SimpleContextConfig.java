package com.crainstorm.jerrydog.session.core;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;

/**
 * Created by chen on 7/10/17.
 */
public class SimpleContextConfig implements LifecycleListener {
    @Override
    public void lifecycleEvent(LifecycleEvent lifecycleEvent) {
        Context context = (Context) lifecycleEvent.getLifecycle();
        context.setConfigured(true);
    }
}
