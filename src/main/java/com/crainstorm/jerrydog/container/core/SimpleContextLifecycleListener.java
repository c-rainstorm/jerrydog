package com.crainstorm.jerrydog.container.core;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;

/**
 * Created by chen on 7/9/17.
 */
public class SimpleContextLifecycleListener implements LifecycleListener {
    @Override
    public void lifecycleEvent(LifecycleEvent event) {
        Lifecycle lifecycle = event.getLifecycle();
        System.out.println("SimpleContextLifecycleListener's event " + event.getType().toString());
        if(Lifecycle.START_EVENT.equals(event.getType())){
            System.out.println("Starting context...");
        }else if(Lifecycle.STOP_EVENT.equals(event.getType())){
            System.out.println("stopping context...");
        }
    }
}
