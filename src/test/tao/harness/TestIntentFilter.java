package test.tao.harness;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TestIntentFilter extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView tv = new TextView(this);
		tv.setText("fkkkk");
		setContentView(tv);

		try {
			tv.setText(getIntent().getDataString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
