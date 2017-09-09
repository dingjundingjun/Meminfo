package com.example.memtest;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {
	public native String  getMemInfo(String start,String end,String proc);
	private Button mBtnGetMeminfo;
	private TextView mShowResult;
	private String mStrResult;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mShowResult = (TextView)findViewById(R.id.show_result);
        mBtnGetMeminfo = (Button)findViewById(R.id.btn_get_meminfo);
        mBtnGetMeminfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String pid = getProcId("com.example.demo");
				if(pid != null) {
					mStrResult = "PID:" + pid + "\n";
				    getMemInfo("","",pid);
				}
			}
		});
    }

   private String getProcId(String packageName) {
	   ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
	   PackageManager packageManager = getPackageManager();
	   List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
	   for (ActivityManager.RunningAppProcessInfo runprocessInfo : appProcesses) {
		   try {
			   String processName = runprocessInfo.processName;
	           PackageInfo packageInfo = packageManager.getPackageInfo(processName, 0);
//	           Drawable icon = packageInfo.applicationInfo.loadIcon(packageManager);
	           //获取到app的名字
	           String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
	           if(packageInfo.equals(packageInfo.packageName)) {
	        	   return "" + runprocessInfo.pid;
	           }
		   } catch(Exception e) {
			   e.printStackTrace();
		   }
	   }
	   return null;
   }
}