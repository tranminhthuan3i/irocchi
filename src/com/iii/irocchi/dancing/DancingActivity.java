package com.iii.irocchi.dancing;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.iii.irocchi.R;
import com.iii.irocchi.common.CommonInfor;
import com.iii.irocchi.common.Main_Irocchi_Activity;
import com.iii.irocchi.irocchiflying.ViewThread;
import com.iii.irocchi.map.ShowMap_Activity;
import com.iii.irocchi.menu.MenuActivity;

public class DancingActivity extends Activity {

	/** Called when the activity is first created. */
	private MediaPlayer recorder = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_irocchi_dancing);

		// soundBG off
		if (Main_Irocchi_Activity.recorderBG.isPlaying()){
			Main_Irocchi_Activity.recorderBG.pause();
		}

		// add flag
		ImageView btnFlagDancing = (ImageView) findViewById(R.id.btnFlagDancing);
		if (CommonInfor.getCountryFlag(getApplicationContext(),
				CommonInfor.countryName) != null) {
			btnFlagDancing.setImageBitmap(CommonInfor.getCountryFlag(
					getApplicationContext(), CommonInfor.countryName));
		}
		// add Country name
		TextView txtCountryNameDacing = (TextView) findViewById(R.id.txtCountryNameDacing);
		if (CommonInfor.countryName != "")
			txtCountryNameDacing.setText(CommonInfor.countryName);
		if (ShowMap_Activity.sex == 0) {
			ImageView btnSexDance = (ImageView) findViewById(R.id.btnSexDance);
			btnSexDance.setImageResource(R.drawable.temp_thumbnail_mimirin);
		}
		// add irocchi
		ImageView irocchiDancingRight = (ImageView) findViewById(R.id.irocchiDancingRight);
		ImageView irocchiDance = (ImageView) findViewById(R.id.irocchiDance);
		if (CommonInfor.getBitmapImageLast("Irocchi/Pictures/local/ICrop") != null) {

			irocchiDance.setImageBitmap(Bitmap.createScaledBitmap(CommonInfor
					.getBitmapImageLast("Irocchi/Pictures/local/ICrop"), 180,
					220, true));
			irocchiDancingRight.setImageResource(R.drawable.thuan);
		}

		checkOnTime();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (recorder == null) {
					recorder = MediaPlayer.create(getBaseContext(),
							R.raw.ir_comp_dance);
				}
				if (!recorder.isPlaying()) {
					recorder.start();
				}
			}
		});
	}

	public void checkOnTime() {

		new Thread(new Runnable() {
			int index = 0;

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (index < 8) {
					index++;

					System.out.println("DANCING.................");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				DancingThread.running = false;

				//turn on Sound background
				if(Main_Irocchi_Activity.recorderBG!=null){
					if(Main_Irocchi_Activity.recorderBG.isPlaying()){
						Main_Irocchi_Activity.recorderBG.prepareAsync();
						Main_Irocchi_Activity.recorderBG.start();
					}
				}
				Main_Irocchi_Activity.recorderBG.start();
				Intent intentGreeting = new Intent(DancingActivity.this,
						MenuActivity.class);
				startActivity(intentGreeting);
				finish();// III79 add
			}
		}).start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.finish();
	}

}