package com.iii.irocchi.configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.iii.irocchi.R;
import com.iii.irocchi.account.Account_detail;
//import org.json.JSONArray;

public class ConfigurationDB {

	private static final String DATABASE_NAME = "DataImages.db";
	private SQLiteDatabase mDB;
	private DatabaseHelper myDataHelPer;
	private static final int DATABASE_VERSION = 1;
	private Context mcontext = null;

	private static String G_id = "_id";
	private static String G_Group_Name = "Group_Name";
	private static String G_Update_time = "Update_time";
	private static String G_Create_time = "Create_time";
	private static String G_Flag = "Flag";
	private static String G_Description = "Description";

	private static final String G_DATABASE_TABLE_GROUP = "Group";
	private static final String G_DATABASE_CREATE_GROUP = "CREATE TABLE ["
			+ G_DATABASE_TABLE_GROUP + "]([" + G_id
			+ "] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, [" + G_Group_Name
			+ "] Varchar(200) NOT NULL, [" + G_Update_time
			+ "] timestamp NOT NULL , [" + G_Create_time
			+ "] timestamp NOT NULL, [" + G_Flag + "] boolean NOT NULL, ["
			+ G_Description + "] Varchar(500) NOT NULL);";

	private static String T_TimeZoneID = "TimeZoneID";
	private static String T_GMT = "GMT";
	private static String T_Name = "Name";

	private static final String T_DATABASE_TABLE_TIMEZONES = "timezones";
	private static final String T_DATABASE_CREATE_TIMEZONES = "CREATE TABLE `"
			+ T_DATABASE_TABLE_TIMEZONES + "` (`" + T_TimeZoneID
			+ "` INTEGER NOT NULL, `" + T_GMT + "` varchar(5) NOT NULL, `"
			+ T_Name + "` varchar(120) NOT NULL);";

	private static String C_CountryID = "CountryID";
	private static String C_CountryName = "CountryName";
	private static String C_CountryCode = "CountryCode";
	private static String C_Description = "Description";
	private static String C_Latitude = "Latitude";
	private static String C_Longitude = "Longitude";

	private static final String C_DATABASE_TABLE_COUNTRY = "country";
	private static final String C_DATABASE_CREATE_COUNTRY = "CREATE TABLE `"
			+ C_DATABASE_TABLE_COUNTRY + "` (`" + C_CountryID
			+ "` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `" + C_CountryName
			+ "` varchar(1000) NOT NULL, `" + C_CountryCode
			+ "` varchar(5) NOT NULL, `" + C_Description
			+ "` varchar(500) NOT NULL, `" + C_Latitude
			+ "` varchar(30) NOT NULL, `" + C_Longitude
			+ "` varchar(30) NOT NULL);";

	private static String I_ImagesID = "_id";
	private static String I_img_name = "img_name";
	private static String I_img_path = "img_path";
	private static String I_img_datecreate = "img_datecreate";
	private static String I_img_status = "img_status";
	private static String I_img_description = "img_description";

	private static final String I_DATABASE_TABLE_IMAGES = "images";
	private static final String I_DATABASE_CREATE_IMAGES = "CREATE TABLE `"
			+ I_DATABASE_TABLE_IMAGES + "` (`" + I_ImagesID
			+ "` INTEGER PRIMARY KEY AUTOINCREMENT  NULL, `" + I_img_name
			+ "` varchar(500)  NULL, `" + I_img_path
			+ "` varchar(500) NULL, `" + I_img_datecreate
			+ "` timestamp Attributes CURRENT_TIMESTAMP  NULL, `"
			+ I_img_status + "` tinyint(1)  NULL,`" + I_img_description
			+ "` varchar(200)  NULL);";

	private static String A_AudioID = "_id";
	private static String A_AudioName = "audio_name";
	private static String A_AudioPath = "audio_path";
	private static String A_audioDate_Create = "audio_date_create";
	private static String A_audio_Status = "audio_status";
	private static String A_audio_Description = "audio_description";

	private static final String A_DATABASE_TABLE_AUDIOS = "audios";
	private static final String A_DATABASE_CREATE_AUDIOS = "CREATE TABLE `"
			+ A_DATABASE_TABLE_AUDIOS + "` (`" + A_AudioID
			+ "` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `" + A_AudioName
			+ "` varchar(500)  NULL, `" + A_AudioPath
			+ "` varchar(500)  NULL, `" + A_audioDate_Create
			+ "` timestamp Attributes CURRENT_TIMESTAMP NULL, `"
			+ A_audio_Status + "` tinyint(1)  NULL, `" +  A_audio_Description
			+ "` varchar(200)  NULL);";

