package com.MemAlloc;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

public class MemAllocAppActivity extends TabActivity{
	@Override
	public void onContentChanged() {
		// TODO Auto-generated method stub
		super.onContentChanged();
		
		Log.e(LOG_TAG, "[onContentChanged] : ");		
	}


	private String LOG_TAG = "MemAllocAppActivity";	
	

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
		Log.e(LOG_TAG, "[onCreate] : ");		
        TabHost tabHost = getTabHost();
        
        tabHost.addTab(tabHost.newTabSpec("AllocPage")
        		.setIndicator("메모리 할당")
        		.setContent(new Intent(this, MemAllocActivity.class)));
        
        tabHost.addTab(tabHost.newTabSpec("VMInfoPage")
        		.setIndicator("프로세스 정보")
        		.setContent(new Intent(this, MemAllocActivity.class)));
        
        tabHost.addTab(tabHost.newTabSpec("PMInfoPage")
        		.setIndicator("물리메모리 정보")
        		.setContent(new Intent(this, MemAllocActivity.class)));
    }
}
