package com.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.Produces;

import com.auth.BasicAuth;
import com.auth.token.TokenManager;
import com.db.UserDB;
import com.exceptions.DuplicateUserException;
import com.exceptions.ObjectMappingException;
import com.exceptions.UserNotAuthorizedException;
import com.exceptions.UserNotFoundException;
import com.objects.domain.User;
import com.objects.mapping.ObjectManager;

@Path("/user")
public class UserResource {
	//curl -k --user test:test https://localhost:8443/beertrader/rest/user/Steven
	//curl -k -H "Authorization: dGVzdGluZw==" https://localhost:8443/beertrader/rest/user/Steven
    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("username") String username) {
    	try {
    		User user = UserDB.getUser(username);
    		String result = ObjectManager.writeObjectAsString(user);
    		return Response.status(200).entity(result).build();
    	} catch(UserNotFoundException | ObjectMappingException e) {
    		return Response.status(400).entity(e.getMessage()).build();
    	}
    }
    
    //curl -k -H "Content-Type: application/json" -X POST -d "{\"username\":\"blah\", \"password\":\"blob\"}" https://localhost:8443/beertrader/rest/user/createUser
    @POST
    @Path("/createUser")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(String user) {
    	try {
			User newUser = (User) ObjectManager.readObjectAsString(user, User.class);
			UserDB.registerUser(newUser.getUsername(), newUser.getPassword());
			String result = "User created with username: " + newUser.getUsername();
			return Response.status(200).entity(result).build();
    	} catch(DuplicateUserException | ObjectMappingException e) {
	    	return Response.status(400).entity(e.getMessage()).build();
	    }
    }
    
    //curl -k --user musch711:testing123 https://localhost:8443/beertrader/rest/user/login
    @GET
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(@Context HttpHeaders headers) {
    	String auth = headers.getRequestHeaders().getFirst("authorization");
        String[] lap = BasicAuth.decode(auth);
   	 
        //If login or password fail
        if(lap == null || lap.length != 2){
	    	return Response.status(400).entity(Status.UNAUTHORIZED).build();
        }
 
        User authorizedUser;
        try {
        	authorizedUser = UserDB.authenticateUser(lap[0], lap[1]);
        	String token = TokenManager.getNewToken(authorizedUser.getUsername());
        	token = BasicAuth.encodeToken(token);
        	System.out.println("New Encoded Token: " + token);
	        TokenManager.addToken(token, authorizedUser);
	        return Response.status(200).entity(token).build();
        } catch (UserNotAuthorizedException e) {
	    	return Response.status(400).entity(e.getMessage()).build();
        }
    }
}