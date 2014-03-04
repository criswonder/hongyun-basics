/**
 * 
 */
package com.example.hongyunbasic;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * @author wb-maohongyun
 *
 */
public class MyApplication extends Application {
 
	public MyApplication() {
		super();
	}
 
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
		intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
		intentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
		intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		intentFilter.addDataScheme("package");
		try {
			BroadcastReceiver mReceiver = new BroadcastReceiver() {
				
				@Override
				public void onReceive(Context context, Intent intent) {
					if(intent!=null && intent.getComponent()!=null){
						Log.d("deviceNote3", "deviceNote3 receive action"+intent.getScheme()+
								" componentClassName"+intent.getComponent().getClassName()+
								" componentPackageName"+intent.getComponent().getPackageName()
								);
						if(intent.getExtras()!=null && intent.getExtras().keySet()!=null){
							StringBuilder sb = new StringBuilder();
							for(String key:intent.getExtras().keySet()){
								sb.append(key).append("=").append(intent.getExtras().get(key)).append(",");
							}
							sb.deleteCharAt(sb.length()-1);
							Log.d("deviceNote3", "extras is="+sb.toString());
						}
					}else{
						Log.d("deviceNote3", "null intent");
					}
					Log.d("deviceNote3", "intent="+intent);
				}
			};
			 registerReceiver(mReceiver , intentFilter);
		} catch (Exception e) {
			//do nothing
		}
	}
	
	
}
