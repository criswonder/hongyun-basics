package test.tao.harness;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by hongyunmhy on 15/9/9.
 */
public class Test20150909 extends TestBaseActivity implements AdapterView.OnItemClickListener {
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Log.d(TAG, "onItemClick");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.getListView().setOnItemClickListener(this);

	}

	public void testMethod3(){
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
		sendBroadcast(intent);
	}
}
