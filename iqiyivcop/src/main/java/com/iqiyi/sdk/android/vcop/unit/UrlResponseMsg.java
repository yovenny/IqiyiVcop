//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.unit;

import com.iqiyi.sdk.android.vcop.api.ReturnCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UrlResponseMsg extends BaseResponseMsg {
    private Map<String, String> dataMap = new HashMap();

    public Map<String, String> getDataMap() {
        return this.dataMap;
    }

    public void setDataMap(Map<String, String> dataMap) {
        this.dataMap = dataMap;
    }

    public UrlResponseMsg() {
    }

    public static UrlResponseMsg parseMsg(String jsonStr) {
        if(jsonStr.indexOf("{") < 0) {
            return new UrlResponseMsg();
        } else {
            try {
                UrlResponseMsg e = new UrlResponseMsg();
                JSONObject json = new JSONObject(jsonStr);
                e.setCode(json.optString("code"));
                if(!ReturnCode.isSuccess(e.getCode())) {
                    e.setMsg(json.optString("msg"));
                } else {
                    JSONObject jsonData = json.getJSONObject("data");
                    if(jsonData != null) {
                        Iterator it = jsonData.keys();

                        while(it.hasNext()) {
                            String key = (String)it.next();
                            e.dataMap.put(key.trim(), jsonData.optString(key));
                        }
                    }
                }

                return e;
            } catch (JSONException var6) {
                var6.printStackTrace();
                return new UrlResponseMsg();
            }
        }
    }
}
