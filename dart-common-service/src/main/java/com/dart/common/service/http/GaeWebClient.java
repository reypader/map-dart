package com.dart.common.service.http;

import com.dart.common.service.http.exception.WebClientException;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.http.*;
import com.google.common.net.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by rpader on 4/5/16.
 */
public class GaeWebClient implements WebClient {

    private static final Logger logger = Logger.getLogger(GaeWebClient.class.getName());

    private static final UrlFetchTransport urlFetch = UrlFetchTransport.getDefaultInstance();

    @Override
    public InputStream get(String path, Map<String, Object> headers) throws IOException, WebClientException {
        logger.info("Sending GET request to (" + path + ") with headers: " + headers);
        GenericUrl url = new GenericUrl(new URL(path));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.putAll(headers);
        HttpRequest request = urlFetch.createRequestFactory()
                                      .buildGetRequest(url);
        request.setHeaders(httpHeaders);
        HttpResponse response = request.execute();
        int statusCode = response.getStatusCode();
        if (200 <= statusCode && statusCode < 300) {
            return response.getContent();
        } else {
            throw new WebClientException(response.getStatusMessage(), statusCode);
        }
    }

    @Override
    public InputStream post(String path, MediaType mediaType, byte[] data,
                            Map<String, Object> headers) throws IOException, WebClientException {
        logger.info("Sending POST request to (" + path + ") with headers: " + headers);
        GenericUrl url = new GenericUrl(new URL(path));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.putAll(headers);
        logger.fine("Request body (" + mediaType.toString() + ")\n" + new String(data));
        HttpContent content = new ByteArrayContent(mediaType.toString(), data);
        HttpRequest request = urlFetch.createRequestFactory()
                                      .buildPostRequest(url, content);
        request.setHeaders(httpHeaders);
        HttpResponse response = request.execute();
        int statusCode = response.getStatusCode();
        if (200 <= statusCode && statusCode < 300) {
            return response.getContent();
        } else {
            throw new WebClientException(response.getStatusMessage(), statusCode);
        }
    }


}
