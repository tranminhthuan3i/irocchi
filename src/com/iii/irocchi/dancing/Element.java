package com.iii.irocchi.dancing;

import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import com.iii.irocchi.R;
public class Element {
   private float mX;
    private float mY;
    
    private int mSpeedX;
    private int mSpeedY;
    
    private Bitmap mBitmap;
    
    public Element(Resources res, int x, int y,int music) {
        Random rand = new Random();
        mBitmap = BitmapFactory.decodeResource(res, music);
        mX = x - mBitmap.getWidth() / 2;
        mY = y - mBitmap.getHeight() / 2;
        mSpeedX = rand.nextInt(7) - 3;
        mSpeedY = rand.nextInt(7) - 3;
    }
    
    public void doDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, mX, mY, null);
    }
  
    public void animate(long elapsedTime) {
        mX += mSpeedX * (elapsedTime / 20f);
        mY += mSpeedY * (elapsedTime / 20f);
        checkBorders();
    }

    private void checkBorders() {
        if (mX <= 0) {
            mSpeedX = -mSpeedX;
            mX = 0;
        } else if (mX + mBitmap.getWidth() >= DancingPanel.mWidth) {
            mSpeedX = -mSpeedX;
            mX = DancingPanel.mWidth - mBitmap.getWidth();
        }
        if (mY <= 0) {
            mY = 0;
            mSpeedY = -mSpeedY;
        }
        if (mY + mBitmap.getHeight() >= DancingPanel.mHeight) {
            mSpeedY = -mSpeedY;
            mY = DancingPanel.mHeight - mBitmap.getHeight();
        }
    }
}
