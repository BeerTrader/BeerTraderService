package com.auth;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerRequestContext;

import com.auth.token.TokenManager;

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
    public void filter(ContainerRequestContext containerRequest) {
        String path = containerRequest.getUriInfo().getPath();

        //Allow users to register without authentication
        if (!path.contains("createUser")) {
	        //Get the authentification passed in HTTP headers parameters
	        String auth = containerRequest.getHeaderString("authorization");
	        //If the user does not have the right (does not provide any HTTP Basic Auth)
	        if(auth == null){
	            throw new WebApplicationException(Status.UNAUTHORIZED);
	        }
	        
	        if (!path.contains("login")) {
		        if (!TokenManager.tokenExists(auth)) {
		        	throw new WebApplicationException(Status.UNAUTHORIZED);
		        }
		        
		        String token = BasicAuth.decodeToken(auth);
		        if (TokenManager.isTokenExpired(token)) {
		        	throw new WebApplicationException(Status.UNAUTHORIZED);
		        }
	        }
        }
    }
}