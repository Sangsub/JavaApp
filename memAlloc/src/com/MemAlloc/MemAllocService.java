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
	private int mVMarrCount;
	private int mVMarrSize;
	private int mNativearrCount;
	private int mNativearrSize;	
	private Thread mVMAllocThread;
	private Thread mNativeAllocThread;
	private boolean isVMAllocThreadRunning;
	
    public static final String ACTION_VMALLOC_START = "com.MemAlloc.VMAllocStartThread";
    public static final String ACTION_VMALLOC_STOP = "com.MemAlloc.VMAllocStopThread";
    public static final String ACTION_NATIVEALLOC_START = "com.MemAlloc.NativeAllocStartThread";
    public static final String ACTION_NATIVEALLOC_STOP = "com.MemAlloc.NativeAllocStopThread";
    public static final String ACTION_VMALLOC_STARTED = "com.MemAlloc.VMAllocStartedThread";
    public static final String ACTION_VMALLOC_STOPPED = "com.MemAlloc.VMAllocStoppedThread";
    public static final String ACTION_NATIVEALLOC_STARTED = "com.MemAlloc.NativeAllocStartedThread";
    public static final String ACTION_NATIVEALLOC_STOPPED = "com.MemAlloc.NativeAllocStoppedThread";
    
    public native String stringFromJNI();
    public native void NativeMemAllocStopFromJNI();
    public native boolean NativeMemAllocStartFromJNI(int arrCount, int arrSize);
    
    static
    {
    	System.loadLibrary("memAlloc-jni");
    }

	public void onCreate()
	{
		super.onCreate();
		Log.e(LOG_TAG, "[onCreate]");
		
		isVMAllocThreadRunning = false;
		mVMarrCount = mVMarrSize = mNativearrCount = mNativearrSize = 0;
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_HEADSET_PLUG);
		filter.addAction(ACTION_VMALLOC_START);
		filter.addAction(ACTION_VMALLOC_STOP);
		filter.addAction(ACTION_NATIVEALLOC_START);		
		filter.addAction(ACTION_NATIVEALLOC_STOP);		
		registerReceiver(mMemVMAllocService_KeyListener, filter);
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

	void createVMAllocThread()
	{
		Log.e(LOG_TAG, "[createVMAllocThread]");	
    	final Double arr[][] = new Double[mVMarrCount][];
    	
    	mVMAllocThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(!Thread.interrupted())
				{
					for(int i=0; i<mVMarrCount; i++)
					{
						arr[i] = new Double[mVMarrSize];
					}
					SystemClock.sleep(100);
				}
				
				Log.e(LOG_TAG, "[createVMAllocThread] Loop compleate");				
			}
		});
	}

	void startVMAllocThread()
	{
		Log.e(LOG_TAG, "[startVMThread] prev isVMAllocThreadRunning : " + isVMAllocThreadRunning);		
		isVMAllocThreadRunning = true;
		mVMAllocThread.start();

		sendBroadcast(new Intent(ACTION_VMALLOC_STARTED));
	}

	void stopVMAllocThread()
	{
		Log.e(LOG_TAG, "[stopVMThread] prev isVMAllocThreadRunning : " + isVMAllocThreadRunning);

		mVMAllocThread.interrupt();

		while(mVMAllocThread.isAlive())
		{
			SystemClock.sleep(100);
			Log.e(LOG_TAG, "[stopThread] thread is alive");			
		}

		isVMAllocThreadRunning = false;

		Intent intent = new Intent();
		intent.setAction(ACTION_VMALLOC_STOPPED);
		sendBroadcast(intent);
	}
	
	public void onDestroy()
	{
		if(isVMAllocThreadRunning)
			stopVMAllocThread();
		
		Log.e(LOG_TAG, "[onDestroy] prev isVMAllocThreadRunning : " + isVMAllocThreadRunning);
		Toast.makeText(this, "서비스를 중지", Toast.LENGTH_LONG).show();
		unregisterReceiver(mMemVMAllocService_KeyListener);
	}
	
    private BroadcastReceiver mMemVMAllocService_KeyListener = new BroadcastReceiver() {
    	
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            Toast.makeText(context, action, Toast.LENGTH_LONG).show();
            Log.e(LOG_TAG, "[onReceive] " + action);            
            
            if(action.equals(Intent.ACTION_HEADSET_PLUG))
            {
            	
            }
            else if(action.equals(ACTION_VMALLOC_START))
            {
            	mVMarrCount = intent.getIntExtra("arrCount", 1);
            	mVMarrSize = intent.getIntExtra("arrSize", 1);

                Log.e(LOG_TAG, "[createVMThread] mVMarrCount : " + mVMarrCount + ", mVMarrSize : " + mVMarrSize);
                
                createVMAllocThread();
                startVMAllocThread();
            }
            else if(action.equals(ACTION_VMALLOC_STOP))
            {
            	stopVMAllocThread();
            	
            	mVMarrCount = mVMarrSize = 0;            	
            }
            else if(action.equals(ACTION_NATIVEALLOC_START))
            {
            	mNativearrCount = intent.getIntExtra("arrCount", 1);
            	mNativearrSize = intent.getIntExtra("arrSize", 1);
            	
            	createNativeAllocThread();
            	startNativeAllocThread();
            }
            else if(action.equals(ACTION_NATIVEALLOC_STOP))
            {
            	mNativearrCount = mNativearrSize = 0;            	
            }
        }
    };
    
	void createNativeAllocThread()
	{
		Log.e(LOG_TAG, "[createNativeAllocThread]");	
		
		mNativeAllocThread = new Thread(new Runnable() {
			@Override
			public void run() {
            	boolean result = false;

            	Log.e(LOG_TAG, "[ACTION_NATIVEALLOC_START] mNativearrCount : " + mNativearrCount + ", mNativearrSize : " + mNativearrSize);
            	
            	// call native method : native memory allocation start
            	result = NativeMemAllocStartFromJNI(mNativearrCount, mNativearrSize);
            	
            	Log.e(LOG_TAG, "[ACTION_NATIVEALLOC_Result] result : " + result);
            	
            	sendBroadcast(new Intent(ACTION_NATIVEALLOC_STOPPED));
			}
		});
	}
 
	void startNativeAllocThread()
	{
		Log.e(LOG_TAG, "[startNativeThread] prev isVMAllocThreadRunning : " + isVMAllocThreadRunning);		
		mNativeAllocThread.start();
		sendBroadcast(new Intent(ACTION_NATIVEALLOC_STARTED));
	}
	
    private void postEventFromNative(int what)
    {
    	Log.e(LOG_TAG, "[postEventFromNative] what : " + what);
    }
	
	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		
		Log.e(LOG_TAG, "[onLowMemory]");
	}
}