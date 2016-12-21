package com.template.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

/**
 * http操作的工具类
 * 采用Apache httpClient类
 * Created by Cloud on 2016/6/27.
 */
public class HttpClientUtils {
    Logger log = Logger.getLogger(HttpClientUtils.class);

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

    public void post() {

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