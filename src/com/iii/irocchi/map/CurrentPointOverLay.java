package com.iii.irocchi.map;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapView.Projection;
import org.osmdroid.views.overlay.Overlay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;

public class CurrentPointOverLay extends Overlay {

	private static int latituded;
	private static int longituded;
	private static String description;
	private final int mRadius = 5;
	Context context;
	
	public CurrentPointOverLay(Context context,int latitude, int longitude, String diachi) {
		super(context);
		latituded = latitude;
		longituded = longitude;
		description = diachi;
		this.context = context;

	}


	@Override
	protected void draw(Canvas canvas, MapView mapView, boolean shadow) {
		try {

			Projection projection = mapView.getProjection();
			if (!shadow) {
				GeoPoint geoPoint = new GeoPoint(latituded, longituded);

				Point point = new Point();
				projection.toPixels(geoPoint, point);
				RectF oval = new RectF(point.x - mRadius, point.y - mRadius,
						point.x + mRadius, point.y + mRadius);
				

				Paint paint = new Paint();
				paint.setARGB(250, 255, 255, 255);
				paint.setAntiAlias(true);
				paint.setFakeBoldText(true);
				paint.setTextSize(30);
				paint.setColor(Color.RED);
				paint.setTypeface(Typeface.SERIF);

				Paint backPaint = new Paint();
				backPaint.setARGB(250, 50, 50, 50);
				backPaint.setAntiAlias(true);
				backPaint.setColor(Color.BLACK);
				

				int countString = description.length() * 17;
				RectF backOval = new RectF(point.x + 5 + mRadius, point.y
						- mRadius - 15, point.x + countString, point.y + 25);

				canvas.drawOval(oval, paint);
				canvas.drawRoundRect(backOval, 5, 5, backPaint);
				canvas.drawText("  " + description, point.x + 2 * mRadius,
						point.y + 10, paint);
				

			}
		} catch (Exception e) {
			Log.i("Log : ", "Exception : " + e.getMessage());
		}
	}
}