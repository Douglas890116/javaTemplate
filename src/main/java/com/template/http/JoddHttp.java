package com.template.http;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

/**
 * Created by ws on 2016/7/12.
 */
public class JoddHttp {

    public static void main(String[] arg) {
        HttpRequest request = HttpRequest.get("www.google.com.hk");
        HttpResponse response = request.send();
        System.out.print(response.body());
    }
}