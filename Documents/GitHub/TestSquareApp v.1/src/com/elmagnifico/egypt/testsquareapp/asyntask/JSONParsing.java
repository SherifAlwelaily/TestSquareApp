package com.elmagnifico.egypt.testsquareapp.asyntask;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;

public class JSONParsing {
	DefaultHttpClient client = new DefaultHttpClient();
	HttpResponse httpResponse = null;
	String response = "";
	public static final int GET = 1;
	public static final int POST = 2;

	public String makeServiceCall(String url, int method,
			List<NameValuePair> params) {
		try {
			if (method == GET) {
				if (params != null) {
					url += "/" + params.get(0).getValue();
				}
				HttpGet httpGet = new HttpGet(url);
				httpResponse = client.execute(httpGet);
			} else if (method == POST) {
				client.getParams().setParameter(ClientPNames.COOKIE_POLICY,
						CookiePolicy.RFC_2109);
				HttpPost httpPost = new HttpPost(url);
				httpPost.setHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.215 Safari/535.1");
				httpPost.setHeader("Accept",
						"text/html,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
				httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");
				if (params != null) {
					StringEntity se = null;
					try {
						se = new UrlEncodedFormEntity(params);
					} catch (UnsupportedEncodingException e) {

					}

					httpPost.setEntity(se);
				}
				httpResponse = client.execute(httpPost);
			}

			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null)
				response = EntityUtils.toString(httpEntity);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

}
