package com.db;

import org.apache.commons.lang.StringUtils;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;

import com.objects.domain.TradingEntity;

public class TradingEntityDB {
    private TradingEntityDB() {};
	
	public static boolean entityExists(String name, Label label) {
		if (StringUtils.isEmpty(name)) {
			return false;
		}
		
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			ResourceIterator<Node> iterable = DataManager.getInstance().findNodesByLabelAndProperty(label, "name", name).iterator();
			boolean result = iterable.hasNext();
			iterable.close();
			tx.success();
			return result;
		}
	}
	
	public static Node getTradingEntity(String name, Label label) {
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			Node tradingEntityNode = DataManager.getInstance().findNodesByLabelAndProperty(label, "name", name).iterator().next();
			tx.success();
			return tradingEntityNode;
		}
	}
	
	public static Node addTradingEntity(String name, Label label) {
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			Node newTradingEntityNode = DBHelper.createNodeWithUniqueId(label);
			newTradingEntityNode.setProperty("name", name);
			tx.success();
			return newTradingEntityNode;
		}
	}
	
	public static Node getOrCreateTradingEntity(String name, Label label) {
		if (entityExists(name, label)) {
			return getTradingEntity(name, label);
		} else {
			return addTradingEntity(name, label);
		}
	}
	
	public static TradingEntity convertToTradingEntity(Node te) {
		try (Transaction tx = DataManager.getInstance().beginTx()) {
			TradingEntity t = new TradingEntity(Long.parseLong(te.getProperty("id").toString()), te.getLabels().iterator().next().name(), te.getProperty("name").toString(), null);
			tx.success();
			return t;
		}
	}	
}