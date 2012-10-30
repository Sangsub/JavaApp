package com.MemAlloc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;



public class MemAllocService extends Service
{
	private String LOG_TAG = "MemAlloc_Service";
	public static final String ACTION_MEMALLOC_ENABLED = "com.MemAlloc.ACTION_ENABLED";
	public static final String ACTION_MEMALLOC_DISABLED = "com.MemAlloc.ACTION_DISABLED";
	private int arrCount;
	private int arrSize;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Toast.makeText(this, "서비스를 시작", Toast.LENGTH_LONG).show();

		if (intent == null) {
            // Nothing to process, stop.
            Log.e(LOG_TAG, "START_NOT_STICKY - intent is null.");
            return START_NOT_STICKY;
        }		

		String action = intent.getAction();
		
		if( action.equals(ACTION_MEMALLOC_ENABLED))
		{
			
		}
		else if( action.equals(ACTION_MEMALLOC_DISABLED))
		{
		
		}
		
		return Service.START_STICKY;  
	} 

	void createThread()
	{
    	arrCount = Integer.parseInt(etarrCount.getText().toString());
    	arrSize = Integer.parseInt(etarrSize.getText().toString());
		
    	final Double arr[][] = new Double[arrCount][];

        Log.e(LOG_TAG, "[createThread] arrCount : " + arrCount + ", arrSize : " + arrSize);    	
    	
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(!Thread.interrupted())
				{
					for(int i=0; i<arrCount; i++)
					{
						arr[i] = new Double[arrSize];
					}
					SystemClock.sleep(100);
				}
			}
		});
		
		isRunning = true;
		thread.start();
		btnRun.setText("stop");
		
		etarrSize.setEnabled(false);
		etarrCount.setEnabled(false);
	}

	
	void stopThread()
	{
		Log.e(LOG_TAG, "[stopThread] isRunning : " + isRunning);
		btnRun.setClickable(false);
		isRunning = false;
		thread.interrupt();

		while(thread.isAlive())
		{
			SystemClock.sleep(100);
			Log.e(LOG_TAG, "[stopThread] thread is alive");			
		}
		
		btnRun.setClickable(true);
		btnRun.setText("run");
		etarrSize.setEnabled(true);
		etarrCount.setEnabled(true);		
	}
	
	
	public void onDestroy()
	{
		Toast.makeText(this, "서비스를 중지", Toast.LENGTH_LONG).show();
	}
}
