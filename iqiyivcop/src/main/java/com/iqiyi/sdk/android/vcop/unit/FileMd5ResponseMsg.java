//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.unit;

import org.json.JSONException;
import org.json.JSONObject;

public class FileMd5ResponseMsg extends BaseResponseMsg {
    private String fileMd5 = "";

    public String getFileMd5() {
        return this.fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    public FileMd5ResponseMsg() {
    }

    public static FileMd5ResponseMsg parseMsg(String jsonStr) {
        if(jsonStr.indexOf("{") >= 0) {
            try {
                JSONObject json = new JSONObject(jsonStr);
                FileMd5ResponseMsg e = new FileMd5ResponseMsg();
                e.setCode(json.optString("code"));
                e.setFileId(json.optString("file_id"));
                e.setFileMd5(json.optString("md5"));
                return e;
            } catch (JSONException var3) {
                return new FileMd5ResponseMsg();
            }
        } else {
            return new FileMd5ResponseMsg();
        }
    }
}
