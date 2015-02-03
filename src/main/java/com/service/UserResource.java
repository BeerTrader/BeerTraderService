package com.service;

import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.io.IOException;
import java.util.Collections;
import com.db.DataManager;

@Path("/user")
public class UserResource {

    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @GET
    @Path("/{name}")
    public String findUser(@PathParam("name") String name) throws IOException {
		Label userLabel = DynamicLabel.label("User");
		String nameToFind = "Steven";
		try (Transaction tx = DataManager.getInstance().beginTx())
		{
		    for (Node node : DataManager.getInstance().findNodesByLabelAndProperty(userLabel, "name", nameToFind))
		    {
		    	return OBJECT_MAPPER.writeValueAsString(node);
		    }
		}
        return OBJECT_MAPPER.writeValueAsString(Collections.emptyMap());
    }
}