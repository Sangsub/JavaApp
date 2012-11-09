package com.MemAlloc;

import android.content.Context;
import android.os.Debug.MemoryInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class memInfoThread extends Thread
{
	private int [] m_pid;
	private MemoryInfo info;
	private Handler m_handler;
	private boolean mThreadRunning; 
	private Context m_context;
	private String LOG_TAG = "Meminfo_Thread";

	
	public memInfoThread(Handler handler, Context context)
	{
		Log.e(LOG_TAG, "[memInfoThread]");		

		m_context = context;
		m_handler = handler;
		m_pid = new int[1];
		
		m_pid[0] = memAllocUtil.findPIDByString(context, "com.MemAlloc");
		
		Log.e(LOG_TAG, "[memInfoThread] pid : " + m_pid[0]);

		info = memAllocUtil.getProcessMemoryInfo(context, m_pid);
        mThreadRunning = false;
	}
	
	private int getPSSInfo()
	{
		info = memAllocUtil.getProcessMemoryInfo(m_context, m_pid);
		
		if(info != null && m_pid[0] != -1)
		{
			Log.e(LOG_TAG , "private : " + ""+ info.getTotalPrivateDirty());
			Log.e(LOG_TAG , "PSS : " + ""+ info.getTotalPss());
			Log.e(LOG_TAG , "Shared : " + ""+ info.getTotalSharedDirty());

			return info.getTotalPss();
		}			
		return 0;
	}
	
	/**
	 * Pause this thread
	 */
    public void onStop()
    {
    	Log.e(LOG_TAG, "[onStop] onStop");    	
    	//mPaused = true;
    	this.interrupt();
    }
	
    
    public boolean isThreadRunning()
    {
    	Log.e(LOG_TAG, "[isThreadRunning] " + mThreadRunning);
    	return mThreadRunning;
    }
    
	@Override
	public void run()
	{
		Log.e(LOG_TAG, "[memInfoThread] run");
		int pssSize=0;
		mThreadRunning = true;
		//while(!mPaused)
		while(memInfoThread.interrupted())
		{
			pssSize = getPSSInfo();
			if(pssSize != 0)
			{
				Message msg = Message.obtain(m_handler, 0, pssSize, 0);
				m_handler.sendMessage(msg);	
			}
			
			try
			{
				Thread.sleep(1000);
			}
			catch(InterruptedException e)
			{
				 Log.e(LOG_TAG , "error : " + e);
			}
		}
		
		mThreadRunning = false;		
	}
}
