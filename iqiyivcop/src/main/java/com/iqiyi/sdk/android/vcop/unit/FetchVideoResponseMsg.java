//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.unit;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FetchVideoResponseMsg extends BaseResponseMsg {
    private List<Map<String, Object>> mDataList = new ArrayList();

    public FetchVideoResponseMsg() {
    }

    public List<Map<String, Object>> getDataList() {
        return this.mDataList;
    }

    public void setDataList(List<Map<String, Object>> dataList) {
        this.mDataList = dataList;
    }

    public static FetchVideoResponseMsg parseMsg(String jsonStr) {
        if(jsonStr.indexOf("{") < 0) {
            return new FetchVideoResponseMsg();
        } else {
            try {
                FetchVideoResponseMsg e = new FetchVideoResponseMsg();
                JSONObject json = new JSONObject(jsonStr);
                e.setCode(json.optString("code"));
                if(e.getCode().compareTo("Q00001") == 0) {
                    e.setMsg(json.optString("msg"));
                } else {
                    JSONArray jsonArray = json.getJSONArray("data");

                    for(int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                        HashMap map = new HashMap();
                        Iterator iterator = jsonobject.keys();

                        while(iterator.hasNext()) {
                            String key = (String)iterator.next();
                            map.put(key, jsonobject.opt(key));
                        }

                        Log.d("iqiyivcop", "fetch video: " + map.toString());
                        e.mDataList.add(map);
                    }
                }

                return e;
            } catch (JSONException var9) {
                var9.printStackTrace();
                return new FetchVideoResponseMsg();
            }
        }
    }
}
