package com.iii.irocchi.selectcolor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.iii.irocchi.R;
import com.iii.irocchi.camera.CameraActivity;
import com.iii.irocchi.common.CommonInfor;
import com.iii.irocchi.common.Main_Irocchi_Activity;
import com.iii.irocchi.common.playSound;
import com.iii.irocchi.dancing.DancingActivity;
import com.iii.irocchi.map.ShowMap_Activity;
import com.iii.irocchi.sound.Media_Recorder_Activity;
import com.iii.irocchi.sound.play_again_save_Activity;

public class SelectColorActivity extends Activity {
	Bitmap bm;
	ImageView img;
	public static int key = 1;
	private ImageButton imgBBack;
	private ImageButton imgBHome;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choicecolor);
		ImageView btnRecord111 = (ImageView) findViewById(R.id.btnRecord111);
		if (CommonInfor.getCountryFlag(getApplicationContext(),
				CommonInfor.countryName) != null) {
			btnRecord111.setImageBitmap(CommonInfor.getCountryFlag(
					getApplicationContext(), CommonInfor.countryName));
		}
		
		if(CommonInfor.countryName!=""){
			TextView textView = (TextView) findViewById(R.id.txtCountryNameSelectColor);
			
			textView.setText(CommonInfor.countryName);
		}
		if(ShowMap_Activity.sex==0){
			ImageView btnSexColor =(ImageView) findViewById(R.id.btnSexSelectColor);
			btnSexColor.setImageResource(R.drawable.temp_thumbnail_mimirin);
		}
		// Xu ly Back
		imgBBack = (ImageButton) findViewById(R.id.btnBack);
		imgBBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new playSound(getApplicationContext()).playButton();//add sound to button
				Intent intentBack = new Intent(SelectColorActivity.this,
						Media_Recorder_Activity.class);
				startActivity(intentBack);
			}
		});

		// xu ly Home
		imgBHome = (ImageButton) findViewById(R.id.btnHome);
		imgBHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new playSound(getApplicationContext()).playButton();//add sound to button
				finish();
			}
		});

	}

	public void choiceIrocchiColor(View v) {
		new playSound(getApplicationContext()).playButton();//add sound to button
		key = v.getId();
		if (key != -1) {
			switch (key) {
			case R.id.imageButton1:
				key = 1;
				break;
			case R.id.imageButton2:
				key = 2;
				break;
			case R.id.imageButton3:
				key = 3;
				break;
			case R.id.imageButton4:
				key = 4;
				break;
			default:
				break;
			}

		}
		Bundle bl = new Bundle();
		bl.putString("KEY", String.valueOf(key));
		Intent intentColor = new Intent(SelectColorActivity.this,
				CameraActivity.class);
		intentColor.putExtras(bl);
		startActivity(intentColor);
		finish();
	}
}