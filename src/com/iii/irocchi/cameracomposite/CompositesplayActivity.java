package com.iii.irocchi.cameracomposite;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iii.irocchi.R;
import com.iii.irocchi.common.CommonInfor;
import com.iii.irocchi.common.playSound;
import com.iii.irocchi.map.ShowMap_Activity;

public class CompositesplayActivity extends Activity {
	private Bitmap bitmapCrop;

	private static final String PICTURE_FOLDER = "Irocchi/PictureIrocchi/Composite/withfriend";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_composite_camera);

		// Add flag here
		ImageView btnFlagDisplayCps = (ImageView) findViewById(R.id.btnFlagDisplayCps);
		if (CommonInfor.getCountryFlag(getApplicationContext(),
				CommonInfor.countryName) != null) {
			btnFlagDisplayCps.setImageBitmap(CommonInfor.getCountryFlag(
					getApplicationContext(), CommonInfor.countryName));
		}
		// add Country name
		TextView txtCountryNameDisPlayCps = (TextView) findViewById(R.id.txtCountryNameDisPlayCps);
		if (CommonInfor.countryName != "")
			txtCountryNameDisPlayCps.setText(CommonInfor.countryName);
		if (ShowMap_Activity.sex == 0) {
			ImageView btnSexDisplayCps = (ImageView) findViewById(R.id.btnSexDisplayCps);
			btnSexDisplayCps
					.setImageResource(R.drawable.temp_thumbnail_mimirin);
		}
		// add Irocchi

		ImageView irocchiCpsR = (ImageView) findViewById(R.id.irocchiCpsright);
		ImageView irocchiDisplayCps = (ImageView) findViewById(R.id.irocchiDisplayCps);
		if (CommonInfor.getBitmapImageLast("Irocchi/Pictures/local/ICrop") != null) {
			
			irocchiDisplayCps.setImageBitmap(Bitmap.createScaledBitmap(CommonInfor
					.getBitmapImageLast("Irocchi/Pictures/local/ICrop"),180,220,true));
			irocchiCpsR.setImageResource(R.drawable.thuan);
		}

		// frame

		FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_crop);
		ImageView imgeMe = new ProcessImage(this);
		frameLayout.addView(imgeMe);
		bitmapCrop = ((BitmapDrawable) imgeMe.getDrawable()).getBitmap();
		ImageButton btnCaptureAgain = (ImageButton) findViewById(R.id.capture_again);
		btnCaptureAgain.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new playSound(getApplicationContext()).playButton();//add sound to button
				Intent intent = new Intent(CompositesplayActivity.this,
						CameraCompositeActivity.class);
				startActivity(intent);
				finish();
			}
		});
		ImageButton btnCaptureSave = (ImageButton) findViewById(R.id.capture_composite_save);
		btnCaptureSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// call I_20
				new playSound(getApplicationContext()).playButton();//add sound to button
				saveImageCroped(bitmapCrop);
				Intent intent1 = new Intent(CompositesplayActivity.this,
						CompositeDisplayWithFriend.class);
				startActivity(intent1);
				finish();

			}
		});
	}

	// to save image bitmap
	private void saveImageCroped(Bitmap result) {
		File file = getOutputMediaFile();
		try {
			// file.createNewFile();
			FileOutputStream ostream = new FileOutputStream(file);
			result.compress(CompressFormat.PNG, 100, ostream);
			ostream.flush();
			ostream.close();
			Toast.makeText(CompositesplayActivity.this, "picture is Saved",
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// to get file name to save image
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
		return cropFile;
	}

	// get path
	private String getFilename() {
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath, PICTURE_FOLDER);
		if (!file.exists()) {
			file.mkdirs();
		}
		return (file.getAbsolutePath());
	}
}