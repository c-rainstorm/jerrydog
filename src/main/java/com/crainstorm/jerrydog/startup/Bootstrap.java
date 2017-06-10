package com.crainstorm.jerrydog.startup;

import com.crainstorm.jerrydog.connector.http.HttpConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by chen on 6/2/17.
 */
public class Bootstrap {

    private static final Logger logger = LogManager.getLogger(Bootstrap.class);

    public static void main(String[] args) {
        HttpConnector connector = new HttpConnector();
        connector.start();
        if (logger.isDebugEnabled()) {
            logger.debug("starting connector...");
        }
    }
}
