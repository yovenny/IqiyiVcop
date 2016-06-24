//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.api;

public abstract class OnUploadListener implements UploadResultListener {
  public OnUploadListener() {
  }

  public abstract void onProgress(String var1, int var2, double var3);

  public void onProgress(String fileId, int progress) {
  }
}
