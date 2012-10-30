package com.MemAlloc;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MemAllocActivity extends Activity implements OnClickListener {
	
	private String LOG_TAG = "MemAlloc_Activity";
	private Button btnRun;
	private EditText etarrSize;
	private EditText etarrCount;
	private boolean isRunning;
	private Thread thread; 
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		btnRun = (Button) findViewById(R.id.btnrun);
		etarrSize = (EditText)findViewById(R.id.etarrSize);
		etarrCount= (EditText)findViewById(R.id.etarrCount);	
		
		btnRun.setOnClickListener(this);
	}
	
    public boolean dispatchKeyEvent(KeyEvent event) {
        
    	switch (event.getKeyCode()) {
    	case KeyEvent.KEYCODE_VOLUME_UP:
            Log.e(LOG_TAG, "[dispatchKeyEvent] KEYCODE_VOLUME_UP : ");
        	if(isRunning == false)
        	{
        		createThread();
        	}
    		break;
    	case KeyEvent.KEYCODE_VOLUME_DOWN:
            Log.e(LOG_TAG, "[dispatchKeyEvent] KEYCODE_VOLUME_DOWN : ");
            stopThread();
    		break;
    	}
    	return super.dispatchKeyEvent(event);
    }
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
        Log.e(LOG_TAG, "Clicked button isRunning : " + isRunning);		
		
        if(v == btnRun) {
        	if(isRunning == false)
        	{
        		createThread();
        	}
        	else
        	{
        		stopThread();
        	}
        }
	}


	
	protected void onStop()
	{
		super.onStop();
		isRunning = false;
		btnRun.setText("run");
	}

}