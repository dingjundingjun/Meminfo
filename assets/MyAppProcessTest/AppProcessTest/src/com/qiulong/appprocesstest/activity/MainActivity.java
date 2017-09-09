package com.qiulong.appprocesstest.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Debug;
import android.widget.ListView;
import android.widget.TextView;

import com.jaredrummler.android.processes.ProcessManager;
import com.jaredrummler.android.processes.models.AndroidAppProcess;
import com.qiulong.appprocesslibrary.R;
import com.qiulong.appprocesstest.AppUtils;
import com.qiulong.appprocesstest.adapter.AppAdapter;
import com.qiulong.appprocesstest.entity.AppEntity;

public class MainActivity extends Activity {

	private TextView versionText;
	private ListView listView;
	private AppAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		versionText = (TextView) findViewById(R.id.main_text);
		versionText.setText("当前系统版本：" + android.os.Build.VERSION.RELEASE);
		
		listView = (ListView)findViewById(R.id.main_listview);
		adapter = new AppAdapter(this, getAndroidProcess(this));
		listView.setAdapter(adapter);

	}

	/**
	 * 5.0系统以上获取运行的进程方法
	 * @param context
	 * @param version
	 * @return
	 */
	private List<AppEntity> getAndroidProcess(Context context) {
		List<AppEntity> resule = new ArrayList<AppEntity>();
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = context.getPackageManager();
		AppUtils proutils = new AppUtils(context);
		List<AndroidAppProcess> listInfo = ProcessManager.getRunningAppProcesses();
		if(listInfo.isEmpty() || listInfo.size() == 0){
			return null;
		}
		for (AndroidAppProcess info : listInfo) {
			ApplicationInfo app = proutils.getApplicationInfo(info.name);
			// 过滤自己当前的应用
			if (app == null || context.getPackageName().equals(app.packageName)) {
				continue;
			}
			// 过滤系统的应用
			if ((app.flags & app.FLAG_SYSTEM) > 0) {
				continue;
			}
			AppEntity ent = new AppEntity();
			ent.setAppIcon(app.loadIcon(pm));//应用的图标
			ent.setAppName(app.loadLabel(pm).toString());//应用的名称
			ent.setPackageName(app.packageName);//应用的包名
			// 计算应用所占内存大小
			int[] myMempid = new int[] { info.pid };
			Debug.MemoryInfo[] memoryInfo = am.getProcessMemoryInfo(myMempid);
			double memSize = memoryInfo[0].dalvikPrivateDirty / 1024.0;
			int temp = (int) (memSize * 100);
			memSize = temp / 100.0;
			ent.setMemorySize(memSize);//应用所占内存的大小
			
			resule.add(ent);
		}
		return resule;
	}

}
