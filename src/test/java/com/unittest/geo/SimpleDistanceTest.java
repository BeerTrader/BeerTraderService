package com.unittest.geo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.geo.SimpleDistance;

public class SimpleDistanceTest {
	private double lat1 = 36.12;
	private double lon1 = -86.67;
	private double lat2 = 33.94;
	private double lon2 = -118.40;
	private double distance = 1793.55;
	
	@Test
	public void haversineTest() {
		double hDistance = SimpleDistance.haversine(lat1, lon1, lat2, lon2);
		double result = distance - hDistance;
		assertEquals(0,result,0.01);
	}
}
