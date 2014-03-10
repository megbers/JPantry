package org.egbers.homeautomation.kitchen.upc.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.egbers.homeautomation.kitchen.upc.MainController;
import org.egbers.homeautomation.kitchen.upc.domain.Item;
import org.egbers.homeautomation.kitchen.upc.net.ConnectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class ItemExternalDAO {
	static Logger LOG = Logger.getLogger(MainController.class.getName());
	
	@Autowired
	private ConnectionUtil connectionUtil;
	
	public Item findByUPC(final String upcCode) {
		System.setProperty("http.proxyHost", "proxycorp1v.dteco.com");
		System.setProperty("http.proxyPort", "8080");

		try {
			HttpURLConnection conn = connectionUtil.createHttpUrlConnection(upcCode);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = connectionUtil.createBufferedReader(conn);

			String responseString = readResponse(br);

			conn.disconnect();

			return convertJSONToItem(responseString);

		} catch (Exception e){
			LOG.error(e);
			return null;
		}

	}
	
	private Item convertJSONToItem(String responseString) throws JSONException {
		JSONObject response = new JSONObject(responseString);
		Item item = new Item();
		item.setUpc(response.getString("number"));
		item.setName(response.getString("itemname"));
		item.setDescription(response.getString("description"));
		return item;
	}

	private String readResponse(final BufferedReader br) throws IOException {
		String output;
		StringBuffer responseString = new StringBuffer();
		while ((output = br.readLine()) != null) {
			responseString.append(output);
		}
		return responseString.toString();
	}
	
	public void setConnectionUtil(ConnectionUtil connectionUtil) {
		this.connectionUtil = connectionUtil;
	}

	//TODO Remove this is only here for reference in case we need this elsewhere
	@SuppressWarnings("unused")
	private String apiKey;
	@SuppressWarnings("unused")
	private String apiUrl;
	
	@Value("#{properties.apiKey}")
	public void setApiKey(final String apiKey) {
		this.apiKey = apiKey;
	}

	@Value("#{properties.upcdatabaseApi}")
	public void setApiUrl(final String apiUrl) {
		this.apiUrl = apiUrl;
	}

}
