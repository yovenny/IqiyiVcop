//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.unit;

import android.os.Bundle;
import com.iqiyi.sdk.android.vcop.api.VCOPException;

public interface InteriorUploadResultListener {
  void onProgress(int var1, String var2, double var3, double var5);

  void onFinish(String var1, Bundle var2);

  void onError(VCOPException var1);
}
