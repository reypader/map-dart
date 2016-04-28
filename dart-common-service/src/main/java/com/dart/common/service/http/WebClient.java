package com.dart.common.service.http;

import com.dart.common.service.http.exception.WebClientException;
import com.google.api.client.http.HttpResponse;
import com.google.common.net.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by rpader on 4/6/16.
 */
public interface WebClient {

    InputStream get(String path, Map<String, Object> headers) throws IOException, WebClientException;

    InputStream post(String path, MediaType mediaType, byte[] data, Map<String, Object> headers) throws IOException, WebClientException;

}
