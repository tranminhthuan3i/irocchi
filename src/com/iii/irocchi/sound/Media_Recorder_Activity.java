package com.iii.irocchi.sound;

import java.io.File;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iii.irocchi.R;
import com.iii.irocchi.camera.CameraActivity;
import com.iii.irocchi.camera.CropActivity;
import com.iii.irocchi.cameracomposite.CameraCompositeActivity;
import com.iii.irocchi.common.CommonInfor;
import com.iii.irocchi.common.playSound;
import com.iii.irocchi.irocchiflying.ActivityIrocchiFlying;
import com.iii.irocchi.map.ShowMap_Activity;
import com.iii.irocchi.selectcolor.SelectColorActivity;

public class Media_Recorder_Activity extends Activity {

	// Khai bÃƒÂ¡o biÃ¡ÂºÂ¿n hÃ¡ÂºÂ±ng Ã„â€˜Ã¡Â»Æ’ lÃ†Â°u Ã„â€˜Ã¡Â»â€¹nh
	// dÃ¡ÂºÂ¡ng file Audio
	private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
	private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
	private static final String AUDIO_RECORDER_FOLDER = "Irocchi/Audios/local";

	private MediaRecorder recorder = null;
	private MediaPlayer mediaPlayer;
	private int currentFormat = 0;

	private int output_formats[] = { MediaRecorder.OutputFormat.MPEG_4,
			MediaRecorder.OutputFormat.THREE_GPP };

	private String file_exts[] = { AUDIO_RECORDER_FILE_EXT_MP4,
			AUDIO_RECORDER_FILE_EXT_3GP };

	private boolean flag = true;
	private boolean amount = true;

