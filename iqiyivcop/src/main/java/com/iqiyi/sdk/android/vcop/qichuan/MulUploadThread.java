//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.qichuan;

import android.os.Bundle;
import android.util.Log;

import com.iqiyi.sdk.android.vcop.api.ReturnCode;
import com.iqiyi.sdk.android.vcop.api.VCOPClass;
import com.iqiyi.sdk.android.vcop.api.VCOPException;
import com.iqiyi.sdk.android.vcop.keeper.UploadInforKeeper;
import com.iqiyi.sdk.android.vcop.net.BasicNameValuePair;
import com.iqiyi.sdk.android.vcop.net.HttpUploadTools;
import com.iqiyi.sdk.android.vcop.unit.InteriorUploadResultListener;
import com.iqiyi.sdk.android.vcop.unit.UploadResponseMsg;
import com.iqiyi.sdk.android.vcop.util.VCOPUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class MulUploadThread extends Thread {
    private static final String TAG = "MulUploadThread";
    private UploadInfor uploadInfor = null;
    private InteriorUploadResultListener listener = null;
    private String accessToken = "";
    private long startPos = 0L;
    private long endPos = 0L;
    private long everyTimeEndPos = 0L;
    private long compeleteSize = 0L;
    private boolean isPause = false;
    private boolean isCancel = false;
    private boolean isFinish = false;
    private int readfileSize;
    private long mPostTime;

    public MulUploadThread(UploadInfor uploadInfor, String accessToken, InteriorUploadResultListener listener) {
        this.readfileSize = VCOPClass.READ_DEFAULT_SIZE;
        this.mPostTime = 0L;
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

    public boolean isFinish() {
        return this.isFinish;
    }

    public void setFinish(boolean isFinish) {
        this.isFinish = isFinish;
    }

    public void run() {
        if(this.uploadInfor.getFilePath() == "") {
            if(this.listener != null) {
                this.listener.onError(new VCOPException("C00005"));
            }

        } else if(this.uploadInfor.getStartPos() <= this.uploadInfor.getEndPos() && this.uploadInfor.getComplete() != this.uploadInfor.getEndPos() - this.uploadInfor.getStartPos()) {
            this.updateUploadState(2);
            String var33 = VCOPUtil.getMIMEType(this.uploadInfor.getFilePath());
            RandomAccessFile randomAccessFile = null;

            try {
                randomAccessFile = new RandomAccessFile(this.uploadInfor.getFilePath(), "r");//wd
                randomAccessFile.seek(this.startPos);
                HttpUploadTools e = new HttpUploadTools(this.uploadInfor.getUploadUrl());
                Object buffer = null;

                while(true) {
                    this.mPostTime = System.currentTimeMillis();
                    if(this.endPos > 0L && this.endPos - this.startPos >= 0L && this.endPos - this.startPos < (long)this.readfileSize) {
                        this.readfileSize = (int)(this.endPos - this.startPos + 1L);
                    } else if(this.endPos + 1L == this.startPos) {
                        this.isFinish = true;
                        break;
                    }

                    System.gc();
                    byte[] var34 = new byte[this.readfileSize];
                    if(this.isPause) {
                        return;
                    }

                    if(this.isCancel) {
                        return;
                    }

                    int value = randomAccessFile.read(var34);
                    Log.i("MulUploadThread", "upload read:" + value);
                    if(value == -1) {
                        this.isFinish = true;
                        break;
                    }

                    this.everyTimeEndPos = this.startPos + (long)value - 1L;
                    int tycount = 0;
                    if(this.isPause) {
                        return;
                    }

                    if(this.isCancel) {
                        return;
                    }

                    while(!this.PostFile(e, var34, value, var33) && tycount < 3) {
                        sleep(100L);
                        ++tycount;
                    }

                    buffer = null;
                    System.gc();
                    if(this.isPause) {
                        return;
                    }

                    if(this.isCancel) {
                        return;
                    }

                    if(tycount == 3 && this.listener != null) {
                        this.updateUploadState(1);
                        this.listener.onError(new VCOPException("上传失败", "C00001"));
                        return;
                    }
                }

                if(this.isFinish) {
                    this.updateUploadState(4);
                    if(this.listener != null) {
                        Bundle var35 = new Bundle();
                        var35.putString("code", "A00000");
                        var35.putString("msg", "");
                        this.listener.onFinish(this.uploadInfor.getFileiId(), var35);
                    }
                }
            } catch (FileNotFoundException var28) {
                var28.printStackTrace();
                this.updateUploadState(1);
                if(this.listener != null) {
                    this.listener.onError(new VCOPException("找不到文件", "C000010"));
                }
            } catch (IOException var29) {
                var29.printStackTrace();
                this.updateUploadState(1);
                if(this.listener != null) {
                    this.listener.onError(new VCOPException("IO错误", "C00006"));
                }
            } catch (VCOPException var30) {
                var30.printStackTrace();
                this.updateUploadState(1);
                if(this.listener != null) {
                    this.listener.onError(new VCOPException("网络错误", "C00001"));
                }
            } catch (InterruptedException var31) {
                var31.printStackTrace();
                this.updateUploadState(1);
                if(this.listener != null) {
                    this.listener.onError(new VCOPException("上传出现异常，上传失败", "Q00001"));
                }
            } finally {
                if(randomAccessFile != null) {
                    try {
                        randomAccessFile.close();
                    } catch (IOException var27) {
                        var27.printStackTrace();
                        if(this.listener != null) {
                            this.listener.onError(new VCOPException("IO错误", "C00006"));
                        }
                    }
                }

            }

        } else {
            this.updateUploadState(4);
            this.isFinish = true;
            if(this.listener != null) {
                Bundle MIMEType = new Bundle();
                MIMEType.putString("code", "A00000");
                MIMEType.putString("msg", "");
                this.listener.onFinish(this.uploadInfor.getFileiId(), MIMEType);
            }

        }
    }

    private void updateUploadState(int state) {
        if(this.uploadInfor != null) {
            this.uploadInfor.setUploadState(state);
            UploadInforKeeper.getInstance().SaveInforByThreadId(this.uploadInfor.clone());
        }

    }

    private double getUploadSpeed(long begintimes, long endtimes) {
        double speed = 0.0D;
        long total = (long)((double)(endtimes - begintimes) * 0.7D);
        if(total > 0L) {
            speed = (double)((long)this.readfileSize / total * 1000L);
        }

        Log.i("MulUploadThread", "start time:" + begintimes + " endtimes:" + endtimes + " total:" + total + " speed:" + speed + " size:" + this.readfileSize);
        return speed;
    }

    private void decisionNextSendWindowsSize(long begintimes, long endtimes, boolean isOpSuccess) {
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
        params.add(new BasicNameValuePair("range", this.startPos + "-" + this.everyTimeEndPos));
        params.add(new BasicNameValuePair("file_id", this.uploadInfor.getFileiId()));
        String result = httptools.HttpToolsUploadPartFile(params, buffer, count, MIMEType);
        UploadResponseMsg msg = UploadResponseMsg.parseMsg(result);
        if(this.isCancel) {
            return true;
        } else if(this.isPause) {
            return true;
        } else if(ReturnCode.isSuccess(msg.getCode())) {
            endTime = System.currentTimeMillis();
            if(Integer.parseInt(msg.getFileRangeAccepted()) != count) {
                this.decisionNextSendWindowsSize(beginTime, endTime, false);
                return false;
            } else {
                this.decisionNextSendWindowsSize(beginTime, endTime, true);
                if(VCOPUtil.isSameMd5(msg.getRangeMd5(), VCOPUtil.getMd5(buffer, count))) {
                    this.startPos = this.everyTimeEndPos + 1L;
                    this.compeleteSize += (long)count;
                    double doubleProgress = 0.0D;
                    if(this.uploadInfor.getFileSize() != 0L) {
                        doubleProgress = (double)this.compeleteSize * 100.0D / (double)this.uploadInfor.getFileSize();
                    }

                    if(this.isCancel) {
                        return true;
                    } else if(this.isPause) {
                        return true;
                    } else {
                        this.uploadInfor.setComplete(this.compeleteSize);
                        this.uploadInfor.setStartPos(this.startPos);
                        this.uploadInfor.setEndPos(this.endPos);
                        this.uploadInfor.setProgress(doubleProgress);
                        UploadInforKeeper.getInstance().SaveInforByThreadId(new UploadInfor(this.uploadInfor, this.compeleteSize, this.startPos, this.endPos, doubleProgress));
                        if(this.listener != null && this.uploadInfor != null && !this.isPause && !this.isCancel) {
                            long now = System.currentTimeMillis();
                            double speed = this.getUploadSpeed(this.mPostTime, now);
                            this.listener.onProgress(this.uploadInfor.getThreadId(), this.uploadInfor.getFileiId(), doubleProgress, speed);
                        }

                        return true;
                    }
                } else {
                    this.decisionNextSendWindowsSize(beginTime, endTime, false);
                    return false;
                }
            }
        } else {
            this.decisionNextSendWindowsSize(beginTime, endTime, false);
            return false;
        }
    }

    public synchronized void Cancel() {
        this.isCancel = true;
        this.updateUploadState(3);
    }

    public void pause() {
        this.isPause = true;
        this.updateUploadState(3);
    }
}
