import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.traversal.BidirectionalTraversalDescription;
import org.neo4j.graphdb.traversal.TraversalDescription;

import com.db.DataManager;
import com.db.MatchDB;
import com.factory.LabelFactory;
import com.factory.RelationshipTypeFactory;
import com.objects.domain.Match;
import com.objects.domain.MatchList;
import com.objects.domain.TradingEntity;
import com.objects.domain.User;
import com.objects.mapping.ObjectManager;


public class Neo4JTestClass {
	public static void main(String[] args) {
		try {
			GraphDatabaseService d = DataManager.getInstance();
			//TraversalDescription traversal = d.traversalDescription().breadthFirst();//.relationships(RelationshipTypeFactory.getRelationshipType("OFFERS"));
			//BidirectionalTraversalDescription biTraversal = d.bidirectionalTraversalDescription().startSide(traversal).endSide(traversal);
					//relationships(RelationshipTypeFactory.getRelationshipType("OFFERS")).relationships(RelationshipTypeFactory.getRelationshipType("DESIRES")).relationships(RelationshipTypeFactory.getRelationshipType("IS_A"));
			try (Transaction tx = DataManager.getInstance().beginTx()) {
				Node user = d.findNodesByLabelAndProperty(LabelFactory.BeerLabels.USER, "username", "steven").iterator().next();
				Node user2 = d.findNodesByLabelAndProperty(LabelFactory.BeerLabels.USER, "username", "jase").iterator().next();
				Node user3 = d.findNodesByLabelAndProperty(LabelFactory.BeerLabels.USER, "username", "jim").iterator().next();
				Node beer = d.findNodesByLabelAndProperty(LabelFactory.BeerLabels.BEER, "name", "Stella").iterator().next();
				Node beer2 = d.findNodesByLabelAndProperty(LabelFactory.BeerLabels.BEER, "name", "Coors").iterator().next();
				Node beerType = d.findNodesByLabelAndProperty(LabelFactory.BeerLabels.BEERTYPE, "name", "Pilsner").iterator().next();

				System.out.println(ObjectManager.writeObjectAsString(MatchDB.findMatches(user)));
				/*
				System.out.println("---");
				results = findMatches(user2);
				for (Map.Entry<Node, List<Node>> entry : results.entrySet()) {
					System.out.println(entry.getKey().getId());
					for (Node e: entry.getValue()) {
						System.out.println(e.getId());
					}
				}
				System.out.println("---");
				results = findMatches(user3);
				for (Map.Entry<Node, List<Node>> entry : results.entrySet()) {
					System.out.println(entry.getKey().getId());
					for (Node e: entry.getValue()) {
						System.out.println(e.getId());
					}
				}
				*/
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			DataManager.shutdownGraphDb();
		}
	}

///////////////////////////////////////////////////////////////////////	
	public static List<Node> getDesirers(Node tradingEntity) {
		List<Node> desirers = new ArrayList<>();
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			Iterable<Relationship> desirableRelationships = tradingEntity.getRelationships(RelationshipTypeFactory.getRelationshipType("DESIRES"));
			for (Relationship desirableRelationship: desirableRelationships) {
				desirers.add(desirableRelationship.getOtherNode(tradingEntity));
			}	
		}
		return desirers;
	}
	
	//Given a Beer that someone is offering, am I interested in that Beer
	//Yes, direct relationship between Beer and myself
	//Yes, I'm looking for a BeerType that that Beer is part of
	public static boolean userDesires(Node userNode, Node beerNode) {
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			Iterable<Relationship> desirableRelationships = userNode.getRelationships(RelationshipTypeFactory.getRelationshipType("DESIRES"));
			for (Relationship desirableRelationship: desirableRelationships) {
				Node desiredTradingEntity = desirableRelationship.getOtherNode(userNode);
				if (desiredTradingEntity.equals(beerNode)) {
					return true;
				}
				
				Iterable<Relationship> tradingEntityRelations = desiredTradingEntity.getRelationships(RelationshipTypeFactory.getRelationshipType("IS_A"), RelationshipTypeFactory.getRelationshipType("MADE_BY"));
				for (Relationship tradingEntityRelation: tradingEntityRelations) {
					if (tradingEntityRelation.getOtherNode(desiredTradingEntity).equals(beerNode)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	//Get all users that desire a beer, beer type, or brewery
	public static List<Node> getUserNodesDesire(Node tradingEntity) {
		List<Node> userList = new ArrayList<>();
		//Check direct relationships
		userList.addAll(getDesirers(tradingEntity));
		//Check distant relationships if tradingEntity is NOT a BEER
		if (!tradingEntity.hasLabel(LabelFactory.getLabel("BEER"))) {
			Iterable<Relationship> tradingEntityRelationships = tradingEntity.getRelationships(RelationshipTypeFactory.getRelationshipType("IS_A"), RelationshipTypeFactory.getRelationshipType("MADE_BY"));
			for (Relationship tradingEntityRelationship: tradingEntityRelationships) {
				Node relatedEntityNode = tradingEntityRelationship.getOtherNode(tradingEntity);
				//TODO Check if user is on list already (e.g. if they like Pilsner and Stella)
				userList.addAll(getDesirers(relatedEntityNode));
			}
		}
		return userList;
	}
}
