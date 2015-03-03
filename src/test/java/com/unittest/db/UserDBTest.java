package com.unittest.db;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import com.db.UserDB;
import com.exceptions.DuplicateUserException;
import com.exceptions.UserNotAuthorizedException;
import com.exceptions.UserNotFoundException;
import com.objects.domain.User;
import com.unittest.AbstractDBTest;

public class UserDBTest extends AbstractDBTest {
	@Ignore
	@Test
	public void userExistsTest() {
		boolean result = UserDB.userExists(testUsername);
		System.out.println(result);
		assertTrue(result);
	}
	
	@Ignore
	@Test(expected=UserNotFoundException.class)
	public void getUserFailureTest() throws UserNotFoundException {
		@SuppressWarnings("unused")
		User user = UserDB.getUser(invalidUsername);
	}
	
	@Ignore
	@Test
	public void getUserSuccessTest() throws UserNotFoundException {
		User user = UserDB.getUser(testUsername);
		assertNotNull(user);
	}
	
	@Ignore
	@Test(expected=UserNotAuthorizedException.class)
	public void authenticateUserFailureTest() throws UserNotAuthorizedException {
		UserDB.authenticateUser(invalidUsername, testPassword);
	}
	
	@Ignore
	@Test
	public void authenticateUserSuccessTest() throws UserNotAuthorizedException {
		User user = UserDB.authenticateUser(testUsername, testPassword);
		assertNotNull(user);
	}
	
	@Ignore
	@Test(expected=DuplicateUserException.class)
	public void registerUserFailureTest() throws DuplicateUserException {
		UserDB.registerUser(testUsername, testPassword);
	}
}
