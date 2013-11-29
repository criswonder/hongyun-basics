package com.utils;


/**
 * Author:蒋帅 Date:2012-8-7
 */
public class CameraUtil {

	private static final String TAG = "CameraUtil";

	/**
	 * -1 表示未初始化 0 表示不支持 1 表示支持
	 */
	private static int autoFocuseAble = -1;

	/**
	 * 判断相机是否有自动功能
	 */
	public static boolean unAutoFocuseAble() {
		return false;
//		if (BuiltConfig.getBoolean(R.string.hide_barcodesech)) {
//			TaoLog.Logd(TAG,
//					"BuiltConfig.getBoolean(R.string.hide_barcodesech): true");
//			return true;
//		}
//		if (-1 != autoFocuseAble) {
//			TaoLog.Logd(TAG, "autoFocuseAble:" + autoFocuseAble);
//			return (0 == autoFocuseAble);
//		}
//
//		PackageManager pm = TaoApplication.context.getPackageManager();
//		autoFocuseAble = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)
//				&& pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS) ? 1
//				: 0;
//		TaoLog.Logd(TAG, "autoFocuseAble:" + autoFocuseAble);
//		return (0 == autoFocuseAble);
	}

}
