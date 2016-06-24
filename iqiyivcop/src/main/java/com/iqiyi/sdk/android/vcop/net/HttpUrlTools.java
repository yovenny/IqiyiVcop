//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.net;

import android.util.Log;

import com.iqiyi.sdk.android.vcop.api.VCOPClass;
import com.iqiyi.sdk.android.vcop.api.VCOPException;
import com.iqiyi.sdk.android.vcop.util.VCOPUtil;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class HttpUrlTools {
    private static HttpUrlTools httptool = null;

    public static HttpUrlTools getInstance() {
        if (httptool == null) {
            httptool = new HttpUrlTools();
        }
        return httptool;
    }


    public String HttpToolsGetMethod(String path) throws VCOPException {
        HttpURLConnection conn;
        InputStream is;
        String resultString = "";
        try {
            URL url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(10000);
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                resultString = VCOPUtil.readstream(is);
            } else {
                is = conn.getInputStream();
                resultString = VCOPUtil.readstream(is);
            }
            return resultString;
        } catch (Exception var5) {
            throw new VCOPException(var5);
        }
    }

    public String HttpToolsRequestUpload(List<NameValuePair> params) throws VCOPException {
        String resultString = "";
        String path = VCOPClass.REQUEST_UPLOAD_SERVER + "?" + VCOPUtil.ParamsConstructUrl(params);
        Log.d("iqiyivcop", " upload url : " + path);
        HttpURLConnection conn;
        InputStream is;

        try {
            URL url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(10000);
            conn.setRequestMethod("GET");
            conn.connect();
            Log.d("iqiyivcop", " http response: " + conn.getResponseCode());
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                resultString = VCOPUtil.readstream(is);
            } else {
                is = conn.getInputStream();
                resultString = VCOPUtil.readstream(is);
            }
            return resultString.toString();
        } catch (Exception var10) {
            var10.getLocalizedMessage();
            Log.d("iqiyivcop", " http exception : " + var10.getLocalizedMessage());
            try {
                URL url = new URL(VCOPClass.REQUEST_UPLOAD_SERVER_BACKUP);
                conn = (HttpURLConnection) url.openConnection();
                Iterator var8 = params.iterator();
                while (var8.hasNext()) {
                    NameValuePair e1 = (NameValuePair) var8.next();
                    conn.addRequestProperty(e1.getName(), e1.getValue());
                }
                conn.setConnectTimeout(5 * 1000);
                conn.setReadTimeout(10000);
                conn.setRequestMethod("GET");
                conn.connect();
                if (conn.getResponseCode() == 200) {
                    is = conn.getInputStream();
                    resultString = VCOPUtil.readstream(is);
                } else {
                    is = conn.getInputStream();
                    resultString = VCOPUtil.readstream(is);
                }
                return resultString;
            } catch (Exception var9) {
                throw new VCOPException(var9);
            }
        }
    }

    public String HttpToolsGetMethodWithoutHeader(String path, List<NameValuePair> params) throws VCOPException {
        String resultString = "";
        String tempUrl = path + "?" + VCOPUtil.ParamsConstructUrl(params);
        HttpURLConnection conn;
        InputStream is;
        try {
            URL url = new URL(tempUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(10000);
            conn.setRequestMethod("GET");
            conn.connect();
            Log.d("iqiyivcop", " http response: " + conn.getResponseCode());
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                resultString = VCOPUtil.readstream(is);
            } else {
                is = conn.getInputStream();
                resultString = VCOPUtil.readstream(is);
            }
            return resultString.toString();
        } catch (Exception var7) {
            throw new VCOPException(var7);
        }
    }

    public String HttpToolsGetMethod(String path, List<NameValuePair> params) throws VCOPException {
        String resultString = "";
        HttpURLConnection conn;
        InputStream is;
        Iterator var6 = params.iterator();
        try {
            URL url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(10000);
            conn.setRequestMethod("GET");
            while (var6.hasNext()) {
                NameValuePair e = (NameValuePair) var6.next();
                conn.addRequestProperty(e.getName(), e.getValue());
            }
            conn.connect();
            Log.d("iqiyivcop", " http response: " + conn.getResponseCode());
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                resultString = VCOPUtil.readstream(is);
            } else {
                is = conn.getInputStream();
                resultString = VCOPUtil.readstream(is);
            }
            return resultString.toString();
        } catch (Exception var7) {
            throw new VCOPException(var7);
        }
    }

    public String HttpToolsPostMethod(String path, List<NameValuePair> params) throws VCOPException {
        String resultString = "";
        HttpURLConnection conn;
        InputStream is;
        try {
            URL url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(10000);
            conn.setRequestMethod("POST");
            conn.connect();
            paramsToPostHead(conn.getOutputStream(), params);
            Log.d("iqiyivcop", " http response: " + conn.getResponseCode());
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                resultString = VCOPUtil.readstream(is);
            } else {
                is = conn.getInputStream();
                resultString = VCOPUtil.readstream(is);
            }
            return resultString;
        } catch (Exception var6) {
            throw new VCOPException(var6);
        }
    }


    public String HttpToolsPostMethodWithHead(String path, List<NameValuePair> params) throws VCOPException {
        Log.i("HttpTools", "HttpToolsPostMethodWithHead() url: " + path);
        String resultString = "";
        HttpURLConnection conn;
        InputStream is;
        try {
            URL url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(10000);
            conn.setRequestMethod("POST");
            Iterator var6 = params.iterator();
            while (var6.hasNext()) {
                NameValuePair httpResponse = (NameValuePair) var6.next();
                conn.setRequestProperty(httpResponse.getName(), httpResponse.getValue());
            }
            conn.connect();
            Log.d("iqiyivcop", " http response: " + conn.getResponseCode());
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                resultString = VCOPUtil.readstream(is);
            } else {
                is = conn.getInputStream();
                resultString = VCOPUtil.readstream(is);
            }
            return resultString;
        } catch (Exception var7) {
            throw new VCOPException(var7);
        }
    }

    private void paramsToPostHead(OutputStream baos, List<NameValuePair> params) throws VCOPException {
        Iterator var4 = params.iterator();
        while (var4.hasNext()) {
            NameValuePair valuePair = (NameValuePair) var4.next();
            StringBuilder sb = new StringBuilder(10);
            sb.setLength(0);
            sb.append("--").append("==iqiyiqcboundary").append("\r\n");
            sb.append("content-disposition: form-data; name=\"").append(valuePair.getName()).append("\"").append("\r\n").append("\r\n");
            sb.append(valuePair.getValue()).append("\r\n");
            byte[] res = sb.toString().getBytes();

            try {
                baos.write(res);
            } catch (IOException var8) {
                throw new VCOPException(var8);
            }
        }

    }
}
