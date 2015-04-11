package com.db;

import java.util.ArrayList;
import java.util.List;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import com.factory.LabelFactory;
import com.factory.RelationshipTypeFactory;
import com.objects.domain.Match;
import com.objects.domain.MatchList;
import com.objects.domain.TradingEntity;
import com.objects.domain.User;

public class MatchDB {
	public static MatchList findMatches(Node userNode) {
		//Return object, empty initialized
		MatchList returnList = new MatchList();
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
						User offererUser = User.convertToUser(offerer);
						if (returnList.userInList(offererUser.getUsername())==false) {
							Match m = new Match(offererUser, TradingEntity.convertToTradingEntity(tradingEntity), TradingEntity.convertToTradingEntity(tradingEntity));
							returnList.addMatch(m);
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
							User offererUser = User.convertToUser(offerer);
							if (returnList.userInList(offererUser.getUsername())==false) {
								Match m = new Match(offererUser, TradingEntity.convertToTradingEntity(relatedTradingEntity), TradingEntity.convertToTradingEntity(tradingEntity));
								returnList.addMatch(m);
							}
						}
					}
				}
			}
		}
		return returnList;
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
}