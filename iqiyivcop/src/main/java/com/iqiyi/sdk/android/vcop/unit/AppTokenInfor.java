//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.unit;

import com.iqiyi.sdk.android.vcop.authorize.Authorize2AccessToken;
import java.io.Serializable;

public class AppTokenInfor implements Serializable {
    private String appKey = "";
    private String ouid = "";
    private String nickName = "";
    private String appSecret = "";
    private Authorize2AccessToken authorize2AccessToken = null;

    public AppTokenInfor() {
        this.appKey = "";
        this.appSecret = "";
        this.ouid = "";
        this.nickName = "";
        this.authorize2AccessToken = null;
    }

    public AppTokenInfor(String appKey, String appSecret, Authorize2AccessToken authorize2AccessToken) {
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.authorize2AccessToken = authorize2AccessToken;
    }

    public AppTokenInfor(String appKey, String appSecret, String ouid, String nickName, Authorize2AccessToken authorize2AccessToken) {
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.ouid = ouid;
        this.nickName = nickName;
        this.authorize2AccessToken = authorize2AccessToken;
    }

    public String getOuid() {
        return this.ouid;
    }

    public void setOuid(String ouid) {
        this.ouid = ouid;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return this.appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public Authorize2AccessToken getAuthorize2AccessToken() {
        return this.authorize2AccessToken;
    }

    public void setAuthorize2AccessToken(Authorize2AccessToken authorize2AccessToken) {
        this.authorize2AccessToken = authorize2AccessToken;
    }
}
