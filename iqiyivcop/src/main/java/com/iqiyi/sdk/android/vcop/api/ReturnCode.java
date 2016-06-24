//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.api;

import org.json.JSONObject;

public class ReturnCode {
    public static final String SUCCESS = "A00000";
    public static final String FAILURE = "Q00001";
    public static final String NETWORK_ERROR = "C00001";
    public static final String FILE_ID_NOT_FIND = "C00002";
    public static final String TOKEN_EXPIRE = "C00003";
    public static final String UPLOADING = "C00004";
    public static final String PARAMETER_ERROR = "C00005";
    public static final String IO_ERROR = "C00006";
    public static final String JSON_ERROR = "C00007";
    public static final String TOKEN_NOT_FIND = "C00008";
    public static final String NETWORK_ON_MAIN_THREAD_EXCEPTION = "C00009";
    public static final String FILE_NOT_FOUNDEXCEPTION = "C000010";
    private String statusCode = "";
    private String mCodeMsg = "";

    public static ReturnCode getInstance() {
        return new ReturnCode();
    }

    public static ReturnCode getInstance(String returnCode) {
        return new ReturnCode(returnCode);
    }

    private ReturnCode() {
    }

    private ReturnCode(String returnCode) {
        this.statusCode = returnCode;
    }

    public String getCode() {
        return this.statusCode;
    }

    public void setCode(String code) {
        this.statusCode = code;
    }

    public void parserCode(JSONObject jobj) {
        if(jobj == null) {
            this.setCode("C00007");
            this.setCodeMsg("parameter error");
        }

        this.setCode(jobj.optString("code"));
        this.setCodeMsg(jobj.optString("msg"));
    }

    public boolean isSuccess() {
        return "A00000".equals(this.statusCode);
    }

    public static boolean isSuccess(String code) {
        return code == null?false:(code == ""?false:code.compareTo("A00000") == 0);
    }

    public static boolean isSuccess(ReturnCode returnCode) {
        return returnCode == null?false:(returnCode.getCode() == ""?false:returnCode.getCode().compareTo("A00000") == 0);
    }

    public String getCodeMsg() {
        return this.mCodeMsg;
    }

    public void setCodeMsg(String mCodeMsg) {
        this.mCodeMsg = mCodeMsg;
    }

    public String toString() {
        String str = "code:" + this.statusCode + " msg:" + this.mCodeMsg;
        return str;
    }
}
