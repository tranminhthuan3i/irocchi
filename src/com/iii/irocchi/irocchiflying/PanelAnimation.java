package com.iii.irocchi.irocchiflying;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.iii.irocchi.R;
import com.iii.irocchi.greetingvoice.GreetingActivity;

public class PanelAnimation extends SurfaceView implements
		SurfaceHolder.Callback {

	public static float mWidth;
	public static float mHeight;

	private ViewThread mThread;
	private ArrayList<Irocchi> mElements = new ArrayList<Irocchi>();
	private int mElementNumber = 0;

	private Paint mPaint = new Paint();
	private boolean clicked = false;
	private Bitmap bmBackground, cloud,cloud1,cloud2;
	

	public PanelAnimation(Context context, AttributeSet atts) {
		super(context, atts);
		getHolder().addCallback(this);
		mThread = new ViewThread(this);
		mPaint.setColor(Color.WHITE);
		/*bmBackground = BitmapFactory.decodeResource(getResources(),
				R.drawable.backgrou);*/
		
		cloud = BitmapFactory.decodeResource(getResources(),
				R.drawable.bg_bottom);
		cloud1 = BitmapFactory.decodeResource(getResources(), R.drawable.cloud1);
		cloud2 = BitmapFactory.decodeResource(getResources(), R.drawable.cloud2);
	}

	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);

		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);
		return resizedBitmap;
	}

	public void doDraw(long elapsed, Canvas canvas) {

		//Bitmap bm = getResizedBitmap(bmBackground, (int) mHeight, (int) mWidth);

		/* ----------------------- drawing background ---------------*/
		canvas.drawColor(Color.rgb(135, 206, 250));
		canvas.drawBitmap(cloud, 30, mHeight - cloud.getHeight(), mPaint);
		canvas.drawBitmap(cloud1, 100, mHeight/2, mPaint);
		canvas.drawBitmap(cloud2, 300, 200, mPaint);
		canvas.drawBitmap(cloud2, 600, mHeight/2, mPaint);
		canvas.drawBitmap(cloud1, 900, mHeight/2+100, mPaint);
		canvas.drawBitmap(cloud1, 700, 100, mPaint);
		
		/*---------------------------------------------------*/
		
		synchronized (mElements) {
			for (Irocchi element : mElements) {
				element.doDraw(canvas);
			}
		}
		canvas.drawText("FPS: " + Math.round(1000f / elapsed) + " Elements: "
				+ mElementNumber, 10, 10, mPaint);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		mWidth = width;
		mHeight = height;
		mElements.add(new Irocchi(this.getContext(), 0, (int) mHeight - 20, 1));
		mElements.add(new Irocchi(this.getContext(), (int) mWidth - 20,
				(int) mHeight - 20, 2));

		mElementNumber = mElements.size();
	}
	int i = 0;
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!mThread.isAlive()) {
			mThread = new ViewThread(this);
			mThread.setRunning(true);
			mThread.start();
			
		
		}
	}


	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mThread.isAlive()) {
			mThread.setRunning(false);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		synchronized (mElements) {
			if (!clicked) {
				/*
				 * mElements.add(new Element(getResources(), (int) event.getX(),
				 * (int) event.getY(), 1)); mElements.add(new
				 * Element(getResources(), (int) event.getX(), (int)
				 * event.getY(), 2));
				 */

				clicked = true;
			}
		}
		return super.onTouchEvent(event);
	}

	public void animate(long elapsedTime) {
		synchronized (mElements) {
			for (Irocchi element : mElements) {
				element.animate(elapsedTime);
			}
		}
	}
}
