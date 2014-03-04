/**
 * 
 */
package com.example.hongyunbasic;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author wb-maohongyun
 *
 */
public class WidgetMy  extends AppWidgetProvider {

    private static AppWidgetProvider instance;

    private AppWidgetProvider getWeitaoWidgetInstance() {
        if (instance != null) {
            return instance;
        }
//        try {
//            Class<?> clazz = AtlasBladeHelper.getCurrentClassLoader().loadClass("com.taobao.allspark.widget.WeitaoWidgetProvider");
//            instance = (AppWidgetProvider) clazz.newInstance();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return instance;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//    	super.onReceive(context, intent);
        AppWidgetProvider provider = getWeitaoWidgetInstance();
        if (provider != null) {
            provider.onReceive(context, intent);
        }
        
  
        ComponentName cName =  new ComponentName(context, WidgetMy.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
		int[] appWidgetIds = manager.getAppWidgetIds(cName);
		
        if(1==1){
        	onDisabled(context);
        	onDeleted(context, appWidgetIds);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        AppWidgetProvider provider = getWeitaoWidgetInstance();
        if(1==1){
        	return;
        }
        if (provider != null) {
            provider.onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    @Override
    @TargetApi(16)
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId,
                                          Bundle newOptions) {
    	 if(1==1){
         	return;
         }
        AppWidgetProvider provider = getWeitaoWidgetInstance();
        if (provider != null) {
            provider.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        AppWidgetProvider provider = getWeitaoWidgetInstance();
        if (provider != null) {
            provider.onDeleted(context, appWidgetIds);
        }
    }

    @Override
    public void onEnabled(Context context) {
    	 if(1==1){
         	return;
         }
        AppWidgetProvider provider = getWeitaoWidgetInstance();
        if (provider != null) {
            provider.onEnabled(context);
        }
    }

    @Override
    public void onDisabled(Context context) {
        AppWidgetProvider provider = getWeitaoWidgetInstance();
        if (provider != null) {
            provider.onDisabled(context);
        }
    }

}