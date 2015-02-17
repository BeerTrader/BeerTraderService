package com.auth.token;

public interface TokenGenerator {
	public String getNewToken(String username);
	
	public boolean isTokenExpired(String token);
}
