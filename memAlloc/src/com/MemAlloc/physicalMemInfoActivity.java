package com.MemAlloc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class physicalMemInfoActivity extends Activity 
{
	private String LOG_TAG = "physicalMemInfoActivity";
	private TextView mTvBuddyInfo;
	private TextView mTvMemInfo;
	private boolean mPhysicalViewStarted;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Log.e(LOG_TAG, "[onCreate] ");
		setContentView(R.layout.physical_meminfo_view);
		
		mTvBuddyInfo = (TextView) findViewById(R.id.tvBuddyInfo);
		mTvMemInfo = (TextView) findViewById(R.id.tvMemInfo);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		Log.e(LOG_TAG, "[onDestroy] ");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		Log.e(LOG_TAG, "[onPause] ");
		mPhysicalViewStarted = false;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Log.e(LOG_TAG, "[onResume] ");
		
		mPhysicalViewStarted = true;
		
		mHandler.sendEmptyMessageDelayed(0, 1000);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		Log.e(LOG_TAG, "[onStart] ");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
		Log.e(LOG_TAG, "[onStop] ");
	}
	
	
	Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			Log.e(LOG_TAG, "[handleMessage] msg : " + msg.what + ", msg.arg1" + msg.arg1);			
			if(msg.what == 0)
			{
				updateBuddyInfo();
				updateMeminfo();
				
				if(mPhysicalViewStarted)
				{
					this.sendEmptyMessageDelayed(0, 1000);
				}
			}
		}
	};
	
	private void updateMeminfo()
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader("/proc/meminfo"), 8192);
			String s;
			
			Log.e(LOG_TAG, "[meminfo] ");
			mTvMemInfo.setText("");
			
			while((s=in.readLine())!=null)
			{
		        Log.e(LOG_TAG, "[meminfo 2] " + s);				
		        mTvMemInfo.append(s+"\n");
			}
		}
		catch(IOException e)
		{}
		catch(NumberFormatException e)
		{}
	}
	
	
	private void updateBuddyInfo()
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader("/proc/buddyinfo"), 8192);
			String s;
			
			Log.e(LOG_TAG, "[buddyinfo] ");			
			mTvBuddyInfo.setText("");
			
			while((s=in.readLine())!=null)
			{
		        Log.e(LOG_TAG, "[buddyinfo 2] " + s);				
		        mTvBuddyInfo.append(s+"\n");
			}
		}
		catch(IOException e)
		{}
		catch(NumberFormatException e)
		{}
	}
}
