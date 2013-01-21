
package com.iii.irocchi.dancing;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * @author Tran Minh Thuan
 * 
 *         The Main thread which contains the game loop. The thread must have
 *         access to the surface view and holder to trigger events every game
 *         tick.
 */
public class DancingThread extends Thread {

	private static final String TAG = DancingThread.class.getSimpleName();

	// desired fps
	private final static int MAX_FPS = 50;
	// maximum number of frames to be skipped
	private final static int MAX_FRAME_SKIPS = 5;
	// the frame period
	private final static int FRAME_PERIOD = 1000 / MAX_FPS;

	/* Stuff for stats */

	// Surface holder that can access the physical surface
	private SurfaceHolder surfaceHolder;
	// The actual view that handles inputs
	// and draws to the surface
	private DancingPanel gamePanel;
	private long mElapsed;
	private long mStartime;
	private Matrix mtr1;
	// private Bitmap pic;

	// flag to hold game state
	public static volatile boolean  running=false;

	public void setRunning(boolean running) {
		DancingThread.running = running;
	}

	public DancingThread(SurfaceHolder surfaceHolder, DancingPanel gamePanel,
			Resources resources) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;
		mtr1 = new Matrix();
		// pic= BitmapFactory.decodeResource(resources, R.drawable.bluesky);

	}

	@Override
	public void run() {
		Canvas canvas;
		mStartime= System.currentTimeMillis();
		Log.d(TAG, "Starting game loop");
		// initialise timing elements for stat gathering
		// initTimingElements();

		long beginTime; // the time when the cycle begun
		long timeDiff; // the time it took for the cycle to execute
		int sleepTime; // ms to sleep (<0 if we're behind)
		int framesSkipped; // number of frames being skipped

		sleepTime = 0;

		while (running) {
			canvas = null;
			
			  System.out.println("THUAN---Runging");
			// try locking the canvas for exclusive pixel editing
			// in the surface
			try {
				canvas = this.surfaceHolder.lockCanvas();
				synchronized (surfaceHolder) {
					beginTime = System.currentTimeMillis();
					framesSkipped = 0; // resetting the frames skipped
					if(gamePanel.elaine1.getY()<0||gamePanel.elaine2.getY()<0) running = false;
					// update game state
					this.gamePanel.update(canvas);
					// render state to the screen
					// draws the canvas on the panel
					this.gamePanel.render1(canvas);
					this.gamePanel.animate(mElapsed);
					this.gamePanel.doDraw(mElapsed, canvas);
					mElapsed=System.currentTimeMillis()-mStartime;
					// if(gamePanel.elaine1.getY()>0 &&
					// gamePanel.elaine2.getY()>0) running=false;
					// calculate how long did the cycle take
					timeDiff = System.currentTimeMillis() - beginTime;
					// calculate sleep time
					sleepTime = (int) (FRAME_PERIOD - timeDiff);

					if (sleepTime > 0) {
						// if sleepTime > 0 we're OK
						try {
							// send the thread to sleep for a short period
							// very useful for battery saving
							Thread.sleep(sleepTime);
						} catch (InterruptedException e) {
						}
					}

					while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
						// we need to catch up
						this.gamePanel.update(canvas); // update without
														// rendering
						sleepTime += FRAME_PERIOD; // add frame period to check
													// if in next frame
						framesSkipped++;
					}

					// for statistics
					// calling the routine to store the gathered statistics

				}
			} finally {
				// in case of an exception the surface is not left in
				// an inconsistent state
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
			mStartime=System.currentTimeMillis();
		}
	}

}
