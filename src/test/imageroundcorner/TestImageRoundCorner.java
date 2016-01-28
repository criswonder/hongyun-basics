package test.imageroundcorner;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;

import com.example.hongyunbasic.R;

public class TestImageRoundCorner extends Activity {

	private ImageView myImageView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testimageroundcorner);
		myImageView = (ImageView) findViewById(R.id.imageView1);
//		Bitmap photo = BitmapFactory.decodeResource(getResources(),
//				R.drawable.a);
		
		Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile("/storage/emulated/0/taobao/com.taobao.taobao/persist_images/1409577694239.png", options);
        int dw = options.outWidth / 768;
        int dh = options.outHeight /1024;
        int scale = Math.max(dw, dh);
        options = new Options();
        options.inDensity = (int) 2;
        options.inScaled = true;
        options.inPurgeable = true;
        
        options.inSampleSize = scale==0?1:scale;
        Log.d("andymao",String.format("dw=%d,dh=%d,scale=%d", dw,dh,scale));
        Bitmap mBitmap = null;
        try {
            mBitmap = BitmapFactory.decodeFile("/storage/emulated/0/taobao/com.taobao.taobao/persist_images/1409577694239.png", options);
        }catch(Exception e) {
            e.printStackTrace();
        }
        
		myImageView.setImageBitmap(mBitmap);
		// myImageView.setImageBitmap(createStarPhoto(500,400,photo));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private Bitmap createFramedPhoto(int x, int y, Bitmap image,
			float outerRadiusRat) {
		// 根据源文件新建一个darwable对象
		Drawable imageDrawable = new BitmapDrawable(image);

		// 新建一个新的输出图片
		Bitmap output = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		// 新建一个矩形
		RectF outerRect = new RectF(0, 0, x, y);

		// 产生一个红色的圆角矩形
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.RED);
		canvas.drawRoundRect(outerRect, outerRadiusRat, outerRadiusRat, paint);

		// 将源图片绘制到这个圆角矩形上
		// 详解见http://lipeng88213.iteye.com/blog/1189452
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		imageDrawable.setBounds(0, 0, x, y);
		canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
		imageDrawable.draw(canvas);
		canvas.restore();

		return output;
	}
}
