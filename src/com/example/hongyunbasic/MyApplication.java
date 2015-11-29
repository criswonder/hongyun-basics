/**
 * 
 */
package com.example.hongyunbasic;

import com.taobao.android.dexposed.XC_MethodHook;
import com.taobao.android.dexposed.XposedBridge;
import com.taobao.android.dexposed.XposedHelpers;
import com.taobao.ju.track.JTrack;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.ImageView;

/**
 * @author wb-maohongyun
 *
 */
public class MyApplication extends Application {
	static {
		// load xposed lib for hook.
		try {
			if (android.os.Build.VERSION.SDK_INT > 19 && android.os.Build.VERSION.SDK_INT <= 21){
				System.loadLibrary("dexposed_l");
			} else if (android.os.Build.VERSION.SDK_INT > 14){
				System.loadLibrary("dexposed");
			}
		} catch (Throwable e) {
		}
	}
 
	public MyApplication() {
		super();
	}
 
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		JTrack.init(this);
		JTrack.Page.setCsvFileName("ut-page-config.csv");
		JTrack.Ctrl.setCsvFileName("ut-ctrl-config.csv");
		JTrack.Ext.setCsvFileName("ut-ext-config.csv");

		XposedBridge.hookAllConstructors(ImageView.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable
			{
				super.beforeHookedMethod(param);
				Log.e("andymao", "beforeHookedMethod");
			}
		});




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
					if("com.taobao.taobao".equalsIgnoreCase(intent.getPackage() )){
						System.out.println("fuck");
					}
					if(intent!=null && intent.getData()!=null 
					&& "com.taobao.taobao".equalsIgnoreCase(intent.getData().getSchemeSpecificPart())){
//						Log.d("deviceNote3", "deviceNote3 receive action"+intent.getScheme()+
//								" componentClassName"+intent.getComponent().getClassName()+
//								" componentPackageName"+intent.getComponent().getPackageName()
//								);
//						if(intent.getExtras()!=null && intent.getExtras().keySet()!=null){
//							StringBuilder sb = new StringBuilder();
//							for(String key:intent.getExtras().keySet()){
//								sb.append(key).append("=").append(intent.getExtras().get(key)).append(",");
//							}
//							sb.deleteCharAt(sb.length()-1);
//							Log.d("deviceNote3", "extras is="+sb.toString());
//						}
						
						System.out.println("fuck");
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
