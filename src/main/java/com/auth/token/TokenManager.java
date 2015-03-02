package com.auth.token;

import java.util.HashMap;

import com.objects.domain.User;

public class TokenManager {
	
	private static HashMap<String,User> tokenCollection = new HashMap<>();
	private static TokenGenerator tokenGenerator = new BasicTokenGenerator();
	
	public static void addToken(String token, User user) {
		tokenCollection.put(token, user);
	}
	
	public static boolean tokenExists(String token) {
		return tokenCollection.containsKey(token);
	}
	
	public static String getNewToken(String username) {
		return tokenGenerator.getNewToken(username);
	}
	
	public static boolean isTokenExpired(String token) {
		return tokenGenerator.isTokenExpired(token);
	}
	
	public static User getUser(String token) {
		return tokenCollection.get(token);
	}
}
