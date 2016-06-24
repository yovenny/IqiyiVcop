package com.iqiyi.sdk.android.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.iqiyi.sdk.android.vcop.api.DataRate;
import com.iqiyi.sdk.android.vcop.api.ReturnCode;
import com.iqiyi.sdk.android.vcop.api.Video;
import com.iqiyi.sdk.android.vcop.unit.FetchVideoStatusResponseMsg;
import com.iqiyi.sdk.android.vcop.util.VCOPUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

public class VideoListActivity extends Activity{
	
	private ListView  VideolistView;
	private VideoListAdapter adapter;
	private List<Map<String, Object>> datas=new ArrayList<Map<String, Object>>();
	
	private ProgressDialog  progressDialog;
	private int   videoCount=-1;//视频数量
	
	private  List<Map<String, Object>> extraVideoList=null;
	private  Bundle bundle=null;
	
	public static final String KEY_PAGE_SIZE = "key_page_size";
	public static final String KEY_PAGE_NUMBER = "key_page_number";
	public static final String KEY_FILE_IDS = "key_file_ids";
	
	int mPageSize = -1;
	int mPageNumber = -1;

	
	private Button mBtnBatchDel;
	private Button mBtnBatchGet;
	
	private static final int UPDATE_UI = 1;
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_UI:
				updateUI();
				break;

