package test.tao.harness;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.zip.Adler32;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Rect;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
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
		setContentView(R.layout.testharness);
		mMainViewGroup = (ViewGroup) findViewById(R.id.main);
		reflectionCallTestMethods();
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

	public void testLoadSoFromAsserts() {
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

	public void testThreadCommunicationUseLock() {

	}

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
}
