package com.MemAlloc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Debug.MemoryInfo;
import android.util.Log;

public class memAllocUtil
{
	private static String LOG_TAG = "memAllocUtil";
	
	static public int findPIDByString(Context context, String str)
	{
		Log.e(LOG_TAG, "[memInfoThread]");		

		ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> list = am.getRunningAppProcesses();
		
		for(RunningAppProcessInfo process : list)
		{
			if( process.processName.equals(str))
			{ 
				Log.e(LOG_TAG, "processName : " + process.processName + ", pid : " + process.pid); 
				return process.pid;
			}
		}
	
		return 0;
	}
	
	static MemoryInfo getProcessMemoryInfo(Context context, int [] pid)
	{
		Log.e(LOG_TAG, "getProcessMemoryInfo : ");
		ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
		
		return am.getProcessMemoryInfo(pid)[0];
	}
	
	static BufferedReader getDumpsysMeminfo(int pid)
	{
		Process process;
		BufferedReader in = null;
		String command = String.format("dumpsys meminfo %s", pid);
		
		Log.e(LOG_TAG, "getDumpsysMeminfo : ");
		
		try {
			process = Runtime.getRuntime().exec(command);
			in = new BufferedReader(new InputStreamReader(process.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return in; 

	}
}
