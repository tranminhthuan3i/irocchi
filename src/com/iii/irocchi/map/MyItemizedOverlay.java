package com.iii.irocchi.map;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapView;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapView.Projection;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Environment;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.iii.irocchi.R;
import com.iii.irocchi.common.CommonInfor;

public class MyItemizedOverlay extends ItemizedOverlay<OverlayItem>{

	private ArrayList<OverlayItem> myOverlays;

	private MapView mapView;
	private double latitude = 100.0;
	private double longitude = 10.0;
	
	private Double la = null;
	private Double lo = null;

	private final int DELAY_SPACE = 10000;

	public ShowMap_Activity mShowMap_Activty_instant;
	private int count = 0;
	private float temp = 0;
	private GeoPoint g;
	private static MapController mapcontroler;
	
	public MyItemizedOverlay(Drawable pDefaultMarker,
			ResourceProxy pResourceProxy, ShowMap_Activity pShowMap_Activity,
			MapView mapView, MapController mapController  ) {
		super(pDefaultMarker, pResourceProxy);
		myOverlays = new ArrayList<OverlayItem>();
		mShowMap_Activty_instant = pShowMap_Activity;
		this.mapView = mapView;
		mapcontroler = mapController;
		mapView.setTileSource(TileSourceFactory.MAPQUESTOSM);
		mapView.setBuiltInZoomControls(true);
		mapView.setMultiTouchControls(true);
	}
	
	

	public void addOverlay(OverlayItem overlay) {
		myOverlays.add(overlay);
		populate();
	}

	public void removeItem(int i) {
		myOverlays.remove(i);
		populate();
	}

	@Override
	public void draw(Canvas arg0, MapView arg1, boolean arg2) {
		
		final Canvas canvas = arg0;
		if (ShowMap_Activity.key != -1) {
			GeoPoint geoPoint = new GeoPoint(CommonInfor.latitude,
					CommonInfor.longitude);
			Projection projection = mapView.getProjection();
			Point point = new Point();
			projection.toPixels(geoPoint, point);
			Bitmap pBm = getBitmapImageLast();
			if (pBm != null)
				canvas.drawBitmap(
						Bitmap.createScaledBitmap(pBm, 100, 130, true),
						point.x, point.y, null);

			System.out.println("X+  " + point.x + "  Y=  " + point.y);

			canvas.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory
					.decodeResource(mShowMap_Activty_instant.getResources(),
							R.drawable.thuan), 100, 130, true), 420, 10, null);

			// add name of country and flag
			if (CommonInfor.getCountryFlag(mShowMap_Activty_instant,
					CommonInfor.countryName) != null) {
				mShowMap_Activty_instant.btn_flag.setImageBitmap(CommonInfor
						.getCountryFlag(mShowMap_Activty_instant,
								CommonInfor.countryName));
			}
			if(CommonInfor.countryName!=""){
				mShowMap_Activty_instant.txtCountryNameMap.setText(CommonInfor.countryName);
			}
			

		}
		super.draw(arg0, arg1, arg2);
	}
	
	
	@Override
	public boolean onSingleTapConfirmed(MotionEvent event, MapView mapView) {
		boolean bl = super.onDoubleTap(event, mapView);
		// redraw mapView
		mapView.invalidate();
		if (!bl && ShowMap_Activity.key == -1) {
			try { 
				IGeoPoint p = mapView.getProjection().fromPixels(
						(int) event.getX(), (int) event.getY());
				
				latitude = p.getLatitudeE6() / 1E6;
				longitude = p.getLongitudeE6() / 1E6;
				
				Geocoder geoCoder = new Geocoder(mShowMap_Activty_instant,
						Locale.getDefault());

				List<Address> addresses = geoCoder.getFromLocation(latitude,
						longitude, 1);

				String add = "";
				if (addresses.size() > 0) {
						add +="Country : "+addresses.get(0).getCountryName();
						if(addresses.get(0).getAdminArea() != null){
							add += ", "+addresses.get(0).getAdminArea();
						}
						
						// To save information of country
						CommonInfor.countryName = addresses.get(0)
								.getCountryName();
						CommonInfor.countryCode = addresses.get(0)
								.getCountryCode();
						changeFlag();// change flag

						CommonInfor.latitude = latitude;
						CommonInfor.longitude = longitude;
					}
				if (add.equals("")) {

					Toast.makeText(this.mShowMap_Activty_instant,
							"Please tap to countries", Toast.LENGTH_SHORT)
							.show();
				} else {
					 la = (latitude * 1E6);
					 lo = (longitude * 1E6);

					CurrentPointOverLay myPointOverLay = new CurrentPointOverLay(
							mShowMap_Activty_instant, la.intValue(),
							lo.intValue(), add);
					List<Overlay> overlay = mapView.getOverlays();
					overlay.add(myPointOverLay);
					Toast.makeText(this.mShowMap_Activty_instant,add, Toast.LENGTH_SHORT).show();
				}
				
				
				// -- focus
//				
//				GeoPoint geoPoint = new GeoPoint(latitude, longitude);
//				
//				int minLat = Integer.MAX_VALUE;
//				int maxLat = Integer.MIN_VALUE;
//				int minLon = Integer.MAX_VALUE;
//				int maxLon = Integer.MIN_VALUE;
//				
//				Log.i("Log : ", "minLat : "+minLat);
//				Log.i("Log : ", "maxLat : "+maxLat);
//				Log.i("Log : ", "minLon : "+minLon);
//				Log.i("Log : ", "maxLon : "+maxLon);
//
//				
//				      int lat = geoPoint.getLatitudeE6();
//				      int lon = geoPoint.getLongitudeE6();
//
//				      maxLat = Math.max(lat, maxLat);
//				      minLat = Math.min(lat, minLat);
//				      maxLon = Math.max(lon, maxLon);
//				      minLon = Math.min(lon, minLon);
//
//						Log.i("Log : ", "------------------------ : ");
//
//						Log.i("Log : ", "minLat : "+minLat);
//						Log.i("Log : ", "maxLat : "+maxLat);
//						Log.i("Log : ", "minLon : "+minLon);
//						Log.i("Log : ", "maxLon : "+maxLon);
//				      
//				mapcontroler.zoomToSpan(Math.abs(maxLat - minLat), Math.abs(maxLon - minLon));
//				mapcontroler.animateTo(new GeoPoint( (maxLat + minLat)/2, 
//				(maxLon + minLon)/2 )); 
				
				
				
				
			} catch (IOException e) {
				new AlertDialog.Builder(mShowMap_Activty_instant)
						.setTitle("Error")
						.setMessage("Please Connect Network !")
						.setNegativeButton("Ok",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								}).show();
				Log.i("Log : ", "Exception : " + e.getMessage());
				e.printStackTrace();
			}

		}
		
		return super.onSingleTapConfirmed(event, mapView);
	}
	
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (x * x + y * y);
	}
	
	List<Float> arr;
	List<Float> arr1;
	boolean accept = true;
	
	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {

		if(event.getPointerCount() == 2){
		
	      switch (event.getAction() & MotionEvent.ACTION_MASK) {
	      
	      case MotionEvent.ACTION_POINTER_DOWN:
		         temp = spacing(event);
		         
		         arr = new ArrayList<Float>();
		         arr1 = new ArrayList<Float>();
		         break;
		         
		  case MotionEvent.ACTION_UP:
		  case MotionEvent.ACTION_POINTER_UP:
			  
	         break;
	      case MotionEvent.ACTION_MOVE:
	            float newDist = spacing(event);
	            
	            if(newDist < temp){

		            	if(!((newDist > (temp + DELAY_SPACE)) && (newDist < (temp - DELAY_SPACE)))){
		            		
		            		zoomInOut(newDist, event);
			 	           	 	temp = newDist; 
						}
	            	 return true;
	            }
	            if(newDist > temp) {
	            	
	            	if(!((newDist > (temp + DELAY_SPACE)) && (newDist < (temp - DELAY_SPACE)))){
	            		
		            		zoomInOut(newDist, event);
			 	           	 	temp = newDist;
					}
	            	return true;
				}

	         break;
	      }
		}
		
		
		return super.onTouchEvent(event, mapView);
	}
	
	
	private void zoomInOut(float newDist, MotionEvent event) {
	
       			if(accept){
					arr.add(newDist);
					if(count == 5){
						checkZoomInOut(arr1, event);
		            	accept = false;
		        		count = 0;
		        		arr1.clear();
		            }
					count++;
            	}else {
					arr1.add(newDist);

		            if(count == 5){
		            	checkZoomInOut(arr, event);
		            	accept = true;
		        		count = 0;
		        		arr.clear();
		            }
					count++;
				}
	}
	
	private void checkZoomInOut(List<Float> arrs, MotionEvent event) {
		
		if(arrs.size()>0){
			int size = arrs.size();
			
				if((arrs.get(size-size) < arrs.get(size / 2)) && 
						(arrs.get(size / 2) < arrs.get(size - 1))){
	
		    		g = loadlocation(event);
		        	mapcontroler.zoomInFixing(g);
		        	return;
				}
				if((arrs.get(size-size) > arrs.get(size / 2)) && 
						(arrs.get(size / 2) > arrs.get(size - 1))) {
	
			       	 g = loadlocation(event);
			       	 mapcontroler.zoomOutFixing(g);
			       	 return;
				}
		}
		
	}
	

	private GeoPoint loadlocation(MotionEvent event) {
	
		  float xL1 = event.getX(0);
		  float yL1 = event.getY(0);
        
		  float xL2 = event.getX(1);
		  float yL2 = event.getY(1);
        
        
        
        float tbx = FloatMath.sqrt((xL1 * xL1 + xL2 * xL2)/3);
        float tby = FloatMath.sqrt((yL1 * yL1 + yL2 * yL2)/3);
        
		
  	  	IGeoPoint p = mapView.getProjection().fromPixels( tbx,  tby);
		
	  
  	  	latitude = p.getLatitudeE6() / 1E6;
		longitude = p.getLongitudeE6() / 1E6;
  	  
		return new GeoPoint(latitude, longitude);
		
	}
	
	
	@Override
	public boolean onSnapToItem(int arg0, int arg1, Point point,
			IMapView mapView) {

		return false;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return myOverlays.get(i);
	}

	@Override
	public int size() {
		return myOverlays.size();
	}

	// change flag

	public void changeFlag() {
		if (CommonInfor.countryName != null) {
			String countryNameFlag = "country_"
					+ CommonInfor.countryName.toLowerCase().replaceAll(" ", "");

			int id = mShowMap_Activty_instant.getResources().getIdentifier(
					countryNameFlag, "drawable",
					mShowMap_Activty_instant.getPackageName());
			// III79
			if (id != 0) {
				Bitmap bm = BitmapFactory.decodeResource(
						mShowMap_Activty_instant.getResources(), id);

				mShowMap_Activty_instant.btn_flag.setImageBitmap(Bitmap
						.createScaledBitmap(bm, 250, 190, true));
				mShowMap_Activty_instant.txtCountryNameMap
						.setText(CommonInfor.countryName);
			}
		}
	}

	private Bitmap getBitmapImageLast() {
		Bitmap bm1 = BitmapFactory.decodeResource(
				mShowMap_Activty_instant.getResources(), 1);
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