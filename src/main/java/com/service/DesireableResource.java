package com.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.auth.token.TokenManager;
import com.db.TradingEntityDB;
import com.db.UserDB;
import com.exceptions.ObjectMappingException;
import com.factory.LabelFactory;
import com.objects.domain.TradingEntity;
import com.objects.domain.User;
import com.objects.mapping.ObjectManager;

@Path("/desireable")
public class DesireableResource {
    @POST
    @Path("/addDesireable")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addDesireable(@Context HttpHeaders headers, String tradingEntity) {
    	String auth = headers.getRequestHeaders().getFirst("authorization");
    	User u = TokenManager.getUser(auth);
    	//UserDB.getUser(u.getUsername());
    	try {
			TradingEntity newTradingEntity = (TradingEntity) ObjectManager.readObjectAsString(tradingEntity, TradingEntity.class);
			if (TradingEntityDB.entityExists(newTradingEntity.getName(), LabelFactory.BeerLabels.DESIREABLE)==true) {
				//then related nodes must exist
				
			}
			else {
				TradingEntityDB.addTradingEntity(newTradingEntity.getName(), LabelFactory.BeerLabels.DESIREABLE, LabelFactory.BeerLabels.BEER);
			}
			if (newTradingEntity.getRelations().size()>0) {
				
			}
			
			return Response.status(200).entity("edge").build();
    	}
	    catch(ObjectMappingException e) {
	    	System.out.println(e.getMessage());
	    	return Response.status(400).entity(e.getMessage()).build();
	    }
    }
}
