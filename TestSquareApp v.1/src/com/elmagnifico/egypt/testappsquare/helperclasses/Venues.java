package com.elmagnifico.egypt.testappsquare.helperclasses;

import android.graphics.Bitmap;

public class Venues {

	private String name = "", formattedPhone = "", formattedAddress = "",
			imgUrl = "", id = "";
	private double lat, lon;
	private Bitmap img;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Bitmap getImg() {
		return img;
	}

	public void setImg(Bitmap img) {
		this.img = img;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFormattedPhone() {
		return formattedPhone;
	}

	public void setFormattedPhone(String formattedPhone) {
		this.formattedPhone = formattedPhone;
	}

	public String getFormattedAddress() {
		return formattedAddress;
	}

	public void setFormattedAddress(String formattedAddress) {
		this.formattedAddress = formattedAddress;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

}
