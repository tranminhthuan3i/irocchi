package com.iii.irocchi.irocchiflying;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class ViewThread extends Thread {
    private PanelAnimation mPanel;
    private SurfaceHolder mHolder;
    public static volatile boolean mRun = false;
private long mStartTime;
private long mElapsed;
    
    public ViewThread(PanelAnimation panel) {
        mPanel = panel;
        mHolder = mPanel.getHolder();
    }
    
    public void setRunning(boolean run) {
        mRun = run;
    }
    
    
    @Override
    public void run() {
        Canvas canvas = null;
        mStartTime = System.currentTimeMillis();
        while (mRun) {
        	//setRunning(false);
            canvas = mHolder.lockCanvas();
            if (canvas != null) {
                mPanel.animate(mElapsed);
                mPanel.doDraw(mElapsed, canvas);
                mElapsed = System.currentTimeMillis() - mStartTime;
                mHolder.unlockCanvasAndPost(canvas);
              
            }
            mStartTime = System.currentTimeMillis();
        }
    }
}