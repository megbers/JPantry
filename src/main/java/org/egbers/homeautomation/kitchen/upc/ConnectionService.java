package org.egbers.homeautomation.kitchen.upc;

import org.codehaus.jettison.json.JSONObject;

public interface ConnectionService {
	public JSONObject lookUpUPC(String upcCode) throws Exception;
}
