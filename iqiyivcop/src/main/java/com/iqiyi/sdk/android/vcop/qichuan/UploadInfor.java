//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.qichuan;

import java.io.Serializable;

public class UploadInfor implements Serializable {
    private long fileSize = 0L;
    private String uploadUrl;
    private long complete = 0L;
    private String fileiId;
    private String filePath;
    private int threadId = 0;
    private long startPos = 0L;
    private long endPos = 0L;
    private double progress = 0.0D;
    private double allProgress = 0.0D;
    public static final int UPLOAD_STATE_UNKNOWN = 1;
    public static final int UPLOAD_STATE_UPLOADING = 2;
    public static final int UPLOAD_STATE_PAUSE = 3;
    public static final int UPLOAD_STATE_FINISH = 4;
    private int mUploadState = 1;

    public UploadInfor() {
        this.fileSize = 0L;
        this.uploadUrl = "";
        this.complete = 0L;
        this.fileiId = "";
        this.filePath = "";
        this.threadId = 0;
        this.startPos = 0L;
        this.endPos = 0L;
    }

    public UploadInfor(UploadInfor infor, long complete, long startPos, long endPos, double progress) {
        this.fileSize = infor.getFileSize();
        this.uploadUrl = infor.getUploadUrl();
        if(complete < 0L) {
            this.complete = 0L;
        } else {
            this.complete = complete;
        }

        this.fileiId = infor.getFileiId();
        this.filePath = infor.getFilePath();
        this.threadId = infor.getThreadId();
        this.startPos = startPos;
        this.endPos = endPos;
        this.progress = progress;
        this.mUploadState = infor.getUploadState();
    }

    public UploadInfor clone() {
        UploadInfor infor = new UploadInfor();
        infor.setFileSize(this.fileSize);
        infor.setUploadUrl(this.uploadUrl);
        infor.setComplete(this.complete);
        infor.setFileiId(this.fileiId);
        infor.setFilePath(this.filePath);
        infor.setThreadId(this.threadId);
        infor.setStartPos(this.startPos);
        infor.setEndPos(this.endPos);
        infor.setProgress(this.progress);
        infor.setUploadState(this.mUploadState);
        return infor;
    }

    public double getProgress() {
        return this.progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public double getAllProgress() {
        return this.allProgress;
    }

    public void setAllProgress(double tmpProgress) {
        this.allProgress = tmpProgress;
    }

    public UploadInfor(UploadInfor infor, int threadId, int complete, long startPos, long endPos) {
        this.fileSize = infor.getFileSize();
        this.uploadUrl = infor.getUploadUrl();
        if(complete < 0) {
            this.complete = 0L;
        } else {
            this.complete = (long)complete;
        }

        this.fileiId = infor.getFileiId();
        this.filePath = infor.getFilePath();
        this.threadId = threadId;
        this.startPos = (long)((int)startPos);
        this.endPos = (long)((int)endPos);
        this.threadId = threadId;
    }

    public UploadInfor(long fileSize, String filePath, String uploadUrl, String fileiId) {
        if(fileSize < 0L) {
            this.fileSize = 0L;
        } else {
            this.fileSize = fileSize;
        }

        this.filePath = filePath;
        this.uploadUrl = uploadUrl;
        this.fileiId = fileiId;
    }

    public UploadInfor(int fileSize, String uploadUrl, long complete, String fileiId, String filePath, int threadId, long startPos, long endPos) {
        this.fileSize = (long)fileSize;
        this.uploadUrl = uploadUrl;
        if(complete < 0L) {
            this.complete = 0L;
        } else {
            this.complete = complete;
        }

        this.fileiId = fileiId;
        this.filePath = filePath;
        this.threadId = threadId;
        if(startPos < 0L) {
            this.startPos = 0L;
        } else {
            this.startPos = startPos;
        }

        if(endPos < 0L) {
            this.endPos = 0L;
        } else {
            this.endPos = endPos;
        }

    }

    public int getThreadId() {
        return this.threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public long getStartPos() {
        return this.startPos;
    }

    public void setStartPos(long startPos) {
        if(startPos < 0L) {
            this.startPos = 0L;
        } else {
            this.startPos = startPos;
        }

    }

    public long getEndPos() {
        return this.endPos;
    }

    public void setEndPos(long endPos) {
        if(endPos < 0L) {
            this.endPos = 0L;
        } else {
            this.endPos = endPos;
        }

    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(long fileSize) {
        if(fileSize < 0L) {
            this.fileSize = 0L;
        } else {
            this.fileSize = fileSize;
        }

    }

    public String getUploadUrl() {
        return this.uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public long getComplete() {
        return this.complete < 0L?0L:this.complete;
    }

    public void setComplete(long complete) {
        if(complete < 0L) {
            this.complete = 0L;
        } else {
            this.complete = complete;
        }

    }

    public String getFileiId() {
        return this.fileiId;
    }

    public void setFileiId(String fileiId) {
        this.fileiId = fileiId;
    }

    public String toString() {
        return "UploadInfor[fileSize:" + this.fileSize + " uploadUrl:" + this.uploadUrl + " complete:" + this.complete + " fileiId:" + this.fileiId + " threadId:" + this.threadId + " startPos:" + this.startPos + " endPos:" + this.endPos + " appProgress:" + this.allProgress + " progress:" + this.progress + " state:" + this.mUploadState + "]";
    }

    public int getUploadState() {
        return this.mUploadState;
    }

    public void setUploadState(int mUploadState) {
        this.mUploadState = mUploadState;
    }
}
