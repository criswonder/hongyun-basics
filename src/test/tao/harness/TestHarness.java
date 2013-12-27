package test.tao.harness;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.zip.Adler32;

import android.app.ListActivity;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Contacts.People;
import android.taobao.common.TaoToolBox;
import android.taobao.imagebinder.ImageBinder;
import android.taobao.imagebinder.ImagePoolBinder;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.CookieManager;
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

	public void testCookieManagerOnNullUrl() {
		String cookie = CookieManager.getInstance().getCookie(null);
		System.out.println(cookie);
		Toast.makeText(getApplicationContext(), "fuckkkk", Toast.LENGTH_SHORT).show();
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
	
	public void testImageBinder(){
		mMainViewGroup.removeAllViews();
		ImageView image = new ImageView(this);
		mMainViewGroup.addView(image);
		
		ImageBinder mImagePoolBinder = new ImagePoolBinder(this.getClass().getName(), getApplication(), ImageGroup.PRIORITY_NORMAL,
				ImageCache.CACHE_CATEGORY_MRU);
		String url = "http://gw.alicdn.com/bao/uploaded/i4/17994026573597533/T1QU5oFldcXXXXXXXX_!!0-item_pic.jpg_90x90q90.jpg";
		mImagePoolBinder.setImageDrawable(url, image);
	}

	public void testSystemProperty(){
		String str = System.getProperty("http.agent");
		System.out.println(str);
	}
	public void testLoadSoFromAsserts(){
		SOManager soMgr = new SOManager(getApplicationContext());
		soMgr.loadInetSo();
		
		try {
			InputStream in=  getAssets().open("so/armeabi/libBSPatch.so");
			if(in.available()>0){
				byte[] data = new byte[in.available()];
				in.read(data);
				Adler32 a32 = new Adler32();
				a32.update(data);
				
				Log.e(TAG, ""+a32.getValue());
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			InputStream in=  getAssets().open("so/v7a/libBSPatch.so");
			if(in.available()>0){
				byte[] data = new byte[in.available()];
				in.read(data);
				Adler32 a32 = new Adler32();
				a32.update(data);
				
				Log.e(TAG, ""+a32.getValue());
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			InputStream in=  getAssets().open("so/x86/libBSPatch.so");
			if(in.available()>0){
				byte[] data = new byte[in.available()];
				in.read(data);
				Adler32 a32 = new Adler32();
				a32.update(data);
				
				Log.e(TAG, ""+a32.getValue());
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
