package com.bjlvgo.lvgo.utils.http;


import com.bjlvgo.lvgo.base.LvgoApplication;
import com.bjlvgo.lvgo.utils.http.Cache.StringCache;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * 网络请求封装类
 * Created by Chuxi on 2016/3/22.
 */
public class HttpUtils {

    private static HttpUtils httpUtils = null;
    private final long maxMemory = Runtime.getRuntime().maxMemory() / 8;
    private final File dirFile = LvgoApplication.getContext().getCacheDir();
    private OkHttpClient client = null;

    private HttpUtils() {
        if (client == null) {
            synchronized (HttpUtils.class) {
                client = new OkHttpClient();
                final File cacheFile = new File(dirFile, "HttpResponseCache");
                client.setCache(new Cache(cacheFile, maxMemory));
                client.setConnectTimeout(10, TimeUnit.SECONDS);
                client.setReadTimeout(10, TimeUnit.SECONDS);
                client.setWriteTimeout(30, TimeUnit.SECONDS);
            }
        }
    }

    public static HttpUtils newInstance() {
        if (httpUtils == null) {
            synchronized (HttpUtils.class) {
                httpUtils = new HttpUtils();
            }
        }
        return httpUtils;
    }

    /**
     * 同步网络请求GET
     *
     * @param url
     * @return
     * @throws IOException
     */
    private Response toExecute(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        return client.newCall(request).execute();
    }

    /**
     * 网络获取数据
     *
     * @param url
     * @return
     */
    public String stringRequest(String url) {
        String body = null;
        StringCache cache = StringCache.newInstance();
        try {
            body = cache.readCache(url);
            if (body == null) {
                body = client.newCall(new Request.Builder().url(url).build()).execute().body().string();
            }
            cache.writeCache(body, url);
            return body;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Byte byteeRequest(String url) {

        try {
            toExecute(url).body().bytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream inputRequest(String url) {

        try {
            return toExecute(url).body().byteStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 异步网络请求GET
     *
     * @param url
     */
    public void enqueue(final String url, final Databack databack) {

        final StringCache cache = StringCache.newInstance();
        String body = null;
        body = cache.readCache(url);
        if (body == null) {
            client.newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    databack.onFaile(request, e);
                }
                @Override
                public void onResponse(Response response) throws IOException {
                    String data = response.body().string();
                    cache.writeCache(data, url);
                    databack.onSuccess(data);
                }
            });
        } else {
            databack.onSuccess(body);
        }
    }

    public interface Databack {

        public void onSuccess(String data);

        void onFaile(Request request, IOException e);
    }
}
