package com.crainstorm.jerrydog.httpserver;

/**
 * Created by chen on 6/1/17.
 */
public class StaticResourceProcessor {

    public void process(Request request, Response response){
        response.sendStaticResource();
    }

}
