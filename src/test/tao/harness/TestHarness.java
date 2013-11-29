package test.tao.harness;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.app.ListActivity;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hongyunbasic.R;

public class TestHarness extends ListActivity {
	ListView listView;
	private View header;
	private TextView floatingHead;
	private Rect mVisibleRect = new Rect();
	private int mFloatingCategoryHeight;
	protected String TAG="TestHarness";

	private static String[] GENRES = new String[] {
        "Action", "Adventure", "Animation", "Children", "Comedy", "Documentary", "Drama",
        "Foreign", "History", "Independent", "Romance", "Sci-Fi", "Television", "Thriller"
    };
	private static final String TEST_PREFIX = "test";
	private ArrayList<String> mMethodNames = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		testTaoSearchListActivityFloatingTopbar();
		
//		String cookie = CookieManager.getInstance().getCookie(url);
		setContentView(R.layout.testharness);

     
        
        Class thisClass;
		try {
			thisClass = Class.forName("test.tao.harness.TestHarness");
		    Method[] methods = thisClass.getMethods();
	        Method m;
	        for(int i=0;i<methods.length;i++){
	        	m = methods[i];
	        	Log.d(TAG, m.getName());
	        	if(m.getName().toLowerCase().startsWith(TEST_PREFIX)){
	        		mMethodNames.add(m.getName());
	        	}
	        }
	        
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    
        GENRES =  mMethodNames.toArray(new String[mMethodNames.size()]);
        System.out.println(GENRES);
        
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, GENRES));
        
        final ListView listView = getListView();

        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
        Button runBtn = (Button)findViewById(R.id.btn_run);
        runBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SparseBooleanArray checked = listView.getCheckedItemPositions();
				for (int i = 0; i < GENRES.length; i++)
					 if (checked.get(i)) {
						int pos = i;
						if(pos>=0 && pos<GENRES.length){
							runMethod(GENRES[pos]);
						}else{
							Toast.makeText(getApplicationContext(), "bad pos number", Toast.LENGTH_SHORT).show();
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
			Method method=this.getClass().getDeclaredMethod(string,null);
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

	public void test12(){
		Toast.makeText(getApplicationContext(), "test12", Toast.LENGTH_SHORT).show();
	}
	public void test13(){
		Toast.makeText(getApplicationContext(), "test13", Toast.LENGTH_SHORT).show();
		
	}
	public void test14(){
		Toast.makeText(getApplicationContext(), "test14", Toast.LENGTH_SHORT).show();
		
	}
	private void test15(){
		Toast.makeText(getApplicationContext(), "test15", Toast.LENGTH_SHORT).show();
		
	}
	
	private void testTaoSearchListActivityFloatingTopbar() {
		setContentView(R.layout.activity_testharness_listview);
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
					String log1 = String.format("mVisibleRect.bottom=%s,mVisibleRect.top=%d," +
							"mFloatingCategoryHeight=%d", 
							mVisibleRect.bottom,mVisibleRect.top,mFloatingCategoryHeight);
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
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		
		if(hasFocus && floatingHead!=null){
			mFloatingCategoryHeight = floatingHead.getMeasuredHeight();
		}
	}

}
