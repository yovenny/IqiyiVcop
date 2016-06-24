//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.qichuan;

import android.os.Bundle;
import android.util.Log;

import com.iqiyi.sdk.android.vcop.api.ReturnCode;
import com.iqiyi.sdk.android.vcop.api.UploadListenerHolder;
import com.iqiyi.sdk.android.vcop.api.UploadResultListener;
import com.iqiyi.sdk.android.vcop.api.VCOPClass;
import com.iqiyi.sdk.android.vcop.api.VCOPException;
import com.iqiyi.sdk.android.vcop.authorize.Authorize2AccessToken;
import com.iqiyi.sdk.android.vcop.keeper.UploadInforKeeper;
import com.iqiyi.sdk.android.vcop.net.BasicNameValuePair;
import com.iqiyi.sdk.android.vcop.net.HttpUrlTools;
import com.iqiyi.sdk.android.vcop.unit.CancelResponeMsg;
import com.iqiyi.sdk.android.vcop.unit.FileMd5ResponseMsg;
import com.iqiyi.sdk.android.vcop.unit.InteriorUploadResultListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MulUploader {
    private List<MulUploadThread> uploadThreadList = new ArrayList();
    private List<UploadInfor> uploadInforList = null;
    private Authorize2AccessToken accessToken;
    private UploadListenerHolder mListenerHolder = UploadListenerHolder.getHolder();
    private static final int INIT = 1;
    private static final int UPLOADING = 2;
    private static final int PAUSE = 3;
    private static final int OVER = 4;
    private int uploadState = 1;
    private UploadInfor firstUploadInfor = null;
    public int threadCount;
    private int mUploadErrorCount;

    public MulUploader(List<UploadInfor> uploadInforList, Authorize2AccessToken accessToken, UploadResultListener listener) {
        this.threadCount = VCOPClass.DEFAULT_THREAD_COUNT;
        this.mUploadErrorCount = 0;
        this.uploadInforList = uploadInforList;
        this.accessToken = accessToken;
        this.mListenerHolder.setListener(listener);
    }

    public MulUploader(UploadInfor firstUploadInfor, Authorize2AccessToken accessToken, UploadResultListener listener) {
        this.threadCount = VCOPClass.DEFAULT_THREAD_COUNT;
        this.mUploadErrorCount = 0;
        this.firstUploadInfor = firstUploadInfor;
        this.accessToken = accessToken;
        this.mListenerHolder.setListener(listener);
        this.init();
    }

    public void setUploadInforList(List<UploadInfor> uploadInforList, UploadResultListener listener) {
        this.uploadInforList = uploadInforList;
        this.mListenerHolder.setListener(listener);
    }

    private void init() {
        if(this.firstUploadInfor != null) {
            this.uploadInforList = new ArrayList();
            long fileSize = this.firstUploadInfor.getFileSize();
            this.decisionThreadCount(fileSize);
            long range = fileSize / (long)this.threadCount;

            for(int infor = 0; infor < this.threadCount - 1; ++infor) {
                UploadInfor infor1 = new UploadInfor(this.firstUploadInfor, infor, 0, (long)infor * range, (long)(infor + 1) * range - 1L);
                this.uploadInforList.add(infor1);
                infor1 = null;
            }

            UploadInfor var7 = new UploadInfor(this.firstUploadInfor, this.threadCount - 1, 0, (long)(this.threadCount - 1) * range, fileSize - 1L);
            this.uploadInforList.add(var7);
            var7 = null;
        }

    }

    private void decisionThreadCount(long filesize) {
        if(filesize < (long)VCOPClass.READ_DEFAULT_SIZE) {
            this.threadCount = 1;
        } else if(filesize < 1024000L && filesize > (long)VCOPClass.READ_DEFAULT_SIZE) {
            this.threadCount = 2;
        } else {
            this.threadCount = VCOPClass.DEFAULT_THREAD_COUNT;
        }

    }

    private synchronized boolean trackError() {
        boolean error = false;
        ++this.mUploadErrorCount;
        Log.e("FTD","mUploadErrorCount:"+mUploadErrorCount+"VCOPClass.DEFAULT_THREAD_COUNT:"+VCOPClass.DEFAULT_THREAD_COUNT);
        if(this.mUploadErrorCount == threadCount){//chenbc VCOPClass.DEFAULT_THREAD_COUNT) {
            error = true;
        }

        return error;
    }

    public void startUpload() {
        if(this.uploadInforList != null) {
            if(this.isUploading()) {
                this.mListenerHolder.onError(new VCOPException("已经正在上传", "C00004"));
                return;
            }

            this.uploadState = 2;
            Iterator var2;
            if(this.uploadThreadList != null) {
                var2 = this.uploadThreadList.iterator();

                while(var2.hasNext()) {
                    MulUploadThread uploadInfor = (MulUploadThread)var2.next();
                    if(uploadInfor != null) {
                        uploadInfor = null;
                    }
                }

                this.uploadThreadList.clear();
            }

            this.mUploadErrorCount = 0;
            var2 = this.uploadInforList.iterator();

            while(var2.hasNext()) {
                final UploadInfor uploadInfor1 = (UploadInfor)var2.next();
                MulUploadThread thread = new MulUploadThread(uploadInfor1, this.accessToken.getAccessToken(), new InteriorUploadResultListener() {
                    public void onProgress(int threadId, String fileId, double progress, double speed) {
                        if(uploadInfor1.getFileiId().compareTo(fileId) == 0 && uploadInfor1.getThreadId() == threadId) {
                            uploadInfor1.setProgress(progress);
                        }

                        double doubleprogress = 0.0D;

                        UploadInfor myprogeress;
                        for(Iterator var10 = MulUploader.this.uploadInforList.iterator(); var10.hasNext(); doubleprogress += myprogeress.getProgress()) {
                            myprogeress = (UploadInfor)var10.next();
                        }

                        boolean myprogeress1 = false;
                        int myprogeress2;
                        if(doubleprogress - (double)((int)doubleprogress) > 0.5D) {
                            myprogeress2 = (int)doubleprogress + 1;
                        } else {
                            myprogeress2 = (int)doubleprogress;
                        }

                        MulUploader.this.mListenerHolder.onProgress(fileId, myprogeress2, speed);
                    }

                    public void onFinish(String fileId, Bundle value) {
                        boolean isAllfinish = true;
                        Iterator postparams = MulUploader.this.uploadThreadList.iterator();

                        while(postparams.hasNext()) {
                            MulUploadThread uploadInfor = (MulUploadThread)postparams.next();
                            if(!uploadInfor.isFinish()) {
                                isAllfinish = false;
                                break;
                            }
                        }

                        if(isAllfinish) {
                            UploadInfor uploadInfor1x = (UploadInfor)MulUploader.this.uploadInforList.get(0);
                            if(uploadInfor1x != null) {
                                ArrayList postparams1 = new ArrayList();
                                postparams1.add(new BasicNameValuePair("range_finished", "true"));
                                postparams1.add(new BasicNameValuePair("file_id", uploadInfor1x.getFileiId()));
                                postparams1.add(new BasicNameValuePair("access_token", MulUploader.this.accessToken.getAccessToken()));

                                try {
                                    String finishPostResult = HttpUrlTools.getInstance().HttpToolsGetMethodWithoutHeader(VCOPClass.FINISH_UPLOAD_SERVER, postparams1);
                                    CancelResponeMsg e = CancelResponeMsg.parseMsg(finishPostResult);
                                    if(ReturnCode.isSuccess(e.getCode())) {
                                        MulUploader.this.uploadState = 4;
                                        Bundle bundle = new Bundle();
                                        bundle.putString("code", "A00000");
                                        bundle.putString("msg", "");
                                        UploadInforKeeper.getInstance().deleteInforList(uploadInfor1x.getFileiId());
                                        MulUploader.this.mListenerHolder.onFinish(fileId, value);
                                    }
                                } catch (VCOPException var9) {
                                    var9.printStackTrace();
                                    if(MulUploader.this.trackError()) {
                                        MulUploader.this.mListenerHolder.onError(var9);
                                    }
                                }
                            }
                        }

                    }

                    public void onError(VCOPException e) {
                        Log.e("FTD","------trackError");
                        if(MulUploader.this.trackError()) {//只要有一个片段上传失败,则认为整个上传失败,(避免失败没有回调,使整个帖子的发布状态一直处在发布中.)
                            MulUploader.this.mListenerHolder.onError(e);
                        }

                    }
                });
                thread.start();
                this.uploadThreadList.add(thread);
            }
        }

    }

    public boolean isUploading() {
        return this.uploadState == 2;
    }

    public void pause() {
        this.uploadState = 3;
        Iterator var2 = this.uploadThreadList.iterator();

        while(var2.hasNext()) {
            MulUploadThread thread = (MulUploadThread)var2.next();
            thread.pause();
        }

    }

    public synchronized void cancel() {
        this.uploadState = 1;
        Iterator var2 = this.uploadThreadList.iterator();

        while(var2.hasNext()) {
            MulUploadThread thread = (MulUploadThread)var2.next();
            thread.Cancel();
        }

    }

    private boolean isFileMd5CheckOk(String fileId, String md5) {
        UploadInfor uploadInfor = (UploadInfor)this.uploadInforList.get(0);
        ArrayList postparams = new ArrayList();
        postparams.add(new BasicNameValuePair("file_md5", md5));
        postparams.add(new BasicNameValuePair("file_id", fileId));

        try {
            String finishPostResult = HttpUrlTools.getInstance().HttpToolsPostMethodWithHead(uploadInfor.getUploadUrl(), postparams);
            FileMd5ResponseMsg e = FileMd5ResponseMsg.parseMsg(finishPostResult);
            if(ReturnCode.isSuccess(e.getCode()) && md5.compareTo(e.getFileMd5()) == 0) {
                return true;
            }
        } catch (VCOPException var7) {
            var7.printStackTrace();
        }

        return false;
    }
}
