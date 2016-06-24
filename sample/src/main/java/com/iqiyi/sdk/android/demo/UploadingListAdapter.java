package com.iqiyi.sdk.android.demo;

import java.util.List;
import java.util.Map;

import com.iqiyi.sdk.android.demo.VideoListAdapter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UploadingListAdapter  extends BaseAdapter {
	private Context context;
	private List<Map<String, String>>datas;
	private Button.OnClickListener  listener;

	public UploadingListAdapter(Context context , List<Map<String, String>> dataList,Button.OnClickListener  listener) {
		this.context = context;
		this.datas = dataList;
		this.listener=listener;
	}

	public void clear() 
	{
		datas.clear();
	}
	
	
	public int getCount() {
		if (datas == null) 
		{
			return 0;
		}
		return datas.size();
	}

	
	public Object getItem(int location) {
		 if(location>=datas.size())
			 return null;
		return datas.get(location);
	}

	 public void removeItem ( int location ) 
	 { 
		 if(location>=datas.size())
			 return;
		 datas. remove (location) ; 
		 this . notifyDataSetChanged( ) ; 
	 }
	 
	 public void addItem(Map<String, String> itemdata)
	 {
		 datas.add(itemdata); 
		 this.notifyDataSetChanged();
	 }
	 
	 public void UpdateProgeress(int location,String progress)
	 {
		 if(location>=datas.size())
			 return;
		 Map<String, String> itemdata=datas.get(location);
		 itemdata.put("progress", progress);
		 datas.set(location, itemdata);
		 this . notifyDataSetChanged( ) ; 
	 }
	 
	 public void UpdateState(int location,String state)
	 {
		 if(location>=datas.size())
			 return;
		 Map<String, String> itemdata=datas.get(location);
		 itemdata.put("fileState", state);
		 datas.set(location, itemdata);
		 this . notifyDataSetChanged( ) ; 
	 }
	 
	 public  void  finishState(int location,String state)
	 {
		 if(location>=datas.size())
			 return;
		 Map<String, String> itemdata=datas.get(location);
		 itemdata.put("finish", state);
		 datas.set(location, itemdata);
		 this . notifyDataSetChanged( ) ; 
	 }
	 /*
	  * 更新暂停的状态
	  */
	 public void updatePauseState(int location,String state)
	 {
		 if(location>=datas.size())
			 return;
		 Map<String, String> itemdata=datas.get(location);
		 itemdata.put("pause", state);
		 datas.set(location, itemdata);
		 this . notifyDataSetChanged( ) ; 
	 }
	    
	
	public long getItemId(int location) {
		return location;
	}

	
	public View getView(int location, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder;
		if(datas == null) {  
			return null;  
		} else if (convertView == null) 
		{
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(this.context).inflate(R.layout.upload_listitem, null);
			viewHolder.fileNameTextView = (TextView) convertView.findViewById(R.id.fileNameTextView);
			viewHolder.fileSizeTextView=(TextView)convertView.findViewById(R.id.fileSizeTextView);
			viewHolder.progressBar = (ProgressBar)convertView.findViewById(R.id.UploadProgressBar);
			viewHolder.uploadStateTextView = (TextView)convertView.findViewById(R.id.uploadStateTextView);
			viewHolder.BtnPause=(Button)convertView.findViewById(R.id.BtnUploadPause);
			viewHolder.BtnCancel=(Button)convertView.findViewById(R.id.BtnUploadCancel);
			viewHolder.BtnContinue=(Button)convertView.findViewById(R.id.BtnUploadContinue);
			viewHolder.BtnSetMeta=(Button)convertView.findViewById(R.id.BtnSetMeta);
			
			viewHolder.progressValueTextView=(TextView)convertView.findViewById(R.id.progressTextView);
			
			convertView.setTag(viewHolder);
		} else 
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.fileNameTextView.setText((CharSequence)datas.get(location).get("fileId"));
		viewHolder.fileSizeTextView.setText((CharSequence)datas.get(location).get("fileSize"));
		
		viewHolder.progressBar.setTag(datas.get(location));
		
		if(datas.get(location).get("progress")!=null)
		{
			viewHolder.progressBar.setProgress((int)Double.parseDouble(datas.get(location).get("progress")));	
			viewHolder.progressValueTextView.setText("当期的进度是："+datas.get(location).get("progress"));
		}
		
		viewHolder.uploadStateTextView.setText((CharSequence)datas.get(location).get("fileState"));
		viewHolder.BtnPause.setTag(datas.get(location));
		viewHolder.BtnPause.setOnClickListener(listener);
		
		if(datas.get(location).get("finish")!=null)
		{
			if(datas.get(location).get("finish").compareTo("1")==0)//完成上传
			{
				viewHolder.BtnPause.setEnabled(false);
				viewHolder.BtnContinue.setEnabled(false);
				viewHolder.BtnCancel.setEnabled(false);
				viewHolder.BtnSetMeta.setEnabled(true);//设置Meta信息
			}else if(datas.get(location).get("finish").compareTo("0")==0)//还没有完成上传,这个时候控制暂停与续传才有意义
			{
				//暂停与续传的互斥控制
				if(datas.get(location).get("pause")!=null)
				{
					if(datas.get(location).get("pause").compareTo("1")==0)//暂停
					{
						viewHolder.BtnSetMeta.setEnabled(false);
						viewHolder.BtnPause.setEnabled(false);
						viewHolder.BtnCancel.setEnabled(true);
						viewHolder.BtnContinue.setEnabled(true);
					}else//不是暂停，续传可以用
					{
						viewHolder.BtnPause.setEnabled(true);
						viewHolder.BtnCancel.setEnabled(true);
						viewHolder.BtnContinue.setEnabled(false);
						viewHolder.BtnSetMeta.setEnabled(false);
					}
				}
			}
		}
		
		viewHolder.BtnCancel.setTag(datas.get(location));
		viewHolder.BtnCancel.setOnClickListener(listener);
		
		viewHolder.BtnContinue.setTag(datas.get(location));
		viewHolder.BtnContinue.setOnClickListener(listener);
		
		viewHolder.BtnSetMeta.setTag(datas.get(location));
		viewHolder.BtnSetMeta.setOnClickListener(listener);
		
		return convertView;
	}

	public static class ViewHolder
	{
		public TextView fileNameTextView;
		public TextView fileSizeTextView;
		public TextView progressValueTextView;
		public ProgressBar  progressBar;
		public TextView  uploadStateTextView;
		public Button   BtnPause;
		public Button   BtnCancel;
		public Button   BtnContinue;
		public Button   BtnSetMeta;
	}
}
