package com.auth;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import com.auth.token.TokenManager;
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
        System.out.println("Encoded Token: " + auth);
        
        if (method.equals("GET")&&path.contains("login")) {	 
	        return containerRequest;
        }
        else {
	        if (!TokenManager.tokenExists(auth))
	        	throw new WebApplicationException(Status.UNAUTHORIZED);
	        
	        String token = BasicAuth.decodeToken(auth);
	        System.out.println("Decoded Token: " + token);
	        if (TokenManager.isTokenExpired(token))
	        	throw new WebApplicationException(Status.UNAUTHORIZED);
        }
        return containerRequest;
    }
}