package com.db;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import com.factory.LabelFactory;
import com.factory.RelationshipTypeFactory;
import com.geo.SimpleDistance;
import com.objects.domain.MatchNode;

public class MatchNodeDB {
    private MatchNodeDB() {};
    
	final static double MAX_DISTANCE = 5;
	
	public static List<MatchNode> getPendingMatches(Node userNode) {
		List<MatchNode> returnList = new ArrayList<>();
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			Iterable<Relationship> pendingRelationships = userNode.getRelationships(RelationshipTypeFactory.getRelationshipType("PENDING_DESIRER"));
			for (Relationship pendingRelationship: pendingRelationships) {
				Node matchNode = pendingRelationship.getOtherNode(userNode);
				returnList.add(getMatchNode(matchNode));
			}
			if (returnList.size()<25) {
				List<MatchNode> newMatches = findNewMatches(userNode);
				for (MatchNode newMatch: newMatches) {
					if (returnList.size()<25) {
						if (matchExists(newMatch)==false) {
							addMatch(newMatch);
							returnList.add(newMatch);
						}
					}
					else {
						break;
					}
				}
			}
			tx.success();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return returnList;
	}
	
	public static List<MatchNode> findNewMatches(Node userNode) {
		//Return object, empty initialized
		List<MatchNode> returnList = new ArrayList<>();
		try (Transaction tx = DataManager.getInstance().beginTx()) {
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
								if (matchInRange(userNode,offerer)) {
									if (alreadyMatched(userNode,offerer,tradingEntity)==false) {
										MatchNode m = new MatchNode(offerer,userNode,tradingEntity,tradingEntity);
										returnList.add(m);
									}
								}
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
									if (matchInRange(userNode,offerer)) {
										if (alreadyMatched(userNode,offerer,tradingEntity)==false) {
											MatchNode m = new MatchNode(offerer,userNode,relatedTradingEntity,tradingEntity);
											returnList.add(m);
										}
									}
								}
							}
						}
					}
				}
			}
			tx.success();
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
			tx.success();
		}
		return offerers;
	}
	
	private static void addMatch(MatchNode mNode) {
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			Node newMatchNode = DBHelper.createNodeWithUniqueId(LabelFactory.getLabel("MATCH"));
			mNode.setId(Long.parseLong(newMatchNode.getProperty("id").toString()));
			newMatchNode.createRelationshipTo(mNode.getOfferer(), RelationshipTypeFactory.getRelationshipType("PENDING_OFFERER"));
			newMatchNode.createRelationshipTo(mNode.getDesirer(), RelationshipTypeFactory.getRelationshipType("PENDING_DESIRER"));
			newMatchNode.createRelationshipTo(mNode.getOfferable(), RelationshipTypeFactory.getRelationshipType("MATCH_OFFER"));
			newMatchNode.createRelationshipTo(mNode.getDesirable(), RelationshipTypeFactory.getRelationshipType("MATCH_DESIRE"));
			tx.success();
		}
	}
	
	private static boolean userInList(String username, List<MatchNode> list) {
		for (MatchNode m: list) {
			if (m.getOfferer().getProperty("username").toString().equals(username)) {
				return true;
			}
		}
		return false;
	}
	
	private static MatchNode getMatchNode(Node matchNode) {
		Node offerer = DBHelper.getFirstNodeOfRelationshipType(matchNode, RelationshipTypeFactory.getRelationshipType("PENDING_OFFERER"));
		Node desirer = DBHelper.getFirstNodeOfRelationshipType(matchNode, RelationshipTypeFactory.getRelationshipType("PENDING_DESIRER"));
		Node desirable = DBHelper.getFirstNodeOfRelationshipType(matchNode, RelationshipTypeFactory.getRelationshipType("MATCH_OFFER"));
		Node offerable = DBHelper.getFirstNodeOfRelationshipType(matchNode, RelationshipTypeFactory.getRelationshipType("MATCH_DESIRE"));
		return new MatchNode(Long.parseLong(matchNode.getProperty("id").toString()),offerer,desirer,offerable,desirable);
	}
	
	private static boolean matchExists(MatchNode matchNode) {
		List<Node> offererMatches = DBHelper.getNodesOfRelationshipType(matchNode.getOfferer(),RelationshipTypeFactory.getRelationshipType("PENDING_OFFERER"));
		for (Node offererMatch: offererMatches) {
			List<Node> desirerMatches = DBHelper.getNodesOfRelationshipType(offererMatch,RelationshipTypeFactory.getRelationshipType("PENDING_DESIRER"));
			for (Node desirerMatch: desirerMatches) {
				if (matchNode.getDesirer().equals(desirerMatch)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private static boolean alreadyMatched(Node userNode, Node otherUser, Node tradingEntity) {
		//Check if user has been matched as a desirer of something from someone
		List<Node> desiredMatches = DBHelper.getNodesOfRelationshipType(userNode, RelationshipTypeFactory.desirerRelations);
		for (Node m: desiredMatches) {
			if (tradingEntity.equals(DBHelper.getFirstNodeOfRelationshipType(m, RelationshipTypeFactory.matchRelations))) {
				if (otherUser.equals(DBHelper.getFirstNodeOfRelationshipType(m, RelationshipTypeFactory.offererRelations))) {
					return true;
				}
			}
		}
		
		//Check if user has been matched as an offerer of something from someone
		List<Node> offererMatches = DBHelper.getNodesOfRelationshipType(userNode, RelationshipTypeFactory.offererRelations);
		for (Node m: offererMatches) {
			if (tradingEntity.equals(DBHelper.getFirstNodeOfRelationshipType(m, RelationshipTypeFactory.matchRelations))) {
				if (otherUser.equals(DBHelper.getFirstNodeOfRelationshipType(m, RelationshipTypeFactory.desirerRelations))) {
					return true;
				}
			}
		}
		/*
		//Check if user has been matched as a desirer of something from someone
		List<Node> desiredMatches = DBHelper.getNodesOfRelationshipType(userNode, RelationshipTypeFactory.getRelationshipType("ACCEPT_DESIRER"), RelationshipTypeFactory.getRelationshipType("REJECT_DESIRER"), RelationshipTypeFactory.getRelationshipType("PENDING_DESIRER"));
		for (Node m: desiredMatches) {
			if (tradingEntity.equals(DBHelper.getFirstNodeOfRelationshipType(m, RelationshipTypeFactory.getRelationshipType("MATCH_DESIRE"), RelationshipTypeFactory.getRelationshipType("MATCH_OFFER")))) {
				if (otherUser.equals(DBHelper.getFirstNodeOfRelationshipType(m, RelationshipTypeFactory.getRelationshipType("ACCEPT_OFFERER"), RelationshipTypeFactory.getRelationshipType("REJECT_OFFERER"), RelationshipTypeFactory.getRelationshipType("PENDING_OFFERER")))) {
					return true;
				}
			}
		}
		
		//Check if user has been matched as an offerer of something from someone
		List<Node> offererMatches = DBHelper.getNodesOfRelationshipType(userNode, RelationshipTypeFactory.getRelationshipType("ACCEPT_OFFERER"), RelationshipTypeFactory.getRelationshipType("REJECT_OFFERER"), RelationshipTypeFactory.getRelationshipType("PENDING_OFFERER"));
		for (Node m: offererMatches) {
			if (tradingEntity.equals(DBHelper.getFirstNodeOfRelationshipType(m, RelationshipTypeFactory.getRelationshipType("MATCH_DESIRE"), RelationshipTypeFactory.getRelationshipType("MATCH_OFFER")))) {
				if (otherUser.equals(DBHelper.getFirstNodeOfRelationshipType(m, RelationshipTypeFactory.getRelationshipType("ACCEPT_DESIRER"), RelationshipTypeFactory.getRelationshipType("REJECT_DESIRER"), RelationshipTypeFactory.getRelationshipType("PENDING_DESIRER")))) {
					return true;
				}
			}
		}
		*/		
		return false;
	}
	
	private static boolean matchInRange(Node userNodeA, Node userNodeB) {
		if (userNodeA.hasProperty("latitude")&&userNodeA.hasProperty("longitude")&&userNodeB.hasProperty("latitude")&&userNodeB.hasProperty("longitude")) {
			double latA = Double.parseDouble(userNodeA.getProperty("latitude").toString());
			double lonA = Double.parseDouble(userNodeA.getProperty("longitude").toString());
			double latB = Double.parseDouble(userNodeB.getProperty("latitude").toString());
			double lonB = Double.parseDouble(userNodeB.getProperty("longitude").toString());
			if (SimpleDistance.haversine(latA, lonA, latB, lonB) <= MAX_DISTANCE) {
				return true;
			}
		}
		return false;
	}
}