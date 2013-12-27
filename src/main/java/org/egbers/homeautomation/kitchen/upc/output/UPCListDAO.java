package org.egbers.homeautomation.kitchen.upc.output;

import org.codehaus.jettison.json.JSONObject;

public interface UPCListDAO {
	public void write(JSONObject product) throws Exception;

	public String read() throws Exception;

	public void reset();
}
