package com.iii.irocchi.withfriend;

import com.iii.irocchi.R;
import com.iii.irocchi.configuration.ConfigurationDB;

import android.app.Activity;
import android.os.Bundle;


public class Activity_talk_withFriend extends Activity {
	
	private ConfigurationDB mDB;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_talk_withfriend);
		mDB = new ConfigurationDB(this);
		mDB.OpenDB();
		
		
	}
	
	
}
