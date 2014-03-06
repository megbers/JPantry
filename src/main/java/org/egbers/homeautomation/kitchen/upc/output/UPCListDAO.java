package org.egbers.homeautomation.kitchen.upc.output;

import org.codehaus.jettison.json.JSONObject;
import org.egbers.homeautomation.kitchen.upc.domain.Item;

public interface UPCListDAO {
	public void write(JSONObject product) throws Exception;

	public String read() throws Exception;

	public void reset() throws Exception;
	
	public Item find(String upcCode) throws Exception;
}
