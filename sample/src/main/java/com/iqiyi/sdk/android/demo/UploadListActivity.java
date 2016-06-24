package com.iqiyi.sdk.android.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.iqiyi.sdk.android.vcop.api.OnUploadListener;
import com.iqiyi.sdk.android.vcop.api.ReturnCode;
import com.iqiyi.sdk.android.vcop.api.UploadResultListener;
import com.iqiyi.sdk.android.vcop.api.VCOPException;
import com.iqiyi.sdk.android.vcop.qichuan.UploadInfor;
import com.iqiyi.sdk.android.vcop.util.VCOPUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadListActivity extends Activity{
	private ListView  uploadListView;
	
	private UploadingListAdapter adapter;
	
	private List<Map<String, String>> datas=new ArrayList<Map<String,String>>();
	
	private Button BtnUpload;
	private Button BtnBrowseUpload;
	
	private Handler  handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);
		
		uploadListView=(ListView)findViewById(R.id.UploadListView);
		
		//获取data的信息
		List<UploadInfor> uploadList=MainActivity.vcopClient.getAllNoCompleteUpload();
		for(UploadInfor infor:uploadList)//是有多个线程的
		{
			Log.i("UploadListActivity", "upload infor: " + infor.toString());
			Map<String, String>  itemdata=new HashMap<String, String>();
			itemdata.put("fileId", infor.getFileiId());
			itemdata.put("filePath", infor.getFilePath());
			itemdata.put("finish", "0");//还未完成的
			
			itemdata.put("fileSize", String.valueOf("文件大小："+infor.getFileSize()/1024/1024)+" M");
			//
			int temp=0;
			if(infor.getAllProgress()-(int)infor.getAllProgress()>0.5)
			{
				temp=(int)infor.getAllProgress()+1;
			}else
			{
				temp=(int)infor.getAllProgress();
			}
			itemdata.put("progress", String.valueOf(temp));
			
			Log.d("logprogress", "日志里保存的进度:"+infor.getAllProgress());
			
			itemdata.put("fileState", "暂停上传");
			itemdata.put("pause","1");//暂停
			
			datas.add(itemdata);
		}
		
		adapter = new UploadingListAdapter(UploadListActivity.this,datas, new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Map<String, String>  object=(Map<String, String>)v.getTag();
				final String fileid=object.get("fileId");
				
				if(v.getId()==R.id.BtnUploadPause)//暂停
				{
					if(ReturnCode.isSuccess(MainActivity.vcopClient.pauseUpload(fileid))==true)
					{
						   for(int i=0;i<datas.size();i++)
						   {
							   if(fileid.compareTo(datas.get(i).get("fileId"))==0)
							   {
								   //取出id的值
									adapter.UpdateState(i, "暂停上传");
									adapter.updatePauseState(i,"1");//暂停
							   }
						   }
						VCOPUtil.showToast(UploadListActivity.this,"暂停提示：暂停成功");
					}else
					{
						VCOPUtil.showToast(UploadListActivity.this, "暂停提示：暂停失败");
					}
					
				}else if(v.getId()==R.id.BtnUploadCancel)//取消上传，然后要把这个取消进行删除掉----------------------------
				{
					Button btnButton=(Button)v;
					btnButton.setEnabled(false);
					if(fileid!="")
					{
						//判定是否上传成功了
						boolean isfinish=true;
						for (int i = 0; i < datas.size(); i++) 
						{
							if (fileid.compareTo(datas.get(i).get("fileId")) == 0) 
							{
								if(datas.get(i)!=null&&datas.get(i).get("finish")!=null)
								{
									if(datas.get(i).get("finish").compareTo("0")==0)
									{
										isfinish=false;
										break;
									}	
								}
							}
						}
						if(isfinish==false)
						{
							new CancleAsyncTask(fileid,btnButton).execute(fileid);

						}else
						{	
							 VCOPUtil.showToast(UploadListActivity.this,"取消上传提示：取消失败，原因是视频已经上传完毕！");
							 btnButton.setEnabled(true);
						}
					}else
					{
						//没有fileid的时候，要依靠 index来进行删除
						Map<String, String> map=(Map<String, String>)v.getTag();
						int removeid=-1;
						if(map!=null&&map.get("index")!=null)
						{
							for(int i=0;i<datas.size();i++)
							{
								if(datas.get(i)!=null&&datas.get(i).get("index")!=null)
								{
									if(map.get("index").compareTo(datas.get(i).get("index"))==0)
									{
										   //取出id的值
											adapter.UpdateState(i, "取消上传");
											removeid=i;
											break;
									}	
								}
							}
							if(removeid!=-1)
							{
								adapter.removeItem(removeid);//删除掉这个
							}
							VCOPUtil.showToast(UploadListActivity.this,"取消上传提示：取消成功！");	
						}else
						{
							VCOPUtil.showToast(UploadListActivity.this,"取消上传提示：取消失败！");	
							btnButton.setEnabled(true);
						}
					}
					
				}else if(v.getId()==R.id.BtnUploadContinue)//继续上传---------------------------------------
				{
					if(fileid!="")
					{
						for (int i = 0; i < datas.size(); i++) 
						{
							if (fileid.compareTo(datas.get(i).get("fileId")) == 0) 
							{
										// 取出id的值
								adapter.UpdateState(i, "续传中....");
								adapter.updatePauseState(i,"0");
							}	
						}
						
						MainActivity.vcopClient.resumeUpload(fileid, new OnUploadListener() {
							
							@Override
							public void onProgress(String fildId, int progress, double speed) {
								// TODO Auto-generated method stub
								Log.d("progeress","上传进度:"+progress);
								Message msg=new Message();
								msg.arg1=new Integer(progress);
								msg.what=100;
								msg.obj=fildId;	
								handler.sendMessage(msg);
								
								Log.i("UploadListActivity", "upload fileId: " + fildId + " speed: " + speed);
								List<UploadInfor> uploadList=MainActivity.vcopClient.getAllNoCompleteUpload();
								for(UploadInfor infor:uploadList)//是有多个线程的
								{
									Log.i("UploadListActivity", "upload infor: " + infor.toString());
								}
							}
							
							@Override
							public void onFinish(String fildId,Bundle value) {
								// TODO Auto-generated method stub
								if(ReturnCode.isSuccess(value.getString("code"))==true)
								{
									Message msg=new Message();
									msg.what=101;
									msg.obj=fileid;
									handler.sendMessage(msg);
								}else
								{
									Message msg=new Message();
									msg.what=102;
									msg.obj=fileid;
									handler.sendMessage(msg);
								}
							}
							
							@Override
							public void onError(VCOPException e) {
								// TODO Auto-generated method stub
								Message msg=new Message();
								msg.what=103;
								if(e.getStatusCode().compareTo(ReturnCode.FAILURE)==0)//失败
								{
									msg.arg1=100;
								}else if(e.getStatusCode().compareTo(ReturnCode.UPLOADING)==0)
								{
									msg.arg1=101;
								}else if(e.getStatusCode().compareTo(ReturnCode.NETWORK_ERROR)==0)//网络连接失败
								{
									msg.arg1=102;
								}else if(e.getStatusCode().compareTo(ReturnCode.FILE_ID_NOT_FIND)==0)
								{
									msg.arg1=113;
								}
								msg.obj=fileid;
								handler.sendMessage(msg);
							}
							
						});
					}else
					{
						VCOPUtil.showAlertDialog(UploadListActivity.this, "续传提示", "没有可以续传的文件，可能原因是：你在添加上传的时候，遇到网络错误，请取消，再上传！或者已经上传完毕了");
					}
				}else if(v.getId()==R.id.BtnSetMeta)//设置Meta信息
				{
					if(fileid!="")
					{
						//弹出填写框
						final Dialog builder = new Dialog(UploadListActivity.this);  
				        builder.setContentView(R.layout.metainforlayout);  
				        final EditText videoNameEdit = (EditText)builder.findViewById(R.id.videNameEdit); 
				        final EditText videoDirsEdit=(EditText)builder.findViewById(R.id.VideoDiscriptionEdit); 
				        
				        Button BtnOk = (Button) builder.findViewById(R.id.BtnSetOk);  
				        BtnOk.setOnClickListener(new View.OnClickListener() {  
				            @SuppressLint("NewApi")
							public void onClick(View v) 
				            {  
				            	if(videoNameEdit.getText().toString().trim().isEmpty()==true||videoDirsEdit.getText().toString().trim().isEmpty()==true)
				            	{
				            		VCOPUtil.showAlertDialog(UploadListActivity.this,"设置提示","视频名和视频描述不能为空,请进行设置");	
				            		
				            	}else
				            	{
				            		/*
				            		 *key:file_name    value：开发者填充
				            		 *key:description  value:开发者填充
				            		 */
				            		Map<String,String> inforMap=new HashMap<String, String>();
				            		inforMap.put("file_name", videoNameEdit.getText().toString());
				            		inforMap.put("description", videoDirsEdit.getText().toString());
					            	if(ReturnCode.isSuccess(MainActivity.vcopClient.setMetadata(fileid,inforMap))==true)
					            	{
						            	VCOPUtil.showToast(UploadListActivity.this,"设置meta信息成功");	
						            	 builder.dismiss();  
					            	}else
					            	{
					            		VCOPUtil.showToast(UploadListActivity.this, "设置meta信息失败");	
					            		builder.dismiss();  
					            	}	
				            	}
				            }  
				        });  
				        
				        Button BtnCancel = (Button) builder.findViewById(R.id.BtnSetCancel);  
				        BtnCancel.setOnClickListener(new View.OnClickListener() {  
				  
				            public void onClick(View v) {  
				                builder.dismiss();  
				            }  
				        });  
				        builder.show();  	
					}else
					{
						VCOPUtil.showToast(UploadListActivity.this, "设置meta信息失败,原因是你上传失败了，没有获取到fileid的值");	
					}
				}
			}
			
		});
		
		uploadListView.setAdapter(adapter);
		
		BtnUpload=(Button)findViewById(R.id.BtnUpload);
		BtnBrowseUpload=(Button)findViewById(R.id.BtnBrowseUpload);
		BtnBrowseUpload.setVisibility(View.GONE);
		
		BtnBrowseUpload.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String[] fileIds=new String[datas.size()];
				for(int i=0;i<datas.size();i++)
				{
					if(datas.get(i).get("fileId")!=null)
					{
						fileIds[i]=(String)datas.get(i).get("fileId");
					}
				}

				Intent intent = new Intent(UploadListActivity.this,VideoListActivity.class);				
				Bundle bundle = new Bundle();
				bundle.putStringArray("fileIds", fileIds);
				intent.putExtras(bundle);
				 
				startActivity(intent);	
			}
			
		});
		
		BtnUpload.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//先判断一下是否有正在上传的，如果有的话，就不要继续添加了
				boolean isAddOk=true;
				int count=0;
				for(Map<String, String> map:datas)
				{
					  if(map.get("finish").compareTo("0")==0)//未完成
					  {
						  count++;
					  }
				}
				if(count>=2)
				{
					isAddOk=false;
				}
				if(isAddOk==true)
				{
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_GET_CONTENT);
			        intent.setType("video/*");
			        /* 取得相片后返回本画面 */
			        startActivityForResult(intent, 2);	
				}else
				{
					VCOPUtil.showAlertDialog(UploadListActivity.this, "上传提示", "当前已经有正在上传的文件有"+count+"个,请等待上传完毕再添加!");
				}
				
			}
		});
		
		/*
		 * 界面消息显示---------------------消息通知
		 * 
		 */
		handler=new Handler()
		{
			@Override
	        public void handleMessage(Message msg) {
	            // TODO Auto-generated method stub
				if(msg==null)
					return;
				
				String msgStr=(String)msg.obj;
				
				switch(msg.what)
				{
				   case 100:
					   //从item里面找出了相应的更新
					   for(int i=0;i<datas.size();i++)
					   {
						   if(msgStr.compareTo(datas.get(i).get("filePath"))==0||msgStr.compareTo(datas.get(i).get("fileId"))==0)
						   {

							   //取出id的值
							   adapter.UpdateProgeress(i, String.valueOf(msg.arg1));
						   }
					   }
					   break;
				   case 101://成功
					   for(int i=0;i<datas.size();i++)
					   {
						   if(msgStr.compareTo(datas.get(i).get("filePath"))==0||msgStr.compareTo(datas.get(i).get("fileId"))==0)
						   {
							   //取出id的值
							   adapter.UpdateState(i,"上传完毕");
							   adapter.finishState(i, "1");
						   }
					   }
					   break;
				   case 102://失败
					   for(int i=0;i<datas.size();i++)
					   {
						   if(msgStr.compareTo(datas.get(i).get("filePath"))==0||msgStr.compareTo(datas.get(i).get("fileId"))==0)
						   {
							   //取出id的值
							   adapter.UpdateState(i,"上传失败");
							   VCOPUtil.showToast(UploadListActivity.this,"上传失败");
						   }
					   }
					   break;
				   case 103://
					   for(int i=0;i<datas.size();i++)
					   {
						   if(msgStr.compareTo(datas.get(i).get("filePath"))==0||msgStr.compareTo(datas.get(i).get("fileId"))==0)
						   {
							   //取出id的值
							   if(msg.arg1==100)
							   {
								   adapter.UpdateState(i, "上传完毕");
								   VCOPUtil.showToast(UploadListActivity.this,"续传提示:你的文件可能已经上传完毕，无法继续续传");	
								   
							   }else if(msg.arg1==101)
							   {
								   VCOPUtil.showToast(UploadListActivity.this, "续传提示：文件正在上传中....");	
							   }else if(msg.arg1==102)
							   {
								   adapter.UpdateState(i, "网络出错，请检查网络");
								   VCOPUtil.showToast(UploadListActivity.this,"网络出错，请检查网络");	
							   }else if(msg.arg1==113)
							   {
								   adapter.UpdateState(i, "续传失败");
								   VCOPUtil.showToast(UploadListActivity.this,"续传失败，可能原因是云端的视频已经被删除，你可以取消上传");	
							   }
							   else
							   {
								   adapter.UpdateState(i,"上传过程失败");   
							   }
						   }
					   }
					   break;
				}
	        }
		};
		
	}
	
	public class CancleAsyncTask extends AsyncTask<String,Integer,ReturnCode>{
		public String fileid;
		private Button btnButton;
		
		
		public  CancleAsyncTask(String fileid,Button btnButton) {
			super();
			this.fileid = fileid;
			this.btnButton = btnButton;
		}
		
		 @Override  
		    protected void onPostExecute(ReturnCode cancelCode){
			 if(ReturnCode.isSuccess(cancelCode))
				{
					   int removeid=-1;
					   for(int i=0;i<datas.size();i++)
					   {
						   if(datas.get(i)!=null&&datas.get(i).get("fileId")!=null)
						   {
							   if(fileid.compareTo(datas.get(i).get("fileId"))==0)
							   {
								   //取出id的值
									adapter.UpdateState(i, "取消上传");
									removeid=i;
									break;
							   }   
						   }
					   }
					   if(removeid!=-1)
					   {
							adapter.removeItem(removeid);//删除掉这个
					   }
					   VCOPUtil.showToast(UploadListActivity.this,"取消上传提示：取消成功！");
				}else
				{
					Log.d(VCOPUtil.DEBUGTAG,"取消上传的返回状态码:"+cancelCode.getCode());
					int removeid=-1;
					for(int i=0;i<datas.size();i++)
					{
						if(datas.get(i)!=null&&datas.get(i).get("fileId")!=null)
						{
							if(fileid.compareTo(datas.get(i).get("fileId"))==0)
							{
								Map<String,String> map=datas.get(i);
								if(datas.get(i).get("fileSize").compareTo("文件大小：0.0 M")==0)
								{
									adapter.UpdateState(i, "取消上传");
									removeid=i;
									break;
								}
							}	
						}
					}
					if(removeid!=-1)
					{
						adapter.removeItem(removeid);//删除掉这个
						VCOPUtil.showToast(UploadListActivity.this,"取消上传提示：取消成功！");
					}else
					{
						VCOPUtil.showToast(UploadListActivity.this,"取消上传提示：取消失败,可能的原因是没有网络,状态码为:"+cancelCode.getCode());
						btnButton.setEnabled(true);
					}
				}	
				
		 }
		

		@Override
		protected ReturnCode doInBackground(String... params) {
			String fileid = params[0];
			ReturnCode cancelCode=MainActivity.vcopClient.cancelUpload(fileid);
			return cancelCode;
			
		}
		
	}
	
	public class LoadAsyncTask extends AsyncTask<Integer, Integer, String>{
		
		Intent data = null;
		String filePath = null;
		
		public LoadAsyncTask(Intent data) {
			super(); 
			this.data = data;
			Uri uri = data.getData();
			filePath=VCOPUtil.VideoUri2FilePath(UploadListActivity.this,uri);
		   if(filePath==""){
			   VCOPUtil.showAlertDialog(UploadListActivity.this, "选择上传文件提示", "你选择的文件无效");
			   return;
		   }
		}

		@Override
		protected String doInBackground(Integer... params) {
			   
			   Map<String, String> metadata = new HashMap<String, String>();
			   metadata.put("file_name", "测试文件");
			   metadata.put("description", "这是一个测试文件");
			   
			   if(filePath==""){
				   VCOPUtil.showAlertDialog(UploadListActivity.this, "选择上传文件提示", "你选择的文件无效");
				   return null;
			   }
			   
			   final String fileid=MainActivity.vcopClient.upload(filePath, metadata, new UploadResultListener() {
				   
				@Override
				public void onProgress(String fileId,int progress) {
					// TODO Auto-generated method stub
					Log.d("progeress","上传进度:"+progress);
					Message msg=new Message();
					msg.arg1=new Integer(progress);
					msg.what=100;
					msg.obj=fileId;	
					handler.sendMessage(msg);
				}
				
				@Override
				public void onFinish(String fileId,Bundle value) {
					// TODO Auto-generated method stub
					Log.d("upload","upload: "+ value.getString("code"));
					if(ReturnCode.isSuccess(value.getString("code"))==true)
					{
						Message msg=new Message();
						msg.what=101;
						msg.obj=fileId;
						handler.sendMessage(msg);
					}else
					{
						Message msg=new Message();
						msg.what=102;
						msg.obj=fileId;
						handler.sendMessage(msg);
					}
				}
				
				@Override
				public void onError(VCOPException e) {
					// TODO Auto-generated method stub
					e.printStackTrace();
					Message msg=new Message();
					msg.what=102;
					msg.obj=filePath;
					handler.sendMessage(msg);
				}
			});
			return fileid;
		}
		
	    @Override  
	    protected void onPostExecute(String result) {  
	    	   if(TextUtils.isEmpty(result))
	    		   return;
			   File  file = new File(filePath);
			   long fileSize=file.length();
				//
				Map<String, String>  itemdata=new HashMap<String, String>();
				itemdata.put("fileId", result);
				itemdata.put("fileSize", String.valueOf("文件大小："+fileSize*1.0/1024/1024+" M"));
				itemdata.put("filePath", filePath);
				itemdata.put("index",String.valueOf(datas.size()-1));//这个索引，是当上传失败的时候，有可能在获取fileid的时候就失败了
				itemdata.put("progress", "0"); //刚开始的时候，进度为0
				itemdata.put("finish", "0");//1表示完成，0表示微完成
				itemdata.put("pause", "0");//1表示暂停，0表示没有暂停
				itemdata.put("fileState", "正在上传");
				adapter.addItem(itemdata);
	    } 
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data)
	{
		if(requestCode == 2)
		{
			if(resultCode==RESULT_OK){
				new LoadAsyncTask(data).execute(100);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	  @Override 
	  public boolean onKeyDown(int keyCode, KeyEvent event) 
	  { 
		  if(keyCode == KeyEvent.KEYCODE_BACK)//按下返回键盘，这个时候需要把所有的线程给暂停了
		  {
			  
			  //进行所有的暂停
			  if(datas!=null&&datas.size()!=0)
			  {
				  boolean isnoFinish=false;
				  for(Map<String, String> map:datas)
				  {
					  if(map.get("finish")!=null)//未完成
					  {
						  if(map.get("finish").compareTo("0")==0)
						  {
							  isnoFinish=true;
							  MainActivity.vcopClient.pauseUpload(map.get("fileId")); //将未完成进行暂停   
						  }
					  }
				  }
				  if(isnoFinish==true)
				  {
					  VCOPUtil.showToast(UploadListActivity.this,"退出上传提示：所有正在上传的将被暂停");    
				  }
			  }
		  }
		  return super.onKeyDown(keyCode, event);
      }
	
}
