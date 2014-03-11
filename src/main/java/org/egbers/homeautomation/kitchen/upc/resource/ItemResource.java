package org.egbers.homeautomation.kitchen.upc.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jettison.json.JSONException;
import org.egbers.homeautomation.kitchen.upc.domain.Item;
import org.egbers.homeautomation.kitchen.upc.service.ItemLookUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("item")
public class ItemResource {

	@Autowired
	private ItemLookUpService itemLookUpService;
	
	@GET
	@Path("/find/{upcCode}")
	@Produces(APPLICATION_JSON)
	public Response findByUPC(@PathParam("upcCode") String upcCode) throws JSONException {
		//TODO PLEASE TEST ASAP
		Item item = itemLookUpService.findItemByUPC(upcCode);
		return Response.status(Status.OK).type(APPLICATION_JSON).entity(item).build();
	}

	public void setItemLookUpService(ItemLookUpService itemLookUpService) {
		this.itemLookUpService = itemLookUpService;
	}
	
}
