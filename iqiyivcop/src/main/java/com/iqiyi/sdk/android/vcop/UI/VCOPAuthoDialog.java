//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.UI;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import com.iqiyi.sdk.android.vcop.api.AuthResultListener;
import com.iqiyi.sdk.android.vcop.api.VCOPException;
import com.iqiyi.sdk.android.vcop.util.VCOPUtil;
import java.io.IOException;
import java.io.InputStream;

public class VCOPAuthoDialog extends Dialog {
    private String url;
    private ProgressDialog progressDialog;
    private WebView webView;
    private AuthResultListener listener;
    private RelativeLayout Dialoglayout;
    private RelativeLayout webViewLayout;
    private static int theme = 16973840;
    private static final String iqiyi_vcop_dialog_bg = "vcop_dialog_bg.9.png";

    public VCOPAuthoDialog(Context context, String url, AuthResultListener listener) {
        super(context, theme);
        this.url = url;
        this.listener = listener;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.progressDialog = new ProgressDialog(this.getContext());
        this.progressDialog.requestWindowFeature(1);
        this.progressDialog.setMessage("正在努力地加载...");
        this.progressDialog.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                VCOPAuthoDialog.this.onGoBack();
                return false;
            }
        });
        this.requestWindowFeature(1);
        this.getWindow().setFeatureDrawableAlpha(0, 0);
        this.Dialoglayout = new RelativeLayout(this.getContext());
        this.initWebView();
        this.addContentView(this.Dialoglayout, new LayoutParams(-1, -1));
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    public void initWebView() {
        this.webViewLayout = new RelativeLayout(this.getContext());
        this.webView = new WebView(this.getContext());
        this.webView.setVerticalScrollBarEnabled(false);
        this.webView.setHorizontalScrollBarEnabled(false);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                VCOPAuthoDialog.this.listener.onError(new VCOPException(description));
                VCOPAuthoDialog.this.dismiss();
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                VCOPAuthoDialog.this.progressDialog.show();
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(VCOPAuthoDialog.this.progressDialog.isShowing()) {
                    VCOPAuthoDialog.this.progressDialog.dismiss();
                }

                VCOPAuthoDialog.this.webView.setVisibility(0);
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler) {
                handler.proceed();
            }
        });
        this.webView.loadUrl(this.url);
        this.webView.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1));
        this.webView.setVisibility(4);
        android.widget.RelativeLayout.LayoutParams layoutParams0 = new android.widget.RelativeLayout.LayoutParams(-1, -1);
        android.widget.RelativeLayout.LayoutParams layoutParams = new android.widget.RelativeLayout.LayoutParams(-1, -1);
        this.Dialoglayout.setBackgroundColor(0);

        try {
            DisplayMetrics asseets = this.getContext().getResources().getDisplayMetrics();
            float is = asseets.density;
            layoutParams0.leftMargin = 10 * (int)is;
            layoutParams0.topMargin = 10 * (int)is;
            layoutParams0.rightMargin = 10 * (int)is;
            layoutParams0.bottomMargin = 10 * (int)is;
            layoutParams.leftMargin = 10 * (int)is;
            layoutParams.topMargin = 30 * (int)is;
            layoutParams.rightMargin = 10 * (int)is;
            layoutParams.bottomMargin = 15 * (int)is;
        } catch (Exception var17) {
            this.listener.onError(new VCOPException(var17));
        }

        AssetManager asseets1 = this.getContext().getAssets();
        InputStream is1 = null;

        try {
            is1 = asseets1.open("vcop_dialog_bg.9.png");
            if(is1 != null) {
                Bitmap e = BitmapFactory.decodeStream(is1);
                if(NinePatch.isNinePatchChunk(e.getNinePatchChunk())) {
                    NinePatchDrawable npd = new NinePatchDrawable(e, e.getNinePatchChunk(), new Rect(0, 0, 0, 0), (String)null);
                    this.webViewLayout.setBackgroundDrawable(npd);
                }
            }
        } catch (Exception var16) {
            var16.printStackTrace();
        } finally {
            if(is1 != null) {
                try {
                    is1.close();
                } catch (IOException var15) {
                    var15.printStackTrace();
                }
            }

        }

        this.webViewLayout.addView(this.webView, layoutParams0);
        this.webViewLayout.setGravity(17);
        this.Dialoglayout.addView(this.webViewLayout, layoutParams);
    }

    private void handleRedirectUrl(WebView view, String url) {
        Bundle values = VCOPUtil.parseUrlParams(url);
        String error = values.getString("code");
        String error_code = values.getString("error_code");
        if(error == null) {
            this.listener.onFinish(values);
        } else {
            this.listener.onError(new VCOPException("授权过程出现异常", "A" + error_code));
        }

    }

    protected void onGoBack() {
        try {
            this.progressDialog.dismiss();
            if(this.webView != null) {
                this.webView.stopLoading();
                this.webView.destroy();
            }
        } catch (Exception var2) {
            ;
        }

        this.dismiss();
    }
}
