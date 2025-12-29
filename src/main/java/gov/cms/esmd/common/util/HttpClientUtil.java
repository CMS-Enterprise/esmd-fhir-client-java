package gov.cms.esmd.common.util;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public final class HttpClientUtil {


    public static HttpPost getHttpPost(String endpointUrl, RequestConfig requestConfig, Map<String, String> headerMap, ObjectNode jsonBody) {

        HttpPost httpPost = new HttpPost(endpointUrl);
        httpPost.setConfig(requestConfig);
        headerMap.forEach(httpPost::addHeader);
        httpPost.setEntity(new StringEntity(jsonBody != null ? jsonBody.toString() : "{}", StandardCharsets.UTF_8));
        return httpPost;
    }

    public static HttpPost getHttpPost(String endpointUrl, int timeoutMs, Map<String, String> headerMap, String content) {

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(timeoutMs)
                .setConnectionRequestTimeout(timeoutMs)
                .setSocketTimeout(timeoutMs)
                .build();

        HttpPost httpPost = new HttpPost(endpointUrl);
        httpPost.setConfig(requestConfig);
        headerMap.forEach(httpPost::addHeader);
        httpPost.setEntity(new StringEntity(content, StandardCharsets.UTF_8));
        return httpPost;
    }

    public static HttpPost getHttpPost(String endpointUrl, int timeoutMs, Map<String, String> headerMap, ObjectNode jsonBody) {

        // Configure timeout
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(timeoutMs)
                .setConnectionRequestTimeout(timeoutMs)
                .setSocketTimeout(timeoutMs)
                .build();
        return getHttpPost(endpointUrl, requestConfig, headerMap, jsonBody);
    }

    public static HttpPost getHttpPost(String endpointUrl, int timeoutMs, Map<String, String> headerMap) {

        // Configure timeout
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(timeoutMs)
                .setConnectionRequestTimeout(timeoutMs)
                .setSocketTimeout(timeoutMs)
                .build();
        return getHttpPost(endpointUrl, requestConfig, headerMap, null);
    }

    public static HttpGet getHttpGet(String endpointUrl, RequestConfig requestConfig, String accept, String token) {

        HttpGet httpGet = new HttpGet(endpointUrl);
        httpGet.addHeader("Accept", accept);
        httpGet.addHeader("Authorization", "Bearer " + token);
        httpGet.setConfig(requestConfig);
        return httpGet;
    }

    public static HttpGet getHttpGet(String endpointUrl, int timeoutMs, String accept, String token) {

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(timeoutMs)
                .setConnectionRequestTimeout(timeoutMs)
                .setSocketTimeout(timeoutMs)
                .build();

        return getHttpGet(endpointUrl, requestConfig, accept, token);
    }

    public static HttpPut getHttpPut(String endpointUrl, int timeoutMs, Map<String, String> headerMap, String content) {

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(timeoutMs)
                .setConnectionRequestTimeout(timeoutMs)
                .setSocketTimeout(timeoutMs)
                .build();

        HttpPut httpPut = new HttpPut(endpointUrl);
        httpPut.setConfig(requestConfig);
        headerMap.forEach(httpPut::addHeader);
        httpPut.setEntity(new StringEntity(content, StandardCharsets.UTF_8));
        return httpPut;
    }


}
