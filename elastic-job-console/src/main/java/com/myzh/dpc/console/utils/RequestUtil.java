package com.myzh.dpc.console.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * 访问第三方接口
 */
@Component
public class RequestUtil {

    private static Logger logger = LoggerFactory.getLogger(RequestUtil.class);

    public static String doRequest(String url) throws IOException {
        logger.debug("request url: {}", url);
        InputStream input = new URL(url).openStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOUtils.copy(input, byteArrayOutputStream);
        String response = byteArrayOutputStream.toString("UTF-8");
        logger.debug("response result: {}", response);
        return response;
    }

    public static String doRequest(String url, String content, String charset) throws IOException {
        logger.debug("request url: {}", url);
        URL console = new URL(url);
        URLConnection conn = (URLConnection) console.openConnection();
        conn.setDoOutput(true);
        conn.connect();
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        out.write(content.getBytes(charset));
        // 刷新、关闭
        out.flush();
        out.close();

        InputStream input = conn.getInputStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOUtils.copy(input, byteArrayOutputStream);
        String response = byteArrayOutputStream.toString(charset);
        logger.debug("response result: {}", response);
        return response;
    }

    /**
     * post方式请求服务器(https协议)
     *
     * @param url
     *            请求地址
     * @param content
     *            参数
     * @param charset
     *            编码
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws IOException
     */
    public String doRequestByPost(String url, String content, String charset)
            throws NoSuchAlgorithmException, KeyManagementException,
            IOException {
        logger.debug("request url: {}", url);
        URL console = new URL(url);
        HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
        conn.connect();
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        out.write(content.getBytes(charset));
        // 刷新、关闭
        out.flush();
        out.close();
        InputStream is = conn.getInputStream();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOUtils.copy(is, byteArrayOutputStream);
        String response = byteArrayOutputStream.toString("UTF-8");
        logger.debug("response result: {}", response);
        return response;
    }

    /**
     * post方式请求服务器(https协议)
     *
     * @param url
     *            请求地址
     * @param content
     *            参数
     * @param charset
     *            编码
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws IOException
     */
    public String doRequestByHttps(String url, String content, String charset)
            throws NoSuchAlgorithmException, KeyManagementException,
            IOException {
        logger.debug("request url: {}", url);
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
                new java.security.SecureRandom());

        URL console = new URL(url);
        HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
        conn.setSSLSocketFactory(sc.getSocketFactory());
        conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
        conn.setDoOutput(true);
        conn.connect();
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        out.write(content.getBytes(charset));
        // 刷新、关闭
        out.flush();
        out.close();
        InputStream is = conn.getInputStream();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOUtils.copy(is, byteArrayOutputStream);
        String response = byteArrayOutputStream.toString("UTF-8");
        logger.debug("response result: {}", response);
        return response;
    }

    private static class TrustAnyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
