package com.elmagnifico.egypt.testsquareapp.asyntask;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AsynTask extends AsyncTask<Void, Void, Void> {

	String response = null;
	String url;
	int method;
	List<NameValuePair> parameters;

	public AsynTask(String url, int method, List<NameValuePair> parameters) {
		this.url = url;
		this.method = method;
		this.parameters = parameters;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	@Override
	protected Void doInBackground(Void... params) {
		JSONParsing jsonParsing = new JSONParsing();
		response = jsonParsing.makeServiceCall(url, method, parameters);

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
	}
}
