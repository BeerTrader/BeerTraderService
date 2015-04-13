package com.factory;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;

public class RelationshipTypeFactory {
	public enum BeerRelationships implements RelationshipType { IS_A, MADE_BY, DESIRES, OFFERS, PENDING_DESIRER, PENDING_OFFERER, ACCEPT, REJECT, MATCH_OFFER, MATCH_DESIRE, ERROR; } 
	
	public static RelationshipType getRelationshipType(String relationshipName) {
		for (RelationshipType r: BeerRelationships.values()) {
			if (r.name().equalsIgnoreCase(relationshipName)) {
				return r;
			}
		}
		System.out.println("Could not find " + relationshipName + " in RelationshipTypeFactory.  Returning ERROR label.");
		return BeerRelationships.ERROR;
	}
	
	public static RelationshipType getRelationshipType(Label sourceLabel, Label destLabel) {
		if (sourceLabel.equals(LabelFactory.BeerLabels.BEER)) {
			if (destLabel.equals(LabelFactory.BeerLabels.BEERTYPE)) {
				return BeerRelationships.IS_A;
			} else if (destLabel.equals(LabelFactory.BeerLabels.BREWERY)) {
				return BeerRelationships.MADE_BY;
			}
		} else if (sourceLabel.equals(LabelFactory.BeerLabels.USER)) {
			if (destLabel.equals(LabelFactory.BeerLabels.DESIRABLE)) {
				return BeerRelationships.DESIRES;
			} else if (destLabel.equals(LabelFactory.BeerLabels.OFFERABLE)) {
				return BeerRelationships.OFFERS;
			}
		}
		System.out.println("Could not find relationship type in RelationshipTypeFactory for Labels: " + sourceLabel.name() + " and " + destLabel.name() + ".  Returning ERROR RelationshipType.");
		return BeerRelationships.ERROR;
	}
}
