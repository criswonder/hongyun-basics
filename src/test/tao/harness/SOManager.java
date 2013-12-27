package test.tao.harness;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.zip.Adler32;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.taobao.util.TaoLog;
import android.text.TextUtils;
import android.util.Log;

public class SOManager {

	private final static String TAG = "SOManager";

	private final static String ARMEABI = "armeabi";
	private final static String ARMEABI_V7A = "armeabi-v7a";
	private final static String X86 = "x86";
	private final static String MIPS = "mips";
	private final static String V7A = "v7a";
	private final static String SO_LIBRARY_NAME = "libBSPatch.so";
	
	private final static String ASSERT_ARMEABI_PATH = "so/armeabi/";
	private final static String ASSERT_X86_PATH = "so/x86/";
	private final static String ASSERT_MIPS_PATH = "so/mips/";
	private final static String ASSERT_V7A__PATH = "so/v7a/";
	// private final static String[] VERSION_HISTORY_LIST = {
	// "libndk-tbsengine.so", "libndk-tbsengine-2.0.so",
	// "libndk-tbsengine-3.0.so", "libndk-tbsengine-4.0.1.so" };
	
	// 依次是：libinet，alisua_jni，alisua_jni（v7a）
	public final static long[] mCheckSums = {3030518739L,880052405L};

	private static StringBuffer logInfo = new StringBuffer();
	private final static String sVoipLibraryS[] = {"alisua_jni"};
	private Context mContext = null;
	private File mLibFile = null;
	private String mAppDataFolder = null;
	
	// private Runtime mRuntime = null;

	public SOManager(Context aContext) {
		this.mContext = aContext;
		try {
			mAppDataFolder = ((ContextWrapper) this.mContext)
					.getApplicationInfo().dataDir;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 在2.3以上可以通过ApplicationInfo.nativeLibraryDir来获取目录
		// 这里我们只相信手机内部的存储
		if (!TextUtils.isEmpty(mAppDataFolder)) {
			mLibFile = new File(mAppDataFolder, "lib" + File.separator
					+ SO_LIBRARY_NAME);
		}
	}

	public static SOManager getInstance(Context aContext) {
		if (aContext != null) {
			return new SOManager(aContext);
		}
		return null;
	}

	private boolean _validateAdler32(byte[] data) {
		if (data != null) {
			Adler32 adler32 = new Adler32();
			adler32.reset();
			adler32.update(data);
			long value = adler32.getValue();
			for (int i = 0; i < mCheckSums.length; i++) {
				if (mCheckSums[i] == value) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean _validateAdler32(FileInputStream fis) {
		try {
			if (fis != null && fis.available() > 0) {
				byte[] data = new byte[fis.available()];
				fis.read(data);
				return _validateAdler32(data);
			}
		} catch (IOException e) {
			logInfo.append("_validateAdler32 err");
			return false;
		} finally {
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					Log.e(TAG, "close file err.", new RuntimeException());
				}
				fis = null;
				logInfo.append(" _validateAdler32 close err");
			}
		}
		return false;
	}

	/**
	 * 校验以及加载 libinet.so
	 * 
	 * @return
	 */
	public void loadInetSo() {
		// if (mLibFile != null) {
		// // 如果系统存储中目标so库存在
		// if (mLibFile.exists()) {
		// // 如果验证通过
		// try {
		// // 验证so库
		// if (_validateAdler32(new FileInputStream(mLibFile))) {
		// // 通过
		// System.load(mLibFile.getAbsolutePath());
		// return;
		// }else{
		// //delete?
		// }
		// } catch (FileNotFoundException e) {
		// Log.e(TAG, "lib file is not found.",new RuntimeException());
		// }
		// }
		logInfo.append("flag=2 ");//方便脚本识别用
//		if (IMChannel.DEBUG) {
//			try {
//				System.loadLibrary("inet");	
//				//成功则返回
//				return;
//			} catch (Exception e) {
//				// TODO: handle exception
//			} catch (UnsatisfiedLinkError e) {
//				// TODO: handle exception
//			}
//		}
		
		// 读取SO
		if (_loadFile()) {
			mLibFile = new File(mLibFile.getAbsolutePath());
			if (mLibFile != null && mLibFile.exists()) {
				System.load(mLibFile.getAbsolutePath());
				Log.w(TAG, "Call System.load() by SOManager");
				return;
			}else{
				throw new UnsatisfiedLinkError("mLibFile not exist");
			}
		}else{
			throw new UnsatisfiedLinkError("_loadFile return false");
		}
	}

	/**
	 * 校验以及加载
	 * 
	 * @return
	 */
	public LoadSoResult validateAndLoad() {
		LoadSoResult lsr = new LoadSoResult();
		if (mLibFile != null) {
			// 如果系统存储中目标so库存在
			if (mLibFile.exists()) {
				// 如果验证通过
				try {
					// 验证so库
					if (_validateAdler32(new FileInputStream(mLibFile))) {
						// 通过
						lsr.setLoadBySoManager(false);
						lsr.setSuccess(true);
						return lsr;
					}
				} catch (FileNotFoundException e) {
					Log.e(TAG, "lib file is not found.", new RuntimeException());
				}
			} else {
				// 读取SO
				if (_loadFile()) {
					mLibFile = new File(mLibFile.getAbsolutePath());
					if (mLibFile != null && mLibFile.exists()) {
						try {
							System.load(mLibFile.getAbsolutePath());
						} catch (Exception e) {
							return lsr;
						}
						Log.w(TAG, "Call System.load() by SOManager");
						lsr.setLoadBySoManager(true);
						lsr.setSuccess(true);
						return lsr;
					}
				}
			}
		}
		return lsr;
	}

	private static String _getFieldReflectively(Build build, String fieldName) {
		try {
			final Field field = Build.class.getField(fieldName);
			return field.get(build).toString();
		} catch (Exception ex) {
			logInfo.append("_getFieldReflectively err=" + ex.getMessage());
			return "Unknown";

		}
	}

	byte[] getAssetsFileData(Context ctx, String fileName) {
		InputStream input = null;
		try {
			input = ctx.getAssets().open(fileName);

			if (input.available() > 0) {
				byte[] data = new byte[input.available()];
				//FIXME 2.2版本如果内容超过1M存在IOExecteption问题。
				//http://stackoverflow.com/questions/5176240/android-inputstream-ioexception-large-file-android-2-2
				input.read(data);
				return data;
			}
		} catch (Exception e) {
			logInfo.append(" getAssetsFileData exception=" + e.getMessage()
					 + " fileName =" + fileName);
			return null;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					Log.e(TAG, "close file err.", new RuntimeException());
				}
			}
		}

		return null;
	}

