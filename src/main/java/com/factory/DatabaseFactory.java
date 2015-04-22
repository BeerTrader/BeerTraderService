package com.factory;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.test.TestGraphDatabaseFactory;

public class DatabaseFactory {
	//private static final String DB_PATH = "C:\\Users\\steven.muschler\\Documents\\Neo4j\\beertrader.graphdb";
	//private static final String DB_PATH = "C:\\Users\\steven.muschler\\Documents\\Neo4j\\default.graphdb";
	private static final String DB_PATH = "C:\\Users\\smuschler\\Documents\\Neo4j\\default.graphdb";

	public static GraphDatabaseService build(String type) {
		if ("Test".equals(type)) {
			return new TestGraphDatabaseFactory().newImpermanentDatabase();
		} else {
			return new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
		}
	}
}