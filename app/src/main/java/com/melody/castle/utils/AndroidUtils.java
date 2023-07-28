package com.melody.castle.utils;import android.app.Activity;import android.app.ActivityManager;import android.app.ActivityManager.RunningAppProcessInfo;import android.app.ActivityManager.RunningTaskInfo;import android.app.PendingIntent;import android.app.PendingIntent.CanceledException;import android.content.ComponentName;import android.content.Context;import android.content.Intent;import android.content.pm.PackageManager;import android.content.pm.ResolveInfo;import java.util.HashMap;import java.util.List;public class AndroidUtils {	public static int getAPILevel()    {        return android.os.Build.VERSION.SDK_INT;    }		public static int getAppState(Context context) {		if( context == null )			return -1;	    ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);	    List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();	    if (appProcesses == null) {	      return -1;	    }	    final String packageName = context.getPackageName();	    for (RunningAppProcessInfo appProcess : appProcesses) {	      if ( appProcess.processName.equals(packageName)) {	    	  return appProcess.importance;	      }	    }	    return -2;	}		public static void runAnotherApp(Context context, String packageName, HashMap<String, String> extra )	{		if( context == null )			return;		try {			 Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);			 if( extra != null )			 {				 for (String key : extra.keySet()) {											intent.putExtra(key, extra.get(key) );				}	 			 }			 			 			 PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);			 pi.send();			 			 context.startActivity(intent);		} 		catch (CanceledException e) {			e.printStackTrace();		}	catch(Exception e) {			e.printStackTrace();		}	}	private static HashMap<String, Object> s_packName = new HashMap<String, Object>(); 		public static void addAppPackageName(Context context, Intent intent )	{		PackageManager packageManager = context.getPackageManager();		List<ResolveInfo> listCam = packageManager.queryIntentActivities(intent, 0);		for (ResolveInfo res : listCam) {			String packName = res.activityInfo.packageName;			s_packName.put(packName, "");//		    Log.e("Camera Application Package Name and Activity Name",res.activityInfo.packageName + " " + res.activityInfo.name));		}	}	public static boolean applicationeInBackground(final Context context) {        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);        List<RunningTaskInfo> tasks = am.getRunningTasks(1);        if (!tasks.isEmpty()) {          ComponentName topActivity = tasks.get(0).topActivity;          if (!topActivity.getPackageName().equals(context.getPackageName()))           {        	  if( s_packName.containsKey(topActivity.getPackageName()) == false )        		  return true;        	  else        		  return false;          }        }        return false;  }		public static void bringtoFrontActivityFromService(Context context, Activity activity )	{		if( context == null || activity == null )			return;				Intent intent = null;		intent = new Intent(context, activity.getClass());				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);				PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);				try {								pi.send();		} catch (CanceledException e) {			e.printStackTrace();		}		}		public static void bringtoFrontActivity(Context context, Activity activity )	{		if( context == null || activity == null )			return;				Intent intent = null;		intent = new Intent(context, activity.getClass());				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);				context.startActivity(intent);	}	}