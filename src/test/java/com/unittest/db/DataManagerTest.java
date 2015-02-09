package com.unittest.db;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.test.TestGraphDatabaseFactory;

import com.db.DataManager;

public class DataManagerTest {
	protected GraphDatabaseService graphDb;
	
	@Before
	public void prepareTestDatabase()
	{
	    graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
		//DataManager.getInstance();
	}

	@After
	public void destroyTestDatabase()
	{
	    graphDb.shutdown();
		//DataManager.getInstance().shutdown();
	}	

	@Test
	public void nodeTest() {
		Label userLabel = DynamicLabel.label("User");
		String nameToFind = "Steven";
		
		try (Transaction tx = DataManager.getInstance().beginTx())
		{
		    for (Node node : DataManager.getInstance().findNodesByLabelAndProperty(userLabel, "name", nameToFind))
		    {
		        node.delete();
		    }
		    tx.success();
		    int count = IteratorUtil.count(DataManager.getInstance().findNodesByLabelAndProperty(userLabel, "name", nameToFind));
		    assertEquals(count, 0);
		}
		
        Node n = null;
        try (Transaction tx = DataManager.getInstance().beginTx())
        {
            n = DataManager.getInstance().createNode();
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
