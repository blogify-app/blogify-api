package com.blogify.blogapi.integration;

import java.net.http.HttpResponse;

public class CustomHttpResponse<T> {

    private final HttpResponse<String> httpResponse;
    private final T responseObject;

    public CustomHttpResponse(HttpResponse<String> httpResponse, T responseObject) {
        this.httpResponse = httpResponse;
        this.responseObject = responseObject;
    }

    public int statusCode() {
        return httpResponse.statusCode();
    }

    public T body() {
        return responseObject;
    }

    public HttpResponse<String> getHttpResponse() {
        return httpResponse;
    }

    public T getResponseObject() {
        return responseObject;
    }
}
