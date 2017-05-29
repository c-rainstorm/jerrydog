package com.crainstorm.jerrydog.httpserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by chen on 5/28/17.
 */
public class HttpServer {

    private static final Logger logger = LogManager.getLogger(HttpServer.class);
    public static final String SHUTDOWN = "/shutdown";
    public static final String WEB_ROOT = System.getenv("JERRYDOG_HOME") + File.separator + "webapps";

    public static void main(String[] args) {
        HttpServer httpServer = new HttpServer();

        httpServer.await();
    }

    private void await() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(9704, 1, InetAddress.getByName("localhost"));
            if(logger.isDebugEnabled()){
                logger.debug("ServerSocket opened! " + serverSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        Socket clientSocket = null;
        boolean shutdown = false;

        while (!shutdown) {
            try {
                clientSocket = serverSocket.accept();
                if(logger.isDebugEnabled()){
                    logger.debug("receive a connection... " + clientSocket);
                }

                Request request = new Request(clientSocket.getInputStream());
                Response response = new Response(request);

                if(logger.isDebugEnabled()){
                    logger.debug("Request: \n" + request);
                }

                response.sendStaticResource(clientSocket.getOutputStream());

                clientSocket.close();

                shutdown = request.getUri().equals(HttpServer.SHUTDOWN);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
