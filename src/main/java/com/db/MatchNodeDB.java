package com.db;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;

import com.factory.LabelFactory;
import com.factory.RelationshipTypeFactory;
import com.objects.domain.MatchNode;

public class MatchNodeDB {
	public static List<MatchNode> getPendingMatches(Node userNode) {
		List<MatchNode> returnList = new ArrayList<>();
		Iterable<Relationship> pendingRelationships = userNode.getRelationships(RelationshipTypeFactory.getRelationshipType("PENDING_DESIRER"));
		for (Relationship pendingRelationship: pendingRelationships) {
			Node matchNode = pendingRelationship.getOtherNode(userNode);
			returnList.add(getMatchNode(matchNode));
		}
		if (returnList.size()<25) {
			List<MatchNode> newMatches = findNewMatches(userNode);
			for (MatchNode newMatch: newMatches) {
				if (returnList.size()<25) {
					addMatch(newMatch);
					returnList.add(newMatch);
				}
				else {
					break;
				}
			}
		}
		return returnList;
	}
	
	public static List<MatchNode> findNewMatches(Node userNode) {
		//Return object, empty initialized
		List<MatchNode> returnList = new ArrayList<>();
		//Loop through TradingEntities user is desiring, if none return empty list
		Iterable<Relationship> desiringRelationships = userNode.getRelationships(RelationshipTypeFactory.getRelationshipType("DESIRES"));
		if (desiringRelationships.iterator().hasNext()) {
			//Loop through desired tradingEntities
			for (Relationship desiringRelationship: desiringRelationships) {
				Node tradingEntity = desiringRelationship.getOtherNode(userNode);
				//if beer, add people offering that beer
				if (tradingEntity.hasLabel(LabelFactory.getLabel("BEER"))) {
					List<Node> offerers = getOfferers(tradingEntity);
					for (Node offerer: offerers) {
						if (userInList(offerer.getProperty("username").toString(),returnList)==false) {
							MatchNode m = new MatchNode(userNode,offerer,tradingEntity,tradingEntity);
							returnList.add(m);
						}
					}
				}
				else {
					//if other tradingEntity, find users who offer beers of those entities
					Iterable<Relationship> relatedEntityRelationships = tradingEntity.getRelationships(RelationshipTypeFactory.getRelationshipType("IS_A"), RelationshipTypeFactory.getRelationshipType("MADE_BY"));
					for (Relationship relatedEntityRelationship: relatedEntityRelationships) {
						Node relatedTradingEntity = relatedEntityRelationship.getOtherNode(tradingEntity);
						List<Node> offerers = getOfferers(relatedTradingEntity);
						for (Node offerer: offerers) {
							if (userInList(offerer.getProperty("username").toString(),returnList)==false) {
								MatchNode m = new MatchNode(userNode,offerer,relatedTradingEntity,tradingEntity);
								returnList.add(m);
							}
						}
					}
				}
			}
		}
		return returnList;
	}

	private static List<Node> getOfferers(Node tradingEntity) {
		List<Node> offerers = new ArrayList<>();
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			Iterable<Relationship> offerableRelationships = tradingEntity.getRelationships(RelationshipTypeFactory.getRelationshipType("OFFERS"));
			for (Relationship offerableRelationship: offerableRelationships) {
				offerers.add(offerableRelationship.getOtherNode(tradingEntity));
			}
		}
		return offerers;
	}
	
	private static void addMatch(MatchNode mNode) {
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			Node newMatchNode = DataManager.getInstance().createNode(LabelFactory.getLabel("MATCH"));
			newMatchNode.createRelationshipTo(mNode.getDesirer(), RelationshipTypeFactory.getRelationshipType("PENDING_DESIRER"));
			newMatchNode.createRelationshipTo(mNode.getOfferer(), RelationshipTypeFactory.getRelationshipType("PENDING_OFFERER"));
			newMatchNode.createRelationshipTo(mNode.getOfferable(), RelationshipTypeFactory.getRelationshipType("MATCH_OFFER"));
			newMatchNode.createRelationshipTo(mNode.getDesirable(), RelationshipTypeFactory.getRelationshipType("MATCH_DESIRE"));
			tx.success();
		}
	}
	
	private static boolean userInList(String username, List<MatchNode> list) {
		for (MatchNode m: list) {
			if (m.getOfferer().getProperty("username").toString().equals(username))
				return true;
		}
		return false;
	}
	
	private static MatchNode getMatchNode(Node matchNode) {
		Node desirer = getFirstNodeOfRelationshipType(matchNode, RelationshipTypeFactory.getRelationshipType("PENDING_DESIRER"));
		Node offerer = getFirstNodeOfRelationshipType(matchNode, RelationshipTypeFactory.getRelationshipType("PENDING_OFFERER"));
		Node desirable = getFirstNodeOfRelationshipType(matchNode, RelationshipTypeFactory.getRelationshipType("MATCH_OFFER"));
		Node offerable = getFirstNodeOfRelationshipType(matchNode, RelationshipTypeFactory.getRelationshipType("MATCH_DESIRE"));
		return new MatchNode(desirer,offerer,desirable,offerable);
	}
	
	//TODO Move to DB helper class
	private static Node getFirstNodeOfRelationshipType(Node sourceNode, RelationshipType rType) {
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			return sourceNode.getRelationships(rType).iterator().next().getOtherNode(sourceNode);
		}
	}

	//TODO Move to DB helper class	
	private static List<Node> getNodesOfRelationshipType(Node sourceNode, RelationshipType rType) {
		List<Node> returnList = new ArrayList<>();
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			Iterable<Relationship> relationships = sourceNode.getRelationships(rType);
			for (Relationship relationship: relationships) {
				returnList.add(relationship.getOtherNode(sourceNode));
			}
		}
		return returnList;
	}
}