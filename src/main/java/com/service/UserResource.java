package com.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.Produces;

import com.db.UserDB;
import com.exceptions.DuplicateUserException;
import com.exceptions.ObjectMappingException;
import com.exceptions.UserNotFoundException;
import com.objects.domain.User;
import com.objects.mapping.ObjectManager;

@Path("/user")
public class UserResource {
	//curl -k --user test:test https://localhost:8443/beertrader/rest/user/Steven
    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("username") String username) {
    	try {
    		User user = UserDB.getUser(username);
    		String result = ObjectManager.writeObjectAsString(user);
    		return Response.status(200).entity(result).build();
    	}
    	catch(UserNotFoundException | ObjectMappingException e) {
    		System.out.println(e.getMessage());
    		return Response.status(400).entity(e.getMessage()).build();
    	}
    }
    
    //curl -k --user test:test -H "Content-Type: application/json" -X POST -d "{\"username\":\"blah\", \"password\":\"blob\"}" https://localhost:8443/beertrader/rest/user/createUser
    @POST
    @Path("/createUser")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(String user) {
    	try {
			User newUser = ObjectManager.readUserAsString(user);
			UserDB.registerUser(newUser.getUsername(), newUser.getPassword());
			String result = "User created with username: " + newUser.getUsername();
			return Response.status(200).entity(result).build();
    	}
	    catch(DuplicateUserException | ObjectMappingException e) {
	    	System.out.println(e.getMessage());
	    	return Response.status(400).entity(e.getMessage()).build();
	    }
    }
}