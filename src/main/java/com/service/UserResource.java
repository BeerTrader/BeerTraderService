package com.service;

import org.apache.commons.lang.StringUtils;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Produces;

import java.io.IOException;
import java.util.Collections;

import com.db.UserDB;
import com.exceptions.UserNotFoundException;
import com.objects.ObjectManager;

@Path("/user")
public class UserResource {

    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public String findUser(@PathParam("username") String username) throws IOException {
    	if (StringUtils.isEmpty(username))
    		return ObjectManager.getInstance().writeValueAsString(Collections.emptyMap());
    	
    	try {
    		return ObjectManager.getInstance().writeValueAsString(UserDB.getUser(username));
    	}
    	catch(UserNotFoundException e) {
    		e.printStackTrace();
    		return ObjectManager.getInstance().writeValueAsString(e);
    	}
    }
}