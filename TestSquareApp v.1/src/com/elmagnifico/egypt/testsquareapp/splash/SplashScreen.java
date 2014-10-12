package com.elmagnifico.egypt.testsquareapp.splash;

import com.elmagnifico.egypt.testsquareapp.oauth.MainActivity;
import com.elmagnifico.egypt.testsquareapp.oauth.R;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.os.Build;

public class SplashScreen extends Activity {

	private int SPLASH_TIME_OUT = 5000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash_screen);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity
				Intent intent = new Intent(getBaseContext(), MainActivity.class);
				finish();
				startActivity(intent);

			}
		}, SPLASH_TIME_OUT);

	}

}
