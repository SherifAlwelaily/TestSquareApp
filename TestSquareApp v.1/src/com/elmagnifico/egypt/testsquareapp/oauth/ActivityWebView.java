package com.elmagnifico.egypt.testsquareapp.oauth;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.elmagnifico.egypt.testsquareapp.asyntask.AsynTask;
import com.elmagnifico.egypt.testsquareapp.asyntask.JSONParsing;
import com.elmagnifico.egypt.testsquareapp.maps.MapActivity;
import com.google.android.gms.internal.da;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.GridLayout.LayoutParams;
import android.os.Build;
import android.graphics.Bitmap;

public class ActivityWebView extends Activity {
	public static final String CALLBACK_URL = "https://foursquare.com/";
	private String venuesId, latLang;
	private String resultMsg = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_web_view);
		Intent intent = getIntent();
		venuesId = intent.getStringExtra("venuesId");
		latLang = intent.getStringExtra("latLang");

		String url = "https://foursquare.com/oauth2/authenticate?client_id="
				+ MainActivity.CLIENT_ID + "&response_type=token&redirect_uri="
				+ CALLBACK_URL;

		WebView webview = (WebView) findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebViewClient(new WebViewClient() {
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				String fragment = "#access_token=";
				int start = url.indexOf(fragment);
				if (start > -1) {
					String accessToken = url.substring(
							start + fragment.length(), url.length());
					String checkinUrl = "https://api.foursquare.com/v2/checkins/add";
					DateFormat df = new SimpleDateFormat("yyyyMMdd");
					String date = df.format(Calendar.getInstance().getTime());
					ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("oauth_token",
							accessToken));
					params.add(new BasicNameValuePair("venueId", venuesId));
					params.add(new BasicNameValuePair("ll", latLang));
					params.add(new BasicNameValuePair("shout", "Check in"));
					params.add(new BasicNameValuePair("v", date));

					AsynTask asynTask = new AsynTask(checkinUrl,
							JSONParsing.POST, params);
					asynTask.execute();
					while (asynTask.getResponse() == null) {

					}
					resultMsg = parseCheckinResponse(asynTask.getResponse());
					
					showDialog(0);
					
	
				}
			}
		});
		webview.loadUrl(url);
	}
	
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		switch(id)
		{
			case 0: 
				TextView tv = new TextView(this);
				tv.setText(resultMsg);
				return new AlertDialog.Builder(this).setView(tv).setPositiveButton("OK",new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
						startActivity(new Intent(getBaseContext(),MapActivity.class));
						
					}
				}).create();
		}
		return super.onCreateDialog(id);
	}

	public String parseCheckinResponse(String response)
	{
		String msg = "";
		try {
			JSONObject jsonObj = new JSONObject(response);
			JSONObject responseObj = jsonObj.getJSONObject("response");
			JSONObject checkinObj = responseObj.getJSONObject("checkin");
			JSONObject userObj = checkinObj.getJSONObject("user");
			JSONObject venueObj = checkinObj.getJSONObject("venue");
			msg = msg + "Hi " + userObj.getString("firstName") + " " + userObj.getString("lastName")+" !!!";
			msg = msg + "\nYou have successfully checked in ";
			msg = msg + venueObj.getString("name")+".";
			msg = msg + "\nPress OK to return to map.";
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msg;
		
		
	}
}
