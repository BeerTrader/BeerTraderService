package com.auth;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import com.db.UserDB;
import com.exceptions.UserNotAuthorizedException;
import com.objects.domain.User;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

/**
 * Jersey HTTP Basic Auth filter
 * @author Deisss (LGPLv3)
 * Implementation taken from https://simplapi.wordpress.com/2013/01/24/jersey-jax-rs-implements-a-http-basic-auth-decoder/
 */
public class AuthFilter implements ContainerRequestFilter {
    /**
     * Apply the filter : check input request, validate or not with user auth
     * @param containerRequest The request from Tomcat server
     */
    @Override
    public ContainerRequest filter(ContainerRequest containerRequest) throws WebApplicationException {
    	System.out.println("Authenticating...");
        String method = containerRequest.getMethod();
        String path = containerRequest.getPath(true);
       
        //Allow users to register without authentication
        if (method.equals("POST")&&path.contains("createUser"))
        	return containerRequest;
 
        //Get the authentification passed in HTTP headers parameters
        String auth = containerRequest.getHeaderValue("authorization");
 
        //If the user does not have the right (does not provide any HTTP Basic Auth)
        if(auth == null){
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }
 
        //lap : loginAndPassword
        String[] lap = BasicAuth.decode(auth);
 
        //If login or password fail
        if(lap == null || lap.length != 2){
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }
 
        User authorizedUser;
        try {
        	authorizedUser = UserDB.authenticateUser(lap[0], lap[1]);
        }
        catch (UserNotAuthorizedException e) {
        	throw new WebApplicationException(Status.UNAUTHORIZED);
        }
 
        //TODO : HERE YOU SHOULD ADD PARAMETER TO REQUEST, TO REMEMBER USER ON YOUR REST SERVICE...
 
        return containerRequest;
    }
}