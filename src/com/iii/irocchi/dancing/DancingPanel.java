package com.iii.irocchi.dancing;

import java.util.ArrayList;

import com.iii.irocchi.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

/**
 * @author Tran Minh Thuan This is the main surface that handles the dancing
 *         Irocchi and draws the irocchi to the screen.
 */
public class DancingPanel extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = DancingPanel.class.getSimpleName();

	public static float mWidth;
	public static float mHeight;
	private ArrayList<Element> mElements = new ArrayList<Element>();
	private Paint p = new Paint();
	private DancingThread thread = null;
	public ElaineAnimated elaine1;
	public ElaineAnimated elaine2;
	private Bitmap m;
	// the fps to be displayed
	private String avgFps;

	public void setAvgFps(String avgFps) {
		this.avgFps = avgFps;
	}

	public DancingPanel(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		getHolder().addCallback(this);

		// adding the callback (this) to the surface holder to intercept events
		// create Elaine and load bitmap
		elaine1 = new ElaineAnimated(BitmapFactory.decodeResource(
				getResources(), R.drawable.dancered), 300, 520 // initial
				// position
				, 35, 47 // width and height of sprite
				, 5, 5); // FPS and number of frames in the animation

		elaine2 = new ElaineAnimated(BitmapFactory.decodeResource(
				getResources(), R.drawable.danceblue), 750, 580 // initial
																// position
				, 35, 47 // width and height of sprite
				, 4, 5);
		initializeNodes();
		// create the game loop thread
		thread = new DancingThread(getHolder(), this, null);

	}

	public void initializeNodes() {
		mElements.add(new Element(this.getResources(), 200, 500,
				R.drawable.nodemusic1));
		mElements.add(new Element(this.getResources(), 800, 600,
				R.drawable.nodemusic2));
		mElements.add(new Element(this.getResources(), 770, 360,
				R.drawable.nodemusic3));
		mElements.add(new Element(this.getResources(), 730, 360,
				R.drawable.nodemusic6));
		mElements.add(new Element(this.getResources(), 800, 600,
				R.drawable.nodemusic7));
		mElements.add(new Element(this.getResources(), 770, 360,
				R.drawable.nodemusic8));
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		mWidth = width;
		mHeight = height;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// at this point the surface is created and
		// we can safely start the game loop
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		boolean retry = true;

		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
		thread.setRunning(false);
		Log.d(TAG, "Thread was shut down cleanly");
	}

	public void Element(Resources res, int x, int y) {

		m = BitmapFactory.decodeResource(res, R.drawable.dancing_musical_note);
		mWidth = x - m.getWidth() / 2;
		mHeight = y - m.getHeight() / 2;
	}

	public void doDraw(long elapsed, Canvas canvas) {

		synchronized (mElements) {
			for (Element element : mElements) {
				element.doDraw(canvas);
			}
		}
	}

	int begin11 = getMeasuredWidth();
	int begin12 = getMeasuredHeight();

	public void render1(Canvas canvas1) {
		Paint paint1 = new Paint();
		paint1.setAntiAlias(true);
		paint1.setFilterBitmap(true);
		paint1.setDither(true);

		Bitmap background = BitmapFactory.decodeResource(getResources(),
				R.drawable.bluesky);
		float scale = (float) background.getHeight() / (float) getHeight();
		int newWidth = Math.round(background.getWidth() / scale);
		int newHeight = Math.round(background.getHeight() / scale);
		Bitmap scaled = Bitmap.createScaledBitmap(background, newWidth,
				newHeight, true);
		if (scaled != null)
			canvas1.drawBitmap(scaled, 0, 0, null);

		begin11--;
		begin12--;

		m = BitmapFactory.decodeResource(getResources(),
				R.drawable.dancing_musical_note2);
		if (begin11 < 0 && begin12 < 0) {
			begin11 = getMeasuredWidth();
			begin12 = getMeasuredHeight();
		}
		canvas1.drawBitmap(m, begin12, begin11, p);
		canvas1.drawBitmap(m, begin11, begin12, p);

		elaine1.draw(canvas1);
		elaine2.draw(canvas1);

	}

	public void animate(long elapsedTime) {
		synchronized (mElements) {
			for (Element element : mElements) {
				element.animate(elapsedTime);
			}
		}
	}

	public void update(Canvas canvas) {
		elaine1.update(System.currentTimeMillis());
		elaine2.update(System.currentTimeMillis());
	}

}
