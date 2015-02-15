package com.auth;

import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.UUID;

public class BasicTokenGenerator {
	public static String getNewToken(String username) {
		String key = UUID.randomUUID().toString().toUpperCase() +
		        "|" + username +
		        "|" + GregorianCalendar.getInstance(TimeZone.getTimeZone("CST")).getTime();		
		return key;
	}
	
	public static boolean isTokenExpired(String token) {
		return false;
	}
}
