package com.iii.irocchi.irocchiflying;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.sax.StartElementListener;

import com.iii.irocchi.R;
import com.iii.irocchi.common.CommonInfor;
import com.iii.irocchi.greetingvoice.GreetingActivity;

public class Irocchi {
	private float mX;
	private float mY;

	private int mSpeedX;
	private int mSpeedY;

	private int count = 0;
	private Bitmap mBitmapirocchi, mBitmapPlane;
	public boolean down = false;

	public boolean flag = false;
	private int icolor = 1;
	private Context mContext;
	private int tempCount=0;
	
	public Irocchi(Context context, int x, int y, int idColor) {
		// Random rand = new Random();
		icolor = idColor;
		
		mContext = context;
		if (idColor == 1) {
			mBitmapirocchi = BitmapFactory.decodeResource(
					mContext.getResources(), R.drawable.irocchiplying1);
			mBitmapPlane = BitmapFactory.decodeResource(
					mContext.getResources(), R.drawable.plane_left);
			mSpeedX = 3;// rand.nextInt(7) - 3;
			mSpeedY = -1;// rand.nextInt(7) - 3;
		} else {
			mBitmapirocchi = BitmapFactory.decodeResource(
					mContext.getResources(), R.drawable.irocchiplying2);
			mBitmapPlane = BitmapFactory.decodeResource(
					mContext.getResources(), R.drawable.plane_right);
			mSpeedX = -3;// //rand.nextInt(7) - 3;
			mSpeedY = -1;// rand.nextInt(7) - 3;
		}

		mX = x - mBitmapirocchi.getWidth() / 2;
		mY = y - mBitmapirocchi.getHeight() / 2;
		/*
		 * mSpeedX = //rand.nextInt(7) - 3; mSpeedY = rand.nextInt(7) - 3;
		 */
	}

	public void doDraw(Canvas canvas) {
		canvas.drawBitmap(mBitmapirocchi, mX, mY, null);

		if (down){
			
			if(icolor==2){
				canvas.drawBitmap(mBitmapPlane, mX - mBitmapirocchi.getWidth() / 2+50, mY + mBitmapirocchi.getHeight() - 50, null);
			}else{
				canvas.drawBitmap(mBitmapPlane, mX - mBitmapirocchi.getWidth() / 2+30
						, mY + mBitmapirocchi.getHeight() - 50, null);
			}
		}

	}

	/**
	 * @param elapsedTime
	 *            in ms.
	 */
	public void animate(long elapsedTime) {
		// initial path for irocchi here
		mX += mSpeedX * (elapsedTime / 8f);
		mY += mSpeedY * (elapsedTime / 8f);
		checkBorders();

	}

	private synchronized void checkBorders() {
		if (count <= 4) {
			if (mX <= 0) {
				mSpeedX = -mSpeedX;
				count++;
				mX = 0;
			} else if (mX + mBitmapirocchi.getWidth() >= PanelAnimation.mWidth) {
				mSpeedX = -mSpeedX;
				mX = PanelAnimation.mWidth - mBitmapirocchi.getWidth();
				count++;
			}
			if (mY <= 0) {
				mY = 0;
				mSpeedY = -mSpeedY;
				count++;
			}
			if (mY + mBitmapirocchi.getHeight() >= PanelAnimation.mHeight) {
				mSpeedY = -mSpeedY;
				count++;
				mY = PanelAnimation.mHeight - mBitmapirocchi.getHeight();
			}
		} else {

			if (mY < -100) {

				if (icolor == 1) {
					if (mX < PanelAnimation.mWidth / 2 - 100) {
						mX = 0;
						mY = PanelAnimation.mHeight / 2;
						mSpeedX = 2;
						mSpeedY = 0;
						down = true;
					}
				} else {
					if (mX > PanelAnimation.mWidth / 2 + 100) {
						mX = PanelAnimation.mWidth - 100;
						mY = PanelAnimation.mHeight / 2;
						mSpeedY = 0;
						mSpeedX = -2;
						down = true;
					}
				}
			} else {
				
				if (down && icolor == 1
						&& (mX > PanelAnimation.mWidth / 2 - 200)) {
					mSpeedY = 2;
					mSpeedX = 0;
					if (mY > PanelAnimation.mHeight
							+ mBitmapirocchi.getHeight())
						ViewThread.mRun = false;
					down = false;
				}
				if (down && icolor == 2
						&& (mX < PanelAnimation.mWidth / 2 + 100)) {
					mSpeedX = 0;
					mSpeedY = 2;
					tempCount++;	
					System.out.println(mY+"<---- toa do y cua Irr");
					if (mY > PanelAnimation.mHeight
							+ mBitmapirocchi.getHeight())
						ViewThread.mRun = false;
					down = false;
					
					if (mY > PanelAnimation.mHeight) {
						ViewThread.mRun=false;
					}
					
					System.out.println("Stop Thread here");
				}
			}
		}
	}
	
	// Play audio file you have saved to SDCard
	// this method is temp file so it is not ...
	/*private MediaPlayer m = new MediaPlayer();
	public void playAudioPlane() {
		try {

			if (m.isPlaying()) {
				m.stop();
				m.release();
				m = new MediaPlayer();
			}
			AssetFileDescriptor descriptor =mContext.getAssets()
					.openFd("airplane1.mp3");
			m.setDataSource(descriptor.getFileDescriptor(),
					descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();

			m.prepare();
			m.setLooping(false);
			m.setVolume(1f, 1f);
			m.setLooping(true);
			m.start();
		} catch (Exception e) {
		}
	}*/
}
