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
	
	public static MatchList convertToMatchList(List<MatchNode> mList) {
		MatchList returnList = new MatchList();
		
		for (MatchNode mNode: mList) {
			User u = UserDB.convertToSecureUser(mNode.getOfferer());
			TradingEntity offerable = TradingEntityDB.convertToTradingEntity(mNode.getOfferable());
			TradingEntity desirable = TradingEntityDB.convertToTradingEntity(mNode.getDesirable());
			Match m = new Match(mNode.getId(),u,offerable,desirable);
			returnList.addMatch(m);
		}
		
		return returnList;
	}
	
	//TODO Be able to handle PENDING_OFFERER requests as well	
	//TODO If relationship exists, do not delete and re-add (???Necessary for course???)
	public static void acceptMatch(Node userNode, Match match) throws NotFoundException {
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			ResourceIterator<Node> matchIterator = DataManager.getInstance().findNodesByLabelAndProperty(LabelFactory.getLabel("MATCH"), "id", match.getId()).iterator();
			if (matchIterator.hasNext()) {
				Node matchNode = matchIterator.next();
				Relationship r = DBHelper.getRelationship(userNode, matchNode, RelationshipTypeFactory.getRelationshipType("PENDING_DESIRER"));
				r.delete();
				matchNode.createRelationshipTo(userNode, RelationshipTypeFactory.getRelationshipType("ACCEPT_DESIRER"));
			} else {
				throw new NotFoundException("Match node with Id " + match.getId() + " not found");
			}
			tx.success();
		}
	}

	//TODO Be able to handle PENDING_OFFERER requests as well
	public static void rejectMatch(Node userNode, Match match) throws NotFoundException {
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			ResourceIterator<Node> matchIterator = DataManager.getInstance().findNodesByLabelAndProperty(LabelFactory.getLabel("MATCH"), "id", match.getId()).iterator();
			if (matchIterator.hasNext()) {
				Node matchNode = matchIterator.next();
				Relationship r = DBHelper.getRelationship(userNode, matchNode, RelationshipTypeFactory.getRelationshipType("PENDING_DESIRER"));
				r.delete();
				matchNode.createRelationshipTo(userNode, RelationshipTypeFactory.getRelationshipType("REJECT_DESIRER"));
			} else {
				throw new NotFoundException("Match node with Id " + match.getId() + " not found");
			}
			tx.success();
		}
	}	
}