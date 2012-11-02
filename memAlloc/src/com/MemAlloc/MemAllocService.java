package com.MemAlloc;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class MemAllocService extends Service
{
	private String LOG_TAG = "MemAlloc_Service";
	private int arrCount;
	private int arrSize;
	private Thread thread;
	private boolean isThreadRunning;
	
	public void onCreate()
	{
		super.onCreate();
		Log.e(LOG_TAG, "[onCreate]");
		
		isThreadRunning = false;
		arrCount = 0;
		arrSize  = 0;
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_HEADSET_PLUG);
		filter.addAction("com.MemAlloc.StartThread");
		filter.addAction("com.MemAlloc.StopThread");
		registerReceiver(mMemAllocService_KeyListener, filter);
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		
		Log.e(LOG_TAG, "[onStart]");		
	}
			
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Log.e(LOG_TAG, "[onBind]");		
		
		return null;
	}

	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Log.e(LOG_TAG, "[onStartCommand]");		
		Toast.makeText(this, "서비스를 시작", Toast.LENGTH_LONG).show();

		if (intent == null) {
            // Nothing to process, stop.
            Log.e(LOG_TAG, "START_NOT_STICKY - intent is null.");
            return START_NOT_STICKY;
        }		

        		
		return Service.START_STICKY;  
	} 

	void createThread()
	{
		Log.e(LOG_TAG, "[createThread]");	
    	final Double arr[][] = new Double[arrCount][];
    	
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
	}

	void startThread()
	{
		Log.e(LOG_TAG, "[startThread] prev isThreadRunning : " + isThreadRunning);		
		isThreadRunning = true;
		thread.start();
		
		Intent intent = new Intent();
		intent.setAction("com.MemAlloc.ThreadStarted");
		sendBroadcast(intent);
	}

	void stopThread()
	{
		Log.e(LOG_TAG, "[stopThread] prev isThreadRunning : " + isThreadRunning);

		thread.interrupt();

		while(thread.isAlive())
		{
			SystemClock.sleep(100);
			Log.e(LOG_TAG, "[stopThread] thread is alive");			
		}

		isThreadRunning = false;

		Intent intent = new Intent();
		intent.setAction("com.MemAlloc.ThreadStopped");
		sendBroadcast(intent);
	}
	
	public void onDestroy()
	{
		if(isThreadRunning)
			stopThread();
		
		Log.e(LOG_TAG, "[onDestroy] prev isThreadRunning : " + isThreadRunning);
		Toast.makeText(this, "서비스를 중지", Toast.LENGTH_LONG).show();
		unregisterReceiver(mMemAllocService_KeyListener);
	}
	
    private BroadcastReceiver mMemAllocService_KeyListener = new BroadcastReceiver() {
    	
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            Toast.makeText(context, action, Toast.LENGTH_LONG).show();
            Log.e(LOG_TAG, "[onReceive] " + action);            
            
            if(action.equals(Intent.ACTION_HEADSET_PLUG))
            {
            	
            }
            else if(action.equals("com.MemAlloc.StartThread"))
            {
        		arrCount = intent.getIntExtra("arrCount", 1);
        		arrSize = intent.getIntExtra("arrSize", 1);

                Log.e(LOG_TAG, "[createThread] arrCount : " + arrCount + ", arrSize : " + arrSize);
                
                createThread();
                startThread();
            }
            else if(action.equals("com.MemAlloc.StopThread"))
            {
        		stopThread();
            }
        }
    };

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}



}
