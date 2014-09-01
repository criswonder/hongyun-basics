package test.tao.harness;

import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.hongyunbasic.R;
//import com.googlecode.tesseract.android.TessBaseAPI;

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
//	public void testParsePng(){
//		TessBaseAPI baseApi=new TessBaseAPI();
//
//		//初始化tess
//		//android下面，tessdata肯定得放到sd卡里了
//		//如果tessdata这个目录放在sd卡的根目录
//		//那么path直接传入sd卡的目录
//		//eng就是英文，关于语言，按ISO 639-3标准的代码就行，具体请移步wiki
//		baseApi.init("/sdcard/tesseract", "eng");
//
//		//options是为了缩放图片，这个酌情缩放，图片小的话可以不缩放
//		BitmapFactory.Options options=new BitmapFactory.Options();
//		InputStream instream;
//		try {
//			instream = getAssets().open("new.png");
//			Bitmap bitmap = BitmapFactory.decodeStream(instream,null,options); 
//			instream.close();
//			baseApi.setImage(bitmap);
//			//根据Init的语言，获得ocr后的字符串
//			String text= baseApi.getUTF8Text();
//			
//			Toast.makeText(getApplicationContext(), text, 2000).show();
//			
//			//释放bitmap
//			baseApi.clear();
//			//------------------------------------------------------
////			Thread.sleep(3000);
////			instream = getAssets().open("shuibiao2.png");
////			bitmap = BitmapFactory.decodeStream(instream,null,options);
////			
////			baseApi.setImage(bitmap);
////			text= baseApi.getUTF8Text();
////			
////			Toast.makeText(getApplicationContext(), text, 2000).show();
//
//			//如果连续ocr多张图片，这个end可以不调用，但每次ocr之后，必须调用clear来对bitmap进行释放
//			//释放native内存
//			baseApi.end();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	public void testAlbumWidget(){
//		Intent intent = new Intent();
//		intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
//		sendBroadcast(intent );
		setContentView(new AlbumBadage(getApplicationContext()));
	}
	public void showPopupWindow(View anchor){
		View contentView = View.inflate(this, R.layout.search_filter, null);
		
		PopupWindow  mPopup = new PopupWindow(this);
		mPopup.setContentView(contentView);


//	        mPopup.showAsDropDown(getListView(),-200,-300);
	        mPopup.showAsDropDown(anchor,20,30);
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
			showPopupWindow(view);
			return false;
		}
		
	}
	class OnItemLongClickListenerTrue implements OnItemLongClickListener{
		
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			Log.d(TAG, "onItemLongClick true");
			showPopupWindow(view);
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
