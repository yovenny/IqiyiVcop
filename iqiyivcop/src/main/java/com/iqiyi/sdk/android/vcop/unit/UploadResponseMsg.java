//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.unit;

import org.json.JSONException;
import org.json.JSONObject;

public class UploadResponseMsg extends CancelResponeMsg {
    private String rangeMd5 = "";
    private String fileRangeAccepted = "";

    public String getRangeMd5() {
        return this.rangeMd5;
    }

    public void setRangeMd5(String rangeMd5) {
        this.rangeMd5 = rangeMd5;
    }

    public String getFileRangeAccepted() {
        return this.fileRangeAccepted;
    }

    public void setFileRangeAccepted(String fileRangeAccepted) {
        this.fileRangeAccepted = fileRangeAccepted;
    }

    public UploadResponseMsg(String code, String rangeMd5, String fileRangeAccepted, String fileId) {
        super(code, fileId);
        this.rangeMd5 = rangeMd5;
        this.fileRangeAccepted = fileRangeAccepted;
    }

    public UploadResponseMsg() {
    }

    public static UploadResponseMsg parseMsg(String jsonStr) {
        if(jsonStr.indexOf("{") >= 0) {
            try {
                UploadResponseMsg e = new UploadResponseMsg();
                JSONObject json = new JSONObject(jsonStr);
                e.setCode(json.optString("code"));
                e.setMsg(json.optString("msg"));
                e.rangeMd5 = json.optString("range_md5");
                e.fileRangeAccepted = json.optString("file_range_accepted");
                e.fileId = json.optString("file_id");
                return e;
            } catch (JSONException var3) {
                var3.printStackTrace();
                return new UploadResponseMsg();
            }
        } else {
            return new UploadResponseMsg();
        }
    }
}
