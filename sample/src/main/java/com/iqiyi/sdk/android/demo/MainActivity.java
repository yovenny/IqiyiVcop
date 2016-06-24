package com.iqiyi.sdk.android.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.iqiyi.sdk.android.vcop.api.ReturnCode;
import com.iqiyi.sdk.android.vcop.api.VCOPClient;
import com.iqiyi.sdk.android.vcop.authorize.Authorize2AccessToken;
import com.iqiyi.sdk.android.vcop.util.VCOPUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/*
 * 这个是demo
 */
@SuppressLint("NewApi")
public class MainActivity extends Activity {
	
	private static final String TAG = "MainActivity";

	private Button  BtnAuthorize;
	private Button  BtnUploadd;
	private Button  BtnBrowse;
	private Button  BtnAuthorBaidu;
	private Button  BtnEnterprise;
//	private Button  BtnVirtualUrlToRealUrl;
	private Button  BtnRefreshToken;
	private Button  BtnSDKVersion;
	
	public static  VCOPClient vcopClient;
	
	private String mTestKey = "8aaaeae9766a4c8da4fefdd193e8cd0b";
	private String mTestKeySecret = "a18b53afec981b2abe7e5afda65ba388";
	
	private int mStateBaidu = 1;
	private int mStateEnterprise = 2;
	private int mStateCommon = 3;
	private int AUTHO_STATE = mStateBaidu;
	private static final String ACCESS_TOKEN_PATH = "/sdcard/Android/data/com.iqiyi.sdk.android.demo/";
	
	private static final String MEIPAI_UID = "2049537617";
	
	private String mUID = MEIPAI_UID;
	
	private final static int COMN_DEVELOP_AUTH_SUCC = 1;
	private final static int COMN_DEVELOP_AUTH_FAIL = 2;
	private final static int COMPANY_AUTH_SUCC = 3;
	private final static int COMPANY_AUTH_FAIL = 4;
	private final static int BAIDU_AUTH_SUCC = 5;
	private final static int BAIDU_AUTH_FAIL = 6;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case COMN_DEVELOP_AUTH_SUCC:
				BtnAuthorBaidu.setEnabled(false);
				BtnAuthorize.setEnabled(false);
				BtnEnterprise.setEnabled(false);
				
				BtnUploadd.setEnabled(true);
				BtnBrowse.setEnabled(true);
				BtnRefreshToken.setEnabled(true);
				BtnSDKVersion.setEnabled(true);
				break;
			case COMN_DEVELOP_AUTH_FAIL:
				break;
			case COMPANY_AUTH_SUCC:
				BtnAuthorBaidu.setEnabled(false);
				BtnAuthorize.setEnabled(false);
				BtnEnterprise.setEnabled(false);
				
				BtnUploadd.setEnabled(true);
				BtnBrowse.setEnabled(true);	
				BtnRefreshToken.setEnabled(true);
				BtnSDKVersion.setEnabled(true);
				break;
			case COMPANY_AUTH_FAIL:
				break;
			case BAIDU_AUTH_SUCC:
				BtnAuthorBaidu.setEnabled(false);
				BtnAuthorize.setEnabled(false);
				BtnEnterprise.setEnabled(false);
				
				BtnUploadd.setEnabled(true);
				BtnBrowse.setEnabled(true);
				BtnRefreshToken.setEnabled(true);
				BtnSDKVersion.setEnabled(true);
				break;
			case BAIDU_AUTH_FAIL:
				break;
				
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		BtnAuthorize=(Button)findViewById(R.id.Authorizebutton);//一般用户授权
		BtnUploadd=(Button)findViewById(R.id.UploadButton);
		BtnBrowse=(Button)findViewById(R.id.BtnBrowse);
		BtnAuthorBaidu=(Button)findViewById(R.id.BtnAuthorBaidu);
		
