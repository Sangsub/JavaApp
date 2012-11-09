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
        		.setIndicator("�޸� �Ҵ�")
        		.setContent(new Intent(this, MemAllocActivity.class)));
        
        tabHost.addTab(tabHost.newTabSpec("VMInfoPage")
        		.setIndicator("���μ��� ����")
        		.setContent(new Intent(this, MemAllocActivity.class)));
        
        tabHost.addTab(tabHost.newTabSpec("PMInfoPage")
        		.setIndicator("�����޸� ����")
        		.setContent(new Intent(this, MemAllocActivity.class)));
    }
}
