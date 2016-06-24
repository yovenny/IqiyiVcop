//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.unit;

import com.iqiyi.sdk.android.vcop.authorize.Authorize2AccessToken;

import org.json.JSONException;
import org.json.JSONObject;

public class BaiduAuthResponseMsg extends BaseResponseMsg {
    private Authorize2AccessToken auth2AccessToken;

    public BaiduAuthResponseMsg() {
    }

    public BaiduAuthResponseMsg(String code, String msg) {
        super(code, msg);
    }

    public Authorize2AccessToken getAuth2AccessToken() {
        return this.auth2AccessToken;
    }

    public void setAuth2AccessToken(Authorize2AccessToken auth2AccessToken) {
        this.auth2AccessToken = auth2AccessToken;
    }

    public static BaiduAuthResponseMsg parseMsg(String jsonStr) {
        if(jsonStr.indexOf("{") >= 0) {
            BaiduAuthResponseMsg baiduAuthResponseMsg = new BaiduAuthResponseMsg();

            try {
                JSONObject json = new JSONObject(jsonStr);
                baiduAuthResponseMsg.setCode(json.optString("code"));
                baiduAuthResponseMsg.setMsg(json.optString("msg"));
                JSONObject e = json.getJSONObject("data");
                baiduAuthResponseMsg.setAuth2AccessToken(new Authorize2AccessToken(e));
                return baiduAuthResponseMsg;
            } catch (JSONException var4) {
                return baiduAuthResponseMsg;
            }
        } else {
            return new BaiduAuthResponseMsg();
        }
    }
}