			default:
				break;
			}
		}
		
	};
	
	private void updateUI(){
		if(videoCount!=-1||extraVideoList!=null){
			adapter.setData(datas);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videolistactivity);
		
		VideolistView=(ListView)findViewById(R.id.VideoListView);
		mBtnBatchDel = (Button) findViewById(R.id.batch_delete);
		mBtnBatchGet = (Button) findViewById(R.id.batch_get);
		
		progressDialog=new ProgressDialog(VideoListActivity.this);
		progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		progressDialog.setMessage("我正在努力地加载....");
		
		bundle=getIntent().getExtras();
		
		mPageSize = bundle.getInt(KEY_PAGE_SIZE);
		mPageNumber = bundle.getInt(KEY_PAGE_NUMBER);
		
		Log.i("videolist", "pagesize: " + mPageSize + " pagenumber: " + mPageNumber);
		
		adapter = new VideoListAdapter(getApplicationContext(), datas, new Button.OnClickListener() {
			
			@Override
			public void onClick(final View v) {
				// TODO Auto-generated method stub
				
				if(v.getId()==R.id.BtnListDel){//删除按钮
					new DeleteVideoAsycn(v).execute(); 
					

				}else if(v.getId()==R.id.BtnPlayUrl){

					new Thread(new Runnable() {
						@Override
						public void run() {
							Map<String, String>  object=(Map<String, String>)v.getTag();//里面的值
							String fildId=object.get("fileId");
							Map<String, Object> res = MainActivity.vcopClient.getVideoVirtualUrl(fildId,DataRate.MOBILE_MP4_SMOOTH);
							ReturnCode code = (ReturnCode) res.get("return_code");
							if (code != null && code.isSuccess()) {
								Map<String, Map<String, String>> url = (Map<String, Map<String, String>>) res.get("url");

								VCOPUtil.showAlertDialog(VideoListActivity.this,"播放地址是",url.toString());	

							} else {
								VCOPUtil.showAlertDialog(VideoListActivity.this,"播放地址是","获取播放地址失败，可能原因是网络出现故障");
							}
							
						}
					}).start();

				} else if (v.getId()==R.id.BtnInfo) {
					
					new Thread(new Runnable() {
						@Override
						public void run() {
							Map<String, String>  object=(Map<String, String>)v.getTag();//里面的值
							String fileId = object.get("fileId");
							Log.d("VideoListActivity", "fileId: " + fileId);
							
							
							Map<String, Object> res = MainActivity.vcopClient.getVideoInfo(fileId);
							if (res!=null) {
								VCOPUtil.showAlertDialog(VideoListActivity.this,"视频信息是",res.toString());	
							}
						}
					}).start();

				} else if (v.getId() == R.id.btn_v2r) {
					
					new Thread(new Runnable() {
						@Override
						public void run() {
							Map<String, String>  object=(Map<String, String>)v.getTag();//里面的值
							String fildId=object.get("fileId");
							
							Map<String, Object> res = MainActivity.vcopClient.getVideoVirtualUrl(fildId,DataRate.MOBILE_MP4_SMOOTH);
							ReturnCode code = (ReturnCode) res.get("return_code");
							if (code != null && code.isSuccess()) {
								Map<String, Map<String, String>> url = (Map<String, Map<String, String>>) res.get("url");
								Map<String, String> mp4Url = url.get("mp4");
								String vurl = mp4Url.get("1");
								String realUrl=MainActivity.vcopClient.virtualUrlToRealUrl(vurl);
								VCOPUtil.showAlertDialog(VideoListActivity.this,"真实地址转虚拟地址","虚拟地址是:"+vurl+"\r\n真实地址是:"+realUrl);
							}
						}
					}).start();
					

				    
				} else if (v.getId() == R.id.btn_state) {
					
					
					new Thread(new Runnable() {
						@Override
						public void run() {
							Map<String, String>  object=(Map<String, String>)v.getTag();//里面的值
							String fileId = object.get("fileId");
							
							FetchVideoStatusResponseMsg res = MainActivity.vcopClient.getVideoStatus(fileId);
							if (res != null) {
								VCOPUtil.showAlertDialog(VideoListActivity.this, "视频状态","code: " + res.getCode() + "\r\n" +
							                                                              "msg: " + res.getMsg() + "\r\n" +
										                                                  "data: " + res.getData());
							}
						}
					}).start();
					

					
					
				} else if (v.getId() == R.id.btn_palyback) {
					
					new Thread(new Runnable() {
						@Override
						public void run() {
							Map<String, String>  object=(Map<String, String>)v.getTag();//里面的值
							String fileId = object.get("fileId");
							Map<String, Object> res = MainActivity.vcopClient.getVideoVirtualUrl(fileId, DataRate.MOBILE_MP4_SMOOTH);
							ReturnCode code = (ReturnCode) res.get("return_code");
							if (code != null && code.isSuccess()) {
								Map<String, Map<String, String>> url = (Map<String, Map<String, String>>) res.get("url");
								Map<String, String> mp4Url = url.get("mp4");
								String vurl = mp4Url.get("1");
								String realUrl=MainActivity.vcopClient.virtualUrlToRealUrl(vurl);
								Intent intent = new Intent(VideoListActivity.this, PlaybackActivity.class);
								intent.putExtra(PlaybackActivity.KEY_URL, realUrl);
								startActivity(intent);


							}
						}
					}).start();
					

				}
				
			}
		});
		
		mBtnBatchDel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (adapter==null) {
				    return;
				}
				final String[] ids = adapter.getDeleteIds();
				if (ids!=null && ids.length>0) {
					Thread t = new Thread(new Runnable() {
	
						@Override
						public void run() {
							
							if(ReturnCode.isSuccess(MainActivity.vcopClient.deleteVideoByIDs(ids)))
							{
								VideoListActivity.this.runOnUiThread(new Runnable() {

									@Override
									public void run() {
										VCOPUtil.showToast(VideoListActivity.this, "删除视频成功");
										
									}});
								
								for (String fileId : ids) {
									for(Map<String, Object> map : datas)
									{
										if(fileId.compareTo((String)map.get("fileId"))==0)
										{
											datas.remove(map);
											break;
										}

									}

								}
								
								
								VideoListActivity.this.runOnUiThread(new Runnable() {

									@Override
									public void run() {
										adapter.notifyDataSetChanged();
										
									}});
								
							}else//删除失败
							{
								VideoListActivity.this.runOnUiThread(new Runnable() {

									@Override
									public void run() {
										VCOPUtil.showToast(VideoListActivity.this, "删除视频失败");
										
									}});
							}
	
							
						}});
					t.start();
				}
			}
		});
		
		mBtnBatchGet.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (adapter == null) {
				    return;
				}
				final String[] ids = adapter.getDeleteIds();
				if (ids!=null && ids.length>0) {
					Thread t = new Thread(new Runnable() {
	
						@Override
						public void run() {
							
							extraVideoList=MainActivity.vcopClient.getVideoInfo(ids);
							
							if(extraVideoList!=null && extraVideoList.size()>0)
							{
								datas = extraVideoList;
								
								
								VideoListActivity.this.runOnUiThread(new Runnable() {

									@Override
									public void run() {
										adapter.setData(datas);
										
									}});

								
							}else//删除失败
							{
								VideoListActivity.this.runOnUiThread(new Runnable() {

									@Override
									public void run() {
										VCOPUtil.showToast(VideoListActivity.this, "获取视频失败");
										
									}});
							}
	
							
						}});
					t.start();
				}
			}
		});
		
		VideoListload  videoListLoad=new VideoListload();
		videoListLoad.execute();
	}
	
	private class DeleteVideoAsycn extends AsyncTask<Void, Integer, ReturnCode>{
		private View v;
		
		public  DeleteVideoAsycn( View v) {
			this.v = v;
		} 

		@Override
		protected ReturnCode doInBackground(Void... params) {
			Map<String, String>  object=(Map<String, String>)v.getTag();//里面的值
			String fildId=object.get("fileId");
			ReturnCode code = MainActivity.vcopClient.deleteVideoByID(fildId);
			return code;

		}

		@Override
		protected void onPostExecute(ReturnCode code) {
			Map<String, String>  object=(Map<String, String>)v.getTag();//里面的值
			String fildId=object.get("fileId");
			if(ReturnCode.isSuccess(code)==true)
			{
				VCOPUtil.showAlertDialog(VideoListActivity.this,"删除视频成功",fildId);	
				//从列表中进行查找
				int findindex=-1;
				int tempCount=0;
				for(Map<String, Object> map : datas)
				{
					if(fildId.compareTo((String)map.get("fileId"))==0)
					{
						findindex=tempCount;
						break;
					}
					tempCount++;
				}
				if(findindex!=-1)
				{
					adapter.removeItem(findindex);
				}
			}else//删除失败
			{
				VCOPUtil.showAlertDialog(VideoListActivity.this,"删除视频失败",fildId);
			}
		}
		
	}
	
	
	// 获取文章列表 异步执行类
	class VideoListload extends AsyncTask<String,Integer,Void>
	{
		
		
		@Override
		protected void onPreExecute() {
			
			if (adapter!=null) {
				adapter.clear();
				adapter.notifyDataSetChanged();
			}
			progressDialog.show();
			
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... URL) {
			
			videoCount=MainActivity.vcopClient.getVideoCount();
			Log.i("videolist", "video count: " + videoCount);
			if(videoCount!=-1)
			{
				//全部显示在一页里
				if (mPageSize > 0 && mPageNumber >= 0) {
					datas = MainActivity.vcopClient.getVideoListPage(mPageSize, mPageNumber);
				} else {
				    datas = MainActivity.vcopClient.getVideoListPage(new Integer(videoCount),new Integer(1));
				}
				Log.i("videolist", "list: " + datas.size());
				mHandler.sendEmptyMessage(UPDATE_UI);

			}
			return null;
		}

		
		protected void onPostExecute(Void result) {

			VideolistView.setAdapter(adapter);
			progressDialog.dismiss();
			
			if(videoCount!=-1)
			{
				VCOPUtil.showToast(VideoListActivity.this,"你在视频云上共有:"+videoCount+"个视频");
			}else
			{
				VCOPUtil.showToast(VideoListActivity.this,"获取视频失败，请稍后重试！");
			}
			
			super.onPostExecute(result);
		}
		
	}
	
	
}
