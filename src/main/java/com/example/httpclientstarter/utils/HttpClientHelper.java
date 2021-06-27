package com.example.httpclientstarter.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * http请求便捷接口
 *
 */
@Slf4j
public class HttpClientHelper {
    /**
     *
     */
    private static final HttpClientHelper INSTANCE = new HttpClientHelper();
    /**
     * 默认使用编码
     */
    public static final String ENCODING_UTF8 = "utf-8";
    /**
     * 默认使用编码
     */
    public static final Charset CHARSET_UTF8 = Charset.forName("utf-8");
    /**
     * httpClient
     */
    private CloseableHttpClient httpClient;

    /**
     * json转换函数
     */
    private Function<Object, String> toJson = ObjectMapperUtils::toJsonStr;

    /**
     * 转换为json数据
     *
     * @param o 需要转为json格式的数据
     * @return json格式数据
     * @throws ClientProtocolException, HttpResponseException, IOException
     */
    private String toJsonStr(Object o) throws ClientProtocolException, HttpResponseException, IOException {
        return toJson.apply(o);
    }

    /**
     * 创建一个新的httpclient工具接口
     *
     * @param toJson
     * @param httpClient
     * @return
     */
    public static HttpClientHelper create(Function<Object, String> toJson, CloseableHttpClient httpClient) {
        if (toJson == null || httpClient == null) {
            throw new NullPointerException();
        }
        HttpClientHelper i = new HttpClientHelper();
        i.toJson = toJson;
        i.httpClient = httpClient;
        return i;
    }

    /**
     * 创建一个新的httpclient工具接口
     *
     * @param toJson
     * @return
     */
    public static HttpClientHelper create(Function<Object, String> toJson) {
        return create(toJson, getInstance().httpClient);
    }

    /**
     * 单根实例
     *
     * @return
     */
    public static HttpClientHelper getInstance() {
        if (INSTANCE.httpClient == null) {
            throw new UnsupportedOperationException("httpClient not init yet");
        }
        return INSTANCE;
    }

    /**
     * 设置默认的httpclient.方便自动初始化处理
     *
     * @param httpClient
     * @return
     */
    public static HttpClientHelper setHttpClient(CloseableHttpClient httpClient) {
        if (httpClient == null) {
            throw new NullPointerException();
        }
        INSTANCE.httpClient = httpClient;
        return INSTANCE;
    }

    /**
     * 通过form表单形式提交数据, UTF8编码
     *
     * @param url
     * @param data
     * @param <T>
     * @return
     * @throws ClientProtocolException, HttpResponseException, IOException
     */
    public <T> String post(String url, Map<String, T> data) throws ClientProtocolException, HttpResponseException, IOException {
        return post(url, data, ENCODING_UTF8);
    }

