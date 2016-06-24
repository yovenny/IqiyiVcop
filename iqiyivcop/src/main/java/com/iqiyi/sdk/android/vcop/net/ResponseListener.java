//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.net;

import com.iqiyi.sdk.android.vcop.api.VCOPException;

public interface ResponseListener {
  void onFinish(String var1);

  void onError(VCOPException var1);
}
