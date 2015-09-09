package com.xixi.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AndyMao
 * 
 */
public class APNUtils {
	private static final String TAG = "APNUtils";

	public static MyProxy getMyProxy(Context context) {
		String apnName = getAPNName(context);
		if (apnName == null) {
			return null;
		} else {
			List<APN> list = getAPNList(context);
			for (APN apn : list) {
				if (apnName.equals(apn.getApn()) && apn.getCurrent() != null) {
					MyProxy proxy = new MyProxy();
					String apnProxy = apn.getProxy();
					String apnPort = apn.getPort();
					if (TextUtils.isEmpty(apnProxy)
							|| TextUtils.isEmpty(apnPort)) {
						return null;
					}
					proxy.host = apn.getProxy();
					proxy.port = Integer.parseInt(apn.getPort());
					return proxy;
				}
			}
		}
		return null;
	}

	public static String getAPNName(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null) {
			Log.i("AndyMao", info.getTypeName() + "," + info.getSubtypeName()
					+ "," + info.getExtraInfo());
			return info.getExtraInfo();
		} else {
			return null;
		}
	}

	public static List<APN> getAPNList(Context context) {
		List<APN> list = new ArrayList<APN>();
		try {
			Cursor cursor = context.getContentResolver().query(
					APNColumns.CONTENT_URI, null, null, null, null);
			while (cursor != null && cursor.moveToNext()) {
				APN apn = new APN();
				apn.setApn(cursor.getString(cursor
						.getColumnIndex(APNColumns.APN)));
				apn.setCurrent(cursor.getString(cursor
						.getColumnIndex(APNColumns.CURRENT)));
				apn.setMcc(cursor.getString(cursor
						.getColumnIndex(APNColumns.MCC)));
				apn.setMmsc(cursor.getString(cursor
						.getColumnIndex(APNColumns.MMSC)));
				apn.setMmsPort(cursor.getString(cursor
						.getColumnIndex(APNColumns.MMSPORT)));
				apn.setMmsProxy(cursor.getString(cursor
						.getColumnIndex(APNColumns.MMSPROXY)));
				apn.setMnc(cursor.getString(cursor
						.getColumnIndex(APNColumns.MNC)));
				apn.setName(cursor.getString(cursor
						.getColumnIndex(APNColumns.NAME)));
				apn.setNumeric(cursor.getString(cursor
						.getColumnIndex(APNColumns.NUMERIC)));
				apn.setPassword(cursor.getString(cursor
						.getColumnIndex(APNColumns.PASSWORD)));
				apn.setPort(cursor.getString(cursor
						.getColumnIndex(APNColumns.PORT)));
				apn.setProxy(cursor.getString(cursor
						.getColumnIndex(APNColumns.PROXY)));
				apn.setServer(cursor.getString(cursor
						.getColumnIndex(APNColumns.SERVER)));
				apn.setType(cursor.getString(cursor
						.getColumnIndex(APNColumns.TYPE)));
				apn.setUser(cursor.getString(cursor
						.getColumnIndex(APNColumns.USER)));
				list.add(apn);
			}
			cursor.close();
		} catch (Exception ex) {
			Log.e(TAG, null, ex);
		}
		return list;
	}

	public static class APN {
		private String name;
		private String apn;
		private String proxy;
		private String port;
		private String mmsProxy;
		private String mmsPort;
		private String server;
		private String user;
		private String password;
		private String mmsc;
		private String mcc;
		private String mnc;
		private String numeric;
		private String type;
		private String current;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getApn() {
			return apn;
		}

		public void setApn(String apn) {
			this.apn = apn;
		}

		public String getProxy() {
			return proxy;
		}

		public void setProxy(String proxy) {
			this.proxy = proxy;
		}

		public String getPort() {
			return port;
		}

		public void setPort(String port) {
			this.port = port;
		}

		public String getMmsProxy() {
			return mmsProxy;
		}

		public void setMmsProxy(String mmsProxy) {
			this.mmsProxy = mmsProxy;
		}

		public String getMmsPort() {
			return mmsPort;
		}

		public void setMmsPort(String mmsPort) {
			this.mmsPort = mmsPort;
		}

		public String getServer() {
			return server;
		}

		public void setServer(String server) {
			this.server = server;
		}

		public String getUser() {
			return user;
		}

		public void setUser(String user) {
			this.user = user;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getMmsc() {
			return mmsc;
		}

		public void setMmsc(String mmsc) {
			this.mmsc = mmsc;
		}

		public String getMcc() {
			return mcc;
		}

		public void setMcc(String mcc) {
			this.mcc = mcc;
		}

		public String getMnc() {
			return mnc;
		}

		public void setMnc(String mnc) {
			this.mnc = mnc;
		}

		public String getNumeric() {
			return numeric;
		}

		public void setNumeric(String numeric) {
			this.numeric = numeric;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getCurrent() {
			return current;
		}

		public void setCurrent(String current) {
			this.current = current;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("name=").append(name);
			sb.append(",apn=").append(apn);
			sb.append(",proxy=").append(proxy);
			sb.append(",port=").append(port);
			sb.append(",mmsProxy=").append(mmsProxy);
			sb.append(",mmsPort=").append(mmsPort);
			sb.append(",server=").append(server);
			sb.append(",user=").append(user);
			sb.append(",password=").append(password);
			sb.append(",mmsc=").append(mmsc);
			sb.append(",mcc=").append(mcc);
			sb.append(",mnc=").append(mnc);
			sb.append(",numeric=").append(numeric);
			sb.append(",type=").append(type);
			sb.append(",current=").append(current);
			return sb.toString();
		}
	}

	public static class APNColumns {
		public static final Uri CONTENT_URI = Uri
				.parse("content://telephony/carriers");
		public static final String NAME = "name";

		public static final String APN = "apn";

		public static final String PROXY = "proxy";

		public static final String PORT = "port";

		public static final String MMSPROXY = "mmsproxy";

		public static final String MMSPORT = "mmsport";

		public static final String SERVER = "server";

		public static final String USER = "user";

		public static final String PASSWORD = "password";

		public static final String MMSC = "mmsc";

		public static final String MCC = "mcc";

		public static final String MNC = "mnc";

		public static final String NUMERIC = "numeric";

		public static final String TYPE = "type";

		public static final String CURRENT = "current";
	}

	public static class MyProxy {

		String host;
		int port;

		public String getHost() {
			return host;
		}

		public int getPort() {
			return port;
		}

	}
}
