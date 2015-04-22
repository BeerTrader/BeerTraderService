package com.unittest.auth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.BeforeClass;
import org.junit.Test;

import com.auth.token.TokenManager;
import com.objects.domain.User;

public class TokenManagerTest {
	private static User u;
	private static User v;
	private static String uToken;
	private static String vToken;
	
	@BeforeClass
	public static void initTokenManager() {
	    u = new User(1,"usernameTest","passwordTest");
	    v = new User(2,"usernameTest2","passwordTest");
	    
	    uToken = TokenManager.getNewToken(u.getUsername());
	    vToken = TokenManager.getNewToken(v.getUsername());
	    
	    TokenManager.addToken(uToken, u);
	    TokenManager.addToken(vToken, v);
	}
	
	@Test
	public void tokenExistsTest() {
		assertEquals(TokenManager.tokenExists(uToken),true);
		assertFalse(TokenManager.tokenExists("invalidToken"));
	}
	
	@Test
	public void getUserTest() {
		User testUser = TokenManager.getUser(uToken);
		assertEquals(testUser.getUsername(),u.getUsername());
		
		User testUser2 = TokenManager.getUser(vToken);
		assertEquals(testUser2.getUsername(),v.getUsername());
	}
}
