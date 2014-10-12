package com.elmagnifico.egypt.testsquareapp.oauth;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.elmagnifico.egypt.testsquareapp.asyntask.AsynTask;
import com.elmagnifico.egypt.testsquareapp.asyntask.JSONParsing;
import com.elmagnifico.egypt.testsquareapp.maps.GPSTracker;
import com.elmagnifico.egypt.testsquareapp.maps.MapActivity;
import com.foursquare.android.nativeoauth.FoursquareOAuth;
import com.foursquare.android.nativeoauth.FoursquareOAuthException;
import com.foursquare.android.nativeoauth.model.AccessTokenResponse;
import com.foursquare.android.nativeoauth.model.AuthCodeResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends Activity {

	public static final int foursquareRequestCode = 1;
	public static final int tokenExchangeRequestCode = 2;

	public static final String CLIENT_ID = "HNQOBCVN0R3D10YERBRZE3JSYFH5AGBQ0VZ4QWTEBSLNOUCM";
	public static final String CLIENT_SECRET = "BJM34HR3IHTHGSJRBIP000WFSLWHVKCU1C54HQOFYDAADSN5";
	private String accessToken = null;
	private Button searchBt;
	private String url;
	private Location currentLocation;
	private GPSTracker gpsTraker;
	private AsynTask asynTask;
	private String jsonResponse = "";
	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		prefs = getSharedPreferences("responseCache", MODE_PRIVATE);
		jsonResponse = prefs.getString("JSONResponse", "");
		if (jsonResponse.length() > 0) {
			Intent mapIntent = new Intent(this, MapActivity.class);
			startActivity(mapIntent);
		}

		searchBt = (Button) findViewById(R.id.searchBt);
		searchBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = FoursquareOAuth.getConnectIntent(
						getBaseContext(), CLIENT_ID);
				if (!FoursquareOAuth.isPlayStoreIntent(intent))
					startActivityForResult(intent, foursquareRequestCode);

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case foursquareRequestCode:
			AuthCodeResponse codeResponse = FoursquareOAuth
					.getAuthCodeFromResult(resultCode, data);
			Intent intent = FoursquareOAuth.getTokenExchangeIntent(this,
					CLIENT_ID, CLIENT_SECRET, codeResponse.getCode());
			startActivityForResult(intent, tokenExchangeRequestCode);
			break;

		case tokenExchangeRequestCode:
			AccessTokenResponse tokenResponse = FoursquareOAuth
					.getTokenFromResult(resultCode, data);
			accessToken = tokenResponse.getAccessToken();
			if (accessToken != null) {
				gpsTraker = new GPSTracker(this);
				currentLocation = gpsTraker.getLocation();
				url = "https://api.foursquare.com/v2/venues/search?near="
						+ currentLocation.getLatitude() + ","
						+ currentLocation.getLongitude() + "&oauth_token="
						+ accessToken + "&v=20141008";

				asynTask = new AsynTask(url, JSONParsing.GET, null);
				asynTask.execute();
				while (asynTask.getResponse() == null) {

				}
				jsonResponse = asynTask.getResponse();
			}
			if (jsonResponse.length() > 0) {
				Editor editor = prefs.edit();
				editor.putString("JSONResponse", jsonResponse);
				editor.commit();

				Intent mapIntent = new Intent(this, MapActivity.class);
				startActivity(mapIntent);
			} else
				Toast.makeText(this, "Check your Network Connection",
						Toast.LENGTH_LONG).show();

			break;

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

}
