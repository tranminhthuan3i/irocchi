package com.iii.irocchi.camera;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.iii.irocchi.R;
import com.iii.irocchi.camera.Base64.InputStream;
import com.iii.irocchi.common.Main_Irocchi_Activity;

public class UploadImage {
	InputStream inputStream;
	private static final String PICTURE_FOLDER = "Irocchi/Pictures/local/ICrop";
	Context mContext;
	String imageFileName = "";

	public UploadImage(Context mcontext) {
		this.mContext = mcontext;

	}

	public void initial() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		Bitmap bitmap = getBitmapImageLast();
		// Bitmap bitmap = BitmapFactory.decodeFile(�/sdcard/android.jpg�);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); // compress to
																// which format
																// you want.
		byte[] byte_arr = stream.toByteArray();
		String image_str = Base64.encodeBytes(byte_arr);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		int user_id = Main_Irocchi_Activity.myAcc.get_id();

		Log.d("THUAN-->", imageFileName);
		nameValuePairs.add(new BasicNameValuePair("image", image_str));
		nameValuePairs.add(new BasicNameValuePair("name", imageFileName));
		nameValuePairs.add(new BasicNameValuePair("user_id", String.valueOf(user_id)));
		
		
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://117.6.131.222:81/irocchi/upload_image.php");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httppost);
			String the_string_response = convertResponseToString(response);

			Toast.makeText(mContext, "Response " + the_string_response,
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(mContext, "Updated",
					Toast.LENGTH_LONG).show();
			System.out.println("Error in http connection " + e.toString());
		}
	}

	public String convertResponseToString(HttpResponse response)
			throws IllegalStateException, IOException {

		String res = "";
		StringBuffer buffer = new StringBuffer();
		inputStream = (InputStream) response.getEntity().getContent();
		int contentLength = (int) response.getEntity().getContentLength(); // getting
																			// content
																			// length�..
		Toast.makeText(mContext, "contentLength : " + contentLength,
				Toast.LENGTH_LONG).show();
		if (contentLength < 0) {
		} else {
			byte[] data = new byte[512];
			int len = 0;
			try {
				while (-1 != (len = inputStream.read(data))) {
					buffer.append(new String(data, 0, len)); // converting to
																// string and
																// appending to
																// stringbuffer�..
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				inputStream.close(); // closing the stream�..
			} catch (IOException e) {
				e.printStackTrace();
			}
			res = buffer.toString(); // converting stringbuffer to string�..

			Toast.makeText(mContext, "Result : " + res, Toast.LENGTH_LONG)
					.show();
			// System.out.println("Response => " +
			// EntityUtils.toString(response.getEntity()));
		}
		return res;
	}

	// Return a Bitmap from folder
	private Bitmap getBitmapImageLast() {
		Bitmap bm1 = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.crop_irocchi);
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath, PICTURE_FOLDER);
		if (!file.exists()) {
			file.mkdirs();
		}
		File[] sdDirList = file.listFiles();
		// Temp

		if (sdDirList != null) {

			String str = filepath + "/" + PICTURE_FOLDER + "/"// temp
					+ sdDirList[sdDirList.length - 1].getName();
			imageFileName = sdDirList[sdDirList.length - 1].getName();
			bm1 = BitmapFactory.decodeFile(str);
		} else {
			Toast.makeText(mContext, "No image to upload", Toast.LENGTH_LONG)
					.show();
		}
		return bm1;
	}
}