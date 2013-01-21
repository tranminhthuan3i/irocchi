package com.iii.irocchi.sound;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.iii.irocchi.configuration.ConfigurationDB;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class UploadAudioIrocchi {

	private static final int SELECT_AUDIO = 2;
	String selectedPath = "";
	private static final String AUDIO_RECORDER_FOLDER1 = "Irocchi/Audios/local";
	private Context mContext = null;
	ConfigurationDB mDB;

	public UploadAudioIrocchi(Context mcontext) {
		this.mContext = mcontext;
		mDB = new ConfigurationDB(mcontext);
		mDB.OpenDB();
	}

	public void uploadAudio() {
		//lets doing the main thread
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		//III 79 insert to SQlite
		
		
		//Upload to server (Audio)		
		doFileUpload();
	}

	public String getFileName(){
		String str="";
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath, AUDIO_RECORDER_FOLDER1);
		if (!file.exists()) {
			file.mkdirs();
		}
		File[] sdDirList = file.listFiles();
		// Temp

		if (sdDirList != null) {
			// get lastest file name of audio
			str = filepath + "/" + AUDIO_RECORDER_FOLDER1 + "/"// temp
					+ sdDirList[sdDirList.length - 1].getName();
		}
		return str;
	}
	private void doFileUpload() {
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		DataInputStream inStream = null;
		String existingFileName = getFileName();//get file name lastest recording
		
		//CHen vao SQLite
		/*boolean t  = mDB.insertInfoAudio(existingFileName, "1", 0, 0, "2");
		if(t){
			Toast.makeText(mContext, "THANH CONG", Toast.LENGTH_LONG).show();
		}else Toast.makeText(mContext, "THAT BAI", Toast.LENGTH_LONG).show();
		*/
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		String responseFromServer = "";
		String urlString = "http://117.6.131.222:81/irocchi/upload_audio.php";
		try {
			// ------------------ CLIENT REQUEST
			FileInputStream fileInputStream = new FileInputStream(new File(
					existingFileName));
			// open a URL connection to the Servlet
			URL url = new URL(urlString);
			// Open a HTTP connection to the URL
			conn = (HttpURLConnection) url.openConnection();
			// Allow Inputs
			conn.setDoInput(true);
			// Allow Outputs
			conn.setDoOutput(true);
			// Don't use a cached copy.
			conn.setUseCaches(false);
			// Use a post method.
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			String key="123";
			conn.setRequestProperty("User_id", key);
			dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""	+ "\"" + ";user_id=\"157\"" + lineEnd);
				
			dos.writeBytes(lineEnd);
			// create a buffer of maximum size
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];
			// read file and write it into form...
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}
			// send multipart form data necesssary after file data...
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			// close streams
			Log.e("Debug", "File is written");
			fileInputStream.close();
			dos.flush();
			dos.close();
		} catch (MalformedURLException ex) {
			Log.e("Debug", "error: " + ex.getMessage(), ex);
		} catch (IOException ioe) {
			Log.e("Debug", "error: " + ioe.getMessage(), ioe);
		}
		// ------------------ read the SERVER RESPONSE
		try {
			inStream = new DataInputStream(conn.getInputStream());
			String str;

			while ((str = inStream.readLine()) != null) {
				Log.e("Debug", "Server Response " + str);
			}
			inStream.close();
			mDB.closeDB();
			// passing to next activity

		} catch (IOException ioex) {
			Log.e("Debug", "error: " + ioex.getMessage(), ioex);
		}
	}
	
	
}