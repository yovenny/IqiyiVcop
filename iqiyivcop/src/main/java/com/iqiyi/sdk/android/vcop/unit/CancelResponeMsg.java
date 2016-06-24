//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.unit;

import org.json.JSONException;
import org.json.JSONObject;

public class CancelResponeMsg extends BaseResponseMsg {
    public CancelResponeMsg(String code, String fileId) {
        super(code);
        this.fileId = fileId;
    }

    public CancelResponeMsg() {
    }

    public static CancelResponeMsg parseMsg(String jsonStr) {
        if(jsonStr.indexOf("{") >= 0) {
            CancelResponeMsg msg = new CancelResponeMsg();

            try {
                JSONObject json = new JSONObject(jsonStr);
                msg.setCode(json.optString("code"));
                msg.setMsg(json.optString("msg"));
                msg.setFileId(json.optString("file_id"));
                return msg;
            } catch (JSONException var4) {
                var4.printStackTrace();
                return msg;
            }
        } else {
            return new CancelResponeMsg();
        }
    }
}
