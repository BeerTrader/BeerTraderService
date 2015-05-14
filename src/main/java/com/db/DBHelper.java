package com.db;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;

public class DBHelper {
    private DBHelper() {};
    
	public static Node getFirstNodeOfRelationshipType(Node sourceNode, RelationshipType... rType) {
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			Node firstRelationshipNode = sourceNode.getRelationships(rType).iterator().next().getOtherNode(sourceNode);
			tx.success();
			return firstRelationshipNode;
		}
	}

	public static List<Node> getNodesOfRelationshipType(Node sourceNode, RelationshipType... rType) {
		List<Node> returnList = new ArrayList<>();
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			Iterable<Relationship> relationships = sourceNode.getRelationships(rType);
			for (Relationship relationship: relationships) {
				returnList.add(relationship.getOtherNode(sourceNode));
			}
			tx.success();
		}
		return returnList;
	}
	
	public static Relationship getRelationship(Node sourceNode, Node destinationNode, RelationshipType... rType) throws NotFoundException {
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			Iterable<Relationship> relationships = sourceNode.getRelationships(rType);
			for (Relationship rel: relationships) {
				if (rel.getOtherNode(sourceNode).equals(destinationNode)) {
					tx.success();
					return rel;
				}
			}
			throw new NotFoundException("Could not find relationship of specified type between " + sourceNode.getId() + " and " + destinationNode.getId());
		}
	}
	
	public static Node createNodeWithUniqueId(Label nodeLabel) {
		Node returnNode;
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			if (nodeLabel==null) {
				returnNode = DataManager.getInstance().createNode();
			} else {
				returnNode = DataManager.getInstance().createNode(nodeLabel);
			}
			long id = GregorianCalendar.getInstance().getTimeInMillis();
			returnNode.setProperty("id", id);
			tx.success();
		}
		return returnNode;
	}
}
