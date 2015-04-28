package com.unittest;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.db.DataManager;
import com.db.UserDB;
import com.exceptions.DuplicateUserException;

public abstract class AbstractDBTest {
	protected static String testUsername="testUsername";
	protected static String testPassword="testPassword";
	protected static String invalidUsername="foo";
	
	@BeforeClass
	public static void prepareTestDatabase() {
	    DataManager.initTestDatabase();
		
	    try {
			UserDB.registerUser(testUsername, testPassword, 41.8369, 87.6847, "dummy");
		} catch (DuplicateUserException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void destroyTestDatabase() {
	    DataManager.shutdownGraphDb();
	}
}
