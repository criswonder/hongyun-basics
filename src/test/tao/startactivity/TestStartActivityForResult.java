package test.tao.startactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class TestStartActivityForResult extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LinearLayout line = new LinearLayout(this);
		line.setOrientation(LinearLayout.VERTICAL);
		Button btn = new Button(this);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent("com.test.View");
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}
			}
		});
		btn.setText("startActivity");
		
		line.addView(btn);
		
		Button btn1 = new Button(this);
		btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent("com.test.View");
					startActivityForResult(intent,11);
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}
			}
		});
		btn1.setText("startActivityForActivity");
		line.addView(btn1);
		
		setContentView(line);
	}

}
