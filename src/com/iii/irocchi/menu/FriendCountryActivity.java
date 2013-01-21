package com.iii.irocchi.menu;

import com.iii.irocchi.R;
import com.iii.irocchi.common.CommonInfor;
import com.iii.irocchi.common.Main_Irocchi_Activity;
import com.iii.irocchi.common.playSound;
import com.iii.irocchi.map.ShowMap_Activity;
import com.iii.irocchi.selectcolor.SelectColorActivity;
import com.iii.irocchi.sound.Media_Recorder_Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendCountryActivity extends Activity {

	private ImageButton imgBBack;
	private ImageButton imgBHome;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_irocchi_ifocountry);
		//add Flag to Infor activity
		ImageView btnFlagInfor = (ImageView) findViewById(R.id.btnFlagInfor);
		if (CommonInfor.getCountryFlag(getApplicationContext(),
				CommonInfor.countryName) != null) {
			btnFlagInfor.setImageBitmap(CommonInfor.getCountryFlag(
					getApplicationContext(), CommonInfor.countryName));
		}		
		//add Country name
		TextView txtCountryInfor = (TextView) findViewById(R.id.txtCountryInfor);
		if(CommonInfor.countryName!="") txtCountryInfor.setText(CommonInfor.countryName);
		if(ShowMap_Activity.sex==0){
			ImageView btnSexInfor =(ImageView) findViewById(R.id.btnSexInfor);
			btnSexInfor.setImageResource(R.drawable.temp_thumbnail_mimirin);
		}
		//add irocchi
		ImageView irocchiInforLeft = (ImageView) findViewById(R.id.irocchiInforLeft);
		ImageView irocchiInforRight = (ImageView) findViewById(R.id.irocchiInforRight);
		if (CommonInfor.getBitmapImageLast("Irocchi/Pictures/local/ICrop") != null) {
			CommonInfor.getBitmapImageLast("Irocchi/Pictures/local/ICrop").recycle();
			irocchiInforLeft.setImageBitmap(Bitmap.createScaledBitmap(CommonInfor.getBitmapImageLast("Irocchi/Pictures/local/ICrop"),180,220,true));
			irocchiInforRight.setImageResource(R.drawable.thuan);
		}
		// Xu ly Back
		imgBBack = (ImageButton) findViewById(R.id.btnBack);
		imgBBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new playSound(getApplicationContext()).playButton();//add sound to button
				Intent intentBack = new Intent(FriendCountryActivity.this,
						MenuActivity.class);
				startActivity(intentBack);
				finish();
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

}
