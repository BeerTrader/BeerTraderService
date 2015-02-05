package com.service;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Produces;

import java.io.IOException;
import java.util.Collections;

import com.db.DataManager;
import com.objects.ObjectManager;
import com.objects.User;

@Path("/user")
public class UserResource {

    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public String findUser(@PathParam("name") String name) throws IOException {
		Label userLabel = DynamicLabel.label("User");
		try (Transaction tx = DataManager.getInstance().beginTx())
		{
		    for (Node node : DataManager.getInstance().findNodesByLabelAndProperty(userLabel, "name", name))
		    {
		    	User u = new User(node.getId(),(String) node.getProperty("name"));
		    	return ObjectManager.getInstance().writeValueAsString(u);
		    }
		}
        return ObjectManager.getInstance().writeValueAsString(Collections.emptyMap());
    }
}