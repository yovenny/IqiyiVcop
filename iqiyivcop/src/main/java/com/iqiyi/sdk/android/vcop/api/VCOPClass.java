//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.api;

public abstract class VCOPClass {
    public static String OPENAPI_URL = "http://openapi.iqiyi.com";
    public static String HTTPS_OPENAPI_URL = "https://openapi.iqiyi.com";
    public static String AUTHORIZE_PERSON_URL;
    public static String AUTHORIZE_BAIDU_SERVER_URL;
    public static String AUTHORIZE_ENTERPRISE;
    /** @deprecated */
    public static String QICHUAN_SERVER_URL;
    public static String QICHUAN_SERVER_2_URL;
    public static String QICHUAN_SERVER_BACKUP_URL;
    public static String REQUEST_UPLOAD_SERVER;
    public static String CANCEL_UPLOAD_SERVER;
    public static String FINISH_UPLOAD_SERVER;
    public static String GET_VIDEO_INFO_INTERNAL;
    public static String REQUEST_UPLOAD_SERVER_BACKUP;
    public static String VIDEO_FILE_SERVER;
    public static String GET_VIDEO_FILE_COUNT_SERVER;
    public static String GET_FETCH_VIDEO_SERVER;
    public static String DELETE_VIDEO_SERVER;
    public static String METE_UPLOAD_SERVER;
    public static String URL_LIST_SERVER;
    public static String GET_VIDEO_STATUS_SERVER;
    public static String GET_VIDEO_INFO_EXTERNAL;
    public static String GET_SINGLE_VIDEO_INFO_EXTERNAL;
    public static int READ_DEFAULT_SIZE;
    public static final int READ_MAX_SIZE = 1024000;
    public static final int READ_MIN_SIZE = 10240;
    public static final int MAX_THREAD_COUNT = 5;
    public static final int MIN_THREAD_COUNT = 1;
    public static int DEFAULT_THREAD_COUNT;
    public static final String APP_METADATA_MANAGER_URL = "managerUrl";
    public static final String APP_METADATA_UPLOAD_URL = "uploadUrl";

    static {
        AUTHORIZE_PERSON_URL = HTTPS_OPENAPI_URL + "/api/person/authorize";
        AUTHORIZE_BAIDU_SERVER_URL = HTTPS_OPENAPI_URL + "/api/baidu/authorize";
        AUTHORIZE_ENTERPRISE = HTTPS_OPENAPI_URL + "/api/iqiyi/authorize";
        QICHUAN_SERVER_URL = "http://qichuan.iqiyi.com";
        QICHUAN_SERVER_2_URL = "http://qichuan.iqiyi.com";//http://upload.iqiyi.com
        QICHUAN_SERVER_BACKUP_URL = "http://qc.iqiyi.com";
        REQUEST_UPLOAD_SERVER = QICHUAN_SERVER_2_URL + "/openupload";
        CANCEL_UPLOAD_SERVER = QICHUAN_SERVER_2_URL + "/cancelupload";
        FINISH_UPLOAD_SERVER = QICHUAN_SERVER_2_URL + "/uploadfinish";
        GET_VIDEO_INFO_INTERNAL = QICHUAN_SERVER_2_URL + "/fileinfo";
        REQUEST_UPLOAD_SERVER_BACKUP = QICHUAN_SERVER_BACKUP_URL + "/openupload";
        VIDEO_FILE_SERVER = OPENAPI_URL + "/api/file";
        GET_VIDEO_FILE_COUNT_SERVER = VIDEO_FILE_SERVER + "/count";
        GET_FETCH_VIDEO_SERVER = VIDEO_FILE_SERVER + "/fetchvideolist";
        DELETE_VIDEO_SERVER = VIDEO_FILE_SERVER + "/delete";
        METE_UPLOAD_SERVER = VIDEO_FILE_SERVER + "/info";
        URL_LIST_SERVER = VIDEO_FILE_SERVER + "/urllist";
        GET_VIDEO_STATUS_SERVER = VIDEO_FILE_SERVER + "/fullStatus";
        GET_VIDEO_INFO_EXTERNAL = VIDEO_FILE_SERVER + "/videoListForExternal";
        GET_SINGLE_VIDEO_INFO_EXTERNAL = VIDEO_FILE_SERVER + "/videoForExternal";
        READ_DEFAULT_SIZE = 102400;
        DEFAULT_THREAD_COUNT = 3;
    }

    public VCOPClass() {
    }

    public static void rebuild() {
        AUTHORIZE_PERSON_URL = HTTPS_OPENAPI_URL + "/api/oauth2/authorize";
        AUTHORIZE_BAIDU_SERVER_URL = HTTPS_OPENAPI_URL + "/api/baidu/authorize";
        AUTHORIZE_ENTERPRISE = HTTPS_OPENAPI_URL + "/api/iqiyi/authorize";
        QICHUAN_SERVER_BACKUP_URL = "http://qc.iqiyi.com";
        REQUEST_UPLOAD_SERVER = QICHUAN_SERVER_2_URL + "/openupload";
        CANCEL_UPLOAD_SERVER = QICHUAN_SERVER_2_URL + "/cancelupload";
        FINISH_UPLOAD_SERVER = QICHUAN_SERVER_2_URL + "/uploadfinish";
        GET_VIDEO_INFO_INTERNAL = QICHUAN_SERVER_2_URL + "/fileinfo";
        REQUEST_UPLOAD_SERVER_BACKUP = QICHUAN_SERVER_BACKUP_URL + "/openupload";
        VIDEO_FILE_SERVER = OPENAPI_URL + "/api/file";
        GET_VIDEO_FILE_COUNT_SERVER = VIDEO_FILE_SERVER + "/count";
        GET_FETCH_VIDEO_SERVER = VIDEO_FILE_SERVER + "/fetchvideolist";
        DELETE_VIDEO_SERVER = VIDEO_FILE_SERVER + "/delete";
        METE_UPLOAD_SERVER = VIDEO_FILE_SERVER + "/info";
        URL_LIST_SERVER = VIDEO_FILE_SERVER + "/urllist";
        GET_VIDEO_STATUS_SERVER = VIDEO_FILE_SERVER + "/fullStatus";
        GET_VIDEO_INFO_EXTERNAL = VIDEO_FILE_SERVER + "/videoListForExternal";
        GET_SINGLE_VIDEO_INFO_EXTERNAL = VIDEO_FILE_SERVER + "/videoForExternal";
    }
}
