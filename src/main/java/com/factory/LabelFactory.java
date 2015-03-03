package com.factory;

import org.neo4j.graphdb.Label;

import com.exceptions.LabelNotFoundException;

public class LabelFactory {
	public enum BeerLabels implements Label { USER, OFFERABLE, DESIREABLE, BEER, BEERTYPE, BREWERY; } 
	
	public static Label getLabel(String labelName) throws LabelNotFoundException {
		for (Label l: BeerLabels.values()) {
			if (l.name().equalsIgnoreCase(labelName))
				return l;
		}
		throw new LabelNotFoundException(labelName);
	}
}
