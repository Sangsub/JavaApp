package com.MemAlloc;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Debug.MemoryInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MemAllocActivity extends Activity implements OnClickListener
{
	private String LOG_TAG = "MemAlloc_Activity";
	private Button mbtnVMHeaprun;
	private Button mbtnNativeHeaprun;
	private EditText meditArrSize;
	private EditText meditArrCount;
	private TextView mtvVMAllocStatus;
	private TextView mtvNativeAllocStatus;
	private TextView mtvPSSInfo;
	private TextView mtvAppSize;
	private boolean isVMAllocThreadRunning;
	private boolean isNativeAllocThreadRunning;
	private int mVMAllocArrcount;
	private int mVMAllocArrsize;
	private int mNativeAllocArrcount;
	private int mNativeAllocArrsize;
	private boolean isActivityRunning;
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.e(LOG_TAG, "[onPause] ");
		
		isActivityRunning = false;
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
        Log.e(LOG_TAG, "[onStart] ");
	}
	
	private void updateStatus()
	{
		String printBuffer;
		
		Log.e(LOG_TAG, "[updateStatus] isVMAllocThreadRunning : " + isVMAllocThreadRunning);
		Log.e(LOG_TAG, "[updateStatus] isNativeAllocThreadRunning : " + isNativeAllocThreadRunning);
		
		if(isVMAllocThreadRunning)
		{
			printBuffer = "Be allocated in VM \n"
						+ "arrCount : " + mVMAllocArrcount + "\n"
						+ "arrSize : " + mVMAllocArrsize;
			
			mtvVMAllocStatus.setText(printBuffer);
		}
		else
		{
			mtvVMAllocStatus.setText(R.string.vmheap_allocthread_not_working);
		}
		
		if(isNativeAllocThreadRunning)
		{
			printBuffer = "Be allocated in Native \n"
					+ "arrCount : " + mNativeAllocArrcount + "\n"
					+ "arrSize : " + mNativeAllocArrsize;			
		
			mtvNativeAllocStatus.setText(printBuffer);
		}
		else
		{
			mtvNativeAllocStatus.setText(R.string.nativeheap_allocthread_not_working);
		}
	}
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.e(LOG_TAG, "[onCreate] ");		
		setContentView(R.layout.mem_alloc_view);
		
		mbtnVMHeaprun = (Button) findViewById(R.id.btnVMHeaprun);
		mbtnNativeHeaprun = (Button) findViewById(R.id.btnNativeHeaprun);
		meditArrSize = (EditText)findViewById(R.id.etarrSize);
		meditArrCount= (EditText)findViewById(R.id.etarrCount);
		mtvVMAllocStatus = (TextView)findViewById(R.id.vmAllocStatus);
		mtvNativeAllocStatus = (TextView)findViewById(R.id.NativeAllocStatus);
		mtvPSSInfo =(TextView) findViewById(R.id.pssinfo);
		mtvAppSize = (TextView) findViewById(R.id.appsize);
		
		mbtnVMHeaprun.setOnClickListener(this);
		mbtnNativeHeaprun.setOnClickListener(this);
		
		isVMAllocThreadRunning = false;
		isNativeAllocThreadRunning = false;
		Intent intent = new Intent(this, MemAllocService.class);
		startService(intent);
		
		mVMAllocArrcount = mVMAllocArrsize = mNativeAllocArrcount = mNativeAllocArrsize = 0;
		
		updateStatus();

	}

	Handler mHandler = new Handler()
	{
		String str;
		public void handleMessage(Message msg)
		{
			Log.e(LOG_TAG, "[handleMessage] msg : " + msg.what + ", msg.arg1" + msg.arg1);			
			if(msg.what == 0)
			{
				int pssSize = msg.arg1;
				Log.e(LOG_TAG, "[handleMessage] pssSize : " + pssSize + ", appSize" + pssSize/1024);
				
				str = getString(R.string.pss_size) + " " +msg.arg1;
				mtvPSSInfo.setText(str);
				str = getString(R.string.pss_size) + " " + msg.arg1/1024 + " MB";
				mtvAppSize.setText(str);
				
				if(isActivityRunning)
				{
					Message tmsg = Message.obtain(mHandler, 0, updateAppSize(), 0);
					mHandler.sendMessageDelayed(tmsg, 1000);
				}
			}
		}
	};
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
        Log.e(LOG_TAG, "[onResume] ");		
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(MemAllocService.ACTION_VMALLOC_STARTED);
		filter.addAction(MemAllocService.ACTION_VMALLOC_STOPPED);
		filter.addAction(MemAllocService.ACTION_NATIVEALLOC_STARTED);
		filter.addAction(MemAllocService.ACTION_NATIVEALLOC_STOPPED);
		filter.addAction(Intent.ACTION_HEADSET_PLUG);		
		registerReceiver(mMemAllocListener, filter);
		
		mtvVMAllocStatus.setText(R.string.vmheap_allocthread_not_working);
		mtvNativeAllocStatus.setText(R.string.nativeheap_allocthread_not_working);
		
		updateStatus();
		
		isActivityRunning = true;
		Message msg = Message.obtain(mHandler, 0, updateAppSize(), 0);
		mHandler.sendMessage(msg);	
	}
	
	
	private int updateAppSize()
	{
		int [] pid = new int[1];
		
		pid[0] = memAllocUtil.findPIDByString(this, "com.MemAlloc");
		Log.e(LOG_TAG, "[updateAppSize] pid : " + pid[0]);
		
		MemoryInfo info = memAllocUtil.getProcessMemoryInfo(this, pid);
		
		if(info != null && pid[0] != -1)
		{
			Log.e(LOG_TAG , "private : " + ""+ info.getTotalPrivateDirty());
			Log.e(LOG_TAG , "PSS : " + ""+ info.getTotalPss());
			Log.e(LOG_TAG , "Shared : " + ""+ info.getTotalSharedDirty());

			return info.getTotalPss();
		}
		return 0;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
        if(v == mbtnVMHeaprun)
        {
        	if(isVMAllocThreadRunning == false)
        	{
        		mVMAllocArrcount = Integer.parseInt(meditArrCount.getText().toString());
        		mVMAllocArrsize = Integer.parseInt(meditArrSize.getText().toString());
        		
        		Intent intent = new Intent(MemAllocService.ACTION_VMALLOC_START);
            	intent.putExtra("arrSize", mVMAllocArrsize);
            	intent.putExtra("arrCount", mVMAllocArrcount);
            	sendBroadcast(intent);
        	}
        	else
        	{
        		mVMAllocArrcount = mVMAllocArrsize = 0;        		
        		sendBroadcast(new Intent(MemAllocService.ACTION_VMALLOC_STOP));
        	}
        	
        	mbtnVMHeaprun.setClickable(false);
        }
        else if(v == mbtnNativeHeaprun)
        {
        	if(isNativeAllocThreadRunning == false)
        	{
        		mNativeAllocArrcount = Integer.parseInt(meditArrCount.getText().toString());
        		mNativeAllocArrsize = Integer.parseInt(meditArrSize.getText().toString());        		
        		
        		Intent intent = new Intent(MemAllocService.ACTION_NATIVEALLOC_START);
            	intent.putExtra("arrSize", mNativeAllocArrsize);
            	intent.putExtra("arrCount", mNativeAllocArrcount);
            	sendBroadcast(intent);        		
        	}
        	else
        	{
        		mNativeAllocArrsize = mNativeAllocArrcount = 0;
            	sendBroadcast(new Intent(MemAllocService.ACTION_NATIVEALLOC_STOP));
        	}
        	
        	mbtnNativeHeaprun.setClickable(false);
        }
	}
	
    private BroadcastReceiver mMemAllocListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();

            Log.e(LOG_TAG, "[onReceive] " + action);            
            
            if (action.equals(MemAllocService.ACTION_VMALLOC_STARTED))
            {
            	mbtnVMHeaprun.setText(R.string.vmheap_alloc_stop);
            	mbtnVMHeaprun.setClickable(true);
        		isVMAllocThreadRunning = true;
            }
            else if (action.equals(MemAllocService.ACTION_VMALLOC_STOPPED))
            {
            	mbtnVMHeaprun.setText(R.string.vmheap_alloc_start);            	
            	mbtnVMHeaprun.setClickable(true);
        		isVMAllocThreadRunning = false;
            }
            else if(action.equals(MemAllocService.ACTION_NATIVEALLOC_STARTED))
            {
            	mbtnNativeHeaprun.setText(R.string.nativeheap_alloc_stop);
            	mbtnNativeHeaprun.setClickable(true);
        		isNativeAllocThreadRunning = true;            	
            }
            else if(action.equals(MemAllocService.ACTION_NATIVEALLOC_STOPPED))
            {
            	mbtnNativeHeaprun.setText(R.string.nativeheap_alloc_start);            	
            	mbtnNativeHeaprun.setClickable(true);
        		isNativeAllocThreadRunning = false;
            }
            
            if(isVMAllocThreadRunning == true && isNativeAllocThreadRunning == true)
            {
        		meditArrSize.setEnabled(false);
        		meditArrCount.setEnabled(false);            	
            }
            else
            {
            	meditArrSize.setEnabled(true);
        		meditArrCount.setEnabled(true);
            }

            updateStatus();
        }
    };
	
	protected void onStop()
	{
		super.onStop();
        Log.e(LOG_TAG, "[onStop] ");
        unregisterReceiver(mMemAllocListener);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		isVMAllocThreadRunning = false;
		isNativeAllocThreadRunning = false;
		Intent intent = new Intent(this, MemAllocService.class);        		
	 	stopService(intent);
	}
}