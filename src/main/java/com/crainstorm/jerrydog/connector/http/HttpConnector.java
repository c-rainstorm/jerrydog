package com.crainstorm.jerrydog.connector.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by chen on 6/2/17.
 */
public class HttpConnector implements Runnable {

    private static final Logger logger = LogManager.getLogger(HttpConnector.class);

    boolean stopped;
    private String scheme = "http";

    public String getScheme() {
        return scheme;
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(9704, 1, InetAddress.getByName("localhost"));
            if (logger.isDebugEnabled()) {
                logger.debug(serverSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while (!stopped) {
            Socket client = null;
            try {
                client = serverSocket.accept();
                if (logger.isDebugEnabled()) {
                    logger.debug("new connection coming in: " + client);
                }

                HttpProcessor processor = new HttpProcessor(this);
                processor.process(client);

                client.close();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }
}
