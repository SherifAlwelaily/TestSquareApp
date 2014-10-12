package com.elmagnifico.egypt.testsquareapp.asyntask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.elmagnifico.egypt.testappsquare.helperclasses.Venues;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.GridView;

public class ReadPhoto extends Thread {
	private Bitmap img;
	public boolean finish = false;
	public ArrayList<Venues> venuesList;

	public ReadPhoto(ArrayList<Venues> venuesList) {
		this.venuesList = venuesList;
	}

	public Bitmap getImg() {
		return img;
	}

	public ReadPhoto() {
	}

	@Override
	public void run() {
		super.run();

		for (int i = 0; i < venuesList.size(); i++) {
			imageDecoding(venuesList.get(i).getImgUrl());
			venuesList.get(i).setImg(img);
			if (i == (venuesList.size() - 1))
				finish = true;
		}

	}

	public void imageDecoding(String imgUrl) {
		try {
			URL url = new URL(imgUrl);
			URLConnection conn = url.openConnection();
			InputStream in = conn.getInputStream();
			img = BitmapFactory.decodeStream(in);
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
