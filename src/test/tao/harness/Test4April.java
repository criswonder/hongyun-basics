package test.tao.harness;

import java.security.MessageDigest;

import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Log;

public class Test4April extends TestBaseActivity{
 
	private ContextWrapper application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		application = getApplication();
	}
	
 
	/**
	 * 从win换到mac过后，旺信sdk对于keystore的校验会导致app crash。
	 * 验证一下runtime怎么获取到signature的
	 * http://android-know-how-to.blogspot.com/2013/08/android-apk-signature-check.html
	 */
	public void testCheckCertificate() {
		SignedCertificate certificate;
		try {
			PackageManager pm = application.getPackageManager();
			Signature sig = pm.getPackageInfo(application.getPackageName(),
					PackageManager.GET_SIGNATURES).signatures[0];
			String md5Fingerprint = doFingerprint(sig.toByteArray(), "MD5");
			Log.d("test", md5Fingerprint);
			md5Fingerprint = doFingerprint(sig.toByteArray(), "SHA1");
			Log.d("test", md5Fingerprint);
			certificate = SignedCertificate.getCertificate(md5Fingerprint);
		} catch (Exception e) {
			certificate = SignedCertificate.Production;
		} finally {
		}
	}

	protected static String doFingerprint(byte[] certificateBytes,
			String algorithm) throws Exception {
		MessageDigest md = MessageDigest.getInstance(algorithm);
		md.update(certificateBytes);
		byte[] digest = md.digest();

		String toRet = "";
		for (int i = 0; i < digest.length; i++) {
			if (i != 0)
				toRet += ":";
			int b = digest[i] & 0xff;
			String hex = Integer.toHexString(b);
			if (hex.length() == 1)
				toRet += "0";
			toRet += hex;
		}
		return toRet;
	}

	private enum SignedCertificate {
		Debug("...", null, true, true), Staging("...", "Staging", false, true), Production(
				"...", "Production", false, false), Unknown(
				"There is no such a certificate", "Production1", false, false);

		private final String md5;

		private final String environment;
		private final boolean debugMode;

		private final boolean showsLogs;

		private SignedCertificate(String md5, String environment,
				boolean debugMode, boolean showsLogs) {
			this.md5 = md5;
			this.environment = environment;
			this.debugMode = debugMode;
			this.showsLogs = showsLogs;
		}

		public static final SignedCertificate getCertificate(String md5) {
			md5 = md5.toLowerCase();
			for (SignedCertificate certificate : values()) {
				if (certificate.md5.toLowerCase().equals(md5))
					return certificate;
			}
			return Unknown;
		}
	}
	
	public void testMonkeySetCategory(){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("com.taobao.Monkey");
		this.startActivity(intent);
	}
	public void testStopService(){
		Intent intent = new Intent();
    	intent.setClassName("com.taobao.taobao", "com.taobao.calendar.sdk.alarm.CalendarAlarmService");
		this.stopService(intent );
	}
}
