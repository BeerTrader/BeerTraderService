package com.factory;

import org.neo4j.graphdb.Label;

public class LabelFactory {
	public enum BeerLabels implements Label { USER, OFFERABLE, DESIRABLE, BEER, BEERTYPE, BREWERY, MATCH, ERROR; } 
	
	public static Label getLabel(String labelName) {
		for (Label l: BeerLabels.values()) {
			if (l.name().equalsIgnoreCase(labelName)) {
				return l;
			}
		}
		System.out.println("Could not find " + labelName + " in LabelFactory.  Returning ERROR label.");
		return BeerLabels.ERROR;
	}
}
