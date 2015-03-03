package com.auth.token;

import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.UUID;

public class BasicTokenGenerator implements TokenGenerator {
	private static long tokenDuration = 86400000;  //Milliseconds in a day
	
	public BasicTokenGenerator() {	
	}
	
	public String getNewToken(String username) {
		String key = UUID.randomUUID().toString().toUpperCase() +
		        "|" + username +
		        "|" + GregorianCalendar.getInstance(TimeZone.getTimeZone("CST")).getTime().getTime();
		return key;
	}
	
	public boolean isTokenExpired(String token) {
		String[] values = token.split("\\|");
		if (values.length!=3) {
			return true;
		}
		
		long createDate = Long.parseLong(values[2]);
		long currentTime = GregorianCalendar.getInstance(TimeZone.getTimeZone("CST")).getTime().getTime();
		
		if (Math.abs(currentTime-createDate)>tokenDuration) {
			return true;
		}
		
		return false;
	}
}
