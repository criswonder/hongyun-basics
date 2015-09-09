package com.xixi.android;

import com.xixi.cache.ImageCache.RetainFragment;

import android.app.Application;
import android.content.Context;

public class XiXiApp extends Application {
	private static RetainFragment cacheFragment;

	public static RetainFragment findFragmentByTag(String tag) {
		return cacheFragment;
	}

	public static void setFragmentByTag(RetainFragment obj) {
		cacheFragment = obj;
	}

	private static Application application;

	@Override
	public void onCreate() {
		application = this;
		super.onCreate();
	}

	public static Context getContext() {
		return application;
	}

}
