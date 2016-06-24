package com.iqiyi.sdk.android.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.iqiyi.sdk.android.vcop.api.ReturnCode;
import com.iqiyi.sdk.android.vcop.util.VCOPUtil;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class VideoListAdapter  extends BaseAdapter {

	private Context context;
	private List<Map<String, Object>>datas;
	private Button.OnClickListener  listener;

	public VideoListAdapter(Context context , List<Map<String, Object>> dataList,Button.OnClickListener  listener) {
		this.context = context;
		this.datas = dataList;
		this.listener=listener;
	}

	public void clear() 
	{
		datas.clear();
	}
	
	public void setData(List<Map<String, Object>> list) {
	    clear();
	    datas = list;
	    this.notifyDataSetChanged();
	}
	
	
	public int getCount() {
		if (datas == null) 
		{
			return 0;
		}
		return datas.size();
	}

	
	public Object getItem(int location) {
		return datas.get(location);
	}

	 public void removeItem ( int location ) 
	 { 
		 datas. remove (location) ; 
		 this . notifyDataSetChanged( ) ; 
	 } 
	    
	
	public long getItemId(int location) {
		return location;
	}
	
	ArrayList<String> deletIdList = new ArrayList<String>();

	
	public View getView(int location, View convertView, ViewGroup arg2) {
		Log.i("adapter", "location: " + location);
		ViewHolder viewHolder;
		if(datas == null) {  
			return null;  
		} else if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(this.context).inflate(R.layout.listitem, null);
			viewHolder.VideoNameTextView = (TextView) convertView.findViewById(R.id.VideoNameTextView);
			viewHolder.fildIdTextView = (TextView)convertView.findViewById(R.id.fildIdTextView);
			viewHolder.VideoStateTextView = (TextView)convertView.findViewById(R.id.VideoStateTextView);
			viewHolder.VideoDesTextView=(TextView)convertView.findViewById(R.id.VideoUrlTextView);
			viewHolder.BtnDel=(Button)convertView.findViewById(R.id.BtnListDel);
			viewHolder.BtnPlayUrl=(Button)convertView.findViewById(R.id.BtnPlayUrl);
			viewHolder.BtnInfo = (Button)convertView.findViewById(R.id.BtnInfo);
			viewHolder.BtnV2R = (Button) convertView.findViewById(R.id.btn_v2r);
			viewHolder.BtnState = (Button) convertView.findViewById(R.id.btn_state);
			viewHolder.CkBox = (CheckBox) convertView.findViewById(R.id.delete_check_box);
			viewHolder.BtnPlay = (Button) convertView.findViewById(R.id.btn_palyback);
			
			convertView.setTag(viewHolder);
		} else 
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Log.i("adapter", "fileId: " + datas.get(location).get("fileId"));
		viewHolder.VideoNameTextView.setText("视频名称:"+(CharSequence)datas.get(location).get("fileName"));
		viewHolder.fildIdTextView.setText("视频id："+(CharSequence) datas.get(location).get("fileId"));
		viewHolder.VideoStateTextView.setText("视频状态 ："+(CharSequence) datas.get(location).get("videoState"));
		viewHolder.VideoDesTextView.setText("视频的介绍："+(CharSequence) datas.get(location).get("description"));
		
		viewHolder.BtnDel.setTag(datas.get(location));
		
		viewHolder.BtnDel.setOnClickListener(listener);
		
		viewHolder.BtnPlayUrl.setTag(datas.get(location));
		viewHolder.BtnPlayUrl.setOnClickListener(listener);
		
		viewHolder.BtnInfo.setTag(datas.get(location));
		viewHolder.BtnInfo.setOnClickListener(listener);
		
		viewHolder.BtnV2R.setTag(datas.get(location));
		viewHolder.BtnV2R.setOnClickListener(listener);
		
		viewHolder.BtnState.setTag(datas.get(location));
		viewHolder.BtnState.setOnClickListener(listener);
		
		viewHolder.CkBox.setTag(datas.get(location));
		
		viewHolder.BtnPlay.setTag(datas.get(location));
		viewHolder.BtnPlay.setOnClickListener(listener);
		
		Map<String, Object> mmap = datas.get(location);
		if (mmap.containsKey("checked")) {
			String checked = (String) mmap.get("checked");
			if ("true".equals(checked)) {
			    viewHolder.CkBox.setChecked(true);
			} else {
				viewHolder.CkBox.setChecked(false);
			}
		} else {
			viewHolder.CkBox.setChecked(false);
		}
		viewHolder.CkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Map<String, String>  object=(Map<String, String>)buttonView.getTag();//里面的值
				String fileId = object.get("fileId");
				Log.i("adapter", "checked changed: " + fileId);
				if (isChecked) {
					
					object.put("checked", "true");
					deletIdList.add(fileId);
				} else {
					object.put("checked", "false");
					deletIdList.remove(fileId);
				}
				
			}});
		
		return convertView;
	}
	
	public String[] getDeleteIds() {
		String[] ids = null;
	    if (deletIdList!=null && deletIdList.size()>0) {
	    	int size = deletIdList.size();
	    	ids = new String[size];
	    	ids = deletIdList.toArray(ids);
	    }
	    return ids;
	}

	public static class ViewHolder
	{
		public TextView VideoNameTextView;
		public TextView fildIdTextView;
		public TextView VideoStateTextView;
		public TextView VideoDesTextView;
		public Button   BtnDel;
		public Button   BtnPlayUrl;
		public Button   BtnInfo;
		public Button	BtnV2R;
		public Button	BtnState;
		public Button	BtnPlay;
		public CheckBox CkBox;
	}

}
