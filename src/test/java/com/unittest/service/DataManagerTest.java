package com.unittest.service;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;

import com.service.DataManager;

public class DataManagerTest {
	protected GraphDatabaseService graphDb;
	
	@Before
	public void prepareTestDatabase()
	{
	    //graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
	}

	@After
	public void destroyTestDatabase()
	{
	    //graphDb.shutdown();
	}	

	@Test
	public void nodeTest() {
        Node n = null;
        try (Transaction tx = DataManager.getInstance().beginTx())
        {
            n = DataManager.getInstance().createNode();
            Label userLabel = DynamicLabel.label("User");
            n.addLabel(userLabel);
            n.setProperty( "name", "Steven" );
            tx.success();
        }

        // The node should have a valid id
        assertTrue(n.getId() > -1L);

        // Retrieve a node by using the id of the created node. The id's and
        // property should match.
        try (Transaction tx = DataManager.getInstance().beginTx())
        {
            Node foundNode = DataManager.getInstance().getNodeById(n.getId());
            assertEquals(foundNode.getId(), n.getId());
            assertEquals((String) foundNode.getProperty("name"), "Steven");
        }
	}
}
