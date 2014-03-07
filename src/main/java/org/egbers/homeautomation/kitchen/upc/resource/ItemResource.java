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
import org.springframework.stereotype.Component;

@Component
@Path("item")
public class ItemResource {

	@GET
	@Path("/{upcCode}/")
	@Produces(APPLICATION_JSON)
	public Response turnOn(@PathParam("upcCode") String upcCode) throws JSONException {
		Item item = new Item();
		return Response.status(Status.OK).entity(item).build();
	}
	
}