	private static String U_id = "_id";
	public static String U_User_Name = "User_Name";
	public static String U_Password = "Password";
	private static String U_Sex = "Sex";
	private static String U_Age = "Age";
	private static String U_Email = "Email";
	private static String U_UpdateTime = "UpdateTime";
	private static String U_Create_time = "Create_time";
	private static String U_Flag = "Flag";
	private static String U_TimeZone_ID = "TimeZone_ID";
	private static String U_City_ID = "City_ID";
	private static String U_Country_ID = "Country_ID";

	private static final String U_DATABASE_TABLE_USER = "User";
	private static final String U_DATABASE_USER = "CREATE TABLE ["
			+ U_DATABASE_TABLE_USER + "]([" + U_id
			+ "] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, [" + U_User_Name
			+ "] Varchar(50) NOT NULL , [" + U_Password
			+ "] Varchar(50) NOT NULL,[" + U_Sex + "] tinyint NOT NULL,["
			+ U_Age + "] int tinyint NULL,[" + U_Email
			+ "] Varchar(50) NOT NULL,[" + U_UpdateTime
			+ "] timestamp  NOT NULL,[" + U_Create_time
			+ "] timestamp NOT NULL,[" + U_Flag + "] boolean NOT NULL,["
			+ U_TimeZone_ID + "] INTEGER NOT NULL,[" + U_City_ID
			+ "] INTEGER NOT NULL,[" + U_Country_ID
			+ "] INTEGER NOT NULL, CONSTRAINT FK_Country_User FOREIGN KEY ("
			+ U_Country_ID + ") REFERENCES " + C_DATABASE_TABLE_COUNTRY + "("
			+ C_CountryID + "));";

	private static String IM_IMID = "_id";
	private static String IM_ImagesID = "im_imagesID";
	private static String IM_UserNameID = "im_usernameID";

	private static final String IM_DATABASE_TABLE_IMAGES = "imageuser";
	private static final String IM_DATABASE_CREATE_IMAGES = "CREATE TABLE `"
			+ IM_DATABASE_TABLE_IMAGES + "` (`" + IM_IMID
			+ "` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `" + IM_ImagesID
			+ "` varchar(500) NOT NULL, `" + IM_UserNameID
			+ "` varchar(500) NOT NULL);";

	private static String AU_IMID = "_id";
	private static String AU_AudioID = "au_audiosID";
	private static String AU_UserNameID = "au_usernameID";

	private static final String AU_DATABASE_TABLE_AUDIO = "audiouser";
	private static final String AU_DATABASE_CREATE_AUDIO = "CREATE TABLE `"
			+ AU_DATABASE_TABLE_AUDIO + "` (`" + AU_IMID
			+ "` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `" + AU_AudioID
			+ "` varchar(500) NOT NULL, `" + AU_UserNameID
			+ "` varchar(500) NOT NULL);";

