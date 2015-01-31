package com.service;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class DataManager {
	private static GraphDatabaseService graphDb = null;
	private static DataManager dm = null;
	private static final String DB_PATH = "C:\\Users\\steven.muschler\\Documents\\Neo4j\\default.graphdb";
	
    private DataManager()
    {
        try
        {
        	graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
        	registerShutdownHook( graphDb );
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
    
    private GraphDatabaseService getGraphDatabaseService()
    {
        return graphDb;
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
            }
        } );
    }    
}
