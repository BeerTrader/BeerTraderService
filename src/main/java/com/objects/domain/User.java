package com.objects.domain;

public class User {
	private long id;
	private String username;
	private String password;
	private double latitude;
	private double longitude;
 
	public User() {
	}
	
	public User(long id, String username, String password, double latitude, double longitude) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
 
	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}
}