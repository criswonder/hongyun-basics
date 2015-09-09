/**
 * 
 */
package com.xixi.component;

import com.example.hongyunbasic.R;
import com.xixi.utils.MiscUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.Arrays;

/**
 * @author HadesLee
 */
public class CheXianNumberView extends View implements Runnable {

    private static final String TAG = "CheXianNumberView";
	private int cellCount = 5;
    private int duration = 2000;
    private int maxDistance = 1000;
    private int sleepTime = 30;
    private Drawable bg;
    private Drawable cellBg;
    private Drawable cellMask;
    private float cellPadding;
    private int cellTopY;
    private int centerY;
    private Paint paint;

    private int number;
    private Thread thread;
    private final int textPadding = MiscUtil.getPxByDip(10);

    private float[] translates;// 每个数字的位置
    private LoopThread loopThread;

    /**
     * @param context
     */
    public CheXianNumberView(Context context) {
        super(context);
        initOther(null);
    }

    public CheXianNumberView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initOther(attrs);
    }

    public CheXianNumberView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initOther(attrs);
    }

    private void initOther(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CheXianNumberView);
            this.cellCount = typedArray.getInteger(R.styleable.CheXianNumberView_cellCount, cellCount);
            this.duration = typedArray.getInteger(R.styleable.CheXianNumberView_duration, duration);
            this.sleepTime = typedArray.getInteger(R.styleable.CheXianNumberView_sleepTime, sleepTime);
            this.maxDistance = typedArray.getInteger(R.styleable.CheXianNumberView_maxDistance, maxDistance);
            typedArray.recycle();
        }
        bg = getResources().getDrawable(R.drawable.bj_bg);
        cellBg = getResources().getDrawable(R.drawable.bj_num_bg);
        cellMask = getResources().getDrawable(R.drawable.bj_num_bg_mask);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.SUBPIXEL_TEXT_FLAG);
        paint.setColor(0xffffffff);
        paint.setTextSize(MiscUtil.getPxByDip(16));
        translates = new float[cellCount];// 每个数字的位置
    }

    public void stopLoop() {
        if (loopThread != null) {
            loopThread.destroy();
            loopThread = null;
        }
    }

    public void loop() {
        if (loopThread != null) {
            loopThread.destroy();
        }
        loopThread = new LoopThread(this, translates, -1000, -10);
        loopThread.start();
    }

    public void set(int cellCount, int duration, int maxDistance) {
        this.cellCount = cellCount;
        this.duration = duration;
        this.maxDistance = maxDistance;
        translates = new float[cellCount];// 每个数字的位置
        doSizeChanged(getWidth(), getHeight());
        postInvalidate();
    }

    public void setNumber(final int newNumber, final boolean animation) {

        post(new Runnable() {

            public void run() {
                stopLoop();
                number = newNumber;
                if (animation) {
                    doStartAnimation();
                } else {
                    translates = getNumberYLocations();
                }
                postInvalidate();
            }
        });
    }

    public void doStartAnimation() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }
    //18986
    private float[] getNumberYLocations() {
        float[] toHeights = new float[cellCount];
        for (int i = 0; i < cellCount; i++) {
            int width = cellCount - i;
            int value = (number % (int) Math.pow(10, width)) / (int) (Math.pow(10, width - 1));
            int height = 0;
            for (int index = 0; index < value; index++) {
                Rect rect = new Rect();
                paint.getTextBounds(String.valueOf(index), 0, 1, rect);
                height += rect.height() + textPadding;
            }
            toHeights[i] = centerY - height;
        }
        return toHeights;
    }

    @Override
    public void run() {
        final float[] toHeights = getNumberYLocations();
        try {
            DecelerateInterpolator inter = new DecelerateInterpolator(1f);

            long from = System.currentTimeMillis();

            float[] fromHeights = new float[toHeights.length];
            for (int i = 0; i < toHeights.length; i++) {
                fromHeights[i] = toHeights[i] - maxDistance;
            }
            while (true) {
                long now = System.currentTimeMillis();
                if (now - from >= duration) {
                    break;
                }
                float input = (now - from) * 1f / duration;
                float output = inter.getInterpolation(input);

                for (int i = 0; i < cellCount; i++) {
                    float deltaHeight = fromHeights[i] - toHeights[i];
                    Log.d(TAG, "deltaHeight="+deltaHeight);
                    float height = fromHeights[i] - output * deltaHeight;
                    translates[i] = height;
                }
                postInvalidate();
                Thread.sleep(sleepTime);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            translates = toHeights;
            postInvalidate();
            thread = null;
        }

    }

    private int getCellBgWidth() {
        return MiscUtil.getPxByDip(24);
    }

    private int getCellBgHeight() {
        return MiscUtil.getPxByDip(24);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "----------onSizeChanged-------------");
        doSizeChanged(w, h);
    }

    private void doSizeChanged(int w, int h) {
        bg.setBounds(0, 0, w, h);
        cellPadding = (w - getCellBgWidth() * cellCount) * 1.0f / (cellCount + 1);
        cellTopY = (h - getCellBgHeight()) / 2;
        centerY = h / 2;
        Arrays.fill(translates, centerY);
    }

    private void drawNumbers(Canvas canvas) {
	    for (int i = 0; i < translates.length; i++) {
	        canvas.save();
	        Rect numberBounds = getNumberBounds(i);
	        numberBounds.top = numberBounds.top + MiscUtil.getPxByDip(1);
	        canvas.clipRect(numberBounds);//这一句很重要，可以注释了看下效果
	        float startX = numberBounds.left + numberBounds.width() / 2;
	        float startY = translates[i];
	        Log.d(TAG, "start drawNumbers i="+i);
	        drawNumbersCenter(canvas, startX, startY);
	        canvas.restore();
	    }
	}

	private void drawNumbersCenter(Canvas canvas, float startX, float startY) {

        while (true) {
            for (int i = 0; i < 10; i++) {
                String text = String.valueOf(i);
                Rect bounds = new Rect();
                paint.getTextBounds(text, 0, text.length(), bounds);
                float x = startX - bounds.width() / 2;
                float y = startY + (bounds.height() / 2);
                if (y > -10) {
                   canvas.drawText(text, x, y, paint);
                }
                Log.d(TAG, "startY="+startY);
                startY += bounds.height() + textPadding;
                if (startY > getHeight()) {
                    return;
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBg(canvas);
        drawNumbers(canvas);
        drawMasks(canvas);
//        Log.d(TAG, "---------onDraw----------");
    }

    private void drawBg(Canvas canvas) {
        bg.draw(canvas);
        for (int i = 0; i < cellCount; i++) {
            cellBg.setBounds(getNumberBounds(i));
            cellBg.draw(canvas);
        }
    }

    private Rect getNumberBounds(int i) {
        int cellX = (int) (getCellBgWidth() * i + cellPadding * (i + 1));
        return new Rect(cellX, cellTopY, cellX + getCellBgHeight(), cellTopY + getCellBgHeight());
    }

    private void drawMasks(Canvas canvas) {
        for (int i = 0; i < cellCount; i++) {
            cellMask.setBounds(getNumberBounds(i));
            cellMask.draw(canvas);
        }
    }

    private static class LoopThread implements Runnable {

        private volatile boolean go;
        private Thread thread;
        private View view;
        private float[] translates;
        private float from, to;
        private float step = 10;

        public LoopThread(View view, float[] translates, float from, float to) {
            this.view = view;
            this.translates = translates;
            if (from > to) {
                throw new IllegalArgumentException("from=" + from + ",to=" + to);
            }
            this.from = from;
            this.to = to;
        }

        public void start() {
            if (thread == null) {
                go = true;
                thread = new Thread(this);
                thread.start();
            }
        }

        public void destroy() {
            if (thread != null) {
                go = false;
                thread.interrupt();
            }
        }

        @Override
        public void run() {
            try {
                while (go) {
                    for (int i = 0; i < translates.length; i++) {
                        if (translates[i] >= to - step) {
                            translates[i] = from;
                        } else {
                            translates[i] += step;
                        }
                    }
                    Thread.sleep(30);
                    view.postInvalidate();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
