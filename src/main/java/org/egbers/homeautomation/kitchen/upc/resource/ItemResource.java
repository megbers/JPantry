package org.egbers.homeautomation.kitchen.upc.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

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
		Item item = itemLookUpService.findItemByUPC(upcCode);
		return Response.status(Status.OK).type(APPLICATION_JSON).entity(item).build();
	}
	
	@GET
	@Path("/find/inventory")
	@Produces(APPLICATION_JSON)
	public Response findInventory() throws JSONException {
		List<Item> items = itemLookUpService.getInventory();
		return Response.status(Status.OK).type(APPLICATION_JSON).entity(items).build();
	}
	
	@GET
	@Path("/find/all")
	@Produces(APPLICATION_JSON)
	public Response findAll() throws JSONException {
		List<Item> items = itemLookUpService.getAllItems();
		return Response.status(Status.OK).type(APPLICATION_JSON).entity(items).build();
	}
	
	@GET
	@Path("/find/shopping")
	@Produces(APPLICATION_JSON)
	public Response findShoppingList() throws JSONException {
		List<Item> items = itemLookUpService.getShoppingList();
		return Response.status(Status.OK).type(APPLICATION_JSON).entity(items).build();
	}
	
	
	@GET
	@Path("/in/{upcCode}/{quantity}")
	@Produces(APPLICATION_JSON)
	public Response itemIntake(@PathParam("upcCode") String upcCode, @PathParam("quantity") Integer quantity) {
		Item item = itemLookUpService.itemIntake(upcCode, quantity);
		return Response.status(Status.OK).type(APPLICATION_JSON).entity(item).build();
	}
	
	@GET
	@Path("/out/{upcCode}/{quantity}/{onList}")
	@Produces(APPLICATION_JSON)
	public Response itemOutbound(@PathParam("upcCode") String upcCode, @PathParam("quantity") Integer quantity, @PathParam("onList") Boolean onList) {
		Item item = itemLookUpService.itemOutbound(upcCode, quantity, onList);
		return Response.status(Status.OK).type(APPLICATION_JSON).entity(item).build();
	}
	
	
	public void setItemLookUpService(ItemLookUpService itemLookUpService) {
		this.itemLookUpService = itemLookUpService;
	}
	
}
