//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.unit;

import com.iqiyi.sdk.android.vcop.api.ReturnCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GetUrlListResponseMsg extends BaseResponseMsg {
    private List<Map<String, Map<String, String>>> urlList = new ArrayList();
    private Map<String, Map<String, String>> urlMap = new HashMap();

    public GetUrlListResponseMsg() {
    }

    public List<Map<String, Map<String, String>>> getUrlList() {
        return this.urlList;
    }

    public void setUrlList(List<Map<String, Map<String, String>>> urlList) {
        this.urlList = urlList;
    }

    public Map<String, Map<String, String>> getUrlMap() {
        return this.urlMap;
    }

    public void setUrlMap(Map<String, Map<String, String>> urlMap) {
        this.urlMap = urlMap;
    }

    private static Map<String, String> parse(JSONObject jsonobject, String type) {
        try {
            JSONObject json = jsonobject.getJSONObject(type);
            HashMap e = new HashMap();
            Iterator it = json.keys();

            while(it.hasNext()) {
                String key = (String)it.next();
                e.put(key, json.optString(key));
            }

            return e;
        } catch (JSONException var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public static GetUrlListResponseMsg parseMsg(String jsonStr) {
        if(jsonStr.indexOf("{") >= 0) {
            try {
                GetUrlListResponseMsg e = new GetUrlListResponseMsg();
                JSONObject json = new JSONObject(jsonStr);
                e.setCode(json.optString("code"));
                if(!ReturnCode.isSuccess(e.getCode())) {
                    e.setMsg(json.optString("msg"));
                } else {
                    JSONObject jsonData = json.getJSONObject("data");
                    if(jsonData != null) {
                        HashMap myMap = new HashMap();
                        Map mp4list = parse(jsonData, "mp4");
                        if(mp4list != null) {
                            myMap.put("mp4", mp4list);
                        }

                        Map m3u8list = parse(jsonData, "m3u8");
                        if(m3u8list != null) {
                            myMap.put("m3u8", m3u8list);
                        }

                        e.urlMap = myMap;
                    }
                }

                return e;
            } catch (JSONException var7) {
                var7.printStackTrace();
                return new GetUrlListResponseMsg();
            }
        } else {
            return new GetUrlListResponseMsg();
        }
    }
}
