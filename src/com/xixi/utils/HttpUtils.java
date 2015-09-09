package com.xixi.utils;

import com.xixi.android.MucangConfig;
import com.xixi.android.XiXiApp;
import com.xixi.cache.ImageCache.RetainFragment;
import com.xixi.utils.APNUtils.MyProxy;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * HTTP协议相关的工具类
 *
 * @author AndyMao
 *
 */
public class HttpUtils {

    private static final String TAG = "AndyMao";
    private static boolean useProxy = false;// 默认是不用代理，也就WIFI连接。
    private static String proxyHost;
    private static int proxyPort;

    public static boolean isUseProxy() {
        return useProxy;
    }

    public static void disableProxy() {
        useProxy = false;
    }

    public static void enableProxy(String host, int port) {
        useProxy = true;
        proxyHost = host;
        proxyPort = port;
        Log.i("AndyMao", "proxy.host=" + host + ",port=" + port);
    }

    public static String httpGet(String url, List<? extends NameValuePair> entries) throws IOException {
        HttpClient httpclient = createHttpClient();
        try {
        	url +=URLEncodedUtils.format(entries, "UTF-8");
            HttpGet req = new HttpGet(url);
            HttpResponse rsp = httpclient.execute(req);
            HttpEntity entity = rsp.getEntity();

            if (entity != null) {
                String s = EntityUtils.toString(entity);
                Log.i(TAG, "HttpUtils,url=" + url + ",content=" + s);
                return s;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new IOException("网络连接失败！");
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return null;
    }

    public static InputStream httpGetStream(String url) throws IOException {
        URL u = new URL(url);
        return u.openStream();
    }

    public static String httpGet(String url) throws IOException {
        HttpClient httpclient = createHttpClient();
        try {
            HttpGet req = new HttpGet(url);
            HttpResponse rsp = httpclient.execute(req);
            HttpEntity entity = rsp.getEntity();
            if (entity != null) {
                String s = EntityUtils.toString(entity, "UTF-8");
                Log.d(TAG, "HttpUtils,url=" + url + ",content=" + s);
                return s;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new IOException("网络连接失败！");
        } finally {
            httpclient.getConnectionManager().shutdown();
        }

        return null;
    }
    
    public static String httpGetWithCache(String url, boolean useCache){
    	RetainFragment cache = XiXiApp.findFragmentByTag("");
    	return null;
    }

    public static String httpPost(String url, List<? extends NameValuePair> entries) throws IOException {
        HttpClient httpclient = createHttpClient();
        try {
            HttpPost post = new HttpPost(url);
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(entries, "UTF-8");
            post.setEntity(entity);
            HttpResponse rsp = httpclient.execute(post);
            HttpEntity backEntity = rsp.getEntity();
            if (backEntity != null) {
                String s = EntityUtils.toString(backEntity);
                Log.i(TAG, "HttpUtils,url=" + url + ",content=" + s);
                return s;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new IOException("网络连接失败！");
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return null;
    }

    public static HttpClient createHttpClient() {
        checkNetworkEnabled();
        Log.d(TAG, "HttpUtils,useProxy:" + useProxy);
        HttpClient httpClient = new DefaultHttpClient();
        if (useProxy) {
            HttpHost proxy = new HttpHost(proxyHost, proxyPort, "http");
            httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }
        // TODO 这两个可以提出来做为设置
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
        return httpClient;
    }

    public static boolean checkNetworkEnabled() {
        ConnectivityManager cm = (ConnectivityManager) MucangConfig.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
            return false;
        } else {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                HttpUtils.disableProxy();
            } else {
                MyProxy proxy = APNUtils.getMyProxy(MucangConfig.getContext());
                if (proxy == null) {
                    HttpUtils.disableProxy();
                } else {
                    HttpUtils.enableProxy(proxy.getHost(), proxy.getPort());
                }
            }
            return true;
        }
    }
}