	private ImageButton recordButton = null;
	private ImageButton playButton = null;
	private ImageButton againButton, btnFlagRecord;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recorder_activity);

		// Button ghi file Audio.MP4
		recordButton = (ImageButton) findViewById(R.id.btnRecord);
		recordButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new playSound(getApplicationContext()).playButton();//add sound to button
				Boolean isSDPresent = android.os.Environment
						.getExternalStorageState().equals(
								android.os.Environment.MEDIA_MOUNTED);
				// NÃ¡ÂºÂ¿u SD-Card mÃƒÂ  tÃ¡Â»â€œn tÃ¡ÂºÂ¡i vÃƒÂ  cÃƒÂ²n
				// trÃ¡Â»â€˜ng thÃƒÂ¬ cho phÃƒÂ©p ghi
				if (isSDPresent) {
					// yes SD-card is present
					if (getAvailableSpaceInMB() > 1) {
						// change Button UI when user press
						changeUI();
						if (flag) {
							// to check if amount >1 then recording
							checkAmount();
							// Start recording when user press recordbutton
							startRecording();
						} else {
							stopRecording();
							Intent intent_next = new Intent(
									Media_Recorder_Activity.this,
									play_again_save_Activity.class);
							startActivity(intent_next);
							finish();

						}
						flag = !flag;

					} else
						Toast.makeText(
								Media_Recorder_Activity.this,
								"Sorry SD card is not enought amount to recording",
								Toast.LENGTH_SHORT).show();
				} else {
					// Sorry
					Toast.makeText(
							Media_Recorder_Activity.this,
							"Sorry SD card is not exist. you can not be recording",
							Toast.LENGTH_SHORT).show();
				}

			}

		});

		/*----------- Button chÃ¡Â»Â¥p Ã¡ÂºÂ£nh, Ã„â€˜Ã¡Â»Æ’ tÃ¡ÂºÂ¡m thÃ¡Â»ï¿½i  ------------------*/

		/*ImageView btnOK1 = (ImageView) findViewById(R.id.btnRecord1);
		btnOK1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intents = new Intent(Media_Recorder_Activity.this,
						CameraActivity.class);
				startActivity(intents);
				finish();
			}
		});*/

		// temp Flying
		ImageView btnOKFly = (ImageView) findViewById(R.id.fly);
		btnOKFly.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new playSound(getApplicationContext()).playButton();//add sound to button
				Intent intents = new Intent(Media_Recorder_Activity.this,
						ActivityIrocchiFlying.class);
				startActivity(intents);
				finish();
			}
		});

		/*----------- Button cÃ¡ÂºÂ¯t Ã¡ÂºÂ£nh, Ã„â€˜Ã¡Â»Æ’ tÃ¡ÂºÂ¡m thÃ¡Â»ï¿½i  ------------------*/
		ImageView btnBackI10 = (ImageView) findViewById(R.id.btnBack1);
		btnBackI10.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new playSound(getApplicationContext()).playButton();//add sound to button
				// TODO Auto-generated method stub
				Intent intents2 = new Intent(Media_Recorder_Activity.this,
						ShowMap_Activity.class);
				startActivity(intents2);
				finish();
			}
		});
		
		// add Flag();
		ImageView btnFlagRecord = (ImageView) findViewById(R.id.btnFlagRecord);
		
		if (CommonInfor.getCountryFlag(getApplicationContext(),
				CommonInfor.countryName) != null) {
			btnFlagRecord.setImageBitmap(CommonInfor.getCountryFlag(
					getApplicationContext(), CommonInfor.countryName));
		}
		if(CommonInfor.countryName!=""){
			TextView textView = (TextView) findViewById(R.id.txtCountryNameRecord);
			
			textView.setText(CommonInfor.countryName);
		}
		if(ShowMap_Activity.sex==0){
			ImageView btnSexRecord =(ImageView) findViewById(R.id.btnSexRecord);
			btnSexRecord.setImageResource(R.drawable.temp_thumbnail_mimirin);
		}
		
		// this page to select color
		ImageView btnOK3 = (ImageView) findViewById(R.id.select_Color);
		btnOK3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new playSound(getApplicationContext()).playButton();//add sound to button
				Intent intents3 = new Intent(Media_Recorder_Activity.this,
						SelectColorActivity.class);
				startActivity(intents3);
				finish();
			}
		});

		// camera composte
		ImageView btnOK4 = (ImageView) findViewById(R.id.cameracomposite);
		btnOK4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new playSound(getApplicationContext()).playButton();//add sound to button
				Intent intents3 = new Intent(Media_Recorder_Activity.this,
						CameraCompositeActivity.class);
				startActivity(intents3);
				finish();
			}
		});

		// back to top I_10
		ImageButton btn_rcd_home = (ImageButton) findViewById(R.id.btn_rcd_home);
		btn_rcd_home.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new playSound(getApplicationContext()).playButton();//add sound to button
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	/*-----PhÃ†Â°Ã†Â¡ng thÃ¡Â»Â©c thay Ã„â€˜Ã¡Â»â€¢i giao diÃ¡Â»â€¡n nÃƒÂºt bÃ¡ÂºÂ¥m Recording--------*/
	private void changeUI() {

		runOnUiThread(new Runnable() {
			public void run() {
				if (flag) {
					recordButton.setImageResource(R.drawable.temp_btn_rec_end);
					Toast.makeText(Media_Recorder_Activity.this,
							"Start Recording", Toast.LENGTH_SHORT).show();
				} else {
					recordButton
							.setImageResource(R.drawable.temp_btn_rec_start);
					Toast.makeText(Media_Recorder_Activity.this,
							"Stop Recording", Toast.LENGTH_SHORT).show();

				}
			}
		});
	}

	/*
	 * the System check amount of the SDCard, if great than 1MB then continous
	 * // else // stop recording
	 */
	private void checkAmount() {
		amount = true;

		Thread d = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (amount) {

					if (getAvailableSpaceInMB() < 1) {
						amount = false;

						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(Media_Recorder_Activity.this,
										"isn't enought to recording continuos",
										Toast.LENGTH_SHORT).show();
								recordButton
										.setImageResource(R.drawable.picture);
								/*
								 * ((TextView) findViewById(R.id.textView3))
								 * .setText("Start");
								 */
								stopRecording();

							}
						});

					}
				}
			}
		});
		d.start();
	}

	// Play audio file you have saved to SDCard
	// this method is temp file so it is not ...
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
				Log.d("THUAN22 : ", filepath + "/" + AUDIO_RECORDER_FOLDER
						+ "/"// temp
						+ sdDirList[sdDirList.length - 1].getName()); // temp
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

	/* Play record*? */

	/*
	 * -----PhÃ†Â°Ã†Â¡ng thÃ¡Â»Â©c trÃ¡ÂºÂ£ vÃ¡Â»ï¿½ tÃƒÂªn Ã„â€˜Ã†Â°Ã¡Â»ï¿½ng
	 * dÃ¡ÂºÂ«n Ã„â€˜Ã¡Â»Æ’ lÃ†Â°u file audio ----
	 */
	private String getFilename() {
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath, AUDIO_RECORDER_FOLDER);
		if (!file.exists()) {
			file.mkdirs();
		}
		return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat]);
	}

	/* -------- PhÃ†Â°Ã†Â¡ng thÃ¡Â»Â©c dÃ¡Â»Â«ng Recording ------------------ */
	private void stopRecording() {
		amount = false;
		if (null != recorder) {
			recorder.stop();
			recorder.reset();
			recorder.release();
			recorder = null;
		}

		System.out.print(getFilename());
	}

	/*------------- BÃ¡ÂºÂ¯t Ã„â€˜Ã¡ÂºÂ§u ghi (Recording) -------------*/
	private void startRecording() {

		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(output_formats[currentFormat]);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOutputFile(getFilename());
		recorder.setOnErrorListener(errorListener);
		recorder.setOnInfoListener(infoListener);
		try {
			recorder.prepare();
			recorder.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
		@Override
		public void onError(MediaRecorder mr, int what, int extra) {
			Toast.makeText(Media_Recorder_Activity.this,
					"Error: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
		}
	};
	private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
		@Override
		public void onInfo(MediaRecorder mr, int what, int extra) {
			Toast.makeText(Media_Recorder_Activity.this,
					"Warning: " + what + ", " + extra, Toast.LENGTH_SHORT)
					.show();
		}
	};

	// Check amount of SD Card to determine recording else.

	/**
	 * @return Number of bytes available on External storage
	 */
	public static long getAvailableSpaceInBytes() {
		long availableSpace = -1L;
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		availableSpace = (long) stat.getAvailableBlocks()
				* (long) stat.getBlockSize();

		return availableSpace;
	}

	/**
	 * @return Number of kilo bytes available on External storage
	 */
	public static long getAvailableSpaceInKB() {
		final long SIZE_KB = 1024L;
		long availableSpace = -1L;
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		availableSpace = (long) stat.getAvailableBlocks()
				* (long) stat.getBlockSize();
		return availableSpace / SIZE_KB;
	}

	/**
	 * @return Number of Mega bytes available on External storage
	 */
	public static long getAvailableSpaceInMB() {
		final long SIZE_KB = 1024L;
		final long SIZE_MB = SIZE_KB * SIZE_KB;
		long availableSpace = -1L;
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		availableSpace = (long) stat.getAvailableBlocks()
				* (long) stat.getBlockSize();
		return availableSpace / SIZE_MB;
	}

	/**
	 * @return Number of gega bytes available on External storage
	 */
	public static long getAvailableSpaceInGB() {
		final long SIZE_KB = 1024L;
		final long SIZE_GB = SIZE_KB * SIZE_KB * SIZE_KB;
		long availableSpace = -1L;
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		availableSpace = (long) stat.getAvailableBlocks()
				* (long) stat.getBlockSize();
		return availableSpace / SIZE_GB;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		amount = false;
		stopRecording();
		super.onDestroy();
	}

}