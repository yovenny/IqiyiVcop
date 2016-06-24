//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.util;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.iqiyi.sdk.android.vcop.net.NameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.List;

public class VCOPUtil {
    public static final String DEBUGTAG = "iqiyivcop";
    private static final String[][] MIME_MapTable = new String[][]{{".3gp", "video/3gpp"}, {".apk", "application/vnd.android.package-archive"}, {".asf", "video/x-ms-asf"}, {".avi", "video/x-msvideo"}, {".bin", "application/octet-stream"}, {".bmp", "image/bmp"}, {".c", "text/plain"}, {".class", "application/octet-stream"}, {".conf", "text/plain"}, {".cpp", "text/plain"}, {".doc", "application/msword"}, {".exe", "application/octet-stream"}, {".gif", "image/gif"}, {".gtar", "application/x-gtar"}, {".gz", "application/x-gzip"}, {".h", "text/plain"}, {".htm", "text/html"}, {".html", "text/html"}, {".jar", "application/java-archive"}, {".java", "text/plain"}, {".jpeg", "image/jpeg"}, {".jpg", "image/jpeg"}, {".js", "application/x-javascript"}, {".log", "text/plain"}, {".m3u", "audio/x-mpegurl"}, {".m4a", "audio/mp4a-latm"}, {".m4b", "audio/mp4a-latm"}, {".m4p", "audio/mp4a-latm"}, {".m4u", "video/vnd.mpegurl"}, {".m4v", "video/x-m4v"}, {".mov", "video/quicktime"}, {".mp2", "audio/x-mpeg"}, {".mp3", "audio/x-mpeg"}, {".mp4", "video/mp4"}, {".mpc", "application/vnd.mpohun.certificate"}, {".mpe", "video/mpeg"}, {".mpeg", "video/mpeg"}, {".mpg", "video/mpeg"}, {".mpg4", "video/mp4"}, {".mpga", "audio/mpeg"}, {".msg", "application/vnd.ms-outlook"}, {".ogg", "audio/ogg"}, {".pdf", "application/pdf"}, {".png", "image/png"}, {".pps", "application/vnd.ms-powerpoint"}, {".ppt", "application/vnd.ms-powerpoint"}, {".prop", "text/plain"}, {".rar", "application/x-rar-compressed"}, {".rc", "text/plain"}, {".rmvb", "audio/x-pn-realaudio"}, {".rtf", "application/rtf"}, {".sh", "text/plain"}, {".tar", "application/x-tar"}, {".tgz", "application/x-compressed"}, {".txt", "text/plain"}, {".wav", "audio/x-wav"}, {".wma", "audio/x-ms-wma"}, {".wmv", "audio/x-ms-wmv"}, {".wps", "application/vnd.ms-works"}, {".xml", "text/plain"}, {".z", "application/x-compress"}, {".zip", "application/zip"}, {"", "*/*"}};

    public VCOPUtil() {
    }

    public static void showAlertDialog(final Context context, final String title, final String msg) {
        (new Handler(context.getMainLooper())).post(new Runnable() {
            public void run() {
                Builder builder = new Builder(context);
                builder.setTitle(title);
                builder.setMessage(msg);
                builder.create().show();
            }
        });
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, 1).show();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connect = (ConnectivityManager)context.getSystemService("connectivity");
        if(connect == null) {
            return false;
        } else {
            NetworkInfo[] info = connect.getAllNetworkInfo();
            if(info != null) {
                for(int i = 0; i < info.length; ++i) {
                    if(info[i].getState() == State.CONNECTED) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public static boolean isInt(String str) {
        char[] content = str.toCharArray();

        for(int i = 0; i < content.length; ++i) {
            if(content[i] > 57 || content[i] < 48) {
                return false;
            }
        }

        return true;
    }

    public static Bundle parseUrlParams(String url) {
        try {
            URL e = new URL(url);
            Bundle b = parseUrl(e.getQuery());
            b.putAll(parseUrl(e.getRef()));
            return b;
        } catch (MalformedURLException var3) {
            return new Bundle();
        }
    }

    public static Bundle parseUrl(String s) {
        Bundle bundle = new Bundle();
        if(s != null) {
            String[] array = s.split("&");
            String[] var6 = array;
            int var5 = array.length;

            for(int var4 = 0; var4 < var5; ++var4) {
                String parameter = var6[var4];
                String[] v = parameter.split("=");
                bundle.putString(URLDecoder.decode(v[0]), URLDecoder.decode(v[1]));
            }
        }

        return bundle;
    }

    public static String ParamsConstructUrl(List<NameValuePair> params) {
        if(params == null) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            Iterator var4 = params.iterator();

            while(var4.hasNext()) {
                NameValuePair param = (NameValuePair)var4.next();
                if(first) {
                    first = false;
                } else {
                    sb.append("&");
                }

                String key = param.getName();
                String value = param.getValue();
                if(value == null) {
                    value = "";
                } else {
                    sb.append(URLEncoder.encode(key) + "=" + URLEncoder.encode(value));
                }
            }

            return sb.toString();
        }
    }

    public static String getMIMEType(String fName) {
        String type = "*/*";
        int dotIndex = fName.lastIndexOf(".");
        if(dotIndex < 0) {
            return type;
        } else {
            String end = fName.substring(dotIndex, fName.length()).toLowerCase();
            if(end == "") {
                return type;
            } else {
                for(int i = 0; i < MIME_MapTable.length; ++i) {
                    if(end.equals(MIME_MapTable[i][0])) {
                        type = MIME_MapTable[i][1];
                    }
                }

                return type;
            }
        }
    }

    public static String VideoUri2FilePath(Activity context, Uri uri) {
        String[] imgs = new String[]{"_data"};
        Cursor cursor = context.managedQuery(uri, imgs, (String)null, (String[])null, (String)null);
        if(cursor != null) {
            int index = cursor.getColumnIndexOrThrow("_data");
            cursor.moveToFirst();
            return cursor.getString(index);
        } else {
            return "";
        }
    }

    public static final String getMd5(byte[] bytes, int count) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            MessageDigest e = MessageDigest.getInstance("MD5");
            e.update(bytes, 0, count);
            byte[] md = e.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;

            for(int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 15];
                str[k++] = hexDigits[byte0 & 15];
            }

            return new String(str);
        } catch (Exception var10) {
            Log.d("iqiyivcop", var10.toString());
            var10.printStackTrace();
            return null;
        }
    }

    public static final boolean isSameMd5(String md51, String md52) {
        return md51 == null && md52 == null?true:(md51 != null && md52 != null?md51.toLowerCase().compareTo(md52.toLowerCase()) == 0:false);
    }

    public static String readstream(InputStream in) {
        StringBuffer resultString = new StringBuffer();

        try {
            BufferedReader e = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = "";

            while((line = e.readLine()) != null) {
                resultString.append('\n');
                resultString.append(line);
            }
        } catch (Exception var4) {
            Log.d("iqiyivcop", var4.toString());
        }

        return resultString.toString();
    }
}
