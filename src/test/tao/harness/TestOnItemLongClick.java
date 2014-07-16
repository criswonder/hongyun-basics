package test.tao.harness;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TestOnItemLongClick extends Activity implements OnItemClickListener {
	public static final String TAG = "TestOnItemLongClick";
	ListView mListView;
	String[] testData = {"AAAAAAAAA","BBBBBBBB","CCCCCCCCC","DDDDDDDDDD"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mListView = new ListView(this);
		mListView.setAdapter(new MyListAdapter());
		mListView.setOnItemLongClickListener(new OnItemLongClickListenerFalse());
		mListView.setOnItemClickListener(this);
		FrameLayout.LayoutParams listViewLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);
		FrameLayout fLayout = new FrameLayout(getApplicationContext());
		fLayout.addView(mListView, listViewLayoutParams);
		
		//add btn
		Button btn = new Button(getApplicationContext());
		btn.setText("hit me");
		FrameLayout.LayoutParams btnFrameLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		btnFrameLayoutParams.gravity =Gravity.BOTTOM;
		fLayout.addView(btn, btnFrameLayoutParams);
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mListView.getOnItemLongClickListener() instanceof OnItemLongClickListenerFalse){
					mListView.setOnItemLongClickListener(new OnItemLongClickListenerTrue());
				}else{
					mListView.setOnItemLongClickListener(new OnItemLongClickListenerFalse());
				}
			}
		});
		
		setContentView(fLayout);
		
	}
	
	class MyListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return testData.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return testData[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView tv = new TextView(getApplicationContext());
			tv.setText(getItem(position)+"");
			tv.setTextColor(Color.GRAY);
			tv.setHeight(100);
			return tv;
		}
		
	} 
	
	class OnItemLongClickListenerFalse implements OnItemLongClickListener{

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			Log.d(TAG, "onItemLongClick false");
			return false;
		}
		
	}
	class OnItemLongClickListenerTrue implements OnItemLongClickListener{
		
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			Log.d(TAG, "onItemLongClick true");
			return true;
		}
		
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.d(TAG, "onItemClick ");
		
	}
}
