//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.unit;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseResponeMsg {
    public String code = "";
    public String fileId = "";

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFileId() {
        return this.fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public BaseResponeMsg(String code, String fileId) {
        this.code = code;
        this.fileId = fileId;
    }

    public BaseResponeMsg() {
    }

    public static BaseResponeMsg parseMsg(String jsonStr) {
        if(jsonStr.indexOf("{") >= 0) {
            try {
                BaseResponeMsg e = new BaseResponeMsg();
                JSONObject json = new JSONObject(jsonStr);
                e.code = json.optString("code");
                e.fileId = json.optString("file_id");
                return e;
            } catch (JSONException var3) {
                var3.printStackTrace();
                return new BaseResponeMsg();
            }
        } else {
            return new BaseResponeMsg();
        }
    }
}
