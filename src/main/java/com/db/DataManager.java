package com.db;

//import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;

import com.factory.DatabaseFactory;

public class DataManager {
		private static GraphDatabaseService graphDb = null;
		//private static ExecutionEngine executionEngine = null;
		
	    private DataManager() {
	        try {
	        	graphDb = DatabaseFactory.build("Prod");
	        	//executionEngine = new ExecutionEngine(graphDb);
	        	registerShutdownHook( graphDb );
	        	System.out.println("Graph Database Initialized.");
	        }
	        catch(Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    public static GraphDatabaseService getInstance() {
	        if (graphDb==null) {
	        	new DataManager();
	        }
	        return DataManager.graphDb;
	    }
	    
	    public static void initTestDatabase() {
	    	shutdownGraphDb();
	    	graphDb = DatabaseFactory.build("Test");
	    }
	    
	    public static void shutdownGraphDb() {
	    	if (graphDb!=null) {
	    		graphDb.shutdown();
	    		System.out.println("Graph Database Shutdown via method call");
	    	}
	    }
	    
	    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
	        // Registers a shutdown hook for the Neo4j instance so that it
	        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
	        // running application).
	        Runtime.getRuntime().addShutdownHook(new Thread() {
	            @Override
	            public void run() {
	                graphDb.shutdown();
	                System.out.println("Graph Database Shutdown via ShutdownHook");
	            }
	        });
	    }    
}
