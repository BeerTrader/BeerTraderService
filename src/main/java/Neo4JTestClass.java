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
				Map<Node,List<Node>> results = findMatches(user);
				MatchList ml = new MatchList();
				Match m = new Match();
				for (Map.Entry<Node, List<Node>> entry : results.entrySet()) {
					m.setUser(User.convertToUser(entry.getKey()));
					Node offerable = entry.getValue().get(0);
					Node desirable = entry.getValue().get(1);
					m.setOfferable(TradingEntity.convertToTradingEntity(offerable));
					m.setDesirable(TradingEntity.convertToTradingEntity(desirable));
				}
				ml.addMatch(m);
				ml.addMatch(m);
				System.out.println(ObjectManager.writeObjectAsString(ml));
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
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			DataManager.shutdownGraphDb();
		}
	}
	
	public static Map<Node,List<Node>> findMatches(Node userNode) {
		//Return object, empty initialized
		Map<Node,List<Node>> returnMap = new HashMap<Node,List<Node>>();
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
						List<Node> offering = new ArrayList<Node>();
						offering.add(tradingEntity);
						offering.add(tradingEntity);
						returnMap.put(offerer, offering);
					}
				}
				else {
					//if other tradingEntity, find users who offer beers of those entities
					Iterable<Relationship> relatedEntityRelationships = tradingEntity.getRelationships(RelationshipTypeFactory.getRelationshipType("IS_A"), RelationshipTypeFactory.getRelationshipType("MADE_BY"));
					for (Relationship relatedEntityRelationship: relatedEntityRelationships) {
						Node relatedTradingEntity = relatedEntityRelationship.getOtherNode(tradingEntity);
						List<Node> offerers = getOfferers(relatedTradingEntity);
						for (Node offerer: offerers) {
							List<Node> offering = new ArrayList<Node>();
							offering.add(relatedTradingEntity);  //beer aka offerable
							offering.add(tradingEntity); //beer type or brewery aka desirable
							returnMap.put(offerer, offering);	
						}
					}
				}
			}
		}
		return returnMap;
	}

	public static List<Node> getOfferers(Node tradingEntity) {
		List<Node> offerers = new ArrayList<>();
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			Iterable<Relationship> offerableRelationships = tradingEntity.getRelationships(RelationshipTypeFactory.getRelationshipType("OFFERS"));
			for (Relationship offerableRelationship: offerableRelationships) {
				offerers.add(offerableRelationship.getOtherNode(tradingEntity));
			}	
		}
		return offerers;
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
