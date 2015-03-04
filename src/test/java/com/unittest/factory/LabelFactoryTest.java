package com.unittest.factory;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.neo4j.graphdb.Label;

import com.factory.LabelFactory;

public class LabelFactoryTest {
	@Test
	public void noLabelFound() {
		Label l = LabelFactory.getLabel("INVALID_LABEL");
		assertEquals("ERROR",l.name());
	}
	
	@Test
	public void labelFound() {
		Label l = LabelFactory.getLabel("DESIREABLE");
		assertEquals("DESIREABLE",l.name());
	}
}
