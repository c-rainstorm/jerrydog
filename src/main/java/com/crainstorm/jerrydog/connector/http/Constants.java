package com.crainstorm.jerrydog.connector.http;

import java.io.File;

/**
 * Created by chen on 6/7/17.
 */
public class Constants {
    public static final String WEB_ROOT = System.getenv("JERRYDOG_HOME") + File.separator + "webroot";
    public static final String PACKAGE = "com.crainstorm.jerrydog.connector.http";
    public static final int DEFAULT_CONNECTION_TIMEOUT = 60000;
    public static final int PROCESSOR_IDLE = 0;
    public static final int PROCESSOR_ACTIVE = 1;
}
