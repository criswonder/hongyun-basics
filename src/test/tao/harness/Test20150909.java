package test.tao.harness;

import com.example.hongyunbasic.R;
import com.taobao.ju.track.JTrack;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

import static test.tao.harness.Test20150909.TMSkinConfig.*;

/**
 * Created by hongyunmhy on 15/9/9.
 */
public class Test20150909 extends TestBaseActivity implements AdapterView.OnItemClickListener {

	private String 		mTest_jutrack = "test_jutrack";

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Log.d(TAG, "onItemClick");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

		this.getListView().setOnItemClickListener(this);

	}

	public void testMethod3(){
		View testView = View.inflate(this, R.layout.test_when_to_inflate_img_src_drawable,null);
		setContentView(testView);
	}
	public void testClassInheritance(){
		C o = new C();
		o.m1();
	}

	static class A{
		public void m1(){
			Log.d("andymao","A m1");
			Object o = null;
			o.toString();
		}
	}

	static class B extends A{
		public void m2(){
			Log.d("andymao","A m2");
		}
	}

	static class C extends B{
		@Override
		public void m1(){
			super.m1();
			Log.d("andymao","A m2");
		}
	}


	public void testElapsedRealtimeVScurrentTimeMillis(){
		long time1 = System.currentTimeMillis();
		long time2 = SystemClock.elapsedRealtime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Log.e("andymao",""+simpleDateFormat.format(time1)+",time2="+time2);
	}


	public void testXprivacy(){
		String name = "serialno";
		printSystemProperty(name);
		printSystemProperty("cid");
		printSystemProperty("imei");
		printSystemProperty("hostname");

		Log.d("hongyun", "SERIAL=" + Build.SERIAL);
	}

	private void printSystemProperty(String name)
	{
		try {
			Class c = Class.forName("android.os.SystemProperties");
			Method m = c.getDeclaredMethod("get", String.class);
			Object obj = m.invoke(null, name);
			Log.d("hongyun", name+"=" + obj);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}









	/**
	 * 测试 jsonTostring
	 */
	public void testprintObj(){
		TMSkinConfig obj = new TMSkinConfig();
		Style obj2 = new Style();
		obj.currentStyle = obj2;
		obj.currentSytleId = 10002l;
		obj.enable = true;
		obj2.endTime = 1l;
		obj2.startTime = 2l;
		obj2.imgZipUrl  = "zipurl";
		obj2.imgMap = new HashMap<String, String>();
		obj2.imgMap.put("key1","value2");
		obj2.imgMap.put("key2","value3");

		System.out.println(obj);
	}

	public class TMSkinConfig {
		public long    currentSytleId;
		public Style   currentStyle;
		public boolean enable;
		public ArrayList styles = new ArrayList();

		@Override
		public String toString()
		{
			try {
				Field[] fields = this.getClass().getFields();
				if (fields != null) {
					JSONObject jsonObject = new JSONObject();
					for (Field f : fields) {
						jsonObject.put(f.getName(), f.get(this));
					}
					return jsonObject.toString();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
			}
			return super.toString();
		}
	}

	public class Style {
		public String                  name;
		public long                    id;
		public long                    startTime;
		public long                    endTime;
		public String                  imgZipUrl;
		public String                  imgZipMd5;
		public HashMap<String, String> imgMap;

		public HashMap<String, Object> modules;

		@Override
		public String toString()
		{
			try {
				Field[] fields = this.getClass().getFields();
				if (fields != null) {
					JSONObject jsonObject = new JSONObject();
					for (Field f : fields) {
						jsonObject.put(f.getName(), f.get(this));
					}
					return jsonObject.toString();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
			}
			return super.toString();
		}
	}

	//--------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------
	public void testJuTrack(){
		String pageName = JTrack.Page.getPageName("SplashActivity");
		Log.d(mTest_jutrack, "pageName=" + pageName);
//		String args = JTrack.getPage(pageName).getArgs(pageName);
//		Log.d(mTest_jutrack,"args="+args);
		JTrack.Ctrl.getParamMap("TABBAR");
	}


	//--------------------------------------------------------------------------------------------------
	// 为了验证登录 autologin 多线程的调用，是否存在问题
	//--------------------------------------------------------------------------------------------------

	private static AtomicBoolean isLogging = new AtomicBoolean(false);

	public void testAutomicBoolean()
	{
		ExecutorService pool = Executors.newFixedThreadPool(30);
		final CountDownLatch latch = new CountDownLatch(1);
		for(int i=0;i<100000;i++){
			pool.execute(new Runnable() {
				@Override
				public void run()
				{
					try {
						latch.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(!isLogging.get()){
						int sum = 0;
						for(int j=0;j<new Random().nextInt(1000);j++){
							sum+=j;
						}
						isLogging.set(true);
						Log.d("andymao","testAutomicBoolean isLogging="+false);
					}
				}
			});
		}
		latch.countDown();
	}

	public void testCloseAutomicBoolean()
	{
		isLogging.set(false);
	}

	//--------------------------------------------------------------------------------------------------
	// 验证 looper.prepare looper.loop
	//--------------------------------------------------------------------------------------------------
 	ExecutorService mExecutor = Executors.newSingleThreadExecutor();

	public void testCountDownTimer()
	{
		Log.d("andymao", "mExecutor.isShutdown()=" + mExecutor.isShutdown());
//		Future<?> future = mExecutor.submit(new Runnable() {
//			@Override
//			public void run()
//			{
//				Looper.prepare();
//
//				CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {
//					@Override
//					public void onTick(long millisUntilFinished)
//					{
//						Log.d("andymao", "onTick" + ",looper=" + Looper.myLooper());
//
//					}
//
//					@Override
//					public void onFinish()
//					{
//						Log.d("andymao", "onFinish");
//					}
//				};
//				countDownTimer.start();
//
//				Looper.loop();
//			}
//		});

		Thread t =new Thread(){
			@Override
			public void run()
			{
				super.run();
				Looper.prepare();

				CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {
					@Override
					public void onTick(long millisUntilFinished)
					{
						Log.d("andymao", "onTick1" + ",looper=" + Looper.myLooper());

					}

					@Override
					public void onFinish()
					{
						Log.d("andymao", "onFinish");
						Looper.myLooper().quit();
					}
				};
				countDownTimer.start();

				Looper.loop();
			}
		};
		t.start();

//		try {
//			future.get();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		}
	}

	//--------------------------------------------------------------------------------------------------
	//http://stackoverflow.com/questions/18120510/dynamically-changing-the-fragments-inside-a-fragment-tab-host
	//Fragment tabhost的测试
	//--------------------------------------------------------------------------------------------------
	public void testFragmentTabHosts(){
		Intent intent = null;
		try {
			intent = new Intent(this,Class.forName("test.tao.harness.TestFragmentTabHost"));
			startActivity(intent);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	//--------------------------------------------------------------------------------------------------
	//Test build info
	//--------------------------------------------------------------------------------------------------
	public void testbuildinfo(){
		StringBuilder sb = new StringBuilder();
		sb.append(Build.MANUFACTURER);
		String str = sb.toString();
		Toast.makeText(this,str,Toast.LENGTH_LONG).show();
	}


	//--------------------------------------------------------------------------------------------------
	//test for loop syncronized
	//--------------------------------------------------------------------------------------------------
	public void testSyncronizedOnForLoop(){
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		SingleInstanceHolder.getIns().addThing("abc1");
		for(int i=0;i<8;i++){
			final int j = i;
			SingleInstanceHolder.getIns().addThing("abc" + j);
		}

		Thread thread = new Thread(){
			@Override
			public void run()
			{
				super.run();
				try {
					countDownLatch.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
//				synchronized (SingleInstanceHolder.getIns().mArrayList){
					SingleInstanceHolder.getIns().removeThing("abc1");
//				}
			}
		};
		thread.start();
		synchronized (SingleInstanceHolder.getIns().mArrayList){
			for(Object obj:SingleInstanceHolder.getIns().mArrayList){
				Log.d(TAG,""+obj);
				if(countDownLatch.getCount()>0){
					countDownLatch.countDown();
				}
			}
		}
	}

	public static class SingleInstanceHolder{
		private List<String> mArrayList = new ArrayList<String>();
//		private List<String> mArrayList = Collections.synchronizedList(new ArrayList<String>());
		private static SingleInstanceHolder ins;

		public static SingleInstanceHolder getIns()
		{
			if (ins == null) {
				ins = new SingleInstanceHolder();
			}
			return ins;
		}

		private SingleInstanceHolder()
		{
		}

		public void addThing(String add)
		{
			if (!mArrayList.contains(add)) {
				mArrayList.add(add);
			}
		}

		public void removeThing(String remove)
		{
			boolean res = mArrayList.remove(remove);
			Log.d("andy", "remove result = " + res);
		}
	}
}
