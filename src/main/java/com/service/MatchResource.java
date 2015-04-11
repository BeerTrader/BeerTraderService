package com.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.neo4j.graphdb.Node;

import com.db.MatchDB;
import com.db.UserDB;
import com.exceptions.ObjectMappingException;
import com.objects.domain.MatchList;
import com.objects.mapping.ObjectManager;

@Path("/match")
public class MatchResource {
    @GET
    @Path("/getMatches")
    public Response getMatches(@Context HttpHeaders headers, String tradingEntity) {
    	String auth = headers.getRequestHeaders().getFirst("authorization");
    	Node userNode = UserDB.getUserNode(auth);
		MatchList newMatches = MatchDB.findMatches(userNode);
		try {
			String results = ObjectManager.writeObjectAsString(newMatches);
			return Response.status(200).entity(results).build();
		} catch (ObjectMappingException e) {
	    	System.out.println(e.getMessage());
	    	return Response.status(400).entity(e.getMessage()).build();
		}
    }
}