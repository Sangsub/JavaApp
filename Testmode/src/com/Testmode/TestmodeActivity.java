package com.Testmode;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class TestmodeActivity extends Activity implements OnClickListener {
	
	private Button mbtn;
	private boolean isStarted;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mbtn = (Button) findViewById(R.id.btn);
        mbtn.setOnClickListener(this);
    }

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
        if(v == mbtn)
        {
        	isStarted = isRunningProcess(this, "com.MemAlloc");
        	Toast.makeText(this, "" + isStarted, Toast.LENGTH_LONG).show(); 
        	
        	if(isStarted)
        	{
            	Intent intent = new Intent();
            	intent.setAction("com.MemAlloc.StartThread");
            	intent.putExtra("arrSize", 1024);
            	intent.putExtra("arrCount", 10000);
            	sendBroadcast(intent);
        	}
        	
        }
	}
	
    /**
     * Process가 실행중인지 여부 확인.
     * @param context, packageName      
     * @return true/false      
     */
	public static boolean isRunningProcess(Context context, String packageName)
	{
		boolean isRunning = false;
		
		ActivityManager actMng = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> list = actMng.getRunningAppProcesses();
		for(RunningAppProcessInfo rap : list)
		{
			if(rap.processName.equals(packageName))                                           
			{
				isRunning = true;
				break;
			}
		}
		return isRunning;
	} 
}