//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.unit;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DeleteResponseMsg extends BaseResponseMsg {
    private List<DeleteResponeseMsgData> dataList = new ArrayList();

    public DeleteResponseMsg() {
    }

    public List<DeleteResponeseMsgData> getDataList() {
        return this.dataList;
    }

    public void setDataList(List<DeleteResponeseMsgData> dataList) {
        this.dataList = dataList;
    }

    public static DeleteResponseMsg parseMsg(String jsonStr) {
        if(jsonStr.indexOf("{") >= 0) {
            DeleteResponseMsg msg = new DeleteResponseMsg();

            try {
                JSONObject json = new JSONObject(jsonStr);
                msg.setCode(json.optString("code"));
                return msg;
            } catch (JSONException var4) {
                var4.printStackTrace();
                return msg;
            }
        } else {
            return new DeleteResponseMsg();
        }
    }
}