    /**
     * 通过form表单形式提交数据
     *
     * @param url
     * @param data
     * @param encoding 编码
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> String post(String url, Map<String, T> data, String encoding) throws ClientProtocolException, HttpResponseException, IOException {
        return post(url, toNameValuePairs(data));
    }

    /**
     * 数据转换
     *
     * @param data
     * @param <T>
     * @return
     */
    private <T> List<NameValuePair> toNameValuePairs(Map<String, T> data) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        List<NameValuePair> r = new ArrayList<>(data.size());
        this.addParams(r::add, data);
        return r;
    }

    /**
     * form表单提交数据,UTF8编码
     *
     * @param url
     * @param data
     * @return
     * @throws ClientProtocolException, HttpResponseException, IOException
     */
    public String post(String url, List<NameValuePair> data) throws ClientProtocolException, HttpResponseException, IOException {
        return post(url, data, (Header[]) null);
    }

    /**
     * form表单提交数据
     *
     * @param url
     * @param data
     * @param encoding
     * @return
     * @throws ClientProtocolException, HttpResponseException, IOException
     */
    public String post(String url, List<NameValuePair> data, String encoding) throws ClientProtocolException, HttpResponseException, IOException {
        return post(url, data, (Header[]) null, encoding);
    }

    /**
     * form表单提交数据,UTF8编码
     *
     * @param url
     * @param data
     * @return
     * @throws ClientProtocolException, HttpResponseException, IOException
     */
    public String post(String url, List<NameValuePair> data, Header[] headers) throws ClientProtocolException, HttpResponseException, IOException {
        return post(url, data, headers, (String) null);
    }

    /**
     * form表单提交数据
     *
     * @param url
     * @param data
     * @return
     * @throws ClientProtocolException, HttpResponseException, IOException
     */
    public String post(String url, List<NameValuePair> data, Header[] headers, String encoding) throws ClientProtocolException, HttpResponseException, IOException {
        if (encoding == null) {
            encoding = ENCODING_UTF8;
        }
        HttpEntity entity = null;
        if (data != null && !data.isEmpty()) {
            entity = new UrlEncodedFormEntity(data, encoding);
        }

        return post(url, entity, headers, (Integer) null);
    }

    /**
     * json格式提交数据
     *
     * @param url
     * @param obj
     * @return
     * @throws Exception
     */
    public String postJson(String url, Object obj) throws ClientProtocolException, HttpResponseException, IOException {
        return this.postJson(url, obj, null);
    }

    /**
     * json格式提交数据
     *
     * @param url
     * @param obj
     * @return
     * @throws Exception
     */
    public String postJson(String url, Object obj, Integer timeout) throws ClientProtocolException, HttpResponseException, IOException {
        return this.postJson(url, toJsonStr(obj), timeout);
    }

    /**
     * json格式提交数据
     *
     * @param url     地址
     * @param json    json格式数
     * @param timeout 超时时间
     * @return
     * @throws Exception
     */
    public String postJson(String url, String json, Integer timeout) throws ClientProtocolException, HttpResponseException, IOException {
        HttpEntity entity = null;
        if (StringUtils.isNotBlank(json)) {
            entity = new StringEntity(json,
                    ContentType.create(ContentType.APPLICATION_JSON.getMimeType(), ENCODING_UTF8));
        }
        return post(url, entity, null, timeout);
    }

    /**
     * 文本格式提交数据
     *
     * @param url
     * @param text
     * @param timeout
     * @return
     * @throws ClientProtocolException, HttpResponseException, IOException
     */
    public String postText(String url, String text, Integer timeout) throws ClientProtocolException, HttpResponseException, IOException {
        HttpEntity entity = null;
        if (StringUtils.isNotBlank(text)) {
            entity = new StringEntity(text, ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), ENCODING_UTF8));
        }
        return post(url, entity, null, (Integer) null);
    }

    /**
     * 文本格式提交数据
     *
     * @param url
     * @param text
     * @param timeout
     * @return
     * @throws ClientProtocolException, HttpResponseException, IOException
     */
    public String postText(String url, String text, Integer timeout, String encoding) throws ClientProtocolException, HttpResponseException, IOException {
        HttpEntity entity = null;
        if (StringUtils.isNotBlank(text)) {
            entity = new StringEntity(text, ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), StringUtils.isEmpty(encoding) ? ENCODING_UTF8 : encoding));
        }
        return post(url, entity, null, (Integer) null);
    }

    /**
     * XML格式提交数据
     *
     * @param url
     * @param xml
     * @param encoding 编码格式
     * @return
     * @throws ClientProtocolException, HttpResponseException, IOException
     */
    public String postXml(String url, String xml, String encoding) throws ClientProtocolException, HttpResponseException, IOException {
        HttpEntity entity = null;
        if (StringUtils.isNotBlank(xml)) {
            entity = new StringEntity(xml, ContentType.create(CONTENT_TYPE_XML, encoding));
        }
        return post(url, entity, null, null);
    }

    /**
     * xml的header
     */
    private static final String CONTENT_TYPE_XML = "application/xml";
//    private static final String CONTENT_TYPE_XML = "text/xml";

    /**
     * XML格式提交数据
     *
     * @param url
     * @param xml
     * @return
     * @throws ClientProtocolException, HttpResponseException, IOException
     */
    public String postXml(String url, String xml, Integer timeout) throws ClientProtocolException, HttpResponseException, IOException {
        HttpEntity entity = null;
        if (StringUtils.isNotBlank(xml)) {
            entity = new StringEntity(xml, ContentType.create(CONTENT_TYPE_XML, ENCODING_UTF8));
        }
        return post(url, entity, null, timeout);
    }

    /**
     * XML格式提交数据
     *
     * @param url
     * @param xml
     * @param encoding 编码格式
     * @return
     * @throws ClientProtocolException, HttpResponseException, IOException
     */
    public String postXml(String url, String xml, Integer timeout, String encoding) throws ClientProtocolException, HttpResponseException, IOException {
        HttpEntity entity = null;
        if (StringUtils.isNotBlank(xml)) {
            entity = new StringEntity(xml, ContentType.create(CONTENT_TYPE_XML, StringUtils.isEmpty(encoding) ? ENCODING_UTF8 : encoding));
        }
        return post(url, entity, null, timeout);
    }


    /**
     * post数据
     *
     * @param url
     * @param entity
     * @param headers
     * @param timeout
     * @return
     * @throws ClientProtocolException, HttpResponseException, IOException
     */
    public String post(String url, HttpEntity entity, Header[] headers, Integer timeout) throws ClientProtocolException, HttpResponseException, IOException {
        HttpPost post = new HttpPost(url);
        if (timeout != null) {
            setHttpConfig(timeout, post);
        }
        post.setHeader("Accept-Charset", ENCODING_UTF8);
        if (entity != null) {
            post.setEntity(entity);
        }
        if (ArrayUtils.isNotEmpty(headers)) {
            post.setHeaders(headers);
        }
        try (CloseableHttpResponse response = httpClient.execute(post)) {
            int status = response.getStatusLine().getStatusCode();
            if (status != 200) {
                EntityUtils.consumeQuietly(response.getEntity());
                throw createHttpResponseException("POST", response, url);
            } else {
                return EntityUtils.toString(response.getEntity(), CHARSET_UTF8);
            }
        }
    }

    /**
     * get 请求
     *
     * @param url
     * @param headers
     * @param timeout
     * @return
     * @throws ClientProtocolException, HttpResponseException, IOException
     */
    public String get(String url, Header[] headers, Integer timeout) throws ClientProtocolException, HttpResponseException, IOException {
        return get(url, null, null, headers, timeout, null);
    }

    /**
     * get 请求, UTF8编码
     *
     * @param url
     * @param params
     * @param <T>
     * @return
     * @throws ClientProtocolException, HttpResponseException, IOException
     */
    public <T> String get(String url, Map<String, T> params) throws ClientProtocolException, HttpResponseException, IOException {
        return this.get(url, params, ENCODING_UTF8, null, null, null);
    }

    /**
     * get 请求
     *
     * @param url
     * @param params
     * @param charset
     * @param <T>
     * @return
     * @throws ClientProtocolException, HttpResponseException, IOException
     */
    public <T> String get(String url, Map<String, T> params, String charset) throws ClientProtocolException, HttpResponseException, IOException {
        return this.get(url, params, charset, null, null, null);
    }

    /**
     * get 请求
     *
     * @param url
     * @param params
     * @param timeout
     * @param <T>
     * @return
     * @throws ClientProtocolException, HttpResponseException, IOException
     */
    public <T> String get(String url, Map<String, T> params, Integer timeout) throws ClientProtocolException, HttpResponseException, IOException {
        return this.get(url, params, null, null, timeout, null);
    }

    /**
     * get 请求
     *
     * @param url
     * @param params
     * @param charset
     * @param <T>
     * @return
     * @throws ClientProtocolException, HttpResponseException, IOException
     */
    public <T> String get(String url, Map<String, T> params, String charset, Integer timeout) throws ClientProtocolException, HttpResponseException, IOException {
        return this.get(url, params, charset, null, timeout, null);
    }

    /**
     * get 请求
     *
     * @param url
     * @param params
     * @param charset
     * @param headers
     * @param authorizationCode
     * @param timeout
     * @param <T>
     * @return
     * @throws ClientProtocolException, HttpResponseException, IOException
     */
    public <T> String get(String url, Map<String, T> params, String charset, Header[] headers, Integer timeout,
                          String authorizationCode) throws ClientProtocolException, HttpResponseException, IOException {
        if (charset == null) {
            charset = ENCODING_UTF8;
        }
        RequestBuilder builder = RequestBuilder.get(url).setCharset(Charset.forName(charset));
        if (ArrayUtils.isNotEmpty(headers)) {
            this.addHeaders(builder::addHeader, headers);
        }

        if (params != null && !params.isEmpty()) {
            this.addParams(builder::addParameter, params);
        }

        if (timeout != null) {
            builder.setConfig(this.getRequestConfig(timeout));
        }
        builder.addHeader("Accept-Charset", ENCODING_UTF8);
        if (StringUtils.isNotBlank(authorizationCode)) {
            builder.addHeader("Authorization", authorizationCode);
        }

        try (CloseableHttpResponse response = httpClient.execute(builder.build())) {
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() != 200) {
                EntityUtils.consumeQuietly(response.getEntity());
                throw createHttpResponseException("GET", response, url);
            } else {
                return EntityUtils.toString(response.getEntity(), CHARSET_UTF8);
            }
        }

    }

    /**
     * 记录最长异常数据
     */
    private static final int ERR_MAX_LENGTH = 512;

    /**
     * 构建非200请求异常
     *
     * @param method
     * @param response
     * @param url
     * @return
     */
    private HttpResponseException createHttpResponseException(String method, CloseableHttpResponse response, String url) {
        StatusLine statusLine = response.getStatusLine();
        String err = null;
        int length = 0;
        try {
            err = EntityUtils.toString(response.getEntity(), CHARSET_UTF8);
            length = StringUtils.length(err);
            if (length > ERR_MAX_LENGTH) {
                err = err.substring(0, ERR_MAX_LENGTH);
            }
        } catch (Exception e) {
        }
        StringBuilder msg = new StringBuilder(StringUtils.length(err) + 256);
        msg.append("ERROR [").append(method).append("][").append(url).append("]: ");
        if (err != null) {
            msg.append(err);
            if (length > err.length()) {
                msg.append("ignore:").append(length - err.length()).append(')');
            }
        }
        return new HttpResponseException(statusLine.getStatusCode(),
                msg.toString());
    }

    /**
     * 以put形式提交json数据
     *
     * @param url
     * @param authorizationCode
     * @param params
     * @return
     * @throws ClientProtocolException, HttpResponseException, IOException
     */
    public String putJson(String url, String authorizationCode, Object params) throws ClientProtocolException, HttpResponseException, IOException {
        return this.putJson(url, authorizationCode, params == null ? null : toJsonStr(params));
    }

    /**
     * 以put形式提交json数据
     *
     * @param url
     * @param authorizationCode
     * @param jsonData
     * @return
     * @throws ClientProtocolException, HttpResponseException, IOException
     */
    public String putJson(String url, String authorizationCode, String jsonData) throws ClientProtocolException, HttpResponseException, IOException {
        HttpPut httpPut = new HttpPut(url);
        httpPut.setHeader("Accept-Charset", ENCODING_UTF8);
        if (StringUtils.isNotBlank(authorizationCode)) {
            httpPut.addHeader("Authorization", authorizationCode);
        }
        StringEntity stringEntity;
        if (jsonData != null) {
            stringEntity = new StringEntity(jsonData,
                    ContentType.create(ContentType.APPLICATION_JSON.getMimeType(), ENCODING_UTF8));
            httpPut.setEntity(stringEntity);
        }

        String res = null;
        try (CloseableHttpResponse httpResponse = httpClient.execute(httpPut)) {
            int status = httpResponse.getStatusLine().getStatusCode();
            if (200 == status) {
                res = EntityUtils.toString(httpResponse.getEntity(), CHARSET_UTF8);
            } else {
                EntityUtils.consumeQuietly(httpResponse.getEntity());
                throw createHttpResponseException("PUT", httpResponse, url);
            }
        }
        return res;
    }

    /**
     * 设置超时时间
     *
     * @param timeout
     * @param request
     */
    private void setHttpConfig(Integer timeout, HttpRequestBase request) {
        if (timeout == null) {
            return;
        }
        request.setConfig(this.getRequestConfig(timeout));
    }

    /**
     * 设置超时时间
     *
     * @param timeout
     */
    private RequestConfig getRequestConfig(int timeout) {
        return (this.httpClient instanceof Configurable ?
                RequestConfig.copy(((Configurable) this.httpClient).getConfig()) : RequestConfig.custom())
                .setSocketTimeout(timeout).build();
    }

    /**
     * 添加Header
     *
     * @param consumer
     * @param headers
     * @return
     */
    private void addHeaders(Consumer<Header> consumer, Header[] headers) {
        if (headers != null) {
            for (Header h : headers) {
                if (StringUtils.equalsIgnoreCase("Content-Length", h.getName())) {
                    continue;
                }
                consumer.accept(h);
            }
        }
    }

    /**
     * 添加请求参数
     *
     * @param consumer
     * @param data
     * @param <T>
     */
    private <T> void addParams(Consumer<NameValuePair> consumer, Map<String, T> data) {
        if (data != null && !data.isEmpty()) {
            for (Map.Entry<String, T> entry : data.entrySet()) {
                Object value = entry.getValue();
                if (value != null) {
                    if (value instanceof String) {
                        consumer.accept(new BasicNameValuePair(entry.getKey(), (String) value));
                    } else if (value instanceof Iterable) {
                        for (Object v : ((Iterable<?>) value)) {
                            if (v != null) {
                                consumer.accept(new BasicNameValuePair(entry.getKey(), String.valueOf(v)));
                            }
                        }
                    } else if (value.getClass().isArray()) {
                        int len = Array.getLength(value);
                        for (int i = 0; i < len; i++) {
                            Object v = Array.get(value, i);
                            if (v != null) {
                                consumer.accept(new BasicNameValuePair(entry.getKey(), String.valueOf(v)));
                            }
                        }
                    } else {
                        consumer.accept(new BasicNameValuePair(entry.getKey(), String.valueOf(value)));
                    }
                }
            }
        }
    }
}
