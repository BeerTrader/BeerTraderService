package com.unittest.db;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;

import com.db.DataManager;

public class DataManagerTest {
	@Test
	public void initTestDatabaseTest() {
		DataManager.initTestDatabase();
		assertTrue(DataManager.getInstance().isAvailable(0));
	}
	
	@Test
	public void shutdownGraphDbTest() {
		GraphDatabaseService s = DataManager.getInstance();
		DataManager.shutdownGraphDb();
		assertFalse(s.isAvailable(0));
	}
}
