package com.iii.irocchi.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MyLocationOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.SimpleLocationOverlay;
import org.osmdroid.views.util.constants.MapViewConstants;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.iii.irocchi.sound.Media_Recorder_Activity;

public class ShowMap_Activity extends Activity implements MapViewConstants,
		OnClickListener {
	private ImageButton btnBack1;
	private ImageButton btnToTop1;
	private ImageView icon_me;
	public ImageView btn_flag, imgBgSearching, imgTitleSearching;
	public TextView txtCountryNameMap;
	// private ImageButton btn_login;
	private Button btn_LoadingCountry, btn_next_to_voice;
	public ImageView irocchiShowmap;
	private LinearLayout layout_right_nemu;
	private RelativeLayout relativeMainLayout;

	private static MapView mapview;
	private static MapController mapcontroler;
	private static int latitude;
	private static int longitude;
	private static Double latitudeD;
	private static Double longitudeD;
	private GeoPoint geoPoint;

	private Location location;
	private LocationManager locationmanager;
	Context context = ShowMap_Activity.this;

	private Button btnOverlay;
	private Button btnAccess;

	private List<Overlay> mapOverLay;
	private Drawable drawable1;
	private Drawable drawable;
	// private MyItemOverlay1 myItemOverlay1;
	private MyItemizedOverlay myItemOverlay;
	private boolean foodIsDisPlay = false;
	private ConfigurationDB mDB;

	MyLocationOverlay myLocationOverlay = null;
	private SimpleLocationOverlay mMyLocationOverlay;
	private ScaleBarOverlay mScaleBarOverlay;

	private Account_detail myAccount;

	private ConfigurationWS myWS;

	private ProgressDialog progress;

	public static int key = -1;
	public static int sex = 1;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (imgBgSearching != null && imgTitleSearching != null) {
				imgBgSearching.setVisibility(View.GONE);
				imgTitleSearching.setVisibility(View.GONE);
				if (irocchiShowmap != null)
					irocchiShowmap.setVisibility(View.GONE);
			}
			if (btn_next_to_voice != null)
				btn_next_to_voice.setEnabled(true);
			if (btnBack1 != null)
				btnBack1.setEnabled(true);
			if (btnToTop1 != null)
				btnToTop1.setEnabled(true);
			if (mapview != null)
				mapview.setVisibility(View.VISIBLE);
			if (layout_right_nemu != null)
				layout_right_nemu.setVisibility(View.VISIBLE);
			if (relativeMainLayout != null)
				relativeMainLayout
						.setBackgroundResource(R.drawable.temp_bg_main);
			if (btn_next_to_voice != null)
				btn_next_to_voice.setVisibility(View.VISIBLE);
			if (key == -1)
				createDialog(context);
		};
	};

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		View view = super.onCreateView(name, context, attrs);
		return view;
	}

	public ShowMap_Activity() {
		setdefault();
	}

	private void setdefault() {
		latitude = 0;
		longitude = 0;
		latitudeD = 0.0;
		longitudeD = 0.0;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.viewmap_activity);
		myAccount = new Account_detail(context);
		myWS = new ConfigurationWS(this);
		btn_next_to_voice = (Button) findViewById(R.id.btn_next_to_voice);
		btn_next_to_voice.setVisibility(View.GONE);
		btnBack1 = (ImageButton) this.findViewById(R.id.btn_back);
		btnBack1.setEnabled(false);
		btnToTop1 = (ImageButton) this.findViewById(R.id.btn_back_totop);
		btnToTop1.setEnabled(false);
		icon_me = (ImageView) this.findViewById(R.id.btn_me_icon);
		btn_flag = (ImageView) this.findViewById(R.id.btn_flag);
		// btn_login = (ImageButton) this.findViewById(R.id.btn_login);
		btn_LoadingCountry = (Button) this
				.findViewById(R.id.btn_loadingCounttry);
		imgBgSearching = (ImageView) findViewById(R.id.imageView1);
		imgTitleSearching = (ImageView) findViewById(R.id.imageView2);
		irocchiShowmap = (ImageView) findViewById(R.id.irocchiShowmap);
		layout_right_nemu = (LinearLayout) findViewById(R.id.layout_right_nemu);
		layout_right_nemu.setVisibility(View.GONE);
		relativeMainLayout = (RelativeLayout) findViewById(R.id.relativeMainLayout);
		relativeMainLayout.setBackgroundColor(Color.GRAY);
		txtCountryNameMap = (TextView) findViewById(R.id.txtCountryNameMap);
		txtCountryNameMap.setText("Japan");
		key = -1;
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			// Log.e("THUAN", "Extra NULL");
		} else {
			String arrayWatt = extras.getString("close");
			if (arrayWatt != null)
				key = Integer.parseInt(arrayWatt);

		}
		if (key == -1) {
			new Thread(new Runnable() {
				int index = 0;

				@Override
				public void run() {
					// TODO Auto-generated method stub

					while (index < 6) {
						index++;
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					Message msg = handler.obtainMessage();
					handler.sendMessage(msg);

				}
			}).start();

		} else {
			btn_next_to_voice.setEnabled(false);
		}
		try {

			mDB = new ConfigurationDB(context);
			mDB.OpenDB();

			btnBack1.setOnClickListener(this);
			btnToTop1.setOnClickListener(this);
			// btn_login.setOnClickListener(this);
			btn_LoadingCountry.setOnClickListener(this);

			mapview = (MapView) this.findViewById(R.id.mapview);
			// // mapview.displayZoomControls(true);
			mapview.getController();
			mapview.getZoomLevel();

			mapview.setTileSource(TileSourceFactory.MAPNIK);
			mapview.setBuiltInZoomControls(true);
			mapview.setMultiTouchControls(true);

			int maxzoom = mapview.getMaxZoomLevel();
			int initzoom = maxzoom;

			mapcontroler = mapview.getController();
			mapcontroler.setZoom(initzoom - 16);

			InsertBg in = new InsertBg();
			in.execute();

			mapview.setVisibility(View.GONE);

			/*
			 * OverlayItem over = new OverlayItem("No Title", "No Descrip", new
			 * GeoPoint(0.0, 1.0)); setOverlayCountry(over);
			 */

			myLocationOverlay.enableMyLocation();
			myLocationOverlay.enableCompass();
			myLocationOverlay.enableFollowLocation();

			myLocationOverlay.runOnFirstFix(new Runnable() {
				public void run() {
					mapview.getController().animateTo(
							myLocationOverlay.getMyLocation());
				}
			});

		} catch (Exception e) {
			Log.i("Log : ", "Exception : " + e.getMessage());
		}

		btn_next_to_voice.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intentVoice = new Intent(ShowMap_Activity.this,
						Media_Recorder_Activity.class);
				startActivity(intentVoice);
				//add sound button
				new playSound(getApplicationContext()).playButton();
				finish();
			}
		});

		//to show that the next Activity is plying
		if (key != -1) {
			if (imgBgSearching != null && imgTitleSearching != null) {
				imgBgSearching.setVisibility(View.GONE);
				imgTitleSearching.setVisibility(View.GONE);
			}
			if (btn_next_to_voice != null)
				btn_next_to_voice.setEnabled(true);
			if (btnBack1 != null)
				btnBack1.setEnabled(true);
			if (btnToTop1 != null)
				btnToTop1.setEnabled(true);
			if (mapview != null)
				mapview.setVisibility(View.VISIBLE);
			if (layout_right_nemu != null)
				layout_right_nemu.setVisibility(View.VISIBLE);
			if (relativeMainLayout != null)
				relativeMainLayout
						.setBackgroundResource(R.drawable.temp_bg_main);
			if (btn_next_to_voice != null)
				btn_next_to_voice.setVisibility(View.GONE);
			// Add flag here

			if (CommonInfor.getCountryFlag(getApplicationContext(),
					CommonInfor.countryName) != null && btn_flag != null) {
				btn_flag.setImageBitmap(CommonInfor.getCountryFlag(
						getApplicationContext(), CommonInfor.countryName));
			}
			// add Irocchi

			if (CommonInfor.getBitmapImageLast("Irocchi/Pictures/local/ICrop") != null) {
				irocchiShowmap
						.setImageBitmap(Bitmap.createScaledBitmap(
								CommonInfor
										.getBitmapImageLast("Irocchi/Pictures/local/ICrop"),
								180, 220, true));
				irocchiShowmap.setVisibility(View.VISIBLE);
				ImageView irocchiShowmapR11 = (ImageView) findViewById(R.id.irocchiShowmapR);
				irocchiShowmapR11.setImageResource(R.drawable.thuan);

			}
			// add Country name

			checkOnTime();
		}
	}

	public void checkOnTime() {

		new Thread(new Runnable() {
			int index = 0;

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (index < 7) {
					index++;
					// if(index==17) playAudioPlane();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				Intent intentFlying = new Intent(ShowMap_Activity.this,
						ActivityIrocchiFlying.class);
				startActivity(intentFlying);
				finish();// III79 add
			}
		}).start();
	}


	private class InsertBg extends AsyncTask<SQLiteDatabase, Integer, Cursor> {

		@Override
		protected Cursor doInBackground(SQLiteDatabase... params) {

			OverlayItem over = new OverlayItem("No Title", "No Descrip",
					new GeoPoint(0.0, 1.0));
			setOverlayCountry(over);
			return null;

		}

		@Override
		protected void onPostExecute(Cursor result) {
			super.onPostExecute(result);
			// progress.dismiss();
			Log.i("Log : ", "Ok 4...");
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// progress = ProgressDialog.show(context, "Loading...",
			// "Please Waiting...");
			// progress.show();

			Log.i("Log : ", "Ok 1...");
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			// progress.setProgress(values[0]);
			Log.i("Log : ", "Ok 3...");
		}

	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		return super.onKeyDown(keyCode, event);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			new playSound(getApplicationContext()).playButton();//add sound to button
			Intent intent1 = new Intent(ShowMap_Activity.this,
					Main_Irocchi_Activity.class);
			startActivity(intent1);
			break;
		case R.id.btn_back_totop:
			new playSound(getApplicationContext()).playButton();//add sound to button
			Intent intent2 = new Intent(ShowMap_Activity.this,
					Main_Irocchi_Activity.class);
			startActivity(intent2);
			break;

		// case R.id.btn_login:
		// Intent intent3 = new Intent(ShowMap_Activity.this,
		// Account_Login.class);
		// startActivity(intent3);
		// break;
		case R.id.btn_loadingCounttry:

			InsertBg in = new InsertBg();
			in.execute();
			btn_LoadingCountry.setVisibility(Button.GONE);
			break;
		default:
			break;

		}
	}

	private int j = 0x7f020032;
	int count = 0;

	public void setOverlayCountry(final OverlayItem overlayItem) {
		try {
			synchronized (overlayItem) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Log.i("Log : ", "count get ID: " + j);
						Log.i("Log : ", "count get : " + count);
						mapOverLay = mapview.getOverlays();
						// add image here III79
						drawable = ShowMap_Activity.this.getResources()
								.getDrawable(R.drawable.dot);

						ResourceProxy resourceProxy = new DefaultResourceProxyImpl(
								getApplicationContext());

						myItemOverlay = new MyItemizedOverlay(drawable,
								resourceProxy, ShowMap_Activity.this, mapview,
								mapcontroler);

//						myItemOverlay.addOverlay(overlayItem);
						// add image
						mapOverLay.add(myItemOverlay);

						mapview.postInvalidate();

						j++;
						count++;
					}
				});
			}

		} catch (Exception e) {
			Log.i("Log : ", "Exception : " + e.getMessage());
		}
	}

	private void createDialog(Context context) {
		final Dialog dialog = new Dialog(context);
		dialog.setTitle("Select Sex");
		dialog.setContentView(R.layout.popup_choosesex);

		Button btnStart = (Button) dialog.findViewById(R.id.btnStart);
		btnStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				/*
				 * UpdataUserInit up = new UpdataUserInit(); up.execute();
				 */
				new playSound(getApplicationContext()).playButton();
				icon_me.setImageResource(R.drawable.temp_thumbnail_mimirin);
				ShowMap_Activity.sex = 0;
				dialog.dismiss();

			}
		});

		ImageView btnThoat = (ImageView) dialog.findViewById(R.id.btnThoat);
		btnThoat.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new playSound(getApplicationContext()).playButton();
				dialog.dismiss();
				ShowMap_Activity.sex = 1;
			}
		});
		dialog.show();
	}

	String us = "";
	ProgressDialog progressDialog;

	private class UpdataUserInit extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {

			String url = getResources().getString(R.string.LoginWS);

			try {

				runOnUiThread(new Runnable() {
					public void run() {

						// JSONObject json = new JSONObject();
						// json.put("UserName", myAccount.getUser_Name());
						// json.put("FullName", myAccount.getPassword());
						//
						// JSONArray jarr = myWS.connectWSPut_Get_Data(url,
						// json, "posts");
						// for (int i = 0; i < jarr.length(); i++) {
						//
						// JSONObject element = jarr.getJSONObject(i);
						//
						// Log.i("Log : ", "element : " + element);
						// String fullName = element.getString("FullName");
						// String userName = element.getString("UserName");
						//
						// Log.i("Log : ", "userName : " + userName);
						// us = userName;
						// }
						// mDB.updateUserDefaul(myAccount.getCity_ID(), "0");
						Toast.makeText(context, "Welcome", Toast.LENGTH_LONG)
								.show();

					}
				});

				// } catch (JSONException e) {
				// Log.i("Log : ", "JSONException : " + e.getMessage());
				// e.printStackTrace();
			} catch (Exception e) {
				Log.i("Log : ", "Exception : " + e.toString());
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
			progressDialog = ProgressDialog.show(context, "Updating...",
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
	protected void onStop() {
		super.onStop();
		setdefault();
		mDB.closeDB();
	}

	@Override
	protected void onResume() {
		super.onResume();

		// myLocationOverlay.enableMyLocation();
		// myLocationOverlay.enableCompass();
		// myLocationOverlay.enableFollowLocation();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// myLocationOverlay.disableMyLocation();
		// myLocationOverlay.disableCompass();
		// myLocationOverlay.disableFollowLocation();
	}

}
