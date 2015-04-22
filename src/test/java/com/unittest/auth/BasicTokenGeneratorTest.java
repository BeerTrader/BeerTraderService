package com.unittest.auth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.BeforeClass;
import org.junit.Test;

import com.auth.token.BasicTokenGenerator;

public class BasicTokenGeneratorTest {
	private static BasicTokenGenerator tokenGenerator;
	
	@BeforeClass
	public static void initTokenGenerator() {
	    tokenGenerator = new BasicTokenGenerator();
	}
	
	@Test
	public void getNewTokenTest() {
		String token = tokenGenerator.getNewToken("usernameTest");
		String[] values = token.split("\\|");
		
		assertEquals(values.length,3);
		assertEquals(values[1],"usernameTest");
	}
	
	@Test
	public void isTokenExpiredTest() {
		String token = tokenGenerator.getNewToken("usernameTest");
		assertFalse(tokenGenerator.isTokenExpired(token));
	}
}
