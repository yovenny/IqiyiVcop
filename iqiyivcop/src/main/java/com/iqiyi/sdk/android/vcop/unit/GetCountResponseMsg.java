//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.unit;

import com.iqiyi.sdk.android.vcop.api.ReturnCode;

import org.json.JSONException;
import org.json.JSONObject;

public class GetCountResponseMsg extends BaseResponseMsg {
    private int count = -1;

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public GetCountResponseMsg() {
        this.count = -1;
    }

    public static GetCountResponseMsg parseMsg(String jsonStr) {
        if(jsonStr.indexOf("{") >= 0) {
            try {
                GetCountResponseMsg e = new GetCountResponseMsg();
                JSONObject json = new JSONObject(jsonStr);
                e.setCode(json.optString("code"));
                if(!ReturnCode.isSuccess(e.getCode())) {
                    e.setMsg(json.optString("msg"));
                } else {
                    JSONObject jsonData = json.getJSONObject("data");
                    if(jsonData != null) {
                        e.setCount(jsonData.getInt("count"));
                    }
                }

                return e;
            } catch (JSONException var4) {
                var4.printStackTrace();
                return new GetCountResponseMsg();
            }
        } else {
            return new GetCountResponseMsg();
        }
    }
}
