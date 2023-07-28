package com.melody.castle.utils;import android.app.AlertDialog;import android.content.Context;import android.content.DialogInterface;import android.widget.Toast;public class MessageUtils {	static Toast g_Toast = null;	static Context g_AppContext = null;		public static void setApplicationContext( Context context )	{		g_AppContext = context;	}		public static void Toast(String message )	{		if( message == null || message.length() < 1 )			return;				if( g_AppContext == null )			return;				if( g_Toast == null )		{			g_Toast = Toast.makeText(g_AppContext, message, Toast.LENGTH_SHORT);		}		else		{			g_Toast.setText(message);		}		//		g_Toast.show();	}		public static void Toast(int resID )	{		Toast.makeText(g_AppContext, g_AppContext.getResources().getString(resID), Toast.LENGTH_SHORT).show();				}		public static AlertDialog showMessageDialog(Context context, String message )	{		if( message == null || message.length() < 1 )			return null;				AlertDialog.Builder alert_confirm = new AlertDialog.Builder(context);		alert_confirm.setMessage(message).setCancelable(false).setPositiveButton("Ok",		new DialogInterface.OnClickListener() {		    @Override		    public void onClick(DialogInterface dialog, int which) {		    			        		    }		});		AlertDialog alert = alert_confirm.create();		alert.show();		return alert;	}}