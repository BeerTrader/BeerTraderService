package com.service;

import ibt.ortc.api.InvalidBalancerServerException;
import ibt.ortc.api.OrtcAuthenticationNotAuthorizedException;

import java.io.IOException;
import java.util.GregorianCalendar;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;

import com.db.MatchDB;
import com.db.UserDB;
import com.exceptions.ObjectMappingException;
import com.exceptions.UserNotFoundException;
import com.objects.domain.Match;
import com.objects.domain.MatchList;
import com.objects.domain.SimpleMessaging;
import com.objects.mapping.ObjectManager;

@Path("/match")
public class MatchResource {
    @GET
    @Path("/getMatches")
    public Response getMatches(@Context HttpHeaders headers) {
    	String auth = headers.getRequestHeaders().getFirst("authorization");
		try {
	    	Node userNode = UserDB.getUserNode(auth);
			MatchList newMatches = MatchDB.getUnrespondedMatches(userNode);
			String results = ObjectManager.writeObjectAsString(newMatches);
			return Response.status(200).entity(results).build();
		} catch (ObjectMappingException | UserNotFoundException e) {
	    	System.out.println(e.getMessage());
	    	return Response.status(400).entity(e.getMessage()).build();
		}
    }
    
    @GET
    @Path("/getPendingOffererMatches")
    public Response getPendingOffererMatches(@Context HttpHeaders headers) {
    	String auth = headers.getRequestHeaders().getFirst("authorization");
		try {
	    	Node userNode = UserDB.getUserNode(auth);
			MatchList newMatches = MatchDB.getPendingOffererMatches(userNode);
			String results = ObjectManager.writeObjectAsString(newMatches);
			return Response.status(200).entity(results).build();
		} catch (ObjectMappingException | UserNotFoundException e) {
	    	System.out.println(e.getMessage());
	    	return Response.status(400).entity(e.getMessage()).build();
		}
    }    

    @POST
    @Path("/acceptMatch")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response acceptMatch(@Context HttpHeaders headers, String matchJson) {
    	String auth = headers.getRequestHeaders().getFirst("authorization");
    	try {
    		Node userNode = UserDB.getUserNode(auth);
    		Match match = (Match) ObjectManager.readObjectAsString(matchJson, Match.class);
    		MatchDB.acceptMatch(userNode, match);
    		String channelName = Long.toString(match.getId());
    		String realtimeAuthenticationToken = Long.toString(GregorianCalendar.getInstance().getTimeInMillis());
    		SimpleMessaging realtimeAuthentication = new SimpleMessaging(channelName,realtimeAuthenticationToken);
    		realtimeAuthentication.realtimeAuthenticate();
    		return Response.status(200).entity(ObjectManager.writeObjectAsString(realtimeAuthentication)).build();
    	}
    	catch (ObjectMappingException | UserNotFoundException | NotFoundException | IOException | InvalidBalancerServerException | OrtcAuthenticationNotAuthorizedException e) {
	    	System.out.println(e.getMessage());
	    	return Response.status(400).entity(e.getMessage()).build();
		}
    }
    
    @POST
    @Path("/rejectMatch")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response rejectMatch(@Context HttpHeaders headers, String matchJson) {
    	String auth = headers.getRequestHeaders().getFirst("authorization");
    	try {
    		Node userNode = UserDB.getUserNode(auth);
    		Match match = (Match) ObjectManager.readObjectAsString(matchJson, Match.class);
    		MatchDB.rejectMatch(userNode, match);
    		return Response.status(200).entity("Match rejected between user node " + userNode.getId() + " and match node " + match.getId()).build();
    	}
    	catch (ObjectMappingException | UserNotFoundException | NotFoundException e) {
	    	System.out.println(e.getMessage());
	    	return Response.status(400).entity(e.getMessage()).build();
		}
    }    
}