//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.unit;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class FetchVideoStatusResponseMsg extends BaseResponseMsg {
    private HashMap<String, Object> mDataMap = new HashMap();

    public FetchVideoStatusResponseMsg() {
    }

    public HashMap<String, Object> getData() {
        return this.mDataMap;
    }

    public void setData(HashMap<String, Object> dataMap) {
        this.mDataMap = dataMap;
    }

    private static HashMap<String, Object> genResponseMap(JSONObject jobj) throws JSONException {
        Iterator iter = jobj.keys();
        HashMap map = new HashMap();

        while(iter.hasNext()) {
            String key = (String)iter.next();
            Object value = jobj.get(key);
            if(value instanceof String||value instanceof Integer) {
                map.put(key, value);
            } else {
                JSONObject jobj2 = jobj.getJSONObject(key);
                HashMap value2 = genResponseMap(jobj2);
                map.put(key, value2);
            }
        }

        return map;
    }

    public static FetchVideoStatusResponseMsg parseMsg(String jsonStr) {
        FetchVideoStatusResponseMsg msg = new FetchVideoStatusResponseMsg();
        if(jsonStr.indexOf("{") >= 0) {
            try {
                JSONObject json = new JSONObject(jsonStr);
                msg.setCode(json.optString("code"));
                msg.setMsg(json.optString("msg"));
                JSONObject e = json.getJSONObject("data");
                HashMap map = genResponseMap(e);
                msg.setData(map);
                return msg;
            } catch (JSONException var5) {
                var5.printStackTrace();
            }
        }

        return msg;
    }
}