	private boolean _loadFile() {
		File privateFilesPath = new File(mAppDataFolder, "files");
		// 如果私有files目录可写
		// if (mContext != null && privateFilesPath != null
		// && privateFilesPath.canWrite()) {
		// // 删除旧的库
		// if (privateFilesPath.exists()) {
		// for (int i = 0; i < VERSION_HISTORY_LIST.length; i++) {
		// if (!TextUtils.isEmpty(VERSION_HISTORY_LIST[i])) {
		// File oFile = new File(privateFilesPath,
		// VERSION_HISTORY_LIST[i]);
		// if (oFile.exists()) {
		// oFile.delete();
		// }
		// }
		// }
		// }
		// }
		// 如果files目录不存在
		if (!privateFilesPath.exists()) {
			privateFilesPath.mkdir();
		}
		if (privateFilesPath.exists()) {
			// 目标so文件
			mLibFile = new File(privateFilesPath, SO_LIBRARY_NAME);
			try {
				if (mLibFile.exists()
						&& _validateAdler32(new FileInputStream(mLibFile))) {
					// 如果已经存在，并且验证成功
					TaoLog.Logd(TAG, "ValidateAdler32 mLibFile success.");
					return true;
				} else {
					TaoLog.Logd(TAG,
							"ValidateAdler32 exist mLibFile fail.We will re copy one.");
				}
			} catch (FileNotFoundException e1) {
				// e1.printStackTrace();
			}
			// COPY SO DATA，兼容sdk1.5
			String abi = _getFieldReflectively(new Build(), "CPU_ABI");
			logInfo.append("abi=" + abi);
			if (TextUtils.isEmpty(abi) || abi.equals("Unknown")) {
				abi = ARMEABI;
			}
			byte[] sodata = null;
			abi = abi.toLowerCase();
			if (abi.equals(MIPS)) {
				TaoLog.Logd(TAG, MIPS);
				sodata = getAssetsFileData(mContext, ASSERT_MIPS_PATH+SO_LIBRARY_NAME);
			} else if (abi.equals(X86)) {
				TaoLog.Logd(TAG, X86);
				sodata = getAssetsFileData(mContext, ASSERT_X86_PATH+SO_LIBRARY_NAME);
			}  else if (abi.equals(ARMEABI_V7A)) {
				TaoLog.Logd(TAG, ARMEABI_V7A);
				sodata = getAssetsFileData(mContext, ASSERT_V7A__PATH+SO_LIBRARY_NAME);
			}else {
				TaoLog.Logd(TAG, ARMEABI);
				sodata = getAssetsFileData(mContext, ASSERT_ARMEABI_PATH+SO_LIBRARY_NAME);
			}
			//just for test
			if (sodata == null){
				logInfo.append(" sodata is null,retry get. ");
				sodata = getAssetsFileData(mContext, ASSERT_ARMEABI_PATH+SO_LIBRARY_NAME);
				if (sodata != null){
					logInfo.append(" getAssetsFileData from armeabi OK ");
				}else{
					logInfo.append("  retry is null ");
				}
				throw new UnsatisfiedLinkError(logInfo.toString());
			}
			
			if (sodata != null) {
				// 如果assets目录下携带的so数据是ok的，并且我们读取OK
				if (!_validateAdler32(sodata)) {
					TaoLog.Logd(TAG, "ValidateAdler32 sodata bytes fail");
					throw new UnsatisfiedLinkError("validate err " + logInfo);
					//return false;
				}
				mLibFile = new File(privateFilesPath, SO_LIBRARY_NAME);
				// 如果可写
				if (mLibFile != null && mLibFile.getParentFile().canWrite()) {
					if (mLibFile.exists()) {// 如果旧的存在，那么删除掉
						mLibFile.delete();
					}
					FileOutputStream fos = null;
					try {
						// 写SO数据
						fos = new FileOutputStream(mLibFile);
						fos.write(sodata, 0, sodata.length);
						fos.flush();
					} catch (FileNotFoundException e) {
						throw new UnsatisfiedLinkError("FileNotFoundException msg=" + e.getMessage()
								 +" " +  logInfo);
						//return false;
					} catch (IOException e) {
						throw new UnsatisfiedLinkError("IOException msg=" + e.getMessage() + " " + logInfo);
						//return false;
					} finally {
						if (fos != null) {
							try {
								fos.close();
							} catch (IOException e) {
								Log.e(TAG, "close file err.",
										new RuntimeException());
							}
							fos = null;
						}
						// 验证
						try {
							if (_validateAdler32(new FileInputStream(mLibFile))) {
								TaoLog.Logd(TAG, "ValidateAdler32 mLibFile success.");
								return true;
							} else {
								if (mLibFile.exists()) {
									mLibFile.delete();
									TaoLog.Logd(TAG,
											"Delete mLibFile because ValidateAdler32 libFile fail.");
								}
								
								throw new UnsatisfiedLinkError("ValidateAdler32 libFile fail " + logInfo);
							}
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
				}else{
					throw new UnsatisfiedLinkError("mLibFile can not Write " + logInfo);
				}
			} 
//			else {
//				Log.i(TAG, "Sodata is null.");
//				throw new UnsatisfiedLinkError("Sodata is null " + logInfo);
//			}
		}else{
			throw new UnsatisfiedLinkError("privateFilesPath not exist " + logInfo);
		}
		return false;
	}
	
	private boolean _loadVoipFile(String SoLibraryName) {
		File privateFilesPath = new File(mAppDataFolder, "files");
		// 如果files目录不存在
		if (!privateFilesPath.exists()) {
			privateFilesPath.mkdir();
		}
		if (privateFilesPath.exists()) {
			//目标so文件
			mLibFile = new File(privateFilesPath, SoLibraryName);
			try {
				if (mLibFile.exists()
						&& _validateAdler32(new FileInputStream(mLibFile))) {
					// 如果已经存在，并且验证成功
					TaoLog.Logd(TAG, "ValidateAdler32 mLibFile success.");
					return true;
				} else {
					TaoLog.Logd(TAG, "ValidateAdler32 exist mLibFile fail.We will re copy one.");
				}
			} catch (FileNotFoundException e1) {
			}
			// COPY SO DATA，兼容sdk1.5
			String abi = _getFieldReflectively(new Build(), "CPU_ABI");
			if (TextUtils.isEmpty(abi) || abi.equals("Unknown")) {
				abi = ARMEABI;
			}
			byte[] sodata = null;
			abi = abi.toLowerCase();
			TaoLog.Logd(TAG, "device abi : " + abi);
			if (abi.equals(MIPS)) {
				TaoLog.Logd(TAG, MIPS);
				sodata = getAssetsFileData(mContext,ASSERT_MIPS_PATH+SoLibraryName);
			} else if (abi.equals(X86)) {
				TaoLog.Logd(TAG, X86);
				sodata = getAssetsFileData(mContext,ASSERT_X86_PATH+SoLibraryName);
			} else if (abi.contains(V7A)) {
				TaoLog.Logd(TAG, V7A + " voip");
				sodata = getAssetsFileData(mContext, ASSERT_V7A__PATH + SoLibraryName);
			} else {
				TaoLog.Logd(TAG, ARMEABI);
				sodata = getAssetsFileData(mContext,ASSERT_ARMEABI_PATH+SoLibraryName);
			}
			if (sodata != null) {
				// 如果assets目录下携带的so数据是ok的，并且我们读取OK
				if (!_validateAdler32(sodata)) {
					TaoLog.Logd(TAG, "ValidateAdler32 sodata bytes fail");
					return false;
				}
				mLibFile = new File(privateFilesPath, SoLibraryName);
				// 如果可写
				if (mLibFile != null && mLibFile.getParentFile().canWrite()) {
					if (mLibFile.exists()) {// 如果旧的存在，那么删除掉
						mLibFile.delete();
					}
					FileOutputStream fos = null;
					try {
						//写SO数据
						fos = new FileOutputStream(mLibFile);
						fos.write(sodata, 0, sodata.length);
					} catch (FileNotFoundException e) {
						return false;
					} catch (IOException e) {
						return false;
					} finally {
						if (fos != null) {
							try {
								fos.close();
							} catch (IOException e) {
								Log.e(TAG, "close file err.", new RuntimeException());
							}
							fos = null;
						}
						// 验证
						try {
							if (_validateAdler32(new FileInputStream(mLibFile))) {
								TaoLog.Logd(TAG, "ValidateAdler32 mLibFile success.");
								return true;
							} else {
								if (mLibFile.exists()) {
									mLibFile.delete();
									TaoLog.Logd(TAG, "Delete mLibFile because ValidateAdler32 libFile fail.");
								}
							}
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				TaoLog.Logd(TAG, "Sodata is null.");
			}
		}
		return false;
	}

	private static boolean DEBUG_VOIP = false;
	public boolean loadVoipSo() {
		for (int i = 0; i < sVoipLibraryS.length; i++) {
			if (!DEBUG_VOIP) {
				String libName = "lib" + sVoipLibraryS[i] + ".so";
				if (_loadVoipFile(libName)) {
					mLibFile = new File(mLibFile.getAbsolutePath());
					if (mLibFile != null && mLibFile.exists()) {
						try {
							System.load(mLibFile.getAbsolutePath());
							return true;
						} catch (UnsatisfiedLinkError e) {
							Log.w(TAG, "v7 load fail");
							e.printStackTrace();
						}
						Log.w(TAG, "Call System.load() by SOManager");
					}
				}
			} else {
				try {
					System.loadLibrary(sVoipLibraryS[i]);
					return true;
				} catch (UnsatisfiedLinkError e) {
					e.printStackTrace();
				} 
			}
		}	
		//throw new UnsatisfiedLinkError("cannt loac voip library");	
		return false;
	}
	
	public static class LoadSoResult {
		private boolean isSuccess = false;
		private boolean loadBySoManager = false;

		public boolean isSuccess() {
			return isSuccess;
		}

		public void setSuccess(boolean isSuccess) {
			this.isSuccess = isSuccess;
		}

		public boolean isLoadBySoManager() {
			return loadBySoManager;
		}

		public void setLoadBySoManager(boolean loadBySoManager) {
			this.loadBySoManager = loadBySoManager;
		}

	}
}
