package com.iii.irocchi.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.iii.irocchi.R;
import com.iii.irocchi.account.Account_detail;
import com.iii.irocchi.configuration.ConfigurationDB;
import com.iii.irocchi.configuration.ConfigurationWS;
import com.iii.irocchi.map.ShowMap_Activity;
import com.iii.irocchi.sound.Media_Recorder_Activity;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class Main_Irocchi_Activity extends Activity {

	private ImageButton ImgBIroochi;
	private ImageButton ImgBFamily;
	private ConfigurationDB mDB;
	private Context context = Main_Irocchi_Activity.this;
	private ConfigurationWS mWS;
	public static Account_detail myAcc;

	public static double la;
	public static double lo;
	public static MediaPlayer recorderBG = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_irocchi);

		lo = 0.0;
		la = 0.0;

		mDB = new ConfigurationDB(context);
		mDB.OpenDB();

		mWS = new ConfigurationWS(context);
		myAcc = new Account_detail(context);

		ImgBIroochi = (ImageButton) findViewById(R.id.imgbFriend);
		ImgBFamily = (ImageButton) findViewById(R.id.iconiroochi_family);

		ImgBIroochi.setOnClickListener(onclick);
		ImgBFamily.setOnClickListener(onclick);

		loginInit init = new loginInit();
		init.execute();

		// play music bground
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (recorderBG == null) {
					recorderBG = MediaPlayer.create(getBaseContext(),
							R.raw.bgmusic);
					recorderBG.setLooping(true);
					recorderBG.setVolume(0.3f, 0.3f);
				}
				if (!recorderBG.isPlaying()) {
					recorderBG.start();
				}
			}
		}).start();

	}

	OnClickListener onclick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {

			case R.id.imgbFriend:
				ShowMap_Activity.key = -1;
				Intent intent1 = new Intent(Main_Irocchi_Activity.this,
						ShowMap_Activity.class);
				new playSound(context).playButton();
				intent1.putExtra("UserName", myAcc.getUser_Name());
				startActivity(intent1);
				break;
			case R.id.iconiroochi_family:
				/*
				 * Intent intent2 = new Intent(Main_Irocchi_Activity.this,
				 * Media_Recorder_Activity.class); intent2.putExtra("UserName",
				 * myAcc.getUser_Name()); startActivity(intent2);
				 */
				break;

			default:
				break;
			}

		}
	};

	ProgressDialog progressDialog;

	private class loginInit extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {

			String url = context.getResources().getString(R.string.RegistryWS);

			try {

				String us = "Irc" + System.currentTimeMillis();
				String ps = "123456";
				myAcc.setUser_Name(us);
				myAcc.setPassword(ps);
				myAcc.set_id(100);

				JSONObject json1 = new JSONObject();
				json1.put("User_Name", myAcc.getUser_Name());
				json1.put("FullName", myAcc.getPassword());

				mWS.connectWS_Put_Data(url, json1);
				mDB.insertUserDefaul(myAcc.getUser_Name(), myAcc.getPassword());

				// Log.i("Log : ", "Ok ---11 ");
				// myAcc = mDB.getInfoUser(us);
				// Log.i("Log : ", "Ok 1 -- -11");

			} catch (JSONException e) {
				Log.i("Log : ",
						"Main_Irocchi_Activity JSONException : "
								+ e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				Log.i("Log : ",
						"Main_Irocchi_Activity Exception 12: " + e.toString());
			}

			return null;

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressDialog.dismiss();

			Log.i("Log : ", "Ok 4...");
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(context, "Loading...",
					"Plese Waiting..");
			Log.i("Log : ", "Ok 1...");
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			progressDialog.setProgress(values[0]);
			Log.i("Log : ", "Ok 3...");
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(recorderBG!=null)
			recorderBG.stop();
		recorderBG = null;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// recorderBG.stop();
		// recorderBG=null;
	}

	@Override
	protected void onStop() {
		super.onStop();
		// recorderBG.stop();
		// recorderBG=null;
		mDB.closeDB();
	}

}