	public Account_detail getInfoUser(String userName) {


		Account_detail accdt = null;	
		try {
			Log.i("Log : ", "Ok32 : ");
			 accdt = new Account_detail(mcontext);	
			 Log.i("Log : ", "okwrư : ");
			 
//			 ContentValues initialValues = new ContentValues();
//				initialValues.put(U_Sex, sex);
//				
//				mDB.update(U_DATABASE_TABLE_USER, initialValues,  U_Sex + " = '"+ sex + "'", null);
//				
				
		Cursor cur =  mDB.query(U_DATABASE_TABLE_USER, new String[] {U_id, U_User_Name,	U_Password, U_Sex, U_Age,
				U_Email,U_UpdateTime, U_Create_time, U_Flag, U_TimeZone_ID, U_City_ID, U_Country_ID }, U_User_Name + " = '"+ userName + "'", null, null, null, null);
		

		if (cur.getCount() > 0) {
			Log.i("Log : ", "count : " + cur.getCount());
			cur.moveToNext();
			if (cur.getColumnCount() > 0) {

				accdt.set_id(cur.getInt(0));
				accdt.setUser_Name(cur.getString(1));
				accdt.setPassword(cur.getString(2));
				accdt.setSex(cur.getString(3));
				accdt.setAge(cur.getString(4));
				accdt.setEmail(cur.getString(5));
				accdt.setUpdateTime(cur.getString(6));
				accdt.setCreate_time(cur.getString(7));
				accdt.setFlag(cur.getString(8));
				accdt.setTimeZone_ID(cur.getString(9));
				accdt.setCity_ID(cur.getString(10));
				accdt.setCountry_ID(cur.getString(11));
			}
		} else {
			Toast.makeText(mcontext, "Not Data..", Toast.LENGTH_LONG).show();
		}
		} catch (Exception e) {
			Log.i("Log : ", "Exception : "+e.getMessage());
		}
		 return accdt;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		ProgressDialog progress;
		Context context;

		private ConfigurationWS mWS;
		private Account_detail myAcc;
		private ConfigurationDB mDB;

		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			 super(context, context.getExternalFilesDir(null).getAbsolutePath() + "/" + DATABASE_NAME, null, DATABASE_VERSION);
			//super(context, name, factory, version);
			this.context = context;
			mWS = new ConfigurationWS(context);
			myAcc = new Account_detail(context);
			mDB = new ConfigurationDB(context);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(G_DATABASE_CREATE_GROUP);
			db.execSQL(U_DATABASE_USER);
			db.execSQL(T_DATABASE_CREATE_TIMEZONES);
			db.execSQL(C_DATABASE_CREATE_COUNTRY);
			db.execSQL(I_DATABASE_CREATE_IMAGES);
			db.execSQL(A_DATABASE_CREATE_AUDIOS);
			db.execSQL(IM_DATABASE_CREATE_IMAGES);
			db.execSQL(AU_DATABASE_CREATE_AUDIO);
			
			InsertBg ins = new InsertBg();
			ins.execute(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.i("Log : ", "Database Updating.....");
			db.execSQL("DROP TABLE IF EXISTS " + G_DATABASE_TABLE_GROUP);
			db.execSQL("DROP TABLE IF EXISTS " + U_DATABASE_TABLE_USER);
			db.execSQL("DROP TABLE IF EXISTS " + T_DATABASE_TABLE_TIMEZONES);
			db.execSQL("DROP TABLE IF EXISTS " + C_DATABASE_TABLE_COUNTRY);
			db.execSQL("DROP TABLE IF EXISTS " + I_DATABASE_TABLE_IMAGES);
			db.execSQL("DROP TABLE IF EXISTS " + A_DATABASE_TABLE_AUDIOS);
			db.execSQL("DROP TABLE IF EXISTS " + IM_DATABASE_TABLE_IMAGES);
			db.execSQL("DROP TABLE IF EXISTS " + AU_DATABASE_TABLE_AUDIO);

			onCreate(db);
		}

		int arrID[] = { R.drawable.istimezones, R.drawable.country };

		private class InsertBg extends
				AsyncTask<SQLiteDatabase, Integer, SQLiteDatabase> {

			@Override
			protected SQLiteDatabase doInBackground(SQLiteDatabase... params) {
				SQLiteDatabase db = params[0];
				for (int i = 0; i < arrID.length; i++) {

					InputStream is = context.getResources().openRawResource(
							arrID[i]);
					BufferedReader br = new BufferedReader(
							new InputStreamReader(is));
					String readLine = null;
					int count = 0;
					try {
						while ((readLine = br.readLine()) != null) {
							Log.d("Log : ", "readLine " + count + " : "
									+ readLine);
							db.execSQL(readLine);
							count++;
						}
					} catch (Exception e) {
						Log.i("Log : ", e.getMessage());
						Log.i(this.getClass().getName(), e.getMessage());
					} finally {
						try {
							is.close();
							br.close();

						} catch (IOException e) {
							Log.i(this.getClass().getName(), e.getMessage());
						}
					}

				}

				return db;
			}

			@Override
			protected void onPostExecute(SQLiteDatabase result) {
				super.onPostExecute(result);
				progress.dismiss();
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
				progress.setProgress(values[0]);
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				try {
					progress = ProgressDialog.show(context, "Loading...",
							"Please Waiting...");
					progress.show();
				} catch (Exception e) {
					Log.v("Log : ", "Exception : " + e.getMessage());
				}
			}
		}

	}

	public ConfigurationDB(Context context) {
		this.mcontext = context;
	}

	public ConfigurationDB OpenDB() {
		myDataHelPer = new DatabaseHelper(mcontext, DATABASE_NAME, null,
				DATABASE_VERSION);
		mDB = myDataHelPer.getWritableDatabase();

		return this;
	}

	public void closeDB() {
		mDB.close();
	}

	public boolean insertDataForUserTable(String user, String pass, String sex,
			String age, String email, String updateTime, String create_time,
			String flag, int timeZone_ID, int city_ID, int country_ID) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(U_User_Name, user);
		initialValues.put(U_Password, user);
		initialValues.put(U_Sex, sex);
		initialValues.put(U_Age, age);
		initialValues.put(U_Email, email);
		initialValues.put(U_UpdateTime, updateTime);
		initialValues.put(U_Create_time, create_time);
		initialValues.put(U_Flag, flag);
		initialValues.put(U_TimeZone_ID, timeZone_ID);
		initialValues.put(U_City_ID, city_ID);

