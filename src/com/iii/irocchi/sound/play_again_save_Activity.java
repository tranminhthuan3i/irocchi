package com.iii.irocchi.sound;

import java.io.File;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iii.irocchi.R;
import com.iii.irocchi.camera.CropActivity;
import com.iii.irocchi.common.CommonInfor;
import com.iii.irocchi.common.Main_Irocchi_Activity;
import com.iii.irocchi.common.playSound;
import com.iii.irocchi.configuration.ConfigurationDB;
import com.iii.irocchi.configuration.ConfigurationWS;
import com.iii.irocchi.map.ShowMap_Activity;
import com.iii.irocchi.selectcolor.SelectColorActivity;

public class play_again_save_Activity extends Activity {

	// Khai bÃƒÂ¡o biÃ¡ÂºÂ¿n hÃ¡ÂºÂ±ng Ã„â€˜Ã¡Â»Æ’ lÃ†Â°u Ã„â€˜Ã¡Â»â€¹nh
	// dÃ¡ÂºÂ¡ng file Audio
	private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
	private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
	private static final String AUDIO_RECORDER_FOLDER = "Irocchi/Audios/local";

	private MediaRecorder recorder = null;
	private int currentFormat = 0;

	private int output_formats[] = { MediaRecorder.OutputFormat.MPEG_4,
			MediaRecorder.OutputFormat.THREE_GPP };

	private String file_exts[] = { AUDIO_RECORDER_FILE_EXT_MP4,
			AUDIO_RECORDER_FILE_EXT_3GP };

	private boolean flag = true;
	private ImageButton recordButton = null;
	private ImageButton playButton = null;
	private ImageButton againButton;
	
