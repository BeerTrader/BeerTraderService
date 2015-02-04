package com.objects;

public class User {
	private long id;
	private String name;
 
	public User() {
		super();
	}
	
	public User(long id, String name) {
		this.id = id;
		this.name = name;
	}
 
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + "]";
	}
}
