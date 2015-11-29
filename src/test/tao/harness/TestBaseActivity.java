package test.tao.harness;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hongyunbasic.R;

public class TestBaseActivity extends ListActivity {
	private String[] testMethodArray = null;
	private static final String TEST_PREFIX = "test";
	private ArrayList<String> mMethodNames = new ArrayList<String>();

	protected String TAG = "TestHarness";
	protected ViewGroup mMainViewGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.testharness);

		mMainViewGroup = (ViewGroup) findViewById(R.id.main);
		reflectionCallTestMethods();

		Log.e("StaticValue", TestStaticProperty.StaticValue + "");
	}

	protected void reflectionCallTestMethods() {
		// setContentView(R.layout.testharness);
		Class thisClass;
		thisClass = this.getClass();
		Method[] methods = thisClass.getMethods();
		Method m;
		for (int i = 0; i < methods.length; i++) {
			m = methods[i];
			// Log.d(TAG, m.getName());
			if (m.getName().toLowerCase().startsWith(TEST_PREFIX)) {
				mMethodNames.add(m.getName());
			}
		}

		testMethodArray = mMethodNames.toArray(new String[mMethodNames.size()]);
		// System.out.println(testMethodArray);
		if (null == testMethodArray) {
			return;
		}
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice,
				testMethodArray));

		final ListView listView = getListView();

		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		Button runBtn = (Button) findViewById(R.id.btn_run);
		runBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SparseBooleanArray checked = listView.getCheckedItemPositions();
				for (int i = 0; i < testMethodArray.length; i++)
					if (checked.get(i)) {
						int pos = i;
						if (pos >= 0 && pos < testMethodArray.length) {
							runMethod(testMethodArray[pos]);
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
			Method method = this.getClass().getDeclaredMethod(string);
			try {
				method.invoke(this);
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
}
