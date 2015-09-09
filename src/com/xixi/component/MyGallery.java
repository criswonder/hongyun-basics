package com.xixi.component;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Gallery;

public class MyGallery extends Gallery {
	public float scale = getResources().getDisplayMetrics().density;

	public float FLINGTHRESHOLD = (int) (20.0f * scale + 0.5f);
	public MyGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		 Log.d("OnePageGallery:onFling:",String.format("VX:%f, VY:%f", velocityX, velocityY));
         // cap the velocityX to scroll only one page
        /* if (velocityX>FLINGTHRESHOLD) {
                 return super.onFling(e1, e2, FLINGTHRESHOLD, velocityY);
         } else if (velocityX<-FLINGTHRESHOLD) {
                 return super.onFling(e1, e2, -FLINGTHRESHOLD, velocityY);
         } else {
                 return super.onFling(e1, e2, velocityX, velocityY);
         }*/
		 //return super.onFling(e1, e2, velocityX, velocityY);
		 return true;
	}

}
