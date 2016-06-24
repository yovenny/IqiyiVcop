//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.api;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.iqiyi.sdk.android.vcop.UI.VCOPAuthoDialog;
import com.iqiyi.sdk.android.vcop.authorize.Authorize2AccessToken;
import com.iqiyi.sdk.android.vcop.keeper.UploadInforKeeper;
import com.iqiyi.sdk.android.vcop.net.BasicNameValuePair;
import com.iqiyi.sdk.android.vcop.net.HttpUrlTools;
import com.iqiyi.sdk.android.vcop.qichuan.MulUploader;
import com.iqiyi.sdk.android.vcop.qichuan.UploadInfor;
import com.iqiyi.sdk.android.vcop.unit.BaiduAuthResponseMsg;
import com.iqiyi.sdk.android.vcop.unit.CancelResponeMsg;
import com.iqiyi.sdk.android.vcop.unit.DeleteResponseMsg;
import com.iqiyi.sdk.android.vcop.unit.FetchVideoResponseMsg;
import com.iqiyi.sdk.android.vcop.unit.FetchVideoStatusResponseMsg;
import com.iqiyi.sdk.android.vcop.unit.GetCountResponseMsg;
import com.iqiyi.sdk.android.vcop.unit.GetUrlListResponseMsg;
import com.iqiyi.sdk.android.vcop.unit.UrlResponseMsg;
import com.iqiyi.sdk.android.vcop.util.VCOPUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class VCOPClient {
    public static String app_key = "";
    private String app_secret;
    private String state;
    private static final String SDK_VERSION = "2.0.0";
    private Authorize2AccessToken author2AcccessToken;
    private Map<String, MulUploader> uploadersManger;

    public VCOPClient(String appKey, String appSecret, Authorize2AccessToken accessToken) {
        this((Context)null, appKey, appSecret, (String)null, accessToken);
    }

    public VCOPClient(String appKey, String appSecret, String state, Authorize2AccessToken accessToken) {
        this((Context)null, appKey, appSecret, state, accessToken);
    }

    public VCOPClient(Context context, String appKey, String appSecret, Authorize2AccessToken accessToken) {
        this(context, appKey, appSecret, (String)null, accessToken);
    }

    private VCOPClient(Context context, String appKey, String appSecret, String state, Authorize2AccessToken accessToken) {
        this.app_secret = "";
        this.state = "";
        this.author2AcccessToken = null;
        this.uploadersManger = new HashMap();
        this.initSDK(context);
        app_key = appKey;
        this.app_secret = appSecret;
        if(!TextUtils.isEmpty(state)) {
            this.state = state;
        }

        this.author2AcccessToken = accessToken;
    }

    private boolean initSDK(Context context) {
        boolean result = false;
        if(context == null) {
            return result;
        } else {
            try {
                ApplicationInfo e = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
                String managerUrl = e.metaData.getString("managerUrl");
                String uploadUrl = e.metaData.getString("uploadUrl");
                if(TextUtils.isEmpty(managerUrl) || TextUtils.isEmpty(uploadUrl)) {
                    return result;
                }

                VCOPClass.OPENAPI_URL = managerUrl;
                VCOPClass.QICHUAN_SERVER_2_URL = uploadUrl;
                VCOPClass.rebuild();
                result = true;
            } catch (NameNotFoundException var6) {
                var6.printStackTrace();
            }

            return result;
        }
    }

    public String getSDKVersion() {
        return "2.0.0";
    }

    public ReturnCode authorize(String uid) {
        if(TextUtils.isEmpty(uid)) {
            return ReturnCode.getInstance("C00005");
        } else {
            ReturnCode code = ReturnCode.getInstance();
            BaiduAuthResponseMsg msg;
            if(this.author2AcccessToken == null) {
                msg = this.authorizeUsers(uid);
                code = msg.getReturnCode();
                if(code.isSuccess()) {
                    this.author2AcccessToken = msg.getAuth2AccessToken();
                    if(!this.author2AcccessToken.isTokenValid()) {
                        code.setCode("C00003");
                        code.setCodeMsg("token expired!!");
                    }
                }
            } else if(this.author2AcccessToken.isTokenValid()) {
                code.setCode("A00000");
                code.setCodeMsg("success!!");
            } else {
                msg = this.authorizeUsers(uid);
                code = msg.getReturnCode();
                if(code.isSuccess()) {
                    this.author2AcccessToken = msg.getAuth2AccessToken();
                    if(!this.author2AcccessToken.isTokenValid()) {
                        code.setCode("C00003");
                        code.setCodeMsg("token expired!!");
                    }
                }
            }

            return code;
        }
    }

    public ReturnCode authorize(String ouid, String nickName) {
        if(!TextUtils.isEmpty(ouid) && !TextUtils.isEmpty(nickName)) {
            ReturnCode code = ReturnCode.getInstance();
            BaiduAuthResponseMsg msg;
            if(this.author2AcccessToken == null) {
                msg = this.authorizeCoop(ouid, nickName);
                code = msg.getReturnCode();
                if(code.isSuccess()) {
                    this.author2AcccessToken = msg.getAuth2AccessToken();
                    if(!this.author2AcccessToken.isTokenValid()) {
                        code.setCode("C00003");
                        code.setCodeMsg("token expired!!");
                    }
                }
            } else if(this.author2AcccessToken.isTokenValid()) {
                code.setCode("A00000");
                code.setCodeMsg("success!!");
            } else {
                msg = this.authorizeCoop(ouid, nickName);
                code = msg.getReturnCode();
                if(code.isSuccess()) {
                    this.author2AcccessToken = msg.getAuth2AccessToken();
                    if(!this.author2AcccessToken.isTokenValid()) {
                        code.setCode("C00003");
                        code.setCodeMsg("token expired!!");
                    }
                }
            }

            return code;
        } else {
            return ReturnCode.getInstance("C00005");
        }
    }

    public ReturnCode authorize() {
        ReturnCode code = ReturnCode.getInstance();
        BaiduAuthResponseMsg msg;
        if(this.author2AcccessToken == null) {
            msg = this.authorizeEnterprise();
            code = msg.getReturnCode();
            if(msg.getReturnCode().isSuccess()) {
                this.author2AcccessToken = msg.getAuth2AccessToken();
                if(!this.author2AcccessToken.isTokenValid()) {
                    code.setCode("C00003");
                    code.setCodeMsg("token expired!");
                }
            }
        } else if(this.author2AcccessToken.isTokenValid()) {
            code.setCode("A00000");
            code.setCodeMsg("success!!");
        } else {
            msg = this.authorizeEnterprise();
            code = msg.getReturnCode();
            if(code.isSuccess()) {
                this.author2AcccessToken = msg.getAuth2AccessToken();
                if(this.author2AcccessToken.isTokenValid()) {
                    code.setCode("A00000");
                    code.setCodeMsg("success!!");
                } else {
                    code.setCode("C00003");
                    code.setCodeMsg("token expired!!");
                }
            }
        }

        return code;
    }

    private BaiduAuthResponseMsg authorizeCoop(String ouid, String nickName) {
        ArrayList params = new ArrayList();
        params.add(new BasicNameValuePair("client_id", app_key));
        params.add(new BasicNameValuePair("ouid", ouid));
        params.add(new BasicNameValuePair("nick", nickName));
        String authoStr = VCOPClass.AUTHORIZE_BAIDU_SERVER_URL + "?" + VCOPUtil.ParamsConstructUrl(params);
        String result = "";

        try {
            result = HttpUrlTools.getInstance().HttpToolsGetMethod(authoStr);
            BaiduAuthResponseMsg e = BaiduAuthResponseMsg.parseMsg(result);
            return e;
        } catch (VCOPException var7) {
            return new BaiduAuthResponseMsg(var7.getStatusCode(), var7.getMessage());
        }
    }

    private BaiduAuthResponseMsg authorizeUsers(String uid) {
        ArrayList params = new ArrayList();
        params.add(new BasicNameValuePair("client_id", app_key));
        params.add(new BasicNameValuePair("client_secret", this.app_secret));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("response_type", "token"));
        if(this.state != "") {
            params.add(new BasicNameValuePair("state", this.state));
        }

        params.add(new BasicNameValuePair("display", "mobile"));
        String authoStr = VCOPClass.AUTHORIZE_PERSON_URL + "?" + VCOPUtil.ParamsConstructUrl(params);
        Log.i("VCOPClient", "authorize:" + authoStr);
        String result = "";

        try {
            result = HttpUrlTools.getInstance().HttpToolsGetMethod(authoStr);
            BaiduAuthResponseMsg e = BaiduAuthResponseMsg.parseMsg(result);
            return e;
        } catch (VCOPException var6) {
            return new BaiduAuthResponseMsg(var6.getStatusCode(), var6.getMessage());
        }
    }

    private BaiduAuthResponseMsg authorizeEnterprise() {
        ArrayList params = new ArrayList();
        params.add(new BasicNameValuePair("client_id", app_key));
        params.add(new BasicNameValuePair("client_secret", this.app_secret));
        String authoStr = VCOPClass.AUTHORIZE_ENTERPRISE + "?" + VCOPUtil.ParamsConstructUrl(params);
        Log.i("VCOPClient", "authorize:" + authoStr);
        String result = "";

        try {
            result = HttpUrlTools.getInstance().HttpToolsGetMethod(authoStr);
            BaiduAuthResponseMsg e = BaiduAuthResponseMsg.parseMsg(result);
            return e;
        } catch (VCOPException var5) {
            return new BaiduAuthResponseMsg(var5.getStatusCode(), var5.getMessage());
        }
    }

    public String virtualUrlToRealUrl(String virtualUrl) {
        if(TextUtils.isEmpty(virtualUrl)) {
            return "";
        } else {
            try {
                String e = HttpUrlTools.getInstance().HttpToolsGetMethod(virtualUrl);
                if(e.indexOf("{") >= 0) {
                    String constStr = "var videoUrl=";
                    String jsonStr = e.substring(constStr.length() + 1, e.length() - 1);
                    UrlResponseMsg responseMsg = UrlResponseMsg.parseMsg(jsonStr);
                    if(!ReturnCode.isSuccess(responseMsg.getCode())) {
                        return "";
                    }

                    if(responseMsg.getDataMap().containsKey("l")) {
                        return (String)responseMsg.getDataMap().get("l");
                    }
                }
            } catch (VCOPException var6) {
                var6.printStackTrace();
            }

            return "";
        }
    }

    public String upload(String filePath, Map<String, String> metaData, UploadResultListener listener) {
        if(filePath != null && filePath != "") {
            if(this.author2AcccessToken == null) {
                if(listener != null) {
                    listener.onError(new VCOPException("没有进行授权", "C00008"));
                }

                return "";
            } else if(metaData == null) {
                return "";
            } else {
                String fileType = filePath.substring(filePath.lastIndexOf(".") + 1);
                if(fileType == null || fileType == "") {
                    fileType = "mp4";
                }

                File file = new File(filePath);
                long fileSize = file.length();
                Bundle requestBundle = this.requestUpload(fileType, String.valueOf(fileSize));
                if(requestBundle.getString("upload_url") == null) {
                    if(listener != null) {
                        if(requestBundle.getString("code") != null) {
                            listener.onError(new VCOPException("获取上传地址失败  msg: " + requestBundle.getString("msg") + " code:" + requestBundle.getString("code")));
                        } else {
                            listener.onError(new VCOPException("获取上传地址失败", "Q00001"));
                        }
                    }
                    return "";
                } else {
                    String fileId = requestBundle.getString("file_id");


                    if(metaData != null) {
                        ReturnCode uploader = this.setMetadata(fileId, metaData);
                        Log.d("iqiyivcop", "setMetadata code: " + uploader.isSuccess());
                        if(!uploader.isSuccess()) {
                            if(listener != null) {
                                listener.onError(new VCOPException("setMetaDataError", "C00005"));
                            }

                            return "";
                        }
                    }

                    MulUploader uploader1 = (MulUploader)this.uploadersManger.get(requestBundle.getString("file_id"));
                    if(uploader1 == null) {
                        UploadInfor uploadInfor = new UploadInfor(fileSize, filePath, requestBundle.getString("upload_url"), requestBundle.getString("file_id"));
                        uploader1 = new MulUploader(uploadInfor, this.author2AcccessToken, listener);
                        this.uploadersManger.put(requestBundle.getString("file_id"), uploader1);
                    }

                    if(uploader1.isUploading()) {
                        return requestBundle.getString("file_id");
                    } else {
                        uploader1.startUpload();
                        return requestBundle.getString("file_id");
                    }
                }
            }
        } else {
            if(listener != null) {
                listener.onError(new VCOPException("没有设置上传文件的路径", "C000010"));
            }

            return "";
        }
    }

    public ReturnCode pauseUpload(String fileId) {
        if(TextUtils.isEmpty(fileId)) {
            return ReturnCode.getInstance("C00005");
        } else {
            List uploadInforList = UploadInforKeeper.getInstance().getInforByfileIdList(fileId);
            if(uploadInforList == null) {
                return ReturnCode.getInstance("Q00001");
            } else if(uploadInforList.size() == 0) {
                return ReturnCode.getInstance("Q00001");
            } else {
                MulUploader uploader = (MulUploader)this.uploadersManger.get(((UploadInfor)uploadInforList.get(0)).getFileiId());
                if(uploader == null) {
                    return ReturnCode.getInstance("Q00001");
                } else if(uploader.isUploading()) {
                    uploader.pause();
                    return ReturnCode.getInstance("A00000");
                } else {
                    return ReturnCode.getInstance("A00000");
                }
            }
        }
    }

    public void resumeUpload(String fileId, UploadResultListener listener) {
        if(fileId != null && fileId != "") {
            if(this.author2AcccessToken == null) {
                if(listener != null) {
                    listener.onError(new VCOPException(ReturnCode.getInstance("C00008")));
                }

            } else {
                List uploadInforList = UploadInforKeeper.getInstance().getInforByfileIdList(fileId);
                if(uploadInforList == null) {
                    if(listener != null) {
                        listener.onError(new VCOPException(ReturnCode.getInstance("Q00001")));
                    }

                } else if(uploadInforList.size() == 0) {
                    if(listener != null) {
                        listener.onError(new VCOPException(ReturnCode.getInstance("Q00001")));
                    }

                } else {
                    MulUploader uploader = (MulUploader)this.uploadersManger.get(((UploadInfor)uploadInforList.get(0)).getFileiId());
                    if(uploader == null) {
                        uploader = new MulUploader(uploadInforList, this.author2AcccessToken, listener);
                        this.uploadersManger.put(((UploadInfor)uploadInforList.get(0)).getFileiId(), uploader);
                    } else {
                        if(uploader.isUploading()) {
                            listener.onError(new VCOPException(ReturnCode.getInstance("C00004")));
                            return;
                        }

                        uploader.setUploadInforList(uploadInforList, listener);
                    }

                    uploader.startUpload();
                }
            }
        } else {
            Log.d("iqiyivcop", "resumeUpload 函数的fileId参数为空,fileId不允许为空");
            if(listener != null) {
                listener.onError(new VCOPException(ReturnCode.getInstance("C00005")));
            }

        }
    }

    public ReturnCode cancelUpload(String fileId) {
        if(fileId != null && fileId != "") {
            if(this.author2AcccessToken == null) {
                return ReturnCode.getInstance("C00008");
            } else {
                List uploadInforList = UploadInforKeeper.getInstance().getInforByfileIdList(fileId);
                if(uploadInforList == null) {
                    return ReturnCode.getInstance("C00002");
                } else if(uploadInforList.size() == 0) {
                    return ReturnCode.getInstance("C00002");
                } else {
                    ReturnCode code = ReturnCode.getInstance("Q00001");
                    MulUploader uploader = (MulUploader)this.uploadersManger.get(((UploadInfor)uploadInforList.get(0)).getFileiId());
                    if(uploader != null) {
                        uploader.cancel();
                    }

                    if(this.postCancelUpload(this.author2AcccessToken.getAccessToken(), ((UploadInfor)uploadInforList.get(0)).getFileiId())) {
                        UploadInforKeeper.getInstance().deleteInforList(((UploadInfor)uploadInforList.get(0)).getFileiId());
                        this.uploadersManger.remove(((UploadInfor)uploadInforList.get(0)).getFileiId());
                        code.setCode("A00000");
                    }

                    return code;
                }
            }
        } else {
            return ReturnCode.getInstance("C00005");
        }
    }

    public List<UploadInfor> getAllNoCompleteUpload() {
        return UploadInforKeeper.getInstance().getAllInfor();
    }

    public ReturnCode deleteVideoByIDs(String[] fileIds) {
        if(fileIds != null && fileIds.length >= 1) {
            if(this.author2AcccessToken == null) {
                return ReturnCode.getInstance("C00008");
            } else {
                ReturnCode code = ReturnCode.getInstance("Q00001");
                List uploadInforList = UploadInforKeeper.getInstance().getInforByfileIdList(fileIds);
                if(this.getDeleteVideo(this.author2AcccessToken.getAccessToken(), fileIds)) {
                    UploadInforKeeper.getInstance().deleteInforList(fileIds);
                    code.setCode("A00000");
                }

                return code;
            }
        } else {
            return ReturnCode.getInstance("C00005");
        }
    }

    public ReturnCode deleteVideoByID(String fileId) {
        if(fileId != null && fileId != "") {
            if(this.author2AcccessToken == null) {
                return ReturnCode.getInstance("C00008");
            } else {
                String[] fileIds = new String[]{fileId};
                ReturnCode code = ReturnCode.getInstance("Q00001");
                List uploadInforList = UploadInforKeeper.getInstance().getInforByfileIdList(fileId);
                if(uploadInforList != null && uploadInforList.size() != 0) {
                    MulUploader uploader = (MulUploader)this.uploadersManger.get(((UploadInfor)uploadInforList.get(0)).getFileiId());
                    if(uploader != null) {
                        uploader.cancel();
                        if(this.postCancelUpload(this.author2AcccessToken.getAccessToken(), ((UploadInfor)uploadInforList.get(0)).getFileiId())) {
                            UploadInforKeeper.getInstance().deleteInforList(((UploadInfor)uploadInforList.get(0)).getFilePath());
                            this.uploadersManger.remove(((UploadInfor)uploadInforList.get(0)).getFileiId());
                            code.setCode("A00000");
                        }
                    } else if(this.getDeleteVideo(this.author2AcccessToken.getAccessToken(), fileIds)) {
                        UploadInforKeeper.getInstance().deleteInforList(((UploadInfor)uploadInforList.get(0)).getFilePath());
                        code.setCode("A00000");
                    }
                } else if(this.getDeleteVideo(this.author2AcccessToken.getAccessToken(), fileIds)) {
                    code.setCode("A00000");
                }

                return code;
            }
        } else {
            return ReturnCode.getInstance("C00005");
        }
    }

    public List<Map<String, Object>> getVideoListPage(Integer pageSize, Integer page) {
        if(pageSize == null) {
            pageSize = Integer.valueOf(20);
        }

        if(page == null) {
            page = Integer.valueOf(1);
        }

        if(pageSize.intValue() <= 0) {
            pageSize = Integer.valueOf(20);
        }

        if(page.intValue() <= 0) {
            page = Integer.valueOf(1);
        }

        if(this.author2AcccessToken == null) {
            return null;
        } else {
            ArrayList getparams = new ArrayList();
            getparams.add(new BasicNameValuePair("access_token", this.author2AcccessToken.getAccessToken()));
            getparams.add(new BasicNameValuePair("page_size", pageSize.toString()));
            getparams.add(new BasicNameValuePair("page", page.toString()));
            String fetchServerStr = VCOPClass.GET_VIDEO_INFO_EXTERNAL + "?" + VCOPUtil.ParamsConstructUrl(getparams);
            return this.fetchVideo(fetchServerStr);
        }
    }

    public Map<String, Object> getVideoVirtualUrl(String fileId, DataRate dataRateType) {
        HashMap resmap = new HashMap();
        if(dataRateType == null) {
            dataRateType = DataRate.MOBILE_MP4_SMOOTH;
        }

        if(TextUtils.isEmpty(fileId)) {
            return resmap;
        } else {
            String Type = "mp4";
            byte vdType = 1;
            if(dataRateType != DataRate.MOBILE_MP4_SMOOTH && dataRateType != DataRate.MOBILE_MP4_HDV) {
                Type = "m3u8";
            } else {
                Type = "mp4";
            }

            if(dataRateType == DataRate.MOBILE_MP4_SMOOTH) {
                vdType = 1;
            } else if(dataRateType == DataRate.MOBILE_MP4_HDV) {
                vdType = 2;
            }

            ArrayList getparams = new ArrayList();
            getparams.add(new BasicNameValuePair("access_token", this.author2AcccessToken.getAccessToken()));
            getparams.add(new BasicNameValuePair("file_id", fileId));
            getparams.add(new BasicNameValuePair("vd", String.valueOf(vdType)));
            String getUrlServerStr = VCOPClass.URL_LIST_SERVER + "?" + VCOPUtil.ParamsConstructUrl(getparams);

            try {
                String e = HttpUrlTools.getInstance().HttpToolsGetMethod(getUrlServerStr);
                GetUrlListResponseMsg responseMsg = GetUrlListResponseMsg.parseMsg(e);
                ReturnCode code = responseMsg.getReturnCode();
                resmap.put("return_code", code);
                if(code.isSuccess()) {
                    Map map = responseMsg.getUrlMap();
                    resmap.put("url", map);
                }
            } catch (VCOPException var12) {
                var12.printStackTrace();
            }

            return resmap;
        }
    }

    public Map<String, Object> getVideoInfo(String fileId) {
        if(TextUtils.isEmpty(fileId)) {
            return new HashMap();
        } else if(this.author2AcccessToken == null) {
            return new HashMap();
        } else {
            ArrayList getparams = new ArrayList();
            getparams.add(new BasicNameValuePair("access_token", this.author2AcccessToken.getAccessToken()));
            getparams.add(new BasicNameValuePair("file_id", fileId));
            String fetchServerStr = VCOPClass.GET_SINGLE_VIDEO_INFO_EXTERNAL + "?" + VCOPUtil.ParamsConstructUrl(getparams);
            return this.fetchSingleVideo(fetchServerStr);
        }
    }

    public List<Map<String, Object>> getVideoInfo(String[] fileIds) {
        if(fileIds == null) {
            return new ArrayList();
        } else if(fileIds.length == 0) {
            return new ArrayList();
        } else if(this.author2AcccessToken == null) {
            return new ArrayList();
        } else {
            String fileidsStr = "";
            if(fileIds.length == 1) {
                fileidsStr = fileIds[0];
            } else {
                for(int getparams = 0; getparams < fileIds.length; ++getparams) {
                    if(fileIds[getparams] != null && fileIds[getparams] != "") {
                        fileidsStr = fileidsStr + fileIds[getparams];
                        if(getparams < fileIds.length - 1) {
                            fileidsStr = fileidsStr + ",";
                        }
                    }
                }
            }

            ArrayList var5 = new ArrayList();
            var5.add(new BasicNameValuePair("access_token", this.author2AcccessToken.getAccessToken()));
            var5.add(new BasicNameValuePair("file_ids", fileidsStr));
            String fetchServerStr = VCOPClass.GET_VIDEO_INFO_EXTERNAL + "?" + VCOPUtil.ParamsConstructUrl(var5);
            return this.fetchVideo(fetchServerStr);
        }
    }

    public FetchVideoStatusResponseMsg getVideoStatus(String fileId) {
        if(TextUtils.isEmpty(fileId)) {
            return null;
        } else {
            ArrayList getparams = new ArrayList();
            getparams.add(new BasicNameValuePair("access_token", this.author2AcccessToken.getAccessToken()));
            getparams.add(new BasicNameValuePair("file_id", fileId));
            FetchVideoStatusResponseMsg msg = null;
            String getVideoStatusStr = VCOPClass.GET_VIDEO_STATUS_SERVER + "?" + VCOPUtil.ParamsConstructUrl(getparams);

            try {
                String e = HttpUrlTools.getInstance().HttpToolsGetMethod(getVideoStatusStr);
                Log.d("iqiyivcop", "video state: " + e);
                msg = FetchVideoStatusResponseMsg.parseMsg(e);
            } catch (VCOPException var6) {
                var6.printStackTrace();
            }

            return msg;
        }
    }

    public int getVideoCount() {
        if(this.author2AcccessToken == null) {
            return -1;
        } else {
            ArrayList getparams = new ArrayList();
            getparams.add(new BasicNameValuePair("access_token", this.author2AcccessToken.getAccessToken()));
            String getCountServerStr = VCOPClass.GET_VIDEO_FILE_COUNT_SERVER + "?" + VCOPUtil.ParamsConstructUrl(getparams);

            try {
                String result = HttpUrlTools.getInstance().HttpToolsGetMethod(getCountServerStr);
                GetCountResponseMsg getCountResponseMsg = GetCountResponseMsg.parseMsg(result);
                if(ReturnCode.isSuccess(getCountResponseMsg.getCode())) {
                    return getCountResponseMsg.getCount();
                }
            } catch (VCOPException var5) {
                ;
            }

            return -1;
        }
    }

    public ReturnCode refreshToken() {
        if(this.author2AcccessToken != null && this.author2AcccessToken.getRefreshToken() != null) {
            String refreshToken = this.author2AcccessToken.getRefreshToken();
            return this.refreshToken(refreshToken, 0);
        } else {
            return ReturnCode.getInstance("C00008");
        }
    }

    public ReturnCode refreshToken(Authorize2AccessToken accessToken) {
        if(accessToken == null || accessToken.getRefreshToken() == null) {
            if(this.author2AcccessToken == null || this.author2AcccessToken.getRefreshToken() == null) {
                return ReturnCode.getInstance("C00008");
            }

            accessToken = this.author2AcccessToken;
        }

        String refreshToken = accessToken.getRefreshToken();
        return this.refreshToken(refreshToken, 0);
    }

    private ReturnCode refreshToken(String refreshToken, int noUse) {
        if(refreshToken != null && refreshToken != "") {
            ArrayList getparams = new ArrayList();
            getparams.add(new BasicNameValuePair("client_id", app_key));
            getparams.add(new BasicNameValuePair("client_secret", this.app_secret));
            getparams.add(new BasicNameValuePair("grant_type", "refresh_token"));
            getparams.add(new BasicNameValuePair("refresh_token", refreshToken));
            String getRefreshTokenStr = VCOPClass.AUTHORIZE_ENTERPRISE + "?" + VCOPUtil.ParamsConstructUrl(getparams);
            Log.d("iqiyivcop", "refreshToken url: " + getRefreshTokenStr);
            ReturnCode code = ReturnCode.getInstance();

            try {
                String e = HttpUrlTools.getInstance().HttpToolsGetMethod(getRefreshTokenStr);
                Log.d("iqiyivcop", "refreshToken: " + e);
                if(e.indexOf("{") >= 0) {
                    try {
                        JSONObject e1 = new JSONObject(e);
                        code.parserCode(e1);
                        if(code.isSuccess()) {
                            JSONObject data = e1.getJSONObject("data");
                            this.author2AcccessToken = new Authorize2AccessToken(data);
                        }
                    } catch (JSONException var9) {
                        var9.printStackTrace();
                        code.setCode("C00007");
                    }
                }
            } catch (VCOPException var10) {
                var10.printStackTrace();
                code.setCode(var10.getStatusCode());
            }

            return code;
        } else {
            return ReturnCode.getInstance("C00005");
        }
    }

    public Authorize2AccessToken getToken() {
        return this.author2AcccessToken;
    }

    public void setToken(Authorize2AccessToken accessToken) {
        this.author2AcccessToken = accessToken;
    }

    public ReturnCode setMetadata(String fileId, Map<String, String> inforMap) {
        if(fileId != null && fileId != "") {
            if(inforMap != null && inforMap.size() != 0) {
                if(this.author2AcccessToken == null) {
                    return ReturnCode.getInstance("C00008");
                } else {
                    ArrayList getparams = new ArrayList();
                    getparams.add(new BasicNameValuePair("access_token", this.author2AcccessToken.getAccessToken()));
                    getparams.add(new BasicNameValuePair("file_id", fileId));
                    Iterator iterator = inforMap.entrySet().iterator();

                    while(iterator.hasNext()) {
                        Entry metaServer = (Entry)iterator.next();
                        Object code = metaServer.getKey();
                        Object e = metaServer.getValue();
                        getparams.add(new BasicNameValuePair((String)code, (String)e));
                    }

                    String metaServer1 = VCOPClass.METE_UPLOAD_SERVER + "?" + VCOPUtil.ParamsConstructUrl(getparams);
                    ReturnCode code1 = ReturnCode.getInstance();
                    Log.e("iqiyivcop", "setMetadata  metaServer = " + metaServer1);

                    try {
                        String e2 = HttpUrlTools.getInstance().HttpToolsGetMethod(metaServer1);
                        Log.e("iqiyivcop", "setMetadata  result = " + e2.toString());
                        if(e2.indexOf("{") >= 0) {
                            try {
                                JSONObject e1 = new JSONObject(e2);
                                code1.parserCode(e1);
                            } catch (JSONException var9) {
                                var9.printStackTrace();
                                code1.setCode("C00007");
                            }
                        }
                    } catch (VCOPException var10) {
                        var10.printStackTrace();
                        code1.setCode(var10.getStatusCode());
                    }

                    Log.e("iqiyivcop", "setMetadata  code = " + code1.toString());
                    return code1;
                }
            } else {
                return ReturnCode.getInstance("C00005");
            }
        } else {
            return ReturnCode.getInstance("C00005");
        }
    }

    private ReturnCode setMetadata(String fileId, String name, String description) {
        if(fileId != null && fileId != "") {
            if(name != null && name != "") {
                if(description == null) {
                    new String();
                    description = "";
                }

                if(this.author2AcccessToken == null) {
                    return ReturnCode.getInstance("C00008");
                } else {
                    ArrayList getparams = new ArrayList();
                    getparams.add(new BasicNameValuePair("access_token", this.author2AcccessToken.getAccessToken()));
                    getparams.add(new BasicNameValuePair("file_id", fileId));
                    getparams.add(new BasicNameValuePair("file_name", name));
                    getparams.add(new BasicNameValuePair("description", description));
                    String metaServer = VCOPClass.METE_UPLOAD_SERVER + "?" + VCOPUtil.ParamsConstructUrl(getparams);
                    ReturnCode code = ReturnCode.getInstance();

                    try {
                        String e = HttpUrlTools.getInstance().HttpToolsGetMethod(metaServer);
                        if(e.indexOf("{") >= 0) {
                            try {
                                JSONObject e1 = new JSONObject(e);
                                code.parserCode(e1);
                            } catch (JSONException var9) {
                                var9.printStackTrace();
                                code.setCode("C00007");
                            }
                        }
                    } catch (VCOPException var10) {
                        code.setCode(var10.getStatusCode());
                    }

                    return code;
                }
            } else {
                return ReturnCode.getInstance("C00005");
            }
        } else {
            return ReturnCode.getInstance("C00005");
        }
    }

    private Map<String, Object> fetchSingleVideo(String fetchServerStr) {
        HashMap videoInfo = new HashMap();

        try {
            String e = HttpUrlTools.getInstance().HttpToolsGetMethod(fetchServerStr);
            Log.d("iqiyivcop", "fetchSingleVideo: " + e);
            FetchVideoResponseMsg fetchVideoResponseMsg = FetchVideoResponseMsg.parseMsg(e);
            if(ReturnCode.isSuccess(fetchVideoResponseMsg.getCode())) {
                Log.d("iqiyivcop", "fetchSingleVideo Success");
                List list = fetchVideoResponseMsg.getDataList();
                if(list != null && list.size() > 0) {
                    return (Map)list.get(0);
                }
            }
        } catch (VCOPException var6) {
            var6.printStackTrace();
        }

        return videoInfo;
    }

    private List<Map<String, Object>> fetchVideo(String fetchServerStr) {
        ArrayList videoList = new ArrayList();

        try {
            String e = HttpUrlTools.getInstance().HttpToolsGetMethod(fetchServerStr);
            FetchVideoResponseMsg fetchVideoResponseMsg = FetchVideoResponseMsg.parseMsg(e);
            if(ReturnCode.isSuccess(fetchVideoResponseMsg.getCode())) {
                return fetchVideoResponseMsg.getDataList();
            }
        } catch (VCOPException var5) {
            var5.printStackTrace();
        }

        return videoList;
    }

    private boolean postCancelUpload(String token, String fileid) {
        ArrayList postparams = new ArrayList();
        postparams.add(new BasicNameValuePair("status", "2"));
        postparams.add(new BasicNameValuePair("access_token", token));
        postparams.add(new BasicNameValuePair("file_id", fileid));

        try {
            String e = HttpUrlTools.getInstance().HttpToolsGetMethodWithoutHeader(VCOPClass.CANCEL_UPLOAD_SERVER, postparams);
            CancelResponeMsg baseResponeMsg = CancelResponeMsg.parseMsg(e);
            return ReturnCode.isSuccess(baseResponeMsg.getCode());
        } catch (VCOPException var6) {
            var6.printStackTrace();
            return false;
        }
    }

    private boolean getDeleteVideo(String token, String[] fileids) {
        if(fileids != null && fileids.length >= 1) {
            StringBuilder sb = new StringBuilder();

            for(int gettparams = 0; gettparams < fileids.length; ++gettparams) {
                if(gettparams < fileids.length - 1) {
                    sb.append(fileids[gettparams]).append(",");
                } else if(gettparams == fileids.length - 1) {
                    sb.append(fileids[gettparams]);
                }
            }

            ArrayList var9 = new ArrayList();
            var9.add(new BasicNameValuePair("access_token", token));
            var9.add(new BasicNameValuePair("delete_type", "1"));
            var9.add(new BasicNameValuePair("file_ids", sb.toString()));
            String deleteServer = VCOPClass.DELETE_VIDEO_SERVER + "?" + VCOPUtil.ParamsConstructUrl(var9);

            try {
                String e = HttpUrlTools.getInstance().HttpToolsGetMethod(deleteServer);
                DeleteResponseMsg deleteResponeMsg = DeleteResponseMsg.parseMsg(e);
                if(ReturnCode.isSuccess(deleteResponeMsg.getCode())) {
                    return true;
                } else {
                    return false;
                }
            } catch (VCOPException var8) {
                var8.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    private Bundle requestUpload(String fileType, String fileSize) {
        Bundle bundle = new Bundle();
        if(this.author2AcccessToken == null) {
            bundle.putString("code", "C00008");
            bundle.putString("msg", "你没有进行了授权");
            return bundle;
        } else if(this.author2AcccessToken.getAccessToken() == "") {
            bundle.putString("code", "C00008");
            bundle.putString("msg", "你没有进行了授权");
            return bundle;
        } else {
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("access_token", this.author2AcccessToken.getAccessToken()));
            params.add(new BasicNameValuePair("role", "openapi"));
            params.add(new BasicNameValuePair("filetype", fileType));
            params.add(new BasicNameValuePair("filesize", fileSize));
            String result = "";

            try {
                result = HttpUrlTools.getInstance().HttpToolsRequestUpload(params);
                Log.d("iqiyivcop", "requestUpload: " + result);
                if(result.indexOf("{") >= 0) {
                    try {
                        JSONObject e = new JSONObject(result);
                        String code = e.optString("code");
                        if(ReturnCode.isSuccess(code)) {
                            JSONObject datajson = e.getJSONObject("data");
                            if(datajson != null) {
                                String fileid = datajson.optString("file_id");
                                String uploadurl = datajson.optString("upload_url");
                                bundle.putString("code", "A00000");
                                bundle.putString("file_id", fileid);
                                bundle.putString("upload_url", uploadurl + "?type=body");
                                return bundle;
                            }

                            bundle.putString("code", "C00002");
                            bundle.putString("msg", "fileid获取失败");
                            return bundle;
                        }

                        bundle.putString("code", code);
                        bundle.putString("msg", e.optString("msg"));
                        return bundle;
                    } catch (JSONException var11) {
                        bundle.putString("code", "C00007");
                        bundle.putString("msg", "json error!");
                        return bundle;
                    }
                }
            } catch (VCOPException var12) {
                Log.d("iqiyivcop", var12.toString());
                if(var12.getCause().toString().contains("NetworkOnMainThreadException")) {
                    bundle.putString("code", "C00009");
                } else {
                    bundle.putString("code", var12.getStatusCode());
                }
            }

            return bundle;
        }
    }

    private void OpenAuthDialog(final Context context, final AuthResultListener listener, String uid) {
        ArrayList params = new ArrayList();
        params.add(new BasicNameValuePair("client_id", app_key));
        params.add(new BasicNameValuePair("client_secret", this.app_secret));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("response_type", "token"));
        if(this.state != "") {
            params.add(new BasicNameValuePair("state", this.state));
        }

        params.add(new BasicNameValuePair("display", "mobile"));
        final String authoStr = VCOPClass.AUTHORIZE_PERSON_URL + "?" + VCOPUtil.ParamsConstructUrl(params);
        if(context.checkCallingOrSelfPermission("android.permission.INTERNET") != 0) {
            VCOPUtil.showAlertDialog(context, "爱奇艺VCOP SDK错误提示", "没有设置INTERNET的权限！你如果是开发者，请在AndroidMainfest.xml进行设置");
        } else {
            (new Handler(context.getMainLooper())).post(new Runnable() {
                public void run() {
                    (new VCOPAuthoDialog(context, authoStr, listener)).show();
                }
            });
        }

    }
}
