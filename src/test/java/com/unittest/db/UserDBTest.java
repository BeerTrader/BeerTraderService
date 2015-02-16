package com.unittest.db;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.db.DataManager;
import com.db.UserDB;
import com.exceptions.DuplicateUserException;
import com.exceptions.UserNotAuthorizedException;
import com.exceptions.UserNotFoundException;
import com.objects.domain.User;

public class UserDBTest {
	private static String testUsername="testUsername";
	private static String testPassword="testPassword";
	private static String invalidUsername="foo";
	
	@BeforeClass
	public static void prepareTestDatabase() {
	    DataManager.initTestDatabase();
		
	    try {
			UserDB.registerUser(testUsername, testPassword);
		} catch (DuplicateUserException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void destroyTestDatabase() {
	    DataManager.shutdownGraphDb();
	}
	
	@Test
	public void userExistsTest() {
		boolean result = UserDB.userExists(testUsername);
		System.out.println(result);
		assertTrue(result);
	}
	
	@Test(expected=UserNotFoundException.class)
	public void getUserFailureTest() throws UserNotFoundException {
		@SuppressWarnings("unused")
		User user = UserDB.getUser(invalidUsername);
	}
	
	@Test
	public void getUserSuccessTest() throws UserNotFoundException {
		User user = UserDB.getUser(testUsername);
		assertNotNull(user);
	}
	
	@Test(expected=UserNotAuthorizedException.class)
	public void authenticateUserFailureTest() throws UserNotAuthorizedException {
		UserDB.authenticateUser(invalidUsername, testPassword);
	}
	
	@Test
	public void authenticateUserSuccessTest() throws UserNotAuthorizedException {
		User user = UserDB.authenticateUser(testUsername, testPassword);
		assertNotNull(user);
	}
	
	@Test(expected=DuplicateUserException.class)
	public void registerUserFailureTest() throws DuplicateUserException {
		UserDB.registerUser(testUsername, testPassword);
	}
}
