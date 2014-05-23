package test.tao.harness;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Looper;
import android.taobao.atlas.hack.AssertionArrayException;
import android.taobao.atlas.hack.AtlasHacks;
import android.taobao.atlas.hack.Hack;
import android.taobao.atlas.hack.Hack.HackDeclaration.HackAssertionException;
import android.taobao.atlas.hack.Hack.HackedField;
import android.taobao.atlas.hack.Interception.InterceptionHandler;
import android.text.style.SuperscriptSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.taobao.android.task.SafeAsyncTask;
import com.taobao.tao.Globals;
import com.taobao.tao.util.Constants;
import com.xixi.utils.MiscUtil;

public class Test5May extends TestBaseActivity {
	/**
	 * 测试目的：SafeAsyncTask.execute 是否会在主线程执行
	 */
	public void testSkiSafeAsyncTask(){
		SafeAsyncTask.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				readAssetFile();
				Log.e(TAG, "after run");
			}
		});
		Log.e(TAG, "after execute");
		
	}
	
	private void readAssetFile(){
		File destFile = new File(getFilesDir(),"dest.zip");
		FileOutputStream outStream = null;
		InputStream inputStream = null;
		try {
			outStream = new FileOutputStream(destFile);
			inputStream = getAssets().open("classes.zip");
			byte[] buff = new byte[8192];
			while(inputStream!=null & inputStream.read(buff)!=-1){
				outStream.write(buff, 0, buff.length);
				System.out.println("....");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			MiscUtil.safeClose(inputStream);
			MiscUtil.safeClose(outStream);
		}
	}
	
	public void testMarketJump(){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?id=" +"com.taobao.taobao"));
		startActivity(intent);
	}
	public void testMarketJump2(){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?id=" +"com.taobao.taobao"));
		startActivity(intent);
	}
	
	public void testLogCrash(){
		Log.d("xxx", null);
	}
	public void testLogCrash1(){
		Log.d("xxx", "");
	}
	private final String ATLAS_TEST_TAG = "atlas_test";
	public void testAtlas$Hack(){
		try {
			if(AtlasHacks.defineAndVerify()){
				if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
	                Class<?> activityThreadCls = AtlasHacks.ActivityThread.getmClass();
	                Object sActivityThread = null;
					if (sActivityThread == null) {
	                    sActivityThread = AtlasHacks.ActivityThread_currentActivityThread.invoke(activityThreadCls);

	                }
					AtlasHacks.ActivityThread_mPackages.on(sActivityThread).hijack(interceptHandler);
	            }
				
			}else{
				Log.d(ATLAS_TEST_TAG, "defineAndVerify FALSE");
			}
		} catch (AssertionArrayException e) {
			e.printStackTrace();
		}
	}
	public void testAtlas$Hack1(){
		try {
			AtlasHacks.ActivityThread_mPackages.get();
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public class TestInterceptionHandler extends InterceptionHandler<Map>{
		public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) throws Throwable {
			Log.d(ATLAS_TEST_TAG, "InterceptionHandler invoke get called...");
			return null;
		};
		public TestInterceptionHandler () {
			super();
		}
	}
	
	public InterceptionHandler<Map> interceptHandler = new TestInterceptionHandler();
	
	public static class Jack{
		private static String name = "jackMao";
		
		/**
		 * 接口类型的field的可以无痛代理
		 */
		public static INavy navy;
		public void printName(){
			System.out.println(String.valueOf(name));
		}
		public void printFullName(String n){
			System.out.println(n);
		}
		public Jack(){
			navy = new INavy() {
				
				@Override
				public void getBadge() {
					System.out.println("getBadge");
				}
				
				@Override
				public void sysHi(String name) {
					System.out.println("Hi-->"+name);
				}
			};
		}
	}
	
	public static interface INavy{
		public void getBadge();
		public void sysHi(String name);
	}
	
	public class TestHandler extends InterceptionHandler<Object>{
		public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) throws Throwable {
			Log.d(ATLAS_TEST_TAG, "InterceptionHandler invoke get called...");
			Hack.into("test.tao.harness.Test5May$Jack").field("name").set("fuckkk"); 
			return super.invoke(proxy, method, args);
//			return null;
		};
		public TestHandler () {
			super();
		}
	}
	
	public void testHijack(){
		try {
			HackedField<Object, String> field = Hack.into("test.tao.harness.Test5May$Jack").field("name").ofType(String.class);
			Jack instance = new Jack();
			/**
			 * 这样就会有下面的异常，hijack的时候将生成一个动态的proxy，设置到这个field上面，由于这个field是string类型的，将会有下面的不匹配产生。
			 * 05-18 09:08:09.212: W/System.err(6385): Caused by: java.lang.IllegalArgumentException: invalid value for field
			 */
			field.on(instance).hijack(new TestHandler());
			instance.printName();
		} catch (HackAssertionException e) {
			e.printStackTrace();
		}
	}
	
	//-----------------------------------------------------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------------------------------------------
	public class TestHandler2 extends InterceptionHandler<Object>{
		public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) throws Throwable {
			Log.d(ATLAS_TEST_TAG, "InterceptionHandler invoke get called method is-->"+method.getName());
			if("sysHi".equals(method.getName()) ){
				args[0] = "xiangrui";
			}
			return super.invoke(proxy, method, args);
		};
		public TestHandler2 () {
			super();
		}
	}
	public void testHijack2(){
		try {
			HackedField<Object, Object> field = Hack.into("test.tao.harness.Test5May$Jack").field("navy").ofType(Object.class);
			Jack instance = new Jack();
			field.on(instance).hijack(new TestHandler2());
			Jack.navy.sysHi("xxxxxx");
			Jack.navy.getBadge();
		} catch (HackAssertionException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	//-----------------------------------------------------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------------------------------------------
	public void testWindowManager(){
		DisplayMetrics temp = this.getApplication().getResources().getDisplayMetrics();
		
		View mNavigationBarFakeView = new View(this);
		mNavigationBarFakeView.setBackgroundColor(Color.RED);
		
		WindowManager mWindowManager = (WindowManager) this.getApplication().getSystemService(Context.WINDOW_SERVICE);
		WindowManager mWindowManager2 = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		Log.d("testWindowManager", "mWindowManager="+mWindowManager);
		Log.d("testWindowManager", "mWindowManager="+mWindowManager2);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = (int) (44 * temp.density);
        params.format = PixelFormat.RGB_565;
        mNavigationBarFakeView.setVisibility(View.INVISIBLE);
        //mNavigationBarFakeView.setTag("taobao_fake_view");

        try {
            mWindowManager.addView(mNavigationBarFakeView, params);
        } catch (WindowManager.BadTokenException e) {
        	e.printStackTrace();
        } catch (RuntimeException e) {
        	e.printStackTrace();
        }
	}
	
}
