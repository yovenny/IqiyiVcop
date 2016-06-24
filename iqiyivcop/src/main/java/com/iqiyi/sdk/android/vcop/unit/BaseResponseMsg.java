//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.unit;

import com.iqiyi.sdk.android.vcop.api.ReturnCode;

public class BaseResponseMsg {
    protected String fileId = "";
    protected ReturnCode mReturnCode = ReturnCode.getInstance();

    public String getCode() {
        return this.mReturnCode.getCode();
    }

    public void setCode(String code) {
        this.mReturnCode.setCode(code);
    }

    public String getMsg() {
        return this.mReturnCode.getCodeMsg();
    }

    public void setMsg(String msg) {
        this.mReturnCode.setCodeMsg(msg);
    }

    public String getFileId() {
        return this.fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public BaseResponseMsg() {
        this.fileId = "";
        this.mReturnCode = ReturnCode.getInstance();
    }

    public BaseResponseMsg(String code) {
        this.mReturnCode.setCode(code);
    }

    public BaseResponseMsg(String code, String msg) {
        this.mReturnCode.setCode(code);
        this.mReturnCode.setCodeMsg(msg);
    }

    public BaseResponseMsg(String code, String msg, String fileId) {
        this.mReturnCode.setCode(code);
        this.mReturnCode.setCodeMsg(msg);
        this.fileId = fileId;
    }

    public ReturnCode getReturnCode() {
        return this.mReturnCode;
    }
}
