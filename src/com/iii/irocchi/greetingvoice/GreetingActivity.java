package com.iii.irocchi.greetingvoice;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iii.irocchi.R;
import com.iii.irocchi.common.CommonInfor;
import com.iii.irocchi.dancing.DancingActivity;
import com.iii.irocchi.irocchiflying.ActivityIrocchiFlying;
import com.iii.irocchi.irocchiflying.ViewThread;
import com.iii.irocchi.map.ShowMap_Activity;

public class GreetingActivity extends Activity {

	int mImageSource = 0;
	private String PICTURE_FOLDER = "Irocchi/Pictures/local/ICrop";
	private String AUDIO_RECORDER_FOLDER = "Irocchi/Audios/local";

	private ImageView imgBircBottomLeft;
	private ImageView imgBircBottomRight;

	private ImageButton imgBLeftirocchi;
	private ImageButton imgBRightirocchi;

	private MediaPlayer recorder = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_irocchi_greetingscreen);
		// add flag
		ImageView btnFlagGreeting = (ImageView) findViewById(R.id.btnFlagGreeting);
		if (CommonInfor.getCountryFlag(getApplicationContext(),
				CommonInfor.countryName) != null) {
			btnFlagGreeting.setImageBitmap(CommonInfor.getCountryFlag(
					getApplicationContext(), CommonInfor.countryName));
		}
		// add country name
		if (CommonInfor.countryName != "") {
			TextView textView = (TextView) findViewById(R.id.txtCountryNameGreeting);
			textView.setText(CommonInfor.countryName);
		}
		// add sex
		if (ShowMap_Activity.sex == 0) {
			ImageView btnSexGreeting = (ImageView) findViewById(R.id.btnSexGreeting);
			btnSexGreeting.setImageResource(R.drawable.temp_thumbnail_mimirin);
		}
		// add Irocchi Bottom Right
		ImageView imgBIRCRight = (ImageView) findViewById(R.id.imgBIRCRight);
		imgBIRCRight.setImageResource(R.drawable.thuan);
		//add Irocchi bottom Left
		imgBircBottomLeft = (ImageView) findViewById(R.id.imgBIRCLeftBottom);
		imgBircBottomLeft.setImageBitmap(getBitmapImageLast());
		
		// Return a Bitmap from folder
		imgBLeftirocchi = (ImageButton) findViewById(R.id.imgBlefttIrocchi);
		imgBLeftirocchi.setImageBitmap(getBitmapImageLast());
		imgBLeftirocchi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						playRecorder();
					}
				});
			}
		});
		imgBRightirocchi = (ImageButton) findViewById(R.id.imgBrightIrocchi);
		imgBRightirocchi.setImageResource(R.drawable.thuan);
		imgBRightirocchi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
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
		});
		// III79 add sau 10s thi no tu dong chuyen den man hinh dance
		playRecorder();
		checkOnTime();
	}

	// III79 add.
	public void checkOnTime() {

		new Thread(new Runnable() {
			int index = 0;

			@Override
			public void run() {
				// TODO Auto-generated method stub

				while (index < 4) {
					index++;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (recorder == null) {
					recorder = MediaPlayer.create(getBaseContext(),
							R.raw.ohayo01mayu);
				}
				if (!recorder.isPlaying()) {
					recorder.start();
				}
				//

				while (index < 9) {
					index++;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				ViewThread.mRun = false;
				Intent intentGreeting = new Intent(GreetingActivity.this,
						DancingActivity.class);
				startActivity(intentGreeting);
				finish();// III79 add
			}
		}).start();
	}

	private Bitmap getBitmapImageLast() {
		Bitmap bm1 = BitmapFactory.decodeResource(getResources(), mImageSource);
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

	public void playRecorder() {
		if (recorder == null) {
			// Temp
			String filepath = Environment.getExternalStorageDirectory()
					.getPath();
			File file = new File(filepath, AUDIO_RECORDER_FOLDER);
			if (!file.exists()) {
				file.mkdirs();
			}
			File[] sdDirList = file.listFiles();
			// Temp

			if (sdDirList != null) {

				Uri uri = Uri.parse(filepath + "/" + AUDIO_RECORDER_FOLDER
						+ "/"// temp
						+ sdDirList[sdDirList.length - 1].getName()); // temp

				MediaPlayer mediaPlayer = MediaPlayer.create(this, uri);
				Toast.makeText(getApplicationContext(), "Sound is playing",
						Toast.LENGTH_SHORT).show();

				try {
					mediaPlayer.prepare();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mediaPlayer.start();
			}
		}

	}
}
