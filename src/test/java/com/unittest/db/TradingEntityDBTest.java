package com.unittest.db;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import org.junit.Test;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import com.db.DataManager;
import com.db.TradingEntityDB;
import com.objects.domain.TradingEntity;
import com.unittest.AbstractDBTest;

public class TradingEntityDBTest extends AbstractDBTest {
	private String beerName = "Stella";
	private Label label = DynamicLabel.label("BEER");
	
	@Test
	public void addTradingEntityTest() {
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			Node t = TradingEntityDB.addTradingEntity(beerName, label);
			assertTrue(t.hasLabel(label));
			assertEquals(beerName,t.getProperty("name"));
			tx.success();
		} catch (NoSuchElementException e) {
			fail("Exception thrown: " + e.toString());
		}
	}
	
	@Test
	public void entityExistsTest() {
		TradingEntityDB.getOrCreateTradingEntity(beerName, label);
		assertTrue(TradingEntityDB.entityExists(beerName, label));
		assertFalse(TradingEntityDB.entityExists("wrong", label));
	}
	
	@Test
	public void getTradingEntityTest() {
		TradingEntityDB.getOrCreateTradingEntity(beerName, label);
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			Node t = TradingEntityDB.getTradingEntity(beerName, label);
			assertNotNull(t);
			assertTrue(t.hasLabel(label));
			assertEquals(beerName,t.getProperty("name"));
		} catch (NoSuchElementException e) {
			fail("Exception thrown: " + e.toString());
		}		
	}
	
	@Test
	public void convertToTradingEntityTest() {
		Node n = TradingEntityDB.getOrCreateTradingEntity(beerName, label);
		TradingEntity t = TradingEntityDB.convertToTradingEntity(n);
		assertEquals(beerName,t.getName());
		assertEquals("BEER",t.getLabel());
	}
}