	private ConfigurationDB mDB;
	private ConfigurationWS mWS;
	String URL = "http://117.6.131.222:81/irocchi/wsuploadaudio.php";
	String audioPath="Irocchi/Audios/local";
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_save_again);
		mWS = new ConfigurationWS(this);
		// add flag
		ImageView btnFlagAgainRecord = (ImageView) findViewById(R.id.btnAgainRecord);
		if (CommonInfor.getCountryFlag(getApplicationContext(),
				CommonInfor.countryName) != null) {
			btnFlagAgainRecord.setImageBitmap(CommonInfor.getCountryFlag(
					getApplicationContext(), CommonInfor.countryName));
		}

		if (CommonInfor.countryName != "") {
			TextView textView = (TextView) findViewById(R.id.txtCountryNameAgain);

			textView.setText(CommonInfor.countryName);
		}
		if (ShowMap_Activity.sex == 0) {
			ImageView btnSexAgain = (ImageView) findViewById(R.id.btnSexAgain);
			btnSexAgain.setImageResource(R.drawable.temp_thumbnail_mimirin);
		}

		ImageButton btnSave = (ImageButton) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//this funcion to save to SQLite
				
				//insertToSQliteDatabase();
				// this funcion to save to server
				//insertToServerDatabase();
				new playSound(getApplicationContext()).playButton();//add sound to button
				Intent intentsColor = new Intent(play_again_save_Activity.this,
						SelectColorActivity.class);
				startActivity(intentsColor);
				finish();

			}
		});

		/*
		 * ImageView btnOK1 = (ImageView) findViewById(R.id.btnRecord1);
		 * btnOK1.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub Intent intents = new Intent(play_again_save_Activity.this,
		 * CameraActivity.class); startActivity(intents); finish(); } });
		 */

		/*----------- Button cÃ¡ÂºÂ¯t Ã¡ÂºÂ£nh, Ã„â€˜Ã¡Â»Æ’ tÃ¡ÂºÂ¡m thÃ¡Â»ï¿½i  ------------------*/
		ImageView btnOK2 = (ImageView) findViewById(R.id.tempIDforCrop);
		btnOK2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new playSound(getApplicationContext()).playButton();//add sound to button
				Intent intents2 = new Intent(play_again_save_Activity.this,
						CropActivity.class);
				startActivity(intents2);
				finish();
			}
		});

		// TEMP
		ImageView btnOK3 = (ImageView) findViewById(R.id.select_Color);
		btnOK3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new playSound(getApplicationContext()).playButton();//add sound to button
				Intent intents3 = new Intent(play_again_save_Activity.this,
						SelectColorActivity.class);
				startActivity(intents3);
				finish();
			}
		});
		// Play recording
		playButton = (ImageButton) findViewById(R.id.btnPlay);
		playButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new playSound(getApplicationContext()).playButton();//add sound to button
				playRecorder();
			}
		});

		// Again recoding
		againButton = (ImageButton) findViewById(R.id.btnreRec);
		againButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new playSound(getApplicationContext()).playButton();//add sound to button
				Intent intent_again = new Intent(play_again_save_Activity.this,
						Media_Recorder_Activity.class);
				startActivity(intent_again);
				finish();
			}
		});
		// back to top I_10
		ImageButton btn_rcd_again_home = (ImageButton) findViewById(R.id.btn_rcd_again_home);
		btn_rcd_again_home.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new playSound(getApplicationContext()).playButton();//add sound to button
				finish();
			}
		});

		// back to preview page
		ImageButton btn_rcd_again_back = (ImageButton) findViewById(R.id.btn_rcd_again_back);
		btn_rcd_again_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent_again = new Intent(play_again_save_Activity.this,
						Media_Recorder_Activity.class);
				startActivity(intent_again);
				finish();
			}
		});

	}

	//III 79 SQLite
	public void insertToSQliteDatabase(){
		//insertDatabaseSQLite();
		mDB = new ConfigurationDB(this.getApplicationContext());
		mDB.OpenDB();

		boolean bl = mDB.insertInfoAudio(Main_Irocchi_Activity.myAcc.get_id(),
				CommonInfor.getFileName(audioPath), "1", 1, "OK");
		if (bl == true) {
			Toast.makeText(this, "insert into database success",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "false", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	//III 79 Server
	public void insertToServerDatabase(){

	/*	JSONObject json = new JSONObject();
		try {
			json.put("audio_name", CommonInfor.getFileName(audioPath));
			json.put("audio_description", "OK");
			json.put("audio_path", "1");
			json.put("audio_status", "1");
			json.put("user_name",
					Main_Irocchi_Activity.myAcc.getUser_Name());
			json.put("user_id", Main_Irocchi_Activity.myAcc.get_id());

			JSONArray jarr = mWS.connectWSPut_Get_Data(URL, json,
					"posts");

			for (int i = 0; i < jarr.length(); i++) {
				JSONObject element = jarr.getJSONObject(i);
				Log.i("Log : ", "element : " + element);
				String msg_images = element.getString("audios");
				String msg_user_image = element.getString("user_audio");
				Log.i("Log : ", "msg_images : " + msg_images);

				Log.i("Log : ", "msg_user_image : " + msg_user_image); // us
																		// =
																		// userName;

				Toast.makeText(this.getApplicationContext(), msg_images, Toast.LENGTH_LONG)
						.show();
			}

		} catch (JSONException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		boolean status = false;
		try {
			
			UploadAudioIrocchi uploadAudio = new UploadAudioIrocchi(
					getApplicationContext());
			uploadAudio.uploadAudio();
			status = true;
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"Error upload" + e.getMessage(), Toast.LENGTH_SHORT)
					.show();
		}
		if (status) {
			Toast.makeText(getApplicationContext(), "Upload success",
					Toast.LENGTH_SHORT).show();
		}
	}

	/*
	 * the System check amount of the SDCard, if great than 1MB then continous
	 * // else // stop recording
	 */

	// Play audio file you have saved to SDCard
	// this method is temp file so it is not ...
	public void playRecorder() {
		// if (recorder == null) {
		// Temp
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath, AUDIO_RECORDER_FOLDER);
		if (!file.exists()) {
			file.mkdirs();
		}
		File[] sdDirList = file.listFiles();
		// Temp

		if (sdDirList != null) {
			Log.d("THUAN22 : ", filepath + "/" + AUDIO_RECORDER_FOLDER + "/"// temp
					+ sdDirList[sdDirList.length - 1].getName()); // temp
			Uri uri = Uri.parse(filepath + "/" + AUDIO_RECORDER_FOLDER + "/"// temp
					+ sdDirList[sdDirList.length - 1].getName()); // temp

			MediaPlayer mediaPlayer = MediaPlayer.create(this, uri);
			Toast.makeText(getApplicationContext(), "Sound is playing",
					Toast.LENGTH_SHORT).show();

			/*try {
				//mediaPlayer.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			mediaPlayer.start();
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
			Toast.makeText(play_again_save_Activity.this,
					"Error: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
		}
	};
	private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
		@Override
		public void onInfo(MediaRecorder mr, int what, int extra) {
			Toast.makeText(play_again_save_Activity.this,
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
		stopRecording();
		super.onDestroy();
	}

}