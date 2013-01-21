package com.iii.irocchi.menu;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.iii.irocchi.R;
import com.iii.irocchi.cameracomposite.CameraCompositeActivity;
import com.iii.irocchi.common.CommonInfor;
import com.iii.irocchi.common.playSound;
import com.iii.irocchi.dancing.DancingActivity;
import com.iii.irocchi.map.ShowMap_Activity;
import com.iii.irocchi.selectcolor.SelectColorActivity;
import com.iii.irocchi.sound.Media_Recorder_Activity;

public class MenuActivity extends Activity {
	private ImageButton imgBInfor;
	private ImageButton imgBBack;
	private ImageButton imgBHome, imgBMap;
	private ImageButton imgBPhoto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_irocchi_menuscreen);

		
		ImageButton imgLeftMenu=(ImageButton) findViewById(R.id.imgLeftIrocchiMenu);
		
		ImageButton imgRightMenu=(ImageButton) findViewById(R.id.imgRightIrocchiMenu);
		imgRightMenu.setImageResource(R.drawable.thuan);
		// change flag
		// Add flag here
		ImageView btnFlagMenu = (ImageView) findViewById(R.id.btnFlagMenu);
		if (CommonInfor.getCountryFlag(getApplicationContext(),
				CommonInfor.countryName) != null) {
			btnFlagMenu.setImageBitmap(CommonInfor.getCountryFlag(
					getApplicationContext(), CommonInfor.countryName));
		}
		//add Country name
		TextView txtCountryNameMenu = (TextView) findViewById(R.id.txtCountryNameMenu);
		if(CommonInfor.countryName!="") txtCountryNameMenu.setText(CommonInfor.countryName);
		if(ShowMap_Activity.sex==0){
			ImageView btnSexMenu =(ImageView) findViewById(R.id.btnSexMenu);
			btnSexMenu.setImageResource(R.drawable.temp_thumbnail_mimirin);
		}
		//add irocchi
		
		ImageView irocchiMenuRight = (ImageView) findViewById(R.id.irocchiMenuRight);
		ImageView irocchiMenu = (ImageView) findViewById(R.id.irocchiMenu);
		if (CommonInfor.getBitmapImageLast("Irocchi/Pictures/local/ICrop") != null) {
			irocchiMenuRight.setImageResource(R.drawable.thuan);
			irocchiMenu.setImageBitmap(Bitmap.createScaledBitmap(CommonInfor.getBitmapImageLast("Irocchi/Pictures/local/ICrop"),180,220,true));
			imgLeftMenu.setImageBitmap(Bitmap.createScaledBitmap(CommonInfor.getBitmapImageLast("Irocchi/Pictures/local/ICrop"),340,400,true));
			
		}
		imgBInfor = (ImageButton) findViewById(R.id.imgBinfor);
		imgBInfor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new playSound(getApplicationContext()).playButton();//add sound to button
				Intent intentCountry = new Intent(MenuActivity.this,
						FriendCountryActivity.class);
				startActivity(intentCountry);
				finish();
			}
		});
		// Xu ly Photo
		imgBPhoto = (ImageButton) findViewById(R.id.imgBphoto);
		imgBPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new playSound(getApplicationContext()).playButton();//add sound to button
				// TODO Auto-generated method stub
				Intent intentPhoto = new Intent(MenuActivity.this,
						CameraCompositeActivity.class);
				startActivity(intentPhoto);
				finish();
			}
		});
		// Xu ly Back
		imgBBack = (ImageButton) findViewById(R.id.btnBackMenu);
		imgBBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new playSound(getApplicationContext()).playButton();//add sound to button
				// TODO Auto-generated method stub
				Intent intentBack = new Intent(MenuActivity.this,
						DancingActivity.class);
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
		//
		imgBMap = (ImageButton) findViewById(R.id.imgBmap);
		imgBMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new playSound(getApplicationContext()).playButton();//add sound to button
				Intent intentCountry = new Intent(MenuActivity.this,
						FriendCountryActivity.class);
				startActivity(intentCountry);
				finish();
			}
		});
	//	new playSound(getApplicationContext())
	}
	public void setDancing(View v){
		Intent intentDancing = new Intent(MenuActivity.this,
				DancingActivity.class);
		startActivity(intentDancing);
		finish();
	}

}
