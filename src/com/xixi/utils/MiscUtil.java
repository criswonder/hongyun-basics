package com.xixi.utils;

import com.xixi.android.MucangConfig;

import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Map;

public class MiscUtil {
	public static boolean isValidMumber(String number){
		if(MiscUtil.isEmpty(number)){
			return false;
		}
		try{
			return number.matches("[0-9]+\\.?[0-9]{0,2}");
		}catch(Exception ex){}
		return false;
	}

	public static String strMD5(String strSrc) {
		MessageDigest md = null;
		byte[] bt = strSrc.getBytes();
		String strDes = null;

		try {
			md = MessageDigest.getInstance("MD5");
			md.update(bt);
			strDes = bytes2Hex(md.digest()); // to HexString
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return strDes;
	}

	public static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}
	public static void safeClose(Closeable close) {
		try {
			close.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static File createIfNotExistsOnPhone(String fileName) {
	   String path =  MucangConfig.getContext().getFilesDir()+"/"+fileName;
        File file = new File(path);
        if (file != null) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static File createDirIfNotExistsOnPhone(String dirName) {
        String path =  MucangConfig.getContext().getFilesDir()+"/"+dirName;
        File file = new File(path);
        if (file != null) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static File createIfNotExistsOnSDCard(String fileName) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = new File(Environment.getExternalStorageDirectory(), "/Android/data/"
                    + MucangConfig.getContext().getPackageName()
                    + "/files/" + fileName);
            if (file != null) {
                try {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return file;
        }
        return null;
    }
    public static File createDirIfNotExistsOnSDCard(String dirName) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = new File(Environment.getExternalStorageDirectory(), "/Android/data/"
                    + MucangConfig.getContext().getPackageName()
                    + "/files/" + dirName);
            if (file != null) {
                try {
                    file.mkdirs();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return file;
        }
        return null;
    }
    public static int getPxByDip(int dip) {
        if (dip > 0) {
            Context context = MucangConfig.getContext();
            int resId = context.getResources().getIdentifier("dimen_" + dip + "_dip", "dimen", context.getPackageName());
            return context.getResources().getDimensionPixelSize(resId);
        } else {
            return 0;
        }
    }

    public static int getPxByDipReal(int dip) {
        DisplayMetrics dm = MucangConfig.getContext().getResources().getDisplayMetrics();
        int px = (int) (dip * dm.density + 0.5f);
        return px;
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    public static boolean isNotEmpty(Collection<?> c) {
        return !isEmpty(c);
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static boolean isEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

    public static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
}
