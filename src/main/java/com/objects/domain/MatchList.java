package com.objects.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MatchList {
	@JsonProperty("matchList")
	private List<Match> matchList;
	
	public MatchList() {
		matchList = new ArrayList<>();
	}
	
	public void addMatch(Match m) {
		matchList.add(m);
	}
	
	public boolean userInList(String username) {
		for (Match m: matchList) {
			if (m.getOfferer().getUsername().equals(username))
				return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "MatchList [matchList=" + matchList + "]";
	}	
}
