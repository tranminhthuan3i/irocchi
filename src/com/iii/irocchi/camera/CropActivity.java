package com.iii.irocchi.camera;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iii.irocchi.R;
import com.iii.irocchi.account.Account_detail;
import com.iii.irocchi.common.CommonInfor;
import com.iii.irocchi.common.Main_Irocchi_Activity;
import com.iii.irocchi.common.playSound;
import com.iii.irocchi.configuration.ConfigurationDB;
import com.iii.irocchi.configuration.ConfigurationWS;
import com.iii.irocchi.irocchiflying.ActivityIrocchiFlying;
import com.iii.irocchi.map.ShowMap_Activity;
import com.iii.irocchi.selectcolor.SelectColorActivity;
import com.iii.irocchi.sound.Media_Recorder_Activity;

public class CropActivity extends Activity {
	private Bitmap bm, bitmapCrop;
	private ImageView img;
	private static final String PICTURE_FOLDER = "Irocchi/Pictures/local/ICrop";
	int user_id;
	private ImageButton imgBBack;
	private ImageButton imgBHome;
	ConfigurationDB mDB = null;
	Context context = CropActivity.this;
	Account_detail acc;
	private ConfigurationWS mWS;
	String URL = "http://117.6.131.222:81/irocchi/wsuploadimg.php";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crop);
		FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_crop);
		ImageView imgCrop = new CropImage(this);
		frameLayout.addView(imgCrop);
		// Save file
		bitmapCrop = ((BitmapDrawable) imgCrop.getDrawable()).getBitmap();

		mWS = new ConfigurationWS(this);

		// add flag
		ImageView btnFlagCrop = (ImageView) findViewById(R.id.btnFlagCrop);
		if (CommonInfor.getCountryFlag(getApplicationContext(),
				CommonInfor.countryName) != null) {
			btnFlagCrop.setImageBitmap(CommonInfor.getCountryFlag(
					getApplicationContext(), CommonInfor.countryName));
		}

		if (CommonInfor.countryName != "") {
			TextView textView = (TextView) findViewById(R.id.txtCountryNameCrop);
			textView.setText(CommonInfor.countryName);
		}
		if (ShowMap_Activity.sex == 0) {
			ImageView btnSexCrop = (ImageView) findViewById(R.id.btnSexCrop);
			btnSexCrop.setImageResource(R.drawable.temp_thumbnail_mimirin);
		}
		// insert

		ImageButton btnCaptureAgain = (ImageButton) findViewById(R.id.capture_again);
		btnCaptureAgain.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new playSound(getApplicationContext()).playButton();//add sound to button
				Intent intent = new Intent(CropActivity.this,
						CameraActivity.class);
				startActivity(intent);
				finish();
			}
		});
		ImageButton btnCaptureSave = (ImageButton) findViewById(R.id.capture_save);
		btnCaptureSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			/*	// insert the image infor to SQLite III79
				insertDatabaseSQLite();

				UploadImage ul = new UploadImage(getApplicationContext());
				ul.initial();

				JSONObject json = new JSONObject();
				try {
					json.put("img_name", name_Image_Crop);
					json.put("img_description", "OK");
					json.put("img_path", "1");
					json.put("img_status", "1");
					json.put("user_name",
							Main_Irocchi_Activity.myAcc.getUser_Name());
					json.put("user_id", Main_Irocchi_Activity.myAcc.get_id());

					JSONArray jarr = mWS.connectWSPut_Get_Data(URL, json,
							"posts");

					for (int i = 0; i < jarr.length(); i++) {
						JSONObject element = jarr.getJSONObject(i);
						Log.i("Log : ", "element : " + element);
						String msg_images = element.getString("images");
						String msg_user_image = element.getString("user_image");
						Log.i("Log : ", "msg_images : " + msg_images);

						Log.i("Log : ", "msg_user_image : " + msg_user_image); // us
																				// =
																				// userName;

						Toast.makeText(context, msg_images, Toast.LENGTH_LONG)
								.show();
					}

				} catch (JSONException e) { // TODO Auto-generated catch block
					e.printStackTrace();
				}*/

				new playSound(getApplicationContext()).playButton();//add sound to button
				int key = 1;// to close dialog
				saveImageCroped(bitmapCrop);
				Bundle bl = new Bundle();
				bl.putString("close", String.valueOf(key));
				Intent intentShowMap = new Intent(CropActivity.this,
						ShowMap_Activity.class);
				intentShowMap.putExtras(bl);
				startActivity(intentShowMap);
				finish();


			}
		});

		// back to preview page
		imgBBack = (ImageButton) findViewById(R.id.btn_crop_back);
		imgBBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new playSound(getApplicationContext()).playButton();//add sound to button
				Intent intentBack = new Intent(CropActivity.this,
						CameraActivity.class);
				startActivity(intentBack);
				finish();
			}
		});

		// xu ly Home
		imgBHome = (ImageButton) findViewById(R.id.btn_crop_home);
		imgBHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new playSound(getApplicationContext()).playButton();//add sound to button
				finish();
			}
		});
	}

	// insert to database
	public void insertDatabaseSQLite() {
		mDB = new ConfigurationDB(context);
		mDB.OpenDB();

		boolean bl = mDB.insertInfoImage(Main_Irocchi_Activity.myAcc.get_id(),
				name_Image_Crop, "1", 1, "OK");
		if (bl == true) {
			Toast.makeText(context, "insert into database success",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(context, "false", Toast.LENGTH_SHORT).show();
		}
	}

	// save lastest croped image
	private void saveImageCroped(Bitmap result) {
		File file = getOutputMediaFile();
		try {
			// file.createNewFile();
			FileOutputStream ostream = new FileOutputStream(file);
			result.compress(CompressFormat.PNG, 100, ostream);
			ostream.flush();
			ostream.close();
			Toast.makeText(CropActivity.this, "Picture croped is saved",
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Tao ra file trong thư mục để lưu imageCrop
	private File getOutputMediaFile() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_UNMOUNTED)) {
			Log.w("CropImage", "SD card unmounted.");
			return null;
		}
		File mediaStorageDir = new File(getFilename());

		// Create the storage directory if it does not exist.
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.e("CropImage", "Failed to create directory ");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File cropFile;
		cropFile = new File(mediaStorageDir.getPath() + File.separator
				+ "Crop_" + timeStamp + ".png");
		name_Image_Crop = "Crop_" + timeStamp + ".png";
		return cropFile;
	}

	String name_Image_Crop = "";

	// Lấy path tới thư mục
	private String getFilename() {
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath, PICTURE_FOLDER);
		if (!file.exists()) {
			file.mkdirs();
		}
		return (file.getAbsolutePath());
	}
}