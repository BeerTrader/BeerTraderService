package com.db;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;

public class RelationshipTypeDB {
	public static void addRelationshipBetweenNodes(Node source, Node dest, RelationshipType relation) {
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			source.createRelationshipTo(dest, relation);
			tx.success();
		}
	}
}
