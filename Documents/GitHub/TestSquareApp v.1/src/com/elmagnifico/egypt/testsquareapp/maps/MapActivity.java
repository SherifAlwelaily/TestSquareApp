package com.elmagnifico.egypt.testsquareapp.maps;

import java.io.IOException;
import java.io.InputStream;
import java.lang.Thread.State;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.elmagnifico.egypt.testappsquare.helperclasses.Venues;
import com.elmagnifico.egypt.testsquareapp.asyntask.AsynTask;
import com.elmagnifico.egypt.testsquareapp.asyntask.JSONParsing;
import com.elmagnifico.egypt.testsquareapp.asyntask.ReadPhoto;
import com.elmagnifico.egypt.testsquareapp.oauth.ActivityWebView;
import com.elmagnifico.egypt.testsquareapp.oauth.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;
import android.os.Build;

public class MapActivity extends Activity {

	private Location currentLocation;
	private GoogleMap map;
	private GPSTracker gpsTraker;
	private String response = "";
	private ArrayList<Venues> venuesList = new ArrayList<Venues>();
	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_map);
		prefs = getSharedPreferences("responseCache", MODE_PRIVATE);

		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.mapFragment)).getMap();
		map.setMyLocationEnabled(true);
		map.getUiSettings().setAllGesturesEnabled(true);
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {
				String title = marker.getTitle();
				int markerOrderSpliterPoint = title.indexOf(". ");
				int markerOrder = Integer.parseInt(title.substring(0,
						markerOrderSpliterPoint));
				String venuesId = venuesList.get(markerOrder).getId();
				String latLang = venuesList.get(markerOrder).getLat() + ","
						+ venuesList.get(markerOrder).getLon();

				Intent intent = new Intent(getBaseContext(),
						ActivityWebView.class);
				intent.putExtra("venuesId", venuesId);
				intent.putExtra("latLang", latLang);
				finish();
				startActivity(intent);

			}
		});

		gpsTraker = new GPSTracker(this);
		currentLocation = gpsTraker.getLocation();
		animateCameraToSpecificPosition(map, currentLocation.getLatitude(),
				currentLocation.getLongitude());

		response = prefs.getString("JSONResponse", "");
		if (response.length() > 0) {
			parseJSONResponse(response);
			// ReadPhoto readPhoto = new ReadPhoto(venuesList);
			// readPhoto.start();
			// while(!readPhoto.finish)
			// {
			//
			// }
			for (int i = 0; i < venuesList.size(); i++) {
				placePinOnMap(map, venuesList.get(i).getLat(), venuesList
						.get(i).getLon(), i + ". "
						+ venuesList.get(i).getName(), null);
				if (i == 0)
					animateCameraToSpecificPosition(map, venuesList.get(i)
							.getLat(), venuesList.get(i).getLon());
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void animateCameraToSpecificPosition(GoogleMap map, double lat,
			double lon) {
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(lat, lon)).zoom(17).build();
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

	public void placePinOnMap(GoogleMap map, double lat, double lon,
			String title, Bitmap icon) {
		MarkerOptions marker = new MarkerOptions()
				.position(new LatLng(lat, lon));
		if (title != null)
			marker.title(title);
		if (icon != null)
			marker.icon(BitmapDescriptorFactory.fromBitmap(icon));
		map.addMarker(marker);
	}

	public void parseJSONResponse(String response) {
		String name = "", formattedPhone = "", formattedAddress = "", imgUrl = "", id = "";
		double lat = 0, lon = 0;
		JSONObject jsonObj = new JSONObject();
		JSONObject innerJsonObj = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		JSONObject arrayObj = new JSONObject();
		JSONObject location = new JSONObject();
		try {
			jsonObj = new JSONObject(response);
			innerJsonObj = jsonObj.getJSONObject("response");
			jsonArr = innerJsonObj.getJSONArray("venues");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < jsonArr.length(); i++) {

			try {
				arrayObj = jsonArr.getJSONObject(i);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				id = arrayObj.getString("id");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				name = arrayObj.getString("name");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				JSONObject contact = arrayObj.getJSONObject("contact");
				formattedPhone = contact.getString("formattedPhone");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				location = arrayObj.getJSONObject("location");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				JSONArray addressArray = location
						.getJSONArray("formattedAddress");
				for (int j = 0; j < addressArray.length(); j++) {
					formattedAddress = formattedAddress
							+ addressArray.getString(j);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				lat = location.getDouble("lat");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				lon = location.getDouble("lng");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				JSONArray categories = arrayObj.getJSONArray("categories");
				JSONObject categoriesInnerObj = categories.getJSONObject(0);
				JSONObject icon = categoriesInnerObj.getJSONObject("icon");
				String prefix = icon.getString("prefix");
				String suffix = icon.getString("suffix");
				imgUrl = prefix + suffix;

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Venues venues = new Venues();
			venues.setFormattedAddress(formattedAddress);
			venues.setFormattedPhone(formattedPhone);
			venues.setLat(lat);
			venues.setLon(lon);
			venues.setName(name);
			venues.setId(id);
			venues.setImgUrl(imgUrl);
			venuesList.add(venues);
		}

	}

}
