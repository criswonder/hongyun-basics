package test.tao.harness;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.Adler32;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.taobao.imagebinder.ImageBinder;
import android.taobao.imagebinder.ImagePoolBinder;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hongyunbasic.R;
import com.taobao.tao.imagepool.ImageCache;
import com.taobao.tao.imagepool.ImageGroup;

public class TestHarness extends ListActivity {
	ListView listView;
	private View header;
	private TextView floatingHead;
	private Rect mVisibleRect = new Rect();
	private int mFloatingCategoryHeight;
	protected String TAG = "TestHarness";
	private ViewGroup mMainViewGroup;
	private static String[] GENRES = new String[] { "Action", "Adventure",
			"Animation", "Children", "Comedy", "Documentary", "Drama",
			"Foreign", "History", "Independent", "Romance", "Sci-Fi",
			"Television", "Thriller" };
	private static final String TEST_PREFIX = "test";
	private ArrayList<String> mMethodNames = new ArrayList<String>();
	private final int REQUEST_CODE_CONTACTS = 12;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    
		setContentView(R.layout.testharness);
		
		mMainViewGroup = (ViewGroup) findViewById(R.id.main);
		reflectionCallTestMethods();
		
		Log.e("StaticValue", TestStaticProperty.StaticValue+"");
	}

 
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		System.out.println(keyCode+"");
		return super.onKeyDown(keyCode, event);
	}
	private void reflectionCallTestMethods() {
		// setContentView(R.layout.testharness);
		Class thisClass;
		try {
			thisClass = Class.forName("test.tao.harness.TestHarness");
			Method[] methods = thisClass.getMethods();
			Method m;
			for (int i = 0; i < methods.length; i++) {
				m = methods[i];
				Log.d(TAG, m.getName());
				if (m.getName().toLowerCase().startsWith(TEST_PREFIX)) {
					mMethodNames.add(m.getName());
				}
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		GENRES = mMethodNames.toArray(new String[mMethodNames.size()]);
		System.out.println(GENRES);

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, GENRES));

		final ListView listView = getListView();

		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		Button runBtn = (Button) findViewById(R.id.btn_run);
		runBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SparseBooleanArray checked = listView.getCheckedItemPositions();
				for (int i = 0; i < GENRES.length; i++)
					if (checked.get(i)) {
						int pos = i;
						if (pos >= 0 && pos < GENRES.length) {
							runMethod(GENRES[pos]);
						} else {
							Toast.makeText(getApplicationContext(),
									"bad pos number", Toast.LENGTH_SHORT)
									.show();
						}
					}
			}
		});
	}

	/**
	 * @param string
	 */
	protected void runMethod(String string) {
		try {
			Method method = this.getClass().getDeclaredMethod(string, null);
			try {
				method.invoke(this, null);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CODE_CONTACTS:
			if (data != null) {
				Cursor c = null;
				Log.e("andymao", "data != null");
				try {
					Uri contactData = data.getData();
					c = getContentResolver().query(contactData, null, null,
							null, null);
					// 没有通讯录权限可能导致某些机型获取联系人异常，catch一下
					if (c != null && c.moveToFirst()) {
						try {
							int nameIndex = c
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
							int numberIndex = c
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

							String name = c.getString(nameIndex);
							String number = c.getString(numberIndex);
							;
							Log.e("andymao", name + ":" + number);

						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						Log.e("andymao",
								"!!!!!!!!!!!!!c != null && c.moveToFirst()");
					}

				} catch (SecurityException e) {
					Log.e("andymao", "SecurityException");
					e.printStackTrace();
				} catch (Exception e) {
					Log.e("andymao", "Exception");
					e.printStackTrace();
				} finally {
					if (c != null) {
						c.close();
					}
				}
			} else {
				Log.e("andymao", "data is nulll Exception");
			}
			break;

		default:
			break;
		}
	}

	public void testCookieManagerOnNullUrl() {
		String cookie = CookieManager.getInstance().getCookie(null);
		System.out.println(cookie);
		Toast.makeText(getApplicationContext(), "fuckkkk", Toast.LENGTH_SHORT)
				.show();
	}

	public void testTaoSearchListActivityFloatingTopbar() {
		mMainViewGroup.removeAllViews();
		mMainViewGroup.addView(View.inflate(getApplicationContext(),
				R.layout.activity_testharness_listview, null));

		listView = (ListView) findViewById(R.id.listView1);
		floatingHead = (TextView) findViewById(R.id.tv_head);
		header = View.inflate(getApplicationContext(),
				R.layout.layout_listview_header, null);
		mFloatingCategoryHeight = floatingHead.getMeasuredHeight();
		listView.addHeaderView(header);
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (floatingHead != null) {
					header.getLocalVisibleRect(mVisibleRect);
					ViewParent parent = header.getParent();
					String log1 = String.format(
							"mVisibleRect.bottom=%s,mVisibleRect.top=%d,"
									+ "mFloatingCategoryHeight=%d",
							mVisibleRect.bottom, mVisibleRect.top,
							mFloatingCategoryHeight);
					Log.d(TAG, log1);

					if (parent == null
							|| (mVisibleRect.bottom - mVisibleRect.top <= mFloatingCategoryHeight)
							|| header.getPaddingTop() < 0) {
						floatingHead.setVisibility(View.VISIBLE);
					} else {
						floatingHead.setVisibility(View.GONE);
					}

				}

			}
		});

		Cursor c = getContentResolver().query(People.CONTENT_URI, null, null,
				null, null);
		startManagingCursor(c);

		@SuppressWarnings("deprecation")
		ListAdapter adapter = new SimpleCursorAdapter(this,
		// Use a template that displays a text view
				android.R.layout.simple_list_item_1,
				// Give the cursor to the list adatper
				c,
				// Map the NAME column in the people database to...
				new String[] { People.NAME },
				// The "text1" view defined in the XML template
				new int[] { android.R.id.text1 });
		listView.setAdapter(adapter);
		listView.invalidate();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);

		if (hasFocus && floatingHead != null) {
			mFloatingCategoryHeight = floatingHead.getMeasuredHeight();
		}
	}

	// END
	// OF-------------------testTaoSearchListActivityFloatingTopbar--------------

	public void testStrictMode() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.detectNetwork().penaltyDialog().build();

		StrictMode.setThreadPolicy(policy);
		InputStream in = null;
		try {
			URL url = new URL("ftp://mirror.csclub.uwaterloo.ca/index.html");
			URLConnection urlConnection = url.openConnection();
			in = new BufferedInputStream(urlConnection.getInputStream());
			System.out.println(in.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}

	public void testImageBinder() {
		mMainViewGroup.removeAllViews();
		ImageView image = new ImageView(this);
		mMainViewGroup.addView(image);

		ImageBinder mImagePoolBinder = new ImagePoolBinder(this.getClass()
				.getName(), getApplication(), ImageGroup.PRIORITY_NORMAL,
				ImageCache.CACHE_CATEGORY_MRU);
		String url = "http://gw.alicdn.com/bao/uploaded/i4/17994026573597533/T1QU5oFldcXXXXXXXX_!!0-item_pic.jpg_90x90q90.jpg";
		mImagePoolBinder.setImageDrawable(url, image);
	}

	public void testSystemProperty() {
		String str = System.getProperty("http.agent");
		System.out.println(str);
	}

	public void testGetPhoneContact() {
		Intent intent = new Intent(Intent.ACTION_PICK,
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI);

		startActivityForResult(intent, REQUEST_CODE_CONTACTS);
	}

	public void testWebViewJumpToTaobao() {
		WebView web = new WebView(this);
		String url = "taobao://m.taobao.com/?point=%7B%22from%22%3A%22h5%22%2C%22url%22%3A%22http%253A%252F%252Fm.taobao.com%252F%22%2C%22h5_uid%22%3A%22j%2BlbC%2FS8zkgCAdIWThqNrd8Q%22%2C%22ap_uri%22%3A%22click_sb_v1_manual%22%7D";
		web.loadUrl(url);

		Intent intent = new Intent();
		intent.setData(Uri.parse(url));
		startActivity(intent);
	}

	public void testLoadSoFromAsserts(){
		SOManager soMgr = new SOManager(getApplicationContext());
		soMgr.loadInetSo();

		try {
			InputStream in = getAssets().open("so/armeabi/libBSPatch.so");
			if (in.available() > 0) {
				byte[] data = new byte[in.available()];
				in.read(data);
				Adler32 a32 = new Adler32();
				a32.update(data);

				Log.e(TAG, "" + a32.getValue());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			InputStream in = getAssets().open("so/v7a/libBSPatch.so");
			if (in.available() > 0) {
				byte[] data = new byte[in.available()];
				in.read(data);
				Adler32 a32 = new Adler32();
				a32.update(data);

				Log.e(TAG, "" + a32.getValue());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			InputStream in = getAssets().open("so/x86/libBSPatch.so");
			if (in.available() > 0) {
				byte[] data = new byte[in.available()];
				in.read(data);
				Adler32 a32 = new Adler32();
				a32.update(data);

				Log.e(TAG, "" + a32.getValue());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testGetMACAddress() {
		String macAddress = null;
		WifiManager wifiMgr = (WifiManager) getApplicationContext()
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
		if (null != info) {
			macAddress = info.getMacAddress();
		}
		if (null != macAddress) {
			macAddress = macAddress.replace(":", "");// mac地址把冒号符号去掉
		}
		System.out.println(macAddress);
	}

	//---------------------------------------------------------------------------
	//---------------------------------------------------------------------------
	/**
	 * use handler
	 */
	public void testThreadCommunication() {
		final MyThread t1 = new MyThread("fk_t1");
		final MyThread t2 = new MyThread("fk_t2");
		t1.runTask(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(800);
					Message msg = Message.obtain();
					msg.obj = "Abc it it it!!";
					t2.handler.sendMessage(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		t2.runTask(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(1800);
					Message msg = Message.obtain();
					msg.obj = "ABCDDDD it it it!!";
					t1.handler.sendMessage(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

	class MyThread extends Thread {
		Handler handler;

		/**
		 * 
		 */
		public MyThread(String tName) {
			super(tName);
			handler = new Handler(Looper.myLooper()) {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					System.out.println(this);
					System.out.println(msg.obj);
				}
			};
		}

		public void runTask(Runnable r) {
			r.run();
			this.start();
		}

	}
	//---------------------------------------------------------------------------
	//---------------------------------------------------------------------------

	public void testPackageInfo() {
		try {
			PackageInfo packageInfo = this.getPackageManager().getPackageInfo(
					this.getPackageName(),
					PackageManager.GET_ACTIVITIES
							| PackageManager.GET_RECEIVERS
							| PackageManager.GET_SERVICES
							| PackageManager.GET_PROVIDERS
							| PackageManager.GET_DISABLED_COMPONENTS);

			System.out.println(packageInfo.receivers);
			System.out.println(packageInfo.activities);
			System.out.println(packageInfo.services);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	//---------------------------------------------------------------------------
	//---------------------------------------------------------------------------
	
	static class Player implements Runnable {

		private CyclicBarrier cyclicBarrier;
		private int id;

		public Player(int id, CyclicBarrier cyclicBarrier) {
			this.cyclicBarrier = cyclicBarrier;
			this.id = id;
		}

		@Override
		public void run() {
			try {
				System.out.println("玩家" + id + "正在玩第一关...");
				cyclicBarrier.await();
				System.out.println("玩家" + id + "进入第二关...");
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
		}
	}

	public static void testCyclicBarrier() {
		CyclicBarrier cyclicBarrier = new CyclicBarrier(4, new Runnable() {

			@Override
			public void run() {
				System.out.println("所有玩家进入第二关！");
			}
		});

		for (int i = 0; i < 4; i++) {
			new Thread(new Player(i, cyclicBarrier)).start();
		}
	}
	
	//---------------------------------------------------------------------------
	//---------------------------------------------------------------------------
	
	
	public void testSendPackageChangeAction() {
		Intent i = new Intent(Intent.ACTION_PACKAGE_CHANGED);
		sendBroadcast(i);
		
	}
	
	public void testEnableDisabaleComponent() {
		try {
			PackageInfo packageInfo = this.getPackageManager().getPackageInfo(
					this.getPackageName(),
					PackageManager.GET_ACTIVITIES
							| PackageManager.GET_RECEIVERS
							| PackageManager.GET_SERVICES
							| PackageManager.GET_PROVIDERS
							| PackageManager.GET_DISABLED_COMPONENTS);

			System.out.println(packageInfo.receivers);
			System.out.println(packageInfo.activities);
			System.out.println(packageInfo.services);
			
			ComponentName componentName = new ComponentName(this.getPackageName(), "com.example.hongyunbasic.WidgetMy");
            this.getPackageManager().setComponentEnabledSetting(componentName,
                                                                   PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                                                   PackageManager.DONT_KILL_APP);
            
            
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}	
	/**
	 * 为了测试Region.OP的几个属性
	 * http://developer.android.com/reference/android/graphics/Region.Op.html
	 */
	public void testDrawRegionOP(){
		
		setContentView(new CustomeView(getApplicationContext()));
	}
	class CustomeView extends View{

		public CustomeView(Context context) {
			super(context);
		}
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			
//			canvas.save();
//			//canvas.clipRect(20, 20, 120, 120);
//			canvas.clipRect(80, 80, 200, 200, Op.XOR);
//			canvas.clipRect(210, 210, 480, 480, Op.XOR);
//			canvas.drawColor(Color.RED);
//			canvas.restore();
			
			Path p,p1;
			p = new Path();
			p.reset();
			p.moveTo(210, 800);
			p.quadTo(250, 750, 300, 730);
			p.lineTo(350, 650);
			p.lineTo(400, 630);
			p.quadTo(430, 660,480,680);
			p.lineTo(480, 800);
			p.close();//close 会把你绘制的区域的缺口链接上
//			
			canvas.save();
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(Color.GREEN);
			canvas.drawPath(p, paint);
			canvas.restore();
			
			p1 = new Path();
			p1.reset();
			p1.moveTo(210, 800);
			p1.lineTo(480, 680);
			p1.lineTo(480, 800);
			p1.close();
		
			canvas.save();
			canvas.clipPath(p);
			canvas.clipPath(p1, Op.DIFFERENCE); //如果这样，则剩下的区域就是p-p1
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(Color.GRAY);
			canvas.drawPath(p1,paint);//在p-p1区域里面绘制p1是没有效果的,p1已经不在画布上了
			canvas.restore();
			
		}
	}
	
	//---------------------------------------------------------------------------
	//---------------------------------------------------------------------------
	/**
	 * 采用锁测试，线程间的通信
	 * 使用CountdownLatch, 一个线程或多个线程等待其他线程运行达到某一目标后进行自己的下一步工作，
	 * 而被等待的“其他线程”达到这个目标后继续自己下面的任务。
	 */
	public void testThreadCommunication2(){
		final String TAG = "testThreadCommunication";
		final CountDownLatch lock = new CountDownLatch(4);
		final Vector<Integer> mResults = new Vector<Integer>();
		Thread main = new Thread("the_main"){
			@Override
			public void run() {
				super.run();
//				synchronized (lock) {
//					for(Integer obj:mResults){
//						Log.d(TAG, "obj="+obj);
//					}
//				}
				try {
					lock.await();
					for(Integer obj:mResults){
						Log.d(TAG, "obj="+obj);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		};
		main.start();
		
		Thread main1 = new Thread("the_main"){
			@Override
			public void run() {
				super.run();
//				synchronized (lock) {
//					for(Integer obj:mResults){
//						Log.d(TAG, "obj="+obj);
//					}
//				}
				try {
					lock.await();
					for(Integer obj:mResults){
						Log.d(TAG, "obj2="+obj);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		};
		main1.start();
		ExecutorService pool = Executors.newCachedThreadPool();
		for( int i=0;i<4;i++){
			pool.execute(new WorkThread(2000+i, lock,mResults));
		}
		
	}
	class WorkThread implements Runnable{
		CountDownLatch lock;
		Vector<Integer> r;
		int test;
		public WorkThread(int t,CountDownLatch l,Vector<Integer> result) {
			this.test = t;
			this.lock = l;
			this.r = result;
		}
		@Override
		public void run() {
			int sum =0;
			for(int i=0;i<test;i++){
				sum+=i;
			}
			r.add(new Integer(sum));
			if(lock!=null)
				lock.countDown();
			for(int i=0;i<2;i++){
				System.out.println("i am stilll running.......");
			}
		}
	} 
	
	/**
	 * 可以看出，线程不会自动退出
	 */
	public  void testThreadReentrant(){
		final ReentrantLock lock = new ReentrantLock();
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
//				TestThreadReentrant tester = new TestThreadReentrant();
//				tester.doSomething();
				int i =100,j=0;
				long start = System.currentTimeMillis();
//				synchronized(this){
//					while(System.currentTimeMillis()<(start+2000)){
//						j++;
//						try {
//							System.out.println("......."+j);
//						} catch (Exception e) {
//							e.printStackTrace();
//							return;
//						}
//					}
//					
//				}
				
				try {
					lock.lockInterruptibly();
					
					while(System.currentTimeMillis()<(start+3000)){
						j++;
						System.out.println("xxxxxxxxxxxx");
//						try {
//							Thread.sleep(100);
//						} catch (Exception e) {
//							Log.e(TAG, "error in sleep",e);
//						}
					}
					
				} catch (InterruptedException e) {
					Log.e(TAG, "error in lock",e);
				}finally{
					lock.unlock();
				}
			}
		});
		thread.start();
		
		try {
			Thread.sleep(500);
		} catch (Exception e) {
			Log.e(TAG, "error in sleep2",e);
		}
		
		thread.interrupt();
	}
	
	/**
	 * 两个线程间歇性的输出1，2，1，2
	 */
	public void testFunWitchThread(){
		final Object lock = new Object();
		Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				synchronized(lock){
					while(true){
						lock.notifyAll();
						try {
							Log.e(TAG, "before lock wait()");
							lock.wait();
							Log.e(TAG, "after lock wait()");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println("1");
					}
				}
				
			}
		});
		
		Thread t2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				synchronized(lock){
					while(true){
						lock.notifyAll();
						try {
							lock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println("2");
					}
					
				}
		}});
		
		t1.start();
//		try {
//			Log.e(TAG, "before join()");
//			t1.join();
//			Log.e(TAG, "after join()");
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		t2.start();
	}
	
	//---------------------------------------------------------------------------
	//---------------------------------------------------------------------------

	public void testChangeStaticValue(){
		TestStaticProperty.StaticValue =12;
		Log.e("StaticValue", TestStaticProperty.StaticValue+"");
	}
}
