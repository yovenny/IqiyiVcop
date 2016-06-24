//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.api;

public class VCOPException extends Exception {
    private static final long serialVersionUID = 1L;
    private String exceptionCode = "";

    public VCOPException() {
    }

    public VCOPException(String msg) {
        super(msg);
    }

    public VCOPException(Exception cause) {
        super(cause);
    }

    public VCOPException(String msg, String exceptionCode) {
        super(msg);
        this.exceptionCode = exceptionCode;
    }

    public VCOPException(String msg, ReturnCode exceptionCode) {
        super(msg);
        this.exceptionCode = exceptionCode.getCode();
    }

    public VCOPException(ReturnCode exceptionCode) {
        this.exceptionCode = exceptionCode.getCode();
    }

    public VCOPException(String msg, Exception cause) {
        super(msg, cause);
    }

    public VCOPException(String msg, Exception cause, String exceptionCode) {
        super(msg, cause);
        this.exceptionCode = exceptionCode;
    }

    public String getStatusCode() {
        return this.exceptionCode;
    }

    public void setStatusCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public VCOPException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public VCOPException(Throwable throwable) {
        super(throwable);
    }
}
