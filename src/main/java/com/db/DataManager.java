package com.db;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class DataManager {
		private static GraphDatabaseService graphDb = null;
		private static ExecutionEngine executionEngine = null;
		private static DataManager dm = null;
		private static final String DB_PATH = "C:\\Users\\steven.muschler\\Documents\\Neo4j\\default.graphdb";
		
	    private DataManager()
	    {
	        try
	        {
	        	graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
	        	executionEngine = new ExecutionEngine(graphDb);
	        	registerShutdownHook( graphDb );
	        	System.out.println("Graph Database Initialized.");
	        }
	        catch(Exception e)
	        {
	            e.printStackTrace();
	        }
	    }
	    
	    public static GraphDatabaseService getInstance()
	    {
	        if (dm==null)
	        {
	        	dm = new DataManager();
	        }
	        return dm.getGraphDatabaseService();
	    }
	    
	    /*
	    public static ExecutionEngine getInstance()
	    {
	    	if (dm==null)
	        {
	        	dm = new DataManager();
	        }
	        return dm.getExecutionEngine();	    	
	    }
	    */
	    private GraphDatabaseService getGraphDatabaseService()
	    {
	        return graphDb;
	    }
	    
	    private ExecutionEngine getExecutionEngine() {
	    	return executionEngine;
	    }
	    
	    private static void registerShutdownHook( final GraphDatabaseService graphDb )
	    {
	        // Registers a shutdown hook for the Neo4j instance so that it
	        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
	        // running application).
	        Runtime.getRuntime().addShutdownHook( new Thread()
	        {
	            @Override
	            public void run()
	            {
	                graphDb.shutdown();
	                System.out.println("Graph Database Shutdown");
	            }
	        } );
	    }    
	
}
