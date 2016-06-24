package com.iqiyi.sdk.android.demo;

import java.io.IOException;


import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class PlaybackActivity extends Activity implements MediaPlayer.OnPreparedListener{

	private MediaPlayer mMediaPlayer;
	
	private SurfaceView mSurfaceView;
	
	private SurfaceHolder mSurfaceHolder;
	
	public static final String KEY_URL = "key_url";
	
	String mUrl = "";
	
	@Override
	public void onPrepared(MediaPlayer player) {
		player.start();
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playback_layout);
	    mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
	    mSurfaceHolder = mSurfaceView.getHolder();
	    mSurfaceHolder.setFixedSize(150, 150);
	    mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    
	    mSurfaceHolder.addCallback(new Callback() {          
            @Override  
            public void surfaceDestroyed(SurfaceHolder holder) {  
          
            }  
          
            @Override  
            public void surfaceCreated(SurfaceHolder holder) {  
            	if (!TextUtils.isEmpty(mUrl)) {
        			performVideoAction(mUrl);
        		}
 
            }             
            @Override  
            public void surfaceChanged(SurfaceHolder holder, int format, int width,  
                    int height) {  
  
            }  
        });
	    
		
		Intent intent = this.getIntent();
		mUrl = intent.getStringExtra(KEY_URL);
		
//		if (!TextUtils.isEmpty(mUrl)) {
//			performVideoAction(mUrl);
//		}
	}
	
	
	private void performVideoAction(String stringUrl) {

		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mMediaPlayer.setDisplay(mSurfaceHolder);
		mMediaPlayer.setOnPreparedListener(this);
		try {
			mMediaPlayer.setDataSource(stringUrl);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mMediaPlayer.prepareAsync();
		// might take long! (for buffering, etc)
//		mMediaPlayer.start();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
