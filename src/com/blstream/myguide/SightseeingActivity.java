
package com.blstream.myguide;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class SightseeingActivity extends Activity implements OnCameraChangeListener {
	
	private GoogleMap mMap;
	
	private float mMinZoom;
	private float mMaxZoom;
	private float mStartZoom;
	private double mStartCenterLat;
	private double mStartCenterLon;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sightseeing);

		mMinZoom = 14.5f;
		mMaxZoom = 19.0f;
		mStartZoom = 15.5f;
		mStartCenterLat = 51.1052072;
		mStartCenterLon = 17.0754498;
		
/* TODO:
 * Kiedy pojawi się odpowiednia klasa Settings będzie wykonywane
 * coś w rodzaju poniższego kodu (zamiast fragmentu, który jest wyżej):
 * 
		Settings settings = ParsedDataSingleton.getInstance().getSettings();
		if (settings != null) {
			mMinZoom = (float) settings.get("min-zoom");
			mMaxZoom = (float) settings.get("max-zoom");
			mStartZoom = (float) settings.get("start-zoom");
			mStartCenterLat = (double) settings.get("center-lat");
			mStartCenterLon = (double) settings.get("center-lon");
		}
*/		
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mStartCenterLat,mStartCenterLon), mStartZoom));
		mMap.setOnCameraChangeListener(this);
	}
	
	public void onCameraChange(CameraPosition camera) {
		if (camera.zoom < mMinZoom) {
			mMap.animateCamera(CameraUpdateFactory.zoomTo(mMinZoom));
		}
		else if (camera.zoom > mMaxZoom) {
			mMap.animateCamera(CameraUpdateFactory.zoomTo(mMaxZoom));
		}
	}
}