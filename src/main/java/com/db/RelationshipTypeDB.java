package com.db;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;

public class RelationshipTypeDB {
    private RelationshipTypeDB() {};	
	
	public static boolean hasRelationship(Node source, Node destination, RelationshipType... relations) {
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			Iterable<Relationship> allRelations = source.getRelationships(relations);
			for (Relationship currentRelation: allRelations) {
				if (currentRelation.getOtherNode(source).equals(destination)) {
					tx.success();
					return true;
				}
			}
			tx.success();
			return false;
		}
	}
	
	public static void addRelationshipBetweenNodes(Node source, Node destination, RelationshipType relation) {
		if (hasRelationship(source,destination,relation)==false) {
			try (Transaction tx = DataManager.getInstance().beginTx()) {
				source.createRelationshipTo(destination, relation);
				tx.success();
			}
		}
	}
	
	public static void removeRelationship(Node source, Node destination, RelationshipType relation) {
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			Iterable<Relationship> relations = source.getRelationships(relation);
			for (Relationship currentRelation: relations) {
				System.out.println(currentRelation.getOtherNode(source) + ": " + destination.getId());
				if (currentRelation.getOtherNode(source).equals(destination)) {
					currentRelation.delete();
				}
			}
			tx.success();
		}		
	}
}