		initialValues.put(U_Country_ID, country_ID);
		mDB.insert(U_DATABASE_TABLE_USER, null, initialValues);
		return true;
	}

	public boolean insertUserDefaul(String userName, String password) {

		ContentValues initialValues = new ContentValues();
		initialValues.put(U_User_Name, userName);
		initialValues.put(U_Password, password);
		initialValues.put(U_Sex, 0);
		initialValues.put(U_Age, 0);
		initialValues.put(U_Email, "");
		initialValues.put(U_UpdateTime, "");
		initialValues.put(U_Create_time, System.currentTimeMillis());
		initialValues.put(U_Flag, "");
		initialValues.put(U_TimeZone_ID, "0");
		initialValues.put(U_City_ID, "0");
		initialValues.put(U_Country_ID, "0");
		mDB.insert(U_DATABASE_TABLE_USER, null, initialValues);
		return true;
	}

	public boolean insertInfoImage( int userID ,String imagesName,
			String imagesPath, int imagesStatus,
			String imagesDescription) {
		
		try {
			
		
			Log.i("Log : ", "Ok-------------------");
		ContentValues initialImages = new ContentValues();
		initialImages.put(I_img_name, imagesName);
		initialImages.put(I_img_path, imagesPath);
		initialImages.put(I_img_status, imagesStatus);
		initialImages.put(I_img_description, imagesDescription);

		Log.i("Log : ", "Ok2--------------------");
		mDB.insert(I_DATABASE_TABLE_IMAGES, null, initialImages);
		Log.i("Log : ", "Ok3--------------------");
		
		} catch (Exception e) {
			Log.i("Log : ", "Exception : "+e.getMessage());
		}
		

		Cursor cursor = mDB.query(I_DATABASE_TABLE_IMAGES,
				new String[] { I_ImagesID }, null, null, null, null, I_ImagesID + " DESC");
		int img_id = 0;
		if (cursor.moveToNext()) {
			img_id = cursor.getInt(0);
		}

		ContentValues initialIM = new ContentValues();
		initialIM.put(IM_ImagesID, img_id);
		initialIM.put(IM_UserNameID, 2);
		mDB.insert(IM_DATABASE_TABLE_IMAGES, null, initialIM);
		return true;
	}

	public boolean insertInfoAudio(int userID ,String audiosName,
			String audiosPath, int audiosStatus,
			String audiosDescription) {

		ContentValues initialAudios = new ContentValues();
		initialAudios.put(A_AudioName, audiosName);
		initialAudios.put(A_AudioPath, audiosPath);
		initialAudios.put(A_audio_Status, audiosStatus);
		initialAudios.put(A_audio_Description, audiosDescription);

		mDB.insert(A_DATABASE_TABLE_AUDIOS, null, initialAudios);
		
		Cursor cursor = mDB.query(I_DATABASE_TABLE_IMAGES,
				new String[] { A_AudioID }, null, null, null, null, A_AudioID + " DESC");
		int audio_id = 0;
		if (cursor.moveToNext()) {
			audio_id = cursor.getInt(0);
		}

		ContentValues initialAD = new ContentValues();
		initialAD.put(A_AudioID, audio_id);
		initialAD.put(IM_UserNameID, 2);
		mDB.insert(A_DATABASE_TABLE_AUDIOS, null, initialAD);
		return true;
		
	}

	public boolean updateUserSex(String u_name, String sex) {

		ContentValues initialValues = new ContentValues();
		initialValues.put(U_Sex, sex);
			
			mDB.update(U_DATABASE_TABLE_USER, initialValues,  U_User_Name + " = '"+ u_name + "'", null);
			
		return true;
	}

	public Cursor getInfoLogin() {
		return mDB.query(U_DATABASE_TABLE_USER, new String[] { U_User_Name,
				U_Password }, null, null, null, null, null);
	}

	public int getInfoCountry(String countryCode) {
		Cursor cursor = mDB.query(C_DATABASE_TABLE_COUNTRY,
				new String[] { C_CountryID }, C_CountryCode + " = '"
						+ countryCode + "'", null, null, null, null);
		int countryID = -1;
		Log.i("Log : ", "count 1 : " + cursor.getCount());
		if (cursor.getCount() > 0) {
			Log.i("Log : ", "count : " + cursor.getCount());
			cursor.moveToFirst();
			// while (cursor.moveToNext()) {
			countryID = cursor.getInt(0);
			Log.i("Log : ", "Ok : -- " + countryID);
			// }
		} else {
			Toast.makeText(mcontext, "Not Data..", Toast.LENGTH_LONG).show();
		}

		return countryID;

		// return mDB.query(C_DATABASE_TABLE_COUNTRY, new String[] {
		// C_CountryID,
		// C_CountryName, C_CountryCode, C_Description ,C_Latitude ,
		// C_Longitude},null,null,null,null,C_Description);
	}

	
}
