package com.crainstorm.jerrydog;

import com.crainstorm.jerrydog.connector.http.HttpRequest;
import com.crainstorm.jerrydog.connector.http.HttpResponse;

import java.io.IOException;

/**
 * Created by chen on 6/1/17.
 */
public class StaticResourceProcessor {

    public void process(HttpRequest request, HttpResponse response) {
        try {
            response.sendStaticResource();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
