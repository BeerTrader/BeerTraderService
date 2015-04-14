package com.db;

import java.util.List;

import org.neo4j.graphdb.Node;

import com.objects.domain.Match;
import com.objects.domain.MatchList;
import com.objects.domain.MatchNode;
import com.objects.domain.TradingEntity;
import com.objects.domain.User;

public class MatchDB {
	public static MatchList getUnrespondedMatches(Node userNode) {
		//Get unresponded matches and convert to MatchList
		return convertToMatchList(MatchNodeDB.getPendingMatches(userNode));
	}
	
	public static MatchList convertToMatchList(List<MatchNode> mList) {
		MatchList returnList = new MatchList();
		
		for (MatchNode mNode: mList) {
			User u = UserDB.convertToUser(mNode.getOfferer());
			TradingEntity offerable = TradingEntityDB.convertToTradingEntity(mNode.getOfferable());
			TradingEntity desirable = TradingEntityDB.convertToTradingEntity(mNode.getDesirable());
			Match m = new Match(u,offerable,desirable);
			returnList.addMatch(m);
		}
		
		return returnList;
	}
}