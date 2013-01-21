package com.iii.irocchi.irocchiflying;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.renderscript.Int2;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.iii.irocchi.R;
import com.iii.irocchi.common.CommonInfor;
import com.iii.irocchi.greetingvoice.GreetingActivity;
import com.iii.irocchi.map.ShowMap_Activity;
import com.iii.irocchi.menu.MenuActivity;

public class ActivityIrocchiFlying extends Activity {
	/** Called when the activity is first created. */
	private MediaPlayer m = new MediaPlayer();
	private int index = 0;
	private String PICTURE_FOLDER = "Irocchi/Pictures/local/ICrop";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_irocchi_flying);
		
		//Add flag here
		ImageView btnFlagFlying = (ImageView) findViewById(R.id.btnFlagFlying);
		if (CommonInfor.getCountryFlag(getApplicationContext(),
				CommonInfor.countryName) != null) {
			
			btnFlagFlying.setImageBitmap(CommonInfor.getCountryFlag(
					getApplicationContext(), CommonInfor.countryName));
		}
		if(CommonInfor.countryName!=""){
			TextView textView = (TextView) findViewById(R.id.txtCountryNameFlying);
			
			textView.setText(CommonInfor.countryName);
		}
		if(ShowMap_Activity.sex==0){
			ImageView btnSexFlying =(ImageView) findViewById(R.id.btnSexFlying);
			btnSexFlying.setImageResource(R.drawable.temp_thumbnail_mimirin);
		}
		
		ImageView irocchiFlyingRight = (ImageView) findViewById(R.id.irocchiFlyingRight);
		irocchiFlyingRight.setImageResource(R.drawable.thuan);
		ImageView imgCropIr = (ImageView) findViewById(R.id.btn_crop_irocchi);
		imgCropIr.setImageBitmap(getBitmapImageLast());
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				playAudioPlane();
			}
		});

		checkOnTime();
		//playAudioPlane();
	}

	public void checkOnTime() {

		new Thread(new Runnable() {
			

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (index < 14) {
					index++;
					//if(index==17) playAudioPlane();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				ViewThread.mRun = false;
				Intent intentGreeting = new Intent(ActivityIrocchiFlying.this,
						GreetingActivity.class);
				startActivity(intentGreeting);
				finish();//III79 add
			}
		}).start();
	}

	// Play audio file you have saved to SDCard
	// this method is temp file so it is not ...
	public void playAudioPlane() {
		try {

			if (m.isPlaying()) {
				m.stop();
				m.release();
				m = new MediaPlayer();
			}
			AssetFileDescriptor descriptor = getAssets()
					.openFd("airplane1.mp3");
			m.setDataSource(descriptor.getFileDescriptor(),
					descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();

			m.prepare();
			m.setLooping(false);
			m.setVolume(1f, 1f);
		
			m.start();
		} catch (Exception e) {
		}
	}
	private Bitmap getBitmapImageLast() {
		Bitmap bm1 = BitmapFactory.decodeResource(getResources(), 1);
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath, PICTURE_FOLDER);
		if (!file.exists()) {
			file.mkdirs();
		}
		File[] sdDirList = file.listFiles();
		// Temp

		if (sdDirList != null) {

			bm1 = BitmapFactory.decodeFile(filepath + "/" + PICTURE_FOLDER
					+ "/"// temp
					+ sdDirList[sdDirList.length - 1].getName());
		}
		return bm1;
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		index=15;
		if(m!=null){
			m.stop();			
		}
		super.onPause();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		index=15;
		if(m!=null){
			m.stop();
			m=null;
		}
		super.onStop();
	}
	/* Play record*? */
}