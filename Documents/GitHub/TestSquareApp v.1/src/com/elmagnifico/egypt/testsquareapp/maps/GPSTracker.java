package com.elmagnifico.egypt.testsquareapp.maps;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class GPSTracker extends Service implements LocationListener {
	private Context context;
	private LocationManager locationManager;
	private boolean networkEnabled = false;
	private boolean gpsEnabled = false;
	private Location location;
	private final static long minTime = 100;
	private final static long minDistance = 1;

	public boolean isNetworkEnabled() {
		return networkEnabled;
	}

	public void setNetworkEnabled(boolean networkEnabled) {
		this.networkEnabled = networkEnabled;
	}

	public boolean isGpsEnabled() {
		return gpsEnabled;
	}

	public void setGpsEnabled(boolean gpsEnabled) {
		this.gpsEnabled = gpsEnabled;
	}

	public GPSTracker(Context context) {
		this.context = context;
	}

	public Location getLocation() {
		locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

		if (locationManager != null) {
			networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			if (networkEnabled) {
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance,this);
				location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			} else if (gpsEnabled) {
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance,this);
				location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			} else if (!(networkEnabled && gpsEnabled)) {
				showAlertDialog();
			}
		}
		return location;

	}

	public void showAlertDialog() {
		((Activity) context).showDialog(1);

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void stopUsingGPS() {
		if (locationManager != null) {
			locationManager.removeUpdates(this);
		}
	}

}
