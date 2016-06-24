//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.api;

import android.os.Bundle;

public interface UploadResultListener {
  void onProgress(String var1, int var2);

  void onFinish(String var1, Bundle var2);

  void onError(VCOPException var1);
}
