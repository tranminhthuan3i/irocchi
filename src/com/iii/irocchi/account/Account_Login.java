package com.iii.irocchi.account;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.ByteArrayEntity;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.params.BasicHttpParams;
//import org.apache.http.params.HttpConnectionParams;
//import org.apache.http.params.HttpParams;
//import org.apache.http.util.EntityUtils;
//import org.json.JSONException;
//import org.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.iii.irocchi.R;
import com.iii.irocchi.common.Main_Irocchi_Activity;
import com.iii.irocchi.configuration.ConfigurationDB;
import com.iii.irocchi.configuration.ConfigurationWS;
import com.iii.irocchi.configuration.ConfigurationWSRestClient;
import com.iii.irocchi.map.ShowMap_Activity;

public class Account_Login extends Activity implements OnClickListener {

	private EditText textTaiKhoan;
	private EditText texxtPass;
	private ConfigurationDB myDB;
	private Button btnlogin;

	private Account_detail myAcount;
	private ConfigurationWS mWS;

	int TIMEOUT_MILLISEC = 10000;
	Context context = Account_Login.this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			setContentView(R.layout.account_login);
			myAcount = new Account_detail();
			mWS = new ConfigurationWS(this);

			myDB = new ConfigurationDB(this);
			myDB.OpenDB();
			textTaiKhoan = (EditText) this.findViewById(R.id.textTaiKhoan);
			texxtPass = (EditText) this.findViewById(R.id.textMatKhau);

			btnlogin = (Button) this.findViewById(R.id.btnlogin);
			btnlogin.setOnClickListener(this);

		} catch (Exception e) {
			Log.i(this.getClass().getName(), e.getMessage());
		}
	}

	private void login() {
		String user = textTaiKhoan.getText().toString();
		String pass = texxtPass.getText().toString();
		String url = getResources().getString(R.string.LoginWS);

		myAcount.setUser_Name(user);
		myAcount.setPassword(pass);

		Log.i("Log : ", "url : " + url);

		Log.i("Log : ", "getUser_Name : " + myAcount.getUser_Name());

		Log.i("Log : ", "getPassword : " + myAcount.getPassword());

		if (checknull(user, pass) == true) {

			try {

				loginInit lg = new loginInit();
				lg.execute();

			} catch (Exception e) {
				Log.i("Log : ", "Exception : " + e.getMessage());
			}
		}
	}

	String us = "";
	ProgressDialog progressDialog;

	private class loginInit extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {

			String url = getResources().getString(R.string.LoginWS);

			try {
				JSONObject json = new JSONObject();
				json.put("UserName", myAcount.getUser_Name());
				json.put("FullName", myAcount.getPassword());

				JSONArray jarr = mWS.connectWSPut_Get_Data(url, json, "posts");
				for (int i = 0; i < jarr.length(); i++) {

					JSONObject element = jarr.getJSONObject(i);

					Log.i("Log : ", "element : " + element);
					String fullName = element.getString("FullName");
					String userName = element.getString("UserName");

					Log.i("Log : ", "userName : " + userName);
					us = userName;
				}

			} catch (JSONException e) {
				Log.i("Log : ", "JSONException : " + e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				Log.i("Log : ", "Exception 12: " + e.toString());
			}

			if (!us.equals("empty")) {

				runOnUiThread(new Runnable() {

					@Override
					public void run() {

						Intent intent = new Intent(Account_Login.this,
								Main_Irocchi_Activity.class);
						startActivity(intent);
						Account_Login.this.finish();

						StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
								.permitAll().build();
						StrictMode.setThreadPolicy(policy);
						Toast.makeText(Account_Login.this,
								"Đăng Nhập Thành Công", Toast.LENGTH_LONG)
								.show();

					}
				});

			} else {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						new AlertDialog.Builder(context)
								.setTitle("Login Messege")
								.setMessage(
										"Tài Khoản Của Bạn Chưa Được Đăng Ký\nBạn Có Muốn Tạo Tài Khoản Mặc Định Không ?")
								.setNegativeButton("Không Đồng Ý",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
											}
										})
								.setNeutralButton("Đồng Ý",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												runOnUiThread(new Runnable() {
													public void run() {

														try {

															String url1 = getResources()
																	.getString(
																			R.string.RegistryWS);

															myDB.insertUserDefaul(
																	myAcount.getUser_Name(),
																	myAcount.getPassword());

															// StrictMode.ThreadPolicy
															// policy = new
															// StrictMode.ThreadPolicy.Builder()
															// .permitAll()
															// .build
															// StrictMode
															// .setThreadPolicy(policy);

															JSONObject json1 = new JSONObject();
															json1.put(
																	"User_Name",
																	myAcount.getUser_Name());
															json1.put(
																	"FullName",
																	myAcount.getPassword());
															mWS.connectWS_Put_Data(
																	url1, json1);

															Intent intent = new Intent(
																	Account_Login.this,
																	Main_Irocchi_Activity.class);
															startActivity(intent);
															Account_Login.this
																	.finish();

														} catch (JSONException e) {
															Log.i("Log : ",
																	"JSONException : "
																			+ e.getMessage());
															e.printStackTrace();
														} catch (Exception e) {
															Log.i("Log : ",
																	"Exception 12: "
																			+ e.toString());
														}

													}
												});
												dialog.dismiss();
											}
										}).show();
					}
				});
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

	private static final String checkUser = "[a-zA-Z0-9]+[a-zA-Z0-9_]*";

	private boolean checknull(String taikhoan, String matkhau) {

		boolean matcherUser = Pattern.matches(checkUser, taikhoan);

		if (taikhoan.equals("")) {
			Toast.makeText(this, "Nhập Tên Tài Khoản", Toast.LENGTH_SHORT)
					.show();
			textTaiKhoan.requestFocus();
			return false;
		}
		if (matkhau.equals("")) {
			Toast.makeText(this, "Nhập Mật Khẩu", Toast.LENGTH_SHORT).show();
			texxtPass.requestFocus();
			return false;
		}
		// if (matcherUser) {
		// Toast.makeText(this, "Nhập Tên Tài Khoản Không Đúng Định Dạng",
		// Toast.LENGTH_SHORT)
		// .show();
		// textTaiKhoan.requestFocus();
		// return false;
		// }
		return true;

	}

	@Override
	protected void onStop() {
		super.onStop();
		myDB.closeDB();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btnlogin:
			login();
			break;

		default:
			break;
		}

	}

}
