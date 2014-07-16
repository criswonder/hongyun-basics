package test.tao.harness;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class Test7July extends TestBaseActivity implements  OnItemClickListener {
	
	public void testMethod1(){
		this.getListView().setOnItemLongClickListener(new OnItemLongClickListenerFalse());
	}
	
	public void testMethod2(){
		this.getListView().setOnItemLongClickListener(new OnItemLongClickListenerTrue());
	}
	public void testMethod3(){
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
		sendBroadcast(intent );
	}
	public void testAlbumWidget(){
//		Intent intent = new Intent();
//		intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
//		sendBroadcast(intent );
		setContentView(new AlbumBadage(getApplicationContext()));
	}
	public void testMethod4(){
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.getListView().setOnItemClickListener(this);
		
	}
 
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.d(TAG, "onItemClick");
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
	
	public class AlbumBadage extends View {

		public AlbumBadage(Context context) {
			super(context);
		}
		public AlbumBadage(Context context, AttributeSet attrs) {
			super(context, attrs);
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
//			canvas.save();
//			canvas.rotate(45,0,0);
//			canvas.translate(0, -100);
			int side = 100;
			int height = (int)(Math.sqrt(2)*side/2);
			int textSize = 28;
			Path path = new Path();
			path.moveTo(0, side);
			path.lineTo(side, 0);
			path.lineTo(0, 0);
			Paint paint = new Paint();
			paint.setColor(getResources().getColor(android.R.color.holo_orange_light));
			canvas.drawPath(path, paint);
			
			canvas.save();
			canvas.rotate(-45,0,0);
			
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(getResources().getColor(android.R.color.black));
			paint.setTextSize(textSize);
			paint.setTextAlign(Align.CENTER);
			canvas.drawText("New", 0, (height+textSize)/2f , paint);
//			canvas.restore();
			canvas.restore();
		}
//		@Override
//		protected void onDraw(Canvas canvas) {
//			super.onDraw(canvas);
////			canvas.save();
//			canvas.rotate(45,0,0);
////			canvas.translate(0, -100);
//			Path path = new Path();
//			path.moveTo(0, 100);
//			path.lineTo(100, 0);
//			path.lineTo(200, 100);
//			Paint paint = new Paint();
//			paint.setColor(getResources().getColor(android.R.color.holo_orange_light));
//			canvas.drawPath(path, paint);
//			
////			canvas.save();
////			canvas.rotate(45);
//			
//			paint = new Paint();
//			paint.setColor(getResources().getColor(android.R.color.black));
//			paint.setTextSize(40);
//			paint.setTextAlign(Align.CENTER);
//			canvas.drawText("abc", 100, 80, paint);
////			canvas.restore();
////			canvas.restore();
//		}

	}
}
