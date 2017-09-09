package com.example.memtest;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
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
//				String pid = getProcId("com.example.demo");
//				if(pid != null) {
					try{
//						getAllProcess();
						execRootCmd("ps");
//					    execCommand("ps");
					}catch(Exception e) {
						
					}
//				}
			}
		});
    }

//	private List<AppEntity> getAndroidProcess(Context context) {  
//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);  
//        PackageManager pm = context.getPackageManager();  
//        List<AndroidAppProcess> listInfo = ProcessManager.getRunningAppProcesses();  
//        if(listInfo.isEmpty() || listInfo.size() == 0){  
//            return null;  
//        }  
//        for (AndroidAppProcess info : listInfo) {  
//            ApplicationInfo app = proutils.getApplicationInfo(info.name);  
//            // 过滤自己当前的应用  
//            if (app == null || context.getPackageName().equals(app.packageName)) {  
//                continue;  
//            }  
//            // 过滤系统的应用  
//            if ((app.flags & app.FLAG_SYSTEM) > 0) {  
//                continue;  
//            }  
//            AppEntity ent = new AppEntity();  
//            ent.setAppIcon(app.loadIcon(pm));//应用的图标  
//            ent.setAppName(app.loadLabel(pm).toString());//应用的名称  
//            ent.setPackageName(app.packageName);//应用的包名  
//            // 计算应用所占内存大小  
//            int[] myMempid = new int[] { info.pid };  
//            Debug.MemoryInfo[] memoryInfo = am.getProcessMemoryInfo(myMempid);  
//            double memSize = memoryInfo[0].dalvikPrivateDirty / 1024.0;  
//            int temp = (int) (memSize * 100);  
//            memSize = temp / 100.0;  
//            ent.setMemorySize(memSize);//应用所占内存的大小  
//              
//            resule.add(ent);  
//        }  
//        return resule;  
//    }  
	
   private String getProcId(String packageName) {
	   ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
	   PackageManager packageManager = getPackageManager();
	   List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
	   Log.d("DJ_MEM","" + appProcesses.size());
	   for (ActivityManager.RunningAppProcessInfo runprocessInfo : appProcesses) {
		   try {
			   String processName = runprocessInfo.processName;
	           PackageInfo packageInfo = packageManager.getPackageInfo(processName, 0);
//	           Drawable icon = packageInfo.applicationInfo.loadIcon(packageManager);
	           //获取到app的名字
	           String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
	           Log.d("DJ_MEM","processName = " + processName + " packageName = " + packageName + " packageInfo.packageName = " + packageInfo.packageName);
	           if(packageName.equals(packageInfo.packageName)) {
	        	   return "" + runprocessInfo.pid;
	           }
		   } catch(Exception e) {
			   e.printStackTrace();
		   }
	   }
	   return null;
   }
   
   public void execCommand(String command) throws IOException {  
	    // start the ls command running  
	    //String[] args =  new String[]{"sh", "-c", command};  
	    Runtime runtime = Runtime.getRuntime();    
	    Process proc = runtime.exec(command);        //这句话就是shell与高级语言间的调用  
	    Log.d("DJ_MEM","1111111111111");
	        //如果有参数的话可以用另外一个被重载的exec方法  
	        //实际上这样执行时启动了一个子进程,它没有父进程的控制台  
	        //也就看不到输出,所以我们需要用输出流来得到shell执行后的输出  
	        InputStream inputstream = proc.getInputStream();  
	        InputStreamReader inputstreamreader = new InputStreamReader(inputstream);  
	        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);  
	        // read the ls output  
	        String line = "";  
	        StringBuilder sb = new StringBuilder(line);  
	        Log.d("DJ_MEM","2222222222222");
	        while ((line = bufferedreader.readLine()) != null) {  
	            //System.out.println(line);  
	        	Log.d("DJ_MEM","line = " + line);
	                sb.append(line);  
	                sb.append('\n');  
	        }  
	        mShowResult.setText(sb.toString());
	        //tv.setText(sb.toString());  
	        //使用exec执行不会等执行成功以后才返回,它会立即返回  
	        //所以在某些情况下是很要命的(比如复制文件的时候)  
	        //使用wairFor()可以等待命令执行完成以后才返回  
	        try {  
	            if (proc.waitFor() != 0) {  
	                System.err.println("exit value = " + proc.exitValue());  
	            }  
	        }  
	        catch (InterruptedException e) {    
	            System.err.println(e);  
	        }  
	    }  
   
   private void getAllProcess() {
	   try {
	   Process p = Runtime.getRuntime().exec("su");
	   }catch(Exception e) {
		   e.printStackTrace();
	   }
	   String path = "/proc";
	   File file = new File(path);
	   if(!file.isDirectory()) {
		   return;
	   }
	   String string[] = file.list();
	   int length = string.length;
	   StringBuilder sb = new StringBuilder();
	   for(int i = 0;i < length;i++) {
		   String procStr = string[i];
		   sb.append(procStr);
		   sb.append("\n");
	   }
	   mShowResult.setText(sb.toString());
   }
   
   public String execRootCmd(String cmd) { 
       String result = ""; 
       DataOutputStream dos = null; 
       DataInputStream dis = null; 
        
       try { 
    	   Log.i("DJ_MEM", "111111"); 
           Process p = Runtime.getRuntime().exec("su");// 经过Root处理的android系统即有su命令 
           Log.i("DJ_MEM", "222222"); 
           dos = new DataOutputStream(p.getOutputStream()); 
           dis = new DataInputStream(p.getInputStream()); 

           Log.i("DJ_MEM", cmd); 
           dos.writeBytes(cmd + "\n"); 
           dos.flush(); 
           dos.writeBytes("exit\n"); 
           dos.flush(); 
           String line = null; 
           while ((line = dis.readLine()) != null) { 
               Log.d("result", line); 
               result += line; 
           } 
           mShowResult.setText(result);
           p.waitFor(); 
       } catch (Exception e) { 
           e.printStackTrace(); 
       } finally { 
           if (dos != null) { 
               try { 
                   dos.close(); 
               } catch (IOException e) { 
                   e.printStackTrace(); 
               } 
           } 
           if (dis != null) { 
               try { 
                   dis.close(); 
               } catch (IOException e) { 
                   e.printStackTrace(); 
               } 
           } 
       } 
       return result; 
   } 
}