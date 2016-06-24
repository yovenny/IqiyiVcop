//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.qichuan;

import com.iqiyi.sdk.android.vcop.api.UploadResultListener;
import com.iqiyi.sdk.android.vcop.api.VCOPException;
import com.iqiyi.sdk.android.vcop.authorize.Authorize2AccessToken;

public class Uploader {
    private UploadInfor uploadInfor = null;
    private UploadResultListener listener = null;
    private Authorize2AccessToken accessToken;
    private UploadThread uploadThread = null;
    private static final int INIT = 1;
    private static final int UPLOADING = 2;
    private static final int PAUSE = 3;
    private int uploadState = 1;

    public Uploader(UploadInfor uploadInfor, Authorize2AccessToken accessToken, UploadResultListener listener) {
        this.uploadInfor = uploadInfor;
        this.listener = listener;
        this.accessToken = accessToken;
        this.uploadThread = null;
    }

    public UploadInfor getUploadInfor() {
        return this.uploadInfor;
    }

    public void setUploadInfor(UploadInfor uploadInfor) {
        this.uploadInfor = uploadInfor;
    }

    public boolean isUploading() {
        return this.uploadState == 2;
    }

    public void startUpload() {
        if(this.uploadInfor == null) {
            this.listener.onError(new VCOPException("C000010"));
        } else if(!this.isUploading()) {
            this.uploadState = 2;
            this.uploadThread = new UploadThread(this.uploadInfor, this.accessToken.getAccessToken(), this.listener);
            this.uploadThread.start();
        }
    }

    public void pause() {
        if(this.uploadThread != null) {
            this.uploadState = 3;
            this.uploadThread.pause();
        }

    }

    public void cancel() {
        if(this.uploadThread != null) {
            this.uploadState = 1;
            this.uploadThread.Cancel();
        }

    }
}
