package com.iii.irocchi.cameracomposite;

import java.io.File;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import com.iii.irocchi.R;
import com.iii.irocchi.camera.CameraActivity;

public class ProcessImage extends ImageView {
	private int mImageSource = 0;
	private int mCropSource = 0;
	private int mBackgroundSource = 0;

	RuntimeException mException;
	private static final String PICTURE_FOLDER = "Irocchi/PictureIrocchi/Composite";

	public ProcessImage(Context context) {
		super(context);
		setImageBitmap(cropImage());
		setScaleType(ScaleType.CENTER);
	}

	// get Bitmap croped
	private Bitmap cropImage() {
		/* -----------------Lấy ID của ảnh------------- */
		mImageSource = R.drawable.tem;
		mCropSource = R.drawable.normal_mask_inner;
		mBackgroundSource = R.drawable.normal_akacchi1;
		switch (CameraActivity.key) {
		case 1:
			mBackgroundSource = R.drawable.normal_akacchi2;
			break;
		case 2:
			mBackgroundSource = R.drawable.normal_akacchi3;
			break;
		case 3:
			mBackgroundSource = R.drawable.normal_akacchi1;
			break;
		case 4:
			mBackgroundSource = R.drawable.normal_akacchi4;
			break;
		default:
			break;
		}
		if (mImageSource == 0 || mCropSource == 0 || mBackgroundSource == 0) {
			mException = new IllegalArgumentException(
					": The content attribute is required and must refer to a valid image.");
		}
		if (mException != null)
			throw mException;
		/*----------------------------------------------------------------------------------*/
		Bitmap original = this.getBitmapImageLast(PICTURE_FOLDER);// to get original pic
		
		Bitmap mask = getBitmap(mCropSource);
		Bitmap result = Bitmap.createBitmap(original.getWidth(),
				original.getHeight(), Config.ARGB_8888);
		
		Bitmap face = Bitmap.createScaledBitmap(getBitmapImageLast("Irocchi/Pictures/local/ICrop"), 160, 180,true);
		
		Canvas mCanvas = new Canvas(result);
		
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		
		/*Bitmap resizeBitmapOriginal = Bitmap.createBitmap(original,
				original.getWidth() / 2 - mask.getWidth() / 2 + 5,
				original.getHeight() / 2 - mask.getHeight() / 2 + 1,
				mask.getWidth(), mask.getHeight());*/
		Bitmap resizeBitmapOriginal1 = Bitmap.createScaledBitmap(original, mask.getWidth(), mask.getHeight(), true);
		mCanvas.drawBitmap(resizeBitmapOriginal1, 0, 0, null);
		paint.setXfermode(null);
		mCanvas.drawBitmap(face, original.getWidth()/2 - face.getWidth(),
				original.getHeight() - face.getHeight() - 100, paint);

		/*------------saving the memory --------------*/
		original.recycle();
		original = null;
		mask.recycle();
		mask = null;
		face.recycle();
		face = null;
		/*----------------------------------------------*/

		return result;
	}

	// Get bitmap from id
	private Bitmap getBitmap(int id) {
		Drawable myIcon = getResources().getDrawable(id);
		Bitmap bitmap = ((BitmapDrawable) myIcon).getBitmap();
		return bitmap;
	}

	// Return a Bitmap from folder
	private Bitmap getBitmapImageLast(String path) {
		Bitmap bm1 = BitmapFactory.decodeResource(getResources(), mImageSource);
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath, path);
		if (!file.exists()) {
			file.mkdirs();
		}
		File[] sdDirList = file.listFiles();
		// Temp

		if (sdDirList != null) {

			bm1 = BitmapFactory.decodeFile(filepath + "/" + path
					+ "/"// temp
					+ sdDirList[sdDirList.length - 1].getName());
		}
		return bm1;
	}
	//to resize the bitmap image
	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);

		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);
		return resizedBitmap;
	}
}
