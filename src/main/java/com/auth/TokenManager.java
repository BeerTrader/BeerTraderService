package com.auth;

import java.util.HashMap;

import com.objects.domain.User;

public class TokenManager {
	
	private static HashMap<String,User> tokenCollection = new HashMap<>();
	
	public static void addToken(String token, User user) {
		tokenCollection.put(token, user);
	}
	
	public static boolean tokenExists(String token) {
		return tokenCollection.containsKey(token);
	}
	
	public static boolean validateToken(String token) {
		//TODO
		return false;
	}
}
