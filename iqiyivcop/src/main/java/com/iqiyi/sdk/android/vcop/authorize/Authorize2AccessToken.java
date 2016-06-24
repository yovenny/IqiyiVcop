//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.authorize;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Authorize2AccessToken implements Serializable {
    private String access_token = "";
    private String refresh_token = "";
    private long expiresTime = 0L;
    public static final String ACCESSTOKEN = "access_token";
    public static final String EXPIRESIN = "expires_in";
    public static final String REFRESHTOKEN = "refresh_token";

    public Authorize2AccessToken() {
        this.access_token = "";
        this.refresh_token = "";
        this.expiresTime = 0L;
    }

    public Authorize2AccessToken(String accessToken, String expiresIn) {
        this.access_token = accessToken;
        this.expiresTime = System.currentTimeMillis() + Long.parseLong(expiresIn) * 1000L;
    }

    public Authorize2AccessToken(String jsonStr) {
        if(jsonStr != null && jsonStr != "" && jsonStr.indexOf("{") >= 0) {
            try {
                JSONObject e = new JSONObject(jsonStr);
                this.setAccessToken(e.optString("access_token"));
                this.setExpiresIn(e.optString("expires_in"));
                this.setRefreshToken(e.optString("refresh_token"));
            } catch (JSONException var3) {
                Log.d("iqiyivcop", var3.toString());
            }
        }

    }

    public Authorize2AccessToken(JSONObject jsonToken) {
        if(jsonToken != null) {
            this.setAccessToken(jsonToken.optString("access_token"));
            this.setExpiresIn(jsonToken.optString("expires_in"));
            this.setRefreshToken(jsonToken.optString("refresh_token"));
        }

    }

    public void setAccessToken(String accessToken) {
        this.access_token = accessToken;
    }

    public String getAccessToken() {
        return this.access_token;
    }

    public void setRefreshToken(String refreshToken) {
        this.refresh_token = refreshToken;
    }

    public String getRefreshToken() {
        return this.refresh_token;
    }

    public void setExpiresIn(String expiresIn) {
        if(expiresIn != null && !expiresIn.equals("0")) {
            this.setExpiresTime(System.currentTimeMillis() + Long.parseLong(expiresIn) * 1000L);
        }

    }

    public void setExpiresTime(long expiresTime) {
        this.expiresTime = expiresTime;
    }

    public long getExpiresTime() {
        return this.expiresTime;
    }

    public boolean isTokenValid() {
        return this.access_token != null && this.access_token.trim() != ""?(this.getExpiresTime() == 0L?false:System.currentTimeMillis() <= this.getExpiresTime()):false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("token: ").append(this.access_token).append(" refreshToken: ").append(this.refresh_token).append(" expiresTime: ").append(this.expiresTime);
        return sb.toString();
    }
}