		BtnEnterprise=(Button)findViewById(R.id.BtnEnterprise);
		
//		BtnVirtualUrlToRealUrl=(Button)findViewById(R.id.BtnVirtualUrlToRealUrl);
		BtnRefreshToken = (Button) findViewById(R.id.BtnRefreshToken);
		BtnSDKVersion = (Button) findViewById(R.id.BtnSDKVersion);
		
		BtnUploadd.setEnabled(false);//先设置为不可用
		BtnBrowse.setEnabled(false);
		BtnRefreshToken.setEnabled(false);
		BtnAuthorBaidu.setEnabled(false);
		BtnSDKVersion.setEnabled(false);
		
		//默认授权  一般用户
		//vcopClient=new  VCOPClient("5b5d1680d7cf4203b3d4579b20be9c0f", "fb2232df-e311-45a7-a113-2bcf0425419e", "http://www.baidu.cn");
		
//		vcopClient = new  VCOPClient("7d1208c58d9449f5942ffa0630f70286", null, "http://www.baidu.com");
//		vcopClient = new  VCOPClient(mTestKey, mTestKeySecret, "http://www.baidu.com");
		ApplicationInfo info;
		try 
		{
			info = this.getPackageManager().getApplicationInfo(
					this.getPackageName(), PackageManager.GET_META_DATA);
			mTestKey = info.metaData.getString("APPKEY");
			mTestKeySecret = info.metaData.getString("APPSECRET");
			
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Authorize2AccessToken token = this.getLocalAccessToken();
		if (token != null) {
		    Log.d(TAG, token.toString());
		}
		vcopClient = new  VCOPClient(mTestKey, mTestKeySecret, token);
		
//		BtnAuthorize.setVisibility(View.GONE);//先隐藏一般用户的授权，现在只开放一般用户
		//BtnAuthorBaidu.setVisibility(View.GONE);
		
		BtnAuthorize.setOnClickListener(listener);
		BtnUploadd.setOnClickListener(listener);
		BtnBrowse.setOnClickListener(listener);
		BtnAuthorBaidu.setOnClickListener(listener);
		BtnEnterprise.setOnClickListener(listener);
		BtnRefreshToken.setOnClickListener(listener);
		BtnSDKVersion.setOnClickListener(listener);
	}
	

	
	public OnClickListener listener = new Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId() == R.id.BtnSDKVersion)
			{
				String version = vcopClient.getSDKVersion();
				VCOPUtil.showAlertDialog(MainActivity.this,"SDK 版本", "SDK 版本: " + version);
			}else if(v.getId() == R.id.BtnRefreshToken)
			{
				new Thread(new Runnable() {
					@Override
					public void run() {
						String token = vcopClient.getToken().getAccessToken();
						long exptime = vcopClient.getToken().getExpiresTime();
						String refreshToken = vcopClient.getToken().getRefreshToken();
						ReturnCode code = null;
						code = vcopClient.refreshToken();
						Log.i("refresh", "refresh code: " + code.getCode());
						if (!ReturnCode.isSuccess(code)) {
							VCOPUtil.showAlertDialog(MainActivity.this,"刷新Token","refresh token error: " + code.getCode());
							return;
						}
						String currtoken = vcopClient.getToken().getAccessToken();
						long currexptime = vcopClient.getToken().getExpiresTime();
						String currrefreshToken = vcopClient.getToken().getRefreshToken();
						VCOPUtil.showAlertDialog(MainActivity.this,"刷新Token","preToken: "+token+"\r\npreExpTime: "+exptime + "\r\npreRefreshToken: " + refreshToken + 
								"\r\ntoken: " + currtoken + "\r\nexpTime: " + currexptime + "\r\nrefreshToken: " + currrefreshToken);
						
					}
				}).start();

			}else if(v.getId() == R.id.BtnEnterprise)
			{
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						ReturnCode code = vcopClient.authorize();
						Log.i(TAG, "enterprise: " + code.getCode() + " " + code.getCodeMsg());
						if(code.isSuccess())
						{
							setLocalAccessToken(vcopClient.getToken());
							AUTHO_STATE = mStateEnterprise;

							Authorize2AccessToken token = vcopClient.getToken();
							setLocalAccessToken(token);
							VCOPUtil.showAlertDialog(MainActivity.this,"企业授权提示","企业授权成功");
							handler.sendEmptyMessage(COMPANY_AUTH_SUCC);
						}else
						{
							VCOPUtil.showAlertDialog(MainActivity.this,"企业授权提示","企业授权失败");
						}
						
					}
				}).start();
				

			}else if(v.getId() == R.id.BtnAuthorBaidu)
			{
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						if(ReturnCode.isSuccess(vcopClient.authorize("zhaixu", "zhaixu"))==true)//
						{
							setLocalAccessToken(vcopClient.getToken());
							AUTHO_STATE = mStateBaidu;

							Authorize2AccessToken token = vcopClient.getToken();
							setLocalAccessToken(token);
							VCOPUtil.showAlertDialog(MainActivity.this,"百度授权提示","百度授权成功");
							handler.sendEmptyMessage(BAIDU_AUTH_SUCC);
						}else
						{
							VCOPUtil.showAlertDialog(MainActivity.this,"百度授权提示","百度授权失败");
						}
						
					}
				}).start();

			}else if(v.getId() == R.id.BtnBrowse)
			{
				if(vcopClient.getToken()==null)
				{
					VCOPUtil.showAlertDialog(MainActivity.this, "上传提示", "请先授权再上传");
				}else
				{
//					Intent intent = new Intent(MainActivity.this,VideoListActivity.class);
//					startActivity(intent);	
					Intent intent = new Intent(MainActivity.this,VideoListConfigActivity.class);
					startActivity(intent);	
				}
			}else if(v.getId() == R.id.UploadButton)
			{
				if(vcopClient.getToken()==null)
				{
					VCOPUtil.showAlertDialog(MainActivity.this, "上传提示", "请先授权再上传");
				}else
				{
					Intent  intent= new Intent(MainActivity.this,UploadListActivity.class);
					startActivity(intent);	
				}
			}else if(v.getId() == R.id.Authorizebutton){
				new Thread(new Runnable() {
					public void run() {
						ReturnCode code = vcopClient.authorize("test");
						if (code.isSuccess()) {
							setLocalAccessToken(vcopClient.getToken());
							AUTHO_STATE = mStateCommon;

							Authorize2AccessToken token = vcopClient.getToken();
							setLocalAccessToken(token);
							VCOPUtil.showAlertDialog(MainActivity.this,"授权结果提示","授权成功");
							handler.sendEmptyMessage(COMN_DEVELOP_AUTH_SUCC);
						} else {
							VCOPUtil.showAlertDialog(MainActivity.this,"授权出现异常", code.toString());
						}
						Log.i(TAG, "personal auth: " + code.toString());
					}
				}).start();

			}
	}};
	
	private Authorize2AccessToken getLocalAccessToken() {
		Authorize2AccessToken token = null;
		try {
			File f = new File(ACCESS_TOKEN_PATH + "accessToken.o");
			FileInputStream in = new FileInputStream(f);
			ObjectInputStream oin = new ObjectInputStream(in);
			token = (Authorize2AccessToken) oin.readObject();
			oin.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}
		return token;
	}
	
	private void setLocalAccessToken(Authorize2AccessToken accessToken) {
		try {
			File dir = new File(ACCESS_TOKEN_PATH);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File f = new File(ACCESS_TOKEN_PATH + "accessToken.o");
			if (f.exists()) {
			    f.delete();
			}
			f.createNewFile();
			FileOutputStream ou = new FileOutputStream(f);
			ObjectOutputStream oou = new ObjectOutputStream(ou);
			oou.writeObject(accessToken);
			oou.flush();
			oou.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}


}
