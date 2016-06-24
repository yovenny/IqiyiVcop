package com.iqiyi.sdk.android.demo;

import com.iqiyi.sdk.android.vcop.unit.FetchVideoStatusResponseMsg;
import com.iqiyi.sdk.android.vcop.util.VCOPUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class VideoListConfigActivity extends Activity {

	private EditText mPageSize;
	private EditText mPageNumber;
	private EditText mFileIds;
	private Button mBtnGo;
	
	private String mSize = "";
	private String mNumber = "";
	private String mIds = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_list_config);
		
		mPageSize = (EditText) findViewById(R.id.edit_page_size);
		mPageNumber = (EditText) findViewById(R.id.edit_page_number);
		mFileIds = (EditText) findViewById(R.id.edit_file_ids);
		mBtnGo = (Button) findViewById(R.id.go_video_list);
		
		mPageSize.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				mSize = arg0.toString();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
			}});
		
		mPageNumber.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				mNumber = s.toString();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
			}});
		
		mFileIds.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				mIds = arg0.toString();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
			}});
		
		mBtnGo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mIds = mIds.trim();
				if (!TextUtils.isEmpty(mIds)) {
					Thread t = new Thread(new Runnable() {

						@Override
						public void run() {
							final FetchVideoStatusResponseMsg msg = MainActivity.vcopClient.getVideoStatus(mIds);
							if (msg != null) {
								VideoListConfigActivity.this.runOnUiThread(new Runnable() {

									@Override
									public void run() {
										VCOPUtil.showAlertDialog(VideoListConfigActivity.this, "视频状态","code: " + msg.getCode() + "\r\n" +
                                                "msg: " + msg.getMsg() + "\r\n" +
                                                "data: " + msg.getData());
										
									}});
								
							}
							
						}});
					t.start();
					return;
				}
				
				Bundle b = new Bundle();
				if (!TextUtils.isEmpty(mNumber) && !TextUtils.isEmpty(mSize)) {
					int number = 0;
					int size = 0;
					try {
						number = Integer.parseInt(mNumber);
						size = Integer.parseInt(mSize);
					} catch (Exception e) {
					    e.printStackTrace();
					    VCOPUtil.showToast(VideoListConfigActivity.this, "请输入1~9之间的合法数字");
					    return;
					}
					
					
					b.putInt(VideoListActivity.KEY_PAGE_NUMBER, number);
					b.putInt(VideoListActivity.KEY_PAGE_SIZE, size);
					
				}

				
				Intent intent = new Intent(VideoListConfigActivity.this, VideoListActivity.class);
				intent.putExtras(b);
				startActivity(intent);
				
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
//		mSize = "";
//		mNumber = "";
//		mIds = "";
	}

}
