package com.unittest.db;

import static org.junit.Assert.*;

import org.junit.Test;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;

import com.db.DataManager;
import com.db.RelationshipTypeDB;
import com.unittest.AbstractDBTest;

public class RelationshipTypeDBTest extends AbstractDBTest {
	private RelationshipType relation = DynamicRelationshipType.withName("IS_A");
	
	@Test
	public void relationshipTypeDBTest() {
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			Node a = DataManager.getInstance().createNode();
			Node b = DataManager.getInstance().createNode();
			Node c = DataManager.getInstance().createNode();
			
			RelationshipTypeDB.addRelationshipBetweenNodes(a, b, relation);
			assertTrue(RelationshipTypeDB.hasRelationship(a, b, relation));
			assertFalse(RelationshipTypeDB.hasRelationship(b, c, relation));
			
			RelationshipTypeDB.removeRelationship(a, b, relation);
			assertFalse(RelationshipTypeDB.hasRelationship(a, b, relation));
		} catch (Exception e) {
			fail("Exception thrown: " + e.toString());
		}
	}
}
