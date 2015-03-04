package com.unittest.factory;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.neo4j.graphdb.RelationshipType;

import com.factory.LabelFactory;
import com.factory.RelationshipTypeFactory;

public class RelationshipTypeFactoryTest {
	@Test
	public void noRelationshipTypeFoundGivenName() {
		RelationshipType r = RelationshipTypeFactory.getRelationshipType("INVALID_RELATIONSHIP_NAME");
		assertEquals("ERROR",r.name());
	}
	
	@Test
	public void relationshipTypeFoundGivenName() {
		RelationshipType r = RelationshipTypeFactory.getRelationshipType("IS_A");
		assertEquals("IS_A",r.name());		
	}

	@Test
	public void noRelationshipTypeFoundGivenLabels() {
		RelationshipType r = RelationshipTypeFactory.getRelationshipType(LabelFactory.BeerLabels.USER,LabelFactory.BeerLabels.ERROR);
		assertEquals("ERROR",r.name());				
	}
	
	@Test
	public void relationshipTypeFoundGivenLabels() {
		RelationshipType r = RelationshipTypeFactory.getRelationshipType(LabelFactory.BeerLabels.BEER,LabelFactory.BeerLabels.BREWERY);
		assertEquals("MADE_BY",r.name());				
	}	
}
