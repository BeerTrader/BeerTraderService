package com.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.neo4j.graphdb.Node;

import com.db.RelationshipTypeDB;
import com.db.TradingEntityDB;
import com.db.UserDB;
import com.exceptions.ObjectMappingException;
import com.factory.LabelFactory;
import com.factory.RelationshipTypeFactory;
import com.objects.domain.TradingEntity;
import com.objects.mapping.ObjectManager;

@Path("/desirable")
public class DesirableResource {
    @POST
    @Path("/addDesirable")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addDesireable(@Context HttpHeaders headers, String tradingEntity) {
    	String auth = headers.getRequestHeaders().getFirst("authorization");
    	try {
    		Node userNode = UserDB.getUserNode(auth);
    		Node tradingEntityNode;
			TradingEntity newTradingEntity = (TradingEntity) ObjectManager.readObjectAsString(tradingEntity, TradingEntity.class);
			if (TradingEntityDB.entityExists(newTradingEntity.getName(), LabelFactory.BeerLabels.DESIRABLE)==true) {
				//then related nodes must exist
				tradingEntityNode = TradingEntityDB.getTradingEntity(newTradingEntity.getName(), LabelFactory.BeerLabels.DESIRABLE);
			}
			else {
				tradingEntityNode = TradingEntityDB.addTradingEntity(newTradingEntity.getName(), LabelFactory.BeerLabels.DESIRABLE, LabelFactory.getLabel(newTradingEntity.getLabel()));
			}
			RelationshipTypeDB.addRelationshipBetweenNodes(userNode, tradingEntityNode, RelationshipTypeFactory.BeerRelationships.DESIRES);
			if (newTradingEntity.getRelations().size()>0) {
				//addRelations
			}
			
			return Response.status(200).entity(newTradingEntity.getName()).build();
    	}
	    catch(ObjectMappingException e) {
	    	System.out.println(e.getMessage());
	    	return Response.status(400).entity(e.getMessage()).build();
	    }
    }
}
