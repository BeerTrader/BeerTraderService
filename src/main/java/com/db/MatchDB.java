package com.db;

import java.util.List;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;

import com.factory.LabelFactory;
import com.factory.RelationshipTypeFactory;
import com.objects.domain.Match;
import com.objects.domain.MatchList;
import com.objects.domain.MatchNode;
import com.objects.domain.TradingEntity;
import com.objects.domain.User;

public class MatchDB {
    private MatchDB() {};
    
	public static MatchList getUnrespondedMatches(Node userNode) {
		//Get unresponded matches and convert to MatchList
		return convertToMatchList(MatchNodeDB.getPendingMatches(userNode));
	}
	
	public static MatchList getPendingOffererMatches(Node userNode) {
		return convertToMatchList(MatchNodeDB.getPendingOffererRequests(userNode));
	}	
	
	public static MatchList convertToMatchList(List<MatchNode> mList) {
		MatchList returnList = new MatchList();
		
		for (MatchNode mNode: mList) {
			User u = UserDB.convertToSecureUser(mNode.getOfferer());
			User v = UserDB.convertToSecureUser(mNode.getDesirer());
			TradingEntity offerable = TradingEntityDB.convertToTradingEntity(mNode.getOfferable());
			TradingEntity desirable = TradingEntityDB.convertToTradingEntity(mNode.getDesirable());
			Match m = new Match(mNode.getId(),u,v,offerable,desirable);
			returnList.addMatch(m);
		}
		
		return returnList;
	}
	
	//TODO Check ACCEPT_DESIRER, REJECT_DESIRER as well as PENDING_DESIRER/Allows for acceptMatch to be called more than once
	//TODO If relationship exists, do not delete and re-add (???Necessary for course???)
	public static void acceptMatch(Node userNode, Match match) throws NotFoundException {
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			ResourceIterator<Node> matchIterator = DataManager.getInstance().findNodesByLabelAndProperty(LabelFactory.getLabel("MATCH"), "id", match.getId()).iterator();
			if (matchIterator.hasNext()) {
				Node matchNode = matchIterator.next();
				if (RelationshipTypeDB.hasRelationship(userNode, matchNode, RelationshipTypeFactory.getRelationshipType("PENDING_DESIRER"))) {
					Relationship r = DBHelper.getRelationship(userNode, matchNode, RelationshipTypeFactory.getRelationshipType("PENDING_DESIRER"));
					r.delete();
					matchNode.createRelationshipTo(userNode, RelationshipTypeFactory.getRelationshipType("ACCEPT_DESIRER"));
				} else if (RelationshipTypeDB.hasRelationship(userNode, matchNode, RelationshipTypeFactory.getRelationshipType("PENDING_OFFERER"))) {
					Relationship r = DBHelper.getRelationship(userNode, matchNode, RelationshipTypeFactory.getRelationshipType("PENDING_OFFERER"));
					r.delete();
					matchNode.createRelationshipTo(userNode, RelationshipTypeFactory.getRelationshipType("ACCEPT_OFFERER"));
				}
				else {
					throw new NotFoundException("Relationship between " + match.getId() + " and " + matchNode.getId() + " not found");
				}
			} else {
				throw new NotFoundException("Match node with Id " + match.getId() + " not found");
			}
			tx.success();
		}
	}

	public static void rejectMatch(Node userNode, Match match) throws NotFoundException {
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			ResourceIterator<Node> matchIterator = DataManager.getInstance().findNodesByLabelAndProperty(LabelFactory.getLabel("MATCH"), "id", match.getId()).iterator();
			if (matchIterator.hasNext()) {
				Node matchNode = matchIterator.next();
				if (RelationshipTypeDB.hasRelationship(userNode, matchNode, RelationshipTypeFactory.getRelationshipType("PENDING_DESIRER"))) {
					Relationship r = DBHelper.getRelationship(userNode, matchNode, RelationshipTypeFactory.getRelationshipType("PENDING_DESIRER"));
					r.delete();
					matchNode.createRelationshipTo(userNode, RelationshipTypeFactory.getRelationshipType("REJECT_DESIRER"));
				} else if (RelationshipTypeDB.hasRelationship(userNode, matchNode, RelationshipTypeFactory.getRelationshipType("PENDING_OFFERER"))) {
					Relationship r = DBHelper.getRelationship(userNode, matchNode, RelationshipTypeFactory.getRelationshipType("PENDING_OFFERER"));
					r.delete();
					matchNode.createRelationshipTo(userNode, RelationshipTypeFactory.getRelationshipType("REJECT_OFFERER"));
				}
				else {
					throw new NotFoundException("Relationship between " + match.getId() + " and " + matchNode.getId() + " not found");
				}				
			} else {
				throw new NotFoundException("Match node with Id " + match.getId() + " not found");
			}
			tx.success();
		}
	}	
}