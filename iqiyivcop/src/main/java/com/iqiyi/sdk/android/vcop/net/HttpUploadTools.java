//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.net;

import com.iqiyi.sdk.android.vcop.api.VCOPException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class HttpUploadTools {
    private static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private static final String TWOHYPHENS = "--";
    private static final String END = "\r\n";
    private static final String BOUNDARY = "==iqiyiqcboundary";
    private static final String END_BOUNDARY_END = "--==iqiyiqcboundary--";
    private final int CONNECT_TIMEOUT = 5000;
    private final int READ_TIMEOUT = 10000;
    private String uploadUrl = "";

    public HttpUploadTools(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    private HttpURLConnection getHttpURLConnection() {
        try {
            URL e = new URL(this.uploadUrl);
            HttpURLConnection con = (HttpURLConnection)e.openConnection();
            con.setReadTimeout(10000);
            con.setConnectTimeout(5000);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary===iqiyiqcboundary");
            con.connect();
            return con;
        } catch (IOException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public String HttpToolsUploadPartFile(List<NameValuePair> params, byte[] buffer, int count, String MIMEType) throws VCOPException {
        HttpURLConnection httpConnect = this.getHttpURLConnection();
        if(httpConnect == null) {
            return "";
        } else {
            InputStream is = null;
            DataOutputStream ds = null;

            try {
                ds = new DataOutputStream(httpConnect.getOutputStream());
                this.paramsToPostHead(ds, params);
                this.postDataToBody(ds, buffer, count, MIMEType);
                ds.flush();
                ds.close();
                is = httpConnect.getInputStream();
                if(is != null) {
                    byte[] e = new byte[10240];
                    is.read(e);
                    is.close();
                    return new String(e);
                } else {
                    return "";
                }
            } catch (IOException var9) {
                var9.printStackTrace();
                throw new VCOPException(var9);
            } catch (VCOPException var10) {
                throw new VCOPException(var10);
            }
        }
    }

    private void paramsToPostHead(OutputStream baos, List<NameValuePair> params) throws VCOPException {
        Iterator var4 = params.iterator();

        while(var4.hasNext()) {
            NameValuePair valuePair = (NameValuePair)var4.next();
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

    private void postDataToBody(OutputStream out, byte[] buffer, int count, String MIMEType) throws VCOPException {
        if(buffer != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("--").append("==iqiyiqcboundary").append("\r\n");
            sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append("upload_file").append("\"").append("\r\n");
            sb.append("Content-Type: ").append(MIMEType).append("\r\n").append("\r\n");
            byte[] res = sb.toString().getBytes();

            try {
                out.write(res);
                out.write(buffer, 0, count);
                out.write("\r\n".getBytes());
                out.write("\r\n--==iqiyiqcboundary--".getBytes());
            } catch (IOException var8) {
                throw new VCOPException(var8);
            }
        }
    }
}
