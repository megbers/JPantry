package org.egbers.homeautomation.kitchen.upc;

import org.codehaus.jettison.json.JSONObject;
import org.egbers.homeautomation.kitchen.upc.domain.Item;
import org.egbers.homeautomation.kitchen.upc.output.UPCListDAO;
import org.springframework.beans.factory.annotation.Autowired;

public class LocalDatabaseConnectionService implements ConnectionService {
	
	@Autowired
	private UPCListDAO upcListDAO;
	@Autowired
	private UPCDatabaseConnectionService upcDatabaseConnectionService;
	
	public JSONObject lookUpUPC(String upcCode) throws Exception {
		JSONObject returnItem = new JSONObject();
		Item item = upcListDAO.find(upcCode);
		
		if(item == null) {
			JSONObject response = upcDatabaseConnectionService.lookUpUPC(upcCode);
			returnItem.put("item", response);
		} else {
			returnItem.put("item", item);
		}
		
		return returnItem;
	}

}
