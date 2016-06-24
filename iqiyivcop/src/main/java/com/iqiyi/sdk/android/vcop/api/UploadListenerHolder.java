//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.api;

import android.os.Bundle;
import android.util.Log;

public class UploadListenerHolder {
    private static final String TAG = "UploadListenerHolder";
    private UploadResultListener mListener;

    public static UploadListenerHolder getHolder() {
        return new UploadListenerHolder();
    }

    private UploadListenerHolder() {
    }

    public UploadResultListener getListener() {
        return this.mListener;
    }

    public void setListener(UploadResultListener mListener) {
        this.mListener = mListener;
    }

    public void onProgress(String fileId, int progress, double speed) {
        Log.i("UploadListenerHolder", "progress:" + progress + " speed:" + speed);
        if(this.mListener != null) {
            if(this.mListener instanceof OnUploadListener) {
                ((OnUploadListener)this.mListener).onProgress(fileId, progress, speed);
            } else if(this.mListener instanceof UploadResultListener) {
                this.mListener.onProgress(fileId, progress);
            }

        }
    }

    public void onFinish(String fileId, Bundle value) {
        if(this.mListener != null) {
            this.mListener.onFinish(fileId, value);
        }
    }

    public void onError(VCOPException e) {
        if(this.mListener != null) {
            this.mListener.onError(e);
        }
    }
}
