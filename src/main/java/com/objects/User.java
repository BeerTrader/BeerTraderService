package com.objects;

public class User {
	private long id;
	private String username;
	private String password;
 
	public User() {
		super();
	}
	
	public User(long id, String username, String password) {
		this.id = id;
		this.username = username;
		this.password = password;
	}
 
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + "]";
	}
}
