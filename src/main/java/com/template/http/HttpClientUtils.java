package com.template.http;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import sun.nio.ch.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * http操作的工具类
 * 采用Apache httpClient类
 * Created by Cloud on 2016/6/27.
 */
public class HttpClientUtils {
    private static Logger log = Logger.getLogger(HttpClientUtils.class);

    // 一些常用固定值
    private static final String CHARSET = "UTF-8";

    public void get() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = "http://ehr.oasoft.co/hrwebapp";
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept-Encoding", "identity");
        System.out.println("excuting request is [" + httpGet.getURI() + "]");
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            System.out.println(response.toString());
            HttpEntity httpEntity = response.getEntity();
            System.out.println("====================分 割 线====================");
            System.out.println("请求响应状态： " + response.getStatusLine());
            System.out.println("Response content length： " + httpEntity.getContentLength());
            System.out.println("--------------------分 割 线--------------------");
            if (null != httpEntity) {
                System.out.println("\n\n\n" + this.readHttpEntity(httpEntity));
            } else {
                System.out.println("httpEntity is null!");
            }
            response.close();
        } catch (IOException e) {
            log.error("http get error !", e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error("CloseableHttpClient关闭时出错！", e);
            }
        }
    }

    /**
     * 发起GET请求
     * @param url
     * @return
     */
    public static String doGetSubmit(String url) {
        GetMethod get = new GetMethod(url);
        // 可换成其他Content-Type格式，此处为json格式演示
        get.setRequestHeader("Content-Type", "application/json");
        get.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 1000 * 10);
        get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        get.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, CHARSET);

        HttpClient httpClient = new HttpClient();
        try {
            int statusCode = httpClient.executeMethod(get);
            InputStream is = get.getResponseBodyAsStream();
            String result = IOUtils.toString(is, CHARSET);
            // TODO 此处可以做一些处理完善
            return result;
        } catch (IOException e) {
            log.error("doGetSubmit 方法错误: ", e);
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 发起POST请求
     * @param url
     * @param params
     * @return
     */
    public static String doPostSubmit(String url, Map<String, String> params) {
        PostMethod post = new PostMethod(url);
        if (params != null && params.size() > 0) {
            NameValuePair[] parametersBody = new NameValuePair[params.size()];
            int i = 0;
            for (String key: params.keySet()) {
                parametersBody[i] = new NameValuePair(key, params.get(key));
            }
            post.setRequestBody(parametersBody);
        }
        // 可换成其他Content-Type格式，此处为普通表单格式演示
        post.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, CHARSET);

        HttpClient httpClient = new HttpClient();
        try {
            int statusCode = httpClient.executeMethod(post);
            InputStream is = post.getResponseBodyAsStream();
            String result = IOUtils.toString(is, CHARSET);
            // 同GET方法此处可做一些完善
            return result;
        } catch (IOException e) {
            log.error("doPostSubmit 方法错误: ", e);
            e.printStackTrace();
        }
        return "";
    }

    private String readHttpEntity(HttpEntity entity) throws IOException {
        if (null == entity) return "";
        InputStream is = entity.getContent();
        int count = 0;
        while (count <= 0) {
            count = (int) entity.getContentLength();
        }
        byte[] bytes = new byte[count];
        int readCount = 0;
        while (readCount < count) {
            readCount += is.read(bytes, readCount, (count - readCount));
        }
        return new String(bytes, 0, readCount, "UTF-8");
    }

    public static void main(String[] arg) {
        HttpClientUtils httpClientUtils = new HttpClientUtils();
        httpClientUtils.get();
    }
}