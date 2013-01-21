package com.iii.irocchi.cameracomposite;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iii.irocchi.R;
import com.iii.irocchi.camera.CameraPreview;
import com.iii.irocchi.common.CommonInfor;
import com.iii.irocchi.map.ShowMap_Activity;

/*
 * Constructs custom camera UI allow user interact
 * Startup belong with application
 */

public class CameraCompositeActivity extends Activity {

	private Camera mCamera;
	private CameraPreview mPreview;
	private ImageButton mSwitchFlashLight;
	private ImageView mRightTransition, mLeftTransition, mSwitchCamera,
			mTakePicture, imagePreview;
	private int mCurentCamId = 0, key = 1;
	private int mFlashMode = 0;

	private int mScreenRotation = 0;
	private static final String CAMERA = "Camera";
	private static final String PICTURE_FOLDER = "Irocchi/PictureIrocchi/Composite";
	private FrameLayout preview;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_composite_camera);
		if (!checkCameraHardware(this)) {
			Log.w(CAMERA, "This device have no camera.");
			return;
		}
		
		// Add flag here
		ImageView btnFlagCps = (ImageView) findViewById(R.id.btnFlagCps);
		if (CommonInfor.getCountryFlag(getApplicationContext(),
				CommonInfor.countryName) != null) {
			btnFlagCps.setImageBitmap(CommonInfor.getCountryFlag(
					getApplicationContext(), CommonInfor.countryName));
		}
		//add Country name
		TextView txtCountryNameCps = (TextView) findViewById(R.id.txtCountryNameCps);
		if(CommonInfor.countryName!="") txtCountryNameCps.setText(CommonInfor.countryName);
		if(ShowMap_Activity.sex==0){
			ImageView btnSexCps =(ImageView) findViewById(R.id.btnSexCps);
			btnSexCps.setImageResource(R.drawable.temp_thumbnail_mimirin);
		}
		//add irocchi
		ImageView irocchiCameraCpsR = (ImageView) findViewById(R.id.irocchiCameraCpsR);
		ImageView cps_irocchi = (ImageView) findViewById(R.id.capture_cps_irocchiL);
		if (CommonInfor.getBitmapImageLast("Irocchi/Pictures/local/ICrop") != null) {
			
			cps_irocchi.setImageBitmap(Bitmap.createScaledBitmap(CommonInfor
					.getBitmapImageLast("Irocchi/Pictures/local/ICrop"),180,220,true));
			irocchiCameraCpsR.setImageResource(R.drawable.thuan);
		}
		
		mCamera = getCameraInstance();
		mPreview = new CameraPreview(this);
		mPreview.setCamera(mCamera);
		preview = (FrameLayout) findViewById(R.id.frame_preview);
		preview.addView(mPreview);

		/*-----------Lá»±a chá»�n camera trÆ°á»›c hay sau device---------*/
		mSwitchCamera = (ImageView) findViewById(R.id.button_switch_camera);
		mSwitchCamera.setOnClickListener(new OnClickListener() {

			@SuppressLint({ "NewApi", "NewApi" })
			@Override
			public void onClick(View arg0) {
				mPreview.stopCamera();
				// Change current camera id to next camera id available
				mCurentCamId = (mCurentCamId + 1) % Camera.getNumberOfCameras();
				mCamera = getCameraInstance();
				mPreview.startCamera(mCamera);
			}
		});

		/*---------------- Chá»¥p vÃ  lÆ°u áº£nh ----------------------*/
		mTakePicture = (ImageView) findViewById(R.id.button_capture_1);
		mTakePicture.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				startTakePictureEffect();
				mCamera.takePicture(null, null, mPicture);
				// When process image data last took, don't allow user take new
				// picture
				mTakePicture.setEnabled(false);
			}
		});

		/*-------------------turn on, turn off flashLight-------------------------*/
		mSwitchFlashLight = (ImageButton) findViewById(R.id.button_flash);
		mSwitchFlashLight.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Parameters camParam = mCamera.getParameters();
				List<String> flashModeSupported = camParam
						.getSupportedFlashModes();
				camParam.setFlashMode(flashModeSupported.get(mFlashMode));
				mCamera.setParameters(camParam);
				Log.i(CAMERA,
						"Current flash mode: "
								+ flashModeSupported.get(mFlashMode));
				// Prepare next flash mode available supported
				mFlashMode = (mFlashMode + 1) % flashModeSupported.size();
			}
		});
		// When application startup, load last image took in SD card
		loadLastImageTook();
		imagePreview = (ImageView) findViewById(R.id.image_took);
		imagePreview.setImageResource(R.drawable.frame1);
		imagePreview
				.setImageBitmap(getBitmapImageLast("Irocchi/PictureIrocchi/Composite"));
		

	}

	public void setFrameIrocchi() {

		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			Log.e("THUAN", "Extra NULL");
		} else {
			String arrayWatt = extras.getString("KEY");
			key = Integer.parseInt(arrayWatt);
			System.out.println(key + "<--KEY");
		}

		switch (key) {
		case 1:

			break;
		case 2:
			imagePreview.setImageResource(R.drawable.frame2);
			break;
		case 3:
			// imagePreview.setImageResource(R.drawable.frame3);
			break;
		case 4:
			// imagePreview.setImageResource(R.drawable.frame4);
			break;
		default:
			break;
		}

	}

	int halfWidthScreen = 0;
	AnimationSet leftTransitonAnim;
	AnimationSet rightTransitonAnim;

	/**
	 * Show snapshot transition: left and right side click together than open
	 * again.
	 */
	private void startTakePictureEffect() {
		if (halfWidthScreen == 0)
			halfWidthScreen = ((LinearLayout) findViewById(R.id.ll_transition))
					.getWidth() / 2;
		if (mLeftTransition == null)
			mLeftTransition = (ImageView) findViewById(R.id.left_transition);
		if (mRightTransition == null)
			mRightTransition = (ImageView) findViewById(R.id.right_transition);

		if (leftTransitonAnim == null) {
			leftTransitonAnim = new AnimationSet(false);
			leftTransitonAnim.setFillAfter(true);

			TranslateAnimation leftTranslateClose = new TranslateAnimation(
					-halfWidthScreen, 0f, 0f, 0f);
			leftTranslateClose.setDuration(250);

			TranslateAnimation leftTranslateOpen = new TranslateAnimation(0f,
					-halfWidthScreen, 0f, 0f);
			leftTranslateOpen.setDuration(250);
			leftTranslateOpen.setStartOffset(250);

			leftTransitonAnim.addAnimation(leftTranslateClose);
			leftTransitonAnim.addAnimation(leftTranslateOpen);

			rightTransitonAnim = new AnimationSet(false);
			rightTransitonAnim.setFillAfter(true);

			TranslateAnimation rightTranslateClose = new TranslateAnimation(
					halfWidthScreen, 0f, 0f, 0f);
			rightTranslateClose.setDuration(250);

			TranslateAnimation rightTranslateOpen = new TranslateAnimation(0f,
					halfWidthScreen, 0f, 0f);
			rightTranslateOpen.setDuration(250);
			rightTranslateOpen.setStartOffset(250);

			rightTransitonAnim.addAnimation(rightTranslateClose);
			rightTransitonAnim.addAnimation(rightTranslateOpen);
		}
		mLeftTransition.startAnimation(leftTransitonAnim);
		mLeftTransition.setVisibility(View.VISIBLE);
		mRightTransition.startAnimation(rightTransitonAnim);
		mRightTransition.setVisibility(View.VISIBLE);
	}

	protected void setDisplayOrientation(Camera camera, int angle) {
		Method downPolymorphic;
		try {
			downPolymorphic = camera.getClass().getMethod(
					"setDisplayOrientation", new Class[] { int.class });
			if (downPolymorphic != null)
				downPolymorphic.invoke(camera, new Object[] { angle });
		} catch (Exception e1) {
		}
	}

	@Override
	protected void onPause() {
		Log.d("Log: ", "Stop Camera Thuan Activity");
		releaseCameraAndPreview();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// mCamera = getCameraInstance();
		// mCamera.startPreview();
		super.onResume();
	}

	/**
	 * Load last image took from SD card, set this image to archive image button
	 */
	private void loadLastImageTook() {
		File mediaStorageDir = new File(
				getFilename("Irocchi/PictureIrocchi/Composite"));
		// Open folder storage image of application
		// If not exists then return
		if (!mediaStorageDir.exists()) {
			return;
		}
		File[] listFile = mediaStorageDir.listFiles();
		// If have no file then return
		if (listFile.length == 0)
			return;
		// Find last file is modified
		long lastFileModifiedTime = 0;
		int lastFileModifiedId = 0;
		for (int i = 0; i < listFile.length; i++) {
			if (listFile[i].getName().length() > 18)
				continue;
			long lastModified = new Date(listFile[i].lastModified()).getTime();
			if (lastModified > lastFileModifiedTime) {
				lastFileModifiedTime = lastModified;
				lastFileModifiedId = i;
			}
		}
		// Decode that image file
		Bitmap image = BitmapFactory.decodeFile(listFile[lastFileModifiedId]
				.getPath());
		// Set image to archive image icon
		ImageView lastImageTook = (ImageView) findViewById(R.id.last_image_took);
		lastImageTook.setImageBitmap(image);
		lastImageTook.setVisibility(View.GONE);

		/* tá»‘i Æ°u bá»™ nhá»›*-- */
		// image.recycle();
		// image = null;

	}

	/**
	 * A PictureCallback to handle image data took from camera
	 */
	private PictureCallback mPicture = new PictureCallback() {

		public void onPictureTaken(final byte[] data, Camera camera) {
			saveAndSendImage(data, "1", "123");

			showPreview(data);
		}

	};

	/**
	 * Create background task to show preview of this image, show minimizing
	 * this image into the photo archive button
	 * 
	 * @param data
	 *            image data is took
	 */
	private void showPreview(final byte[] data) {
		new AsyncTask<Void, Void, Long>() {
			Bitmap oriImageTook;
			int oriWidth, oriHeight;
			Bitmap imageTook;

			@Override
			// Show preview of this image
			protected Long doInBackground(Void... p) {
				try {
					Log.d("LOG: ", "COMING");
					oriImageTook = BitmapFactory.decodeByteArray(data, 0,
							data.length);
					oriWidth = oriImageTook.getWidth();
					oriHeight = oriImageTook.getHeight();
					Matrix matrix = new Matrix();
					int imageRotation;
					if (mCurentCamId == 0)
						imageRotation = mScreenRotation;
					else
						imageRotation = mScreenRotation ;
					matrix.postRotate(imageRotation, oriWidth / 2,
							oriHeight / 2);

					imageTook = Bitmap.createBitmap(oriImageTook, 0, 0,
							oriWidth, oriHeight, matrix, true);
				} catch (Exception em) {
					em.printStackTrace();
				}
				return 0l;
			}

			@Override
			// Show minimizing this image into the photo archive button
			protected void onPostExecute(Long result) {

				imagePreview.setImageBitmap(imageTook);
				imagePreview.setVisibility(View.VISIBLE);

				final ImageView lastImageTook = (ImageView) findViewById(R.id.last_image_took);
				int[] archiveLoc = new int[2];
				lastImageTook.getLocationInWindow(archiveLoc);
				int[] imageLoc = new int[2];
				imagePreview.getLocationInWindow(imageLoc);

				AnimationSet imageTransform = new AnimationSet(false);
				imageTransform.setFillAfter(true);

				TranslateAnimation imageTranslate = new TranslateAnimation(0f,
						archiveLoc[0] - imageLoc[0], 0f, archiveLoc[1]
								- imageLoc[1]);
				imageTranslate.setDuration(250);

				float scaleX = (float) lastImageTook.getWidth()
						/ (float) imagePreview.getWidth();
				float scaleY = (float) lastImageTook.getHeight()
						/ (float) imagePreview.getHeight();
				ScaleAnimation imageScale = new ScaleAnimation(1f, scaleX, 1f,
						scaleY);
				imageScale.setDuration(250);

				imageTransform.addAnimation(imageScale);
				imageTransform.addAnimation(imageTranslate);

				imageTransform.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation arg0) {
					}

					@Override
					public void onAnimationRepeat(Animation arg0) {
					}

					@Override
					public void onAnimationEnd(Animation arg0) {
						lastImageTook.setImageBitmap(imageTook);
						// mCamera.startPreview();
						System.gc();
						// ((ImageView)
						// findViewById(R.id.image_took)).setImageResource(R.drawable.frame2);
						mTakePicture.setEnabled(true);
						// imagePreview.setVisibility(View.INVISIBLE);
						// mPreview.stopCamera();
						// CameraActivity.this.finish();

					}
				});
				imagePreview.startAnimation(imageTransform);
				// Gá»�i Activity cho CropImage khi Ä‘Ã£ chá»¥p
				Intent intents2 = new Intent(CameraCompositeActivity.this,
						CompositesplayActivity.class);
				Bundle bl = new Bundle();
				bl.putString("KEY", String.valueOf(key));
				intents2.putExtras(bl);
				startActivity(intents2);
				finish();
			}

		}.execute();
	}

	/**
	 * Save image in SD card, send it to "Seconds" server
	 * 
	 */
	private void saveAndSendImage(final byte[] data, final String broad_id,
			final String reg_id) {

		new AsyncTask<Void, Void, Void>() {

			@Override
			// Show preview of this image
			protected Void doInBackground(Void... p) {
				// Create a image file to storage image captured
				File pictureFile = getOutputMediaFile();
				if (pictureFile == null) {
					Log.i("Camera",
							"Error creating media file, check storage permissions.");
					return null;
				}
				try {
					FileOutputStream fos = new FileOutputStream(pictureFile);
					fos.write(data);
					fos.close();
				} catch (FileNotFoundException e) {
					Log.e(CAMERA, "File not found: " + e.getMessage());
				} catch (IOException e) {
					Log.e(CAMERA, "Error accessing file: " + e.getMessage());
				}
				return null;
			}
		}.execute();
	}

	/** A safe way to get an instance of the Camera object. */
	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi", "NewApi", "NewApi",
			"NewApi" })
	public Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(mCurentCamId);
			// Find rotation screen to match camera orientation
			Camera.CameraInfo camInfo = new Camera.CameraInfo();
			Camera.getCameraInfo(mCurentCamId, camInfo);
			WindowManager mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
			int rotation = mWindowManager.getDefaultDisplay().getRotation();

			if (rotation == 0) {
				if (mCurentCamId == 0)
					mScreenRotation = camInfo.orientation;
				else
					mScreenRotation = (camInfo.orientation) % 360;//reverse camera here
				setDisplayOrientation(c, mScreenRotation);
			} else if (rotation == 1) {
				// Use when allow screen auto rotation
				if (mCurentCamId == 0)
					setDisplayOrientation(c, camInfo.orientation - 90);
				else
					setDisplayOrientation(c, camInfo.orientation + 90);
			} else if (rotation == 3) {
				// Use when allow screen auto rotation
				if (mCurentCamId == 0)
					setDisplayOrientation(c, camInfo.orientation + 90);
				else
					setDisplayOrientation(c, camInfo.orientation - 90);
			}
			// Find image supported size closest with size 640x480
			Camera.Parameters cameraParameter = c.getParameters();
			List<Camera.Size> imageSizeSupported = cameraParameter
					.getSupportedPictureSizes();
			Camera.Size goodSize = imageSizeSupported.get(0);
			for (Camera.Size tempSize : imageSizeSupported) {
				int maxGoodSize = Math.max(goodSize.width, goodSize.height);
				int minGoodSize = Math.min(goodSize.width, goodSize.height);

				int maxTempSize = Math.max(tempSize.width, tempSize.height);
				int minTempSize = Math.min(tempSize.width, tempSize.height);

				if (Math.abs((maxGoodSize - 640) * (minGoodSize - 480)) >= Math
						.abs((maxTempSize - 640) * (minTempSize - 480)))
					goodSize = tempSize;
			}
			Log.d(CAMERA, "Size closest with (640x480) = " + goodSize.width
					+ "," + goodSize.height);
			cameraParameter.setPictureSize(goodSize.width, goodSize.height);
			c.setParameters(cameraParameter);
		} catch (RuntimeException e) {
			Log.e(CAMERA, "Error when trying open camera id = " + mCurentCamId);
			e.printStackTrace();
		}
		// Returns null if camera is unavailable
		return c;
	}

	/** Check if this device has a camera */
	@SuppressLint({ "NewApi", "NewApi" })
	private boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			Log.w(CAMERA, "This device have: " + Camera.getNumberOfCameras()
					+ " camera.");
			return true;
		} else {
			Log.w(CAMERA, "This device have no camera.");
			return false;
		}
	}

	/**
	 * Disconnects and releases the Camera object resources.
	 */
	private void releaseCameraAndPreview() {
		mPreview.setCamera(null);
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	/** Create a File for saving image */
	private File getOutputMediaFile() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_UNMOUNTED)) {
			Log.w(CAMERA, "SD card unmounted.");
			return null;
		}
		File mediaStorageDir = new File(
				getFilename("Irocchi/PictureIrocchi/Composite"));

		// Create the storage directory if it does not exist.
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.e(CAMERA, "Failed to create directory ");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File pictureFile;
		pictureFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + timeStamp + ".jpg");
		return pictureFile;
	}

	@Override
	// Before finish, release camera is using
	public void finish() {
		releaseCameraAndPreview();
		super.finish();
	}

	private String getFilename(String path) {
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath, path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return (file.getAbsolutePath());
	}

	private Bitmap getBitmapImageLast(String path) {
		Bitmap bm1 = BitmapFactory.decodeResource(getResources(), 1);
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath, "Irocchi/Pictures/local/ICrop");
		if (!file.exists()) {
			file.mkdirs();
		}
		File[] sdDirList = file.listFiles();
		// Temp

		if (sdDirList != null) {

			bm1 = BitmapFactory.decodeFile(filepath + "/"
					+ "Irocchi/Pictures/local/ICrop" + "/"// temp
					+ sdDirList[sdDirList.length - 1].getName());
		}
		return bm1;
	}
}
