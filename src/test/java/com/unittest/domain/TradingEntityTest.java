package com.unittest.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

import com.objects.domain.TradingEntity;
import com.objects.mapping.ObjectManager;

public class TradingEntityTest {
	@Test
	public void readBeerTest() throws IOException {
		TradingEntity beer = ObjectManager.getInstance().readValue("{\"label\": \"BEER\", \"name\": \"Stella\",\"relations\": [{\"label\":\"BEERTYPE\",\"name\":\"PaleAle\"},{\"label\": \"BREWERY\",\"name\":\"GooseIsland\"}]}", TradingEntity.class);
		assertEquals(beer.getLabel(),"BEER");
		assertEquals(beer.getName(),"Stella");
		assertEquals(beer.getRelations().size(),2);
	}
	
	@Test
	public void readBeerTypeTest() throws IOException {
		TradingEntity beerType = ObjectManager.getInstance().readValue("{\"label\": \"BEERTYPE\", \"name\": \"PaleAle\"}", TradingEntity.class);
		assertEquals(beerType.getLabel(),"BEERTYPE");
		assertEquals(beerType.getName(),"PaleAle");
		assertNull(beerType.getRelations());
	}
}
