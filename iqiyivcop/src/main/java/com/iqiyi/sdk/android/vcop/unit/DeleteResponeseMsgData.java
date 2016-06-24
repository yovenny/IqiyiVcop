//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.unit;

public class DeleteResponeseMsgData extends BaseResponseMsg {
    private String statusCode = "";

    public DeleteResponeseMsgData() {
        this.fileId = "";
        this.statusCode = "";
    }

    public DeleteResponeseMsgData(String fileId, String statusCode) {
        this.fileId = fileId;
        this.statusCode = statusCode;
    }

    public String getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
