//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.qichuan;

import android.os.Bundle;

import com.iqiyi.sdk.android.vcop.api.ReturnCode;
import com.iqiyi.sdk.android.vcop.api.UploadResultListener;
import com.iqiyi.sdk.android.vcop.api.VCOPClass;
import com.iqiyi.sdk.android.vcop.api.VCOPException;
import com.iqiyi.sdk.android.vcop.keeper.UploadInforKeeper;
import com.iqiyi.sdk.android.vcop.net.BasicNameValuePair;
import com.iqiyi.sdk.android.vcop.net.HttpUploadTools;
import com.iqiyi.sdk.android.vcop.net.HttpUrlTools;
import com.iqiyi.sdk.android.vcop.unit.CancelResponeMsg;
import com.iqiyi.sdk.android.vcop.unit.UploadResponseMsg;
import com.iqiyi.sdk.android.vcop.util.VCOPUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class UploadThread extends Thread {
    private UploadInfor uploadInfor = null;
    private UploadResultListener listener = null;
    private String accessToken = "";
    private long startPos = 0L;
    private long endPos = 0L;
    private long compeleteSize = 0L;
    private boolean isPause = false;
    private boolean isCancel = false;
    private boolean isFinish = false;
    private int readfileSize;

    public UploadThread(UploadInfor uploadInfor, String accessToken, UploadResultListener listener) {
        this.readfileSize = VCOPClass.READ_DEFAULT_SIZE;
        this.uploadInfor = uploadInfor;
        this.listener = listener;
        if(uploadInfor != null) {
            this.startPos = uploadInfor.getStartPos();
            this.endPos = uploadInfor.getEndPos();
            this.compeleteSize = uploadInfor.getComplete();
        }

        this.isPause = false;
        this.isCancel = false;
        this.isFinish = false;
        this.accessToken = accessToken;
    }

    public void run() {
        if(this.uploadInfor.getFilePath() == "") {
            if(this.listener != null) {
                this.listener.onError(new VCOPException("C00005"));
            }

        } else {
            String MIMEType = VCOPUtil.getMIMEType(this.uploadInfor.getFilePath());
            RandomAccessFile randomAccessFile = null;

            try {
                try {
                    randomAccessFile = new RandomAccessFile(this.uploadInfor.getFilePath(), "r");//rwd
                    randomAccessFile.seek(this.startPos);
                    HttpUploadTools e = new HttpUploadTools(this.uploadInfor.getUploadUrl());
                    Object buffer = null;

                    int finishPostResult;
                    do {
                        System.gc();
                        byte[] var32 = new byte[this.readfileSize];
                        if(this.isPause) {
                            return;
                        }

                        if(this.isCancel) {
                            return;
                        }

                        int postparams = randomAccessFile.read(var32);
                        if(postparams == -1) {
                            this.isFinish = true;
                            if(!this.isFinish) {
                                return;
                            }

                            ArrayList var33 = new ArrayList();
                            var33.add(new BasicNameValuePair("range_finished", "true"));
                            var33.add(new BasicNameValuePair("file_id", this.uploadInfor.getFileiId()));
                            var33.add(new BasicNameValuePair("access_token", this.accessToken));
                            String var34 = HttpUrlTools.getInstance().HttpToolsPostMethodWithHead(this.uploadInfor.getUploadUrl(), var33);
                            CancelResponeMsg baseResponeMsg = CancelResponeMsg.parseMsg(var34);
                            if(!ReturnCode.isSuccess(baseResponeMsg.getCode())) {
                                return;
                            }

                            Bundle value = new Bundle();
                            value.putString("code", "A00000");
                            value.putString("msg", "");
                            UploadInforKeeper.getInstance().deleteInforList(this.uploadInfor.getFileiId());
                            if(this.listener != null) {
                                this.listener.onFinish(this.uploadInfor.getFileiId(), value);
                            }

                            return;
                        }

                        this.endPos = this.startPos + (long)postparams - 1L;

                        for(finishPostResult = 0; !this.PostFile(e, var32, postparams, MIMEType) && finishPostResult < 3; ++finishPostResult) {
                            sleep(100L);
                        }

                        buffer = null;
                    } while(finishPostResult != 3 || this.listener == null);

                    this.listener.onError(new VCOPException("Q00001"));
                    return;
                } catch (FileNotFoundException var27) {
                    var27.printStackTrace();
                    if(this.listener != null) {
                        this.listener.onError(new VCOPException("C000010"));
                        return;
                    }
                } catch (IOException var28) {
                    var28.printStackTrace();
                    if(this.listener != null) {
                        this.listener.onError(new VCOPException("C00006"));
                        return;
                    }
                } catch (VCOPException var29) {
                    var29.printStackTrace();
                    if(this.listener != null) {
                        this.listener.onError(new VCOPException("C00001"));
                        return;
                    }
                } catch (InterruptedException var30) {
                    var30.printStackTrace();
                    if(this.listener != null) {
                        this.listener.onError(new VCOPException("Q00001"));
                        return;
                    }
                }

            } finally {
                if(randomAccessFile != null) {
                    try {
                        randomAccessFile.close();
                    } catch (IOException var26) {
                        var26.printStackTrace();
                        if(this.listener != null) {
                            this.listener.onError(new VCOPException("C00006"));
                        }
                    }
                }

            }
        }
    }

    private void judeNextSendWindowsSize(long begintimes, long endtimes, boolean isOpSuccess) {
        if(!isOpSuccess) {
            if(this.readfileSize / 2 < 10240) {
                this.readfileSize = 10240;
            } else {
                this.readfileSize /= 2;
            }
        } else {
            long total = (long)((double)(endtimes - begintimes) * 0.7D);
            if(total > 0L) {
                long speed = (long)this.readfileSize / total * 1024L;
                if(speed > 1024000L) {
                    this.readfileSize = 1024000;
                } else if(speed < 10240L) {
                    this.readfileSize = 10240;
                } else {
                    this.readfileSize = (int)speed;
                }
            }
        }

    }

    private boolean PostFile(HttpUploadTools httptools, byte[] buffer, int count, String MIMEType) throws VCOPException {
        long endTime = 0L;
        long beginTime = System.currentTimeMillis();
        ArrayList params = new ArrayList();
        params.add(new BasicNameValuePair("file_size", String.valueOf(this.uploadInfor.getFileSize())));
        params.add(new BasicNameValuePair("range", this.startPos + "-" + this.endPos));
        params.add(new BasicNameValuePair("file_id", this.uploadInfor.getFileiId()));
        String result = httptools.HttpToolsUploadPartFile(params, buffer, count, MIMEType);
        UploadResponseMsg msg = UploadResponseMsg.parseMsg(result);
        if(ReturnCode.isSuccess(msg.getCode())) {
            endTime = System.currentTimeMillis();
            if(Integer.parseInt(msg.getFileRangeAccepted()) != count) {
                this.judeNextSendWindowsSize(beginTime, endTime, false);
                return false;
            } else {
                this.judeNextSendWindowsSize(beginTime, endTime, true);
                if(VCOPUtil.isSameMd5(msg.getRangeMd5(), VCOPUtil.getMd5(buffer, count))) {
                    this.startPos = this.endPos + 1L;
                    this.compeleteSize = this.startPos;
                    int progress = 0;
                    if(this.uploadInfor.getFileSize() != 0L) {
                        double tmep = (double)this.startPos * 100.0D / (double)this.uploadInfor.getFileSize();
                        if(tmep > 99.0D) {
                            progress = 100;
                        } else {
                            progress = (int)tmep;
                        }
                    }

                    UploadInforKeeper.getInstance().SaveInfor(new UploadInfor(this.uploadInfor, this.compeleteSize, this.startPos, this.endPos, (double)progress));
                    if(this.listener != null) {
                        this.listener.onProgress(this.uploadInfor.getFileiId(), progress);
                    }

                    return true;
                } else {
                    this.judeNextSendWindowsSize(beginTime, endTime, false);
                    return false;
                }
            }
        } else {
            this.judeNextSendWindowsSize(beginTime, endTime, false);
            return false;
        }
    }

    public void Cancel() {
        this.isCancel = true;
    }

    public void pause() {
        this.isPause = true;
    }
}
