package com.iii.irocchi.camera;

import java.io.File;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import com.iii.irocchi.R;

public class CropImage extends ImageView {
	int mImageSource = 0;
	int mCropSource = 0;
	int mBackgroundSource = 0;
	int temp = 0;

	RuntimeException mException;
	private static final String PICTURE_FOLDER = "Irocchi/Pictures/local";

	public CropImage(Context context) {
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
		Bitmap original = this.getBitmapImageLast();// Lấy ảnh gốc (Background)
		Bitmap mask = getBitmap(mCropSource);

		Log.d("original",
				original.getHeight() + "<--H   " + original.getWidth()
						+ "<--- W");
		// Tạo ảnh mới
		Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(),
				Config.ARGB_8888);

		Log.d("mask ", mask.getHeight() + "<--H   " + mask.getWidth()
				+ "<--- W");

		Bitmap face = getBitmap(mBackgroundSource);
		Canvas mCanvas = new Canvas(result);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

		int a = original.getWidth() / 2 - mask.getWidth() / 2 + 5;

		System.out.println("DEBUG:  " + a);

		Bitmap resizeBitmapOriginal = Bitmap.createBitmap(original,
				original.getWidth() / 2 - mask.getWidth() / 2 + 5,
				original.getHeight() / 2 - mask.getHeight() / 2 + 1,
				mask.getWidth(), mask.getHeight());
		mCanvas.drawBitmap(resizeBitmapOriginal, 0, 0, null);
		mCanvas.drawBitmap(mask, 0, 0, paint);

		paint.setXfermode(null);
		mCanvas.drawBitmap(face, 0, 0, paint);

		/*------------Tối ưu bộ nhớ  (Xóa bitmap --------------*/
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
	private Bitmap getBitmapImageLast() {
		Bitmap bm1 = BitmapFactory.decodeResource(getResources(), mImageSource);
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath, PICTURE_FOLDER);
		if (!file.exists()) {
			file.mkdirs();
		}
		File[] sdDirList = file.listFiles();
		// Temp

		if (sdDirList != null) {

			bm1 = BitmapFactory.decodeFile(filepath + "/" + PICTURE_FOLDER
					+ "/"// temp
					+ sdDirList[sdDirList.length - 1].getName());
		}
		return bm1;
	}
}
