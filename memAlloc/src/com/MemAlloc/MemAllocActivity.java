package com.MemAlloc;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MemAllocActivity extends Activity implements OnClickListener {
	
	private String LOG_TAG = "MemAlloc_Activity";
	private Button btnRun;
	private EditText etarrSize;
	private EditText etarrCount;
	private boolean isServiceRunning;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		btnRun = (Button) findViewById(R.id.btnrun);
		etarrSize = (EditText)findViewById(R.id.etarrSize);
		etarrCount= (EditText)findViewById(R.id.etarrCount);
		
		btnRun.setOnClickListener(this);
		
		isServiceRunning = false;
		Intent intent = new Intent(this, MemAllocService.class);
		startService(intent);
	}
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Toast.makeText(this, "onResume", Toast.LENGTH_LONG).show();
		
        Log.e(LOG_TAG, "[onResume] ");		
		
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.MemAlloc.ThreadStarted");
		filter.addAction("com.MemAlloc.ThreadStopped");
		filter.addAction(Intent.ACTION_HEADSET_PLUG);		
		registerReceiver(mMemAllocListener, filter);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
        Log.e(LOG_TAG, "Clicked button isRunning : " + isServiceRunning);		
		
        if(v == btnRun) {
        	if(isServiceRunning == false)
        	{
            	Intent intent = new Intent();
            	intent.setAction("com.MemAlloc.StartThread");
            	intent.putExtra("arrSize", Integer.parseInt(etarrSize.getText().toString()));
            	intent.putExtra("arrCount", Integer.parseInt(etarrCount.getText().toString()));
            	sendBroadcast(intent);
        	}
        	else
        	{
            	Intent intent = new Intent();
            	intent.setAction("com.MemAlloc.StopThread");
            	sendBroadcast(intent);        		
        	}
        	
        	btnRun.setClickable(false);
        }
	}
	
    private BroadcastReceiver mMemAllocListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();

            Log.e(LOG_TAG, "[onReceive] " + action);            
            
            if (action.equals("com.MemAlloc.ThreadStarted"))
            {
            	btnRun.setText("Stop");
            	btnRun.setClickable(true);
        		etarrSize.setEnabled(false);
        		etarrCount.setEnabled(false);
        		isServiceRunning = true;
            }
            else if (action.equals("com.MemAlloc.ThreadStopped"))
            {
            	btnRun.setText("Start");
            	btnRun.setClickable(true);
        		etarrSize.setEnabled(true);
        		etarrCount.setEnabled(true);       
        		isServiceRunning = false;
            }
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
		
		isServiceRunning = false;
		Intent intent = new Intent(this, MemAllocService.class);        		
	 	stopService(intent);
	}
}