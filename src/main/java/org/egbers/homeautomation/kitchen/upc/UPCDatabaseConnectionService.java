package org.egbers.homeautomation.kitchen.upc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

public class UPCDatabaseConnectionService implements ConnectionService {
	private String apiKey;
	private String apiUrl;

	public JSONObject lookUpUPC(final String upcCode) {
		System.setProperty("http.proxyHost", "proxycorp1v.dteco.com");
		System.setProperty("http.proxyPort", "8080");
		System.setProperty("http.proxyUser", "megbers");
		System.setProperty("http.proxyPassword", "e8XKWB11");

		try {

			URL url = createURL(upcCode);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = createBufferedReader(conn);

			String responseString = readResponse(br);

			conn.disconnect();

			return new JSONObject(responseString);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	private String readResponse(final BufferedReader br) throws IOException {
		String output;
		StringBuffer responseString = new StringBuffer();
		while ((output = br.readLine()) != null) {
			responseString.append(output);
		}
		return responseString.toString();
	}

	// This is for testing
	BufferedReader createBufferedReader(final HttpURLConnection conn) throws IOException {
		return new BufferedReader(new InputStreamReader((conn.getInputStream())));
	}

	// This is for testing
	URL createURL(final String upcCode) throws MalformedURLException {
		return new URL(apiUrl + apiKey + "/" + upcCode);
	}

	@Value("#{properties.apiKey}")
	public void setApiKey(final String apiKey) {
		this.apiKey = apiKey;
	}

	@Value("#{properties.upcdatabaseApi}")
	public void setApiUrl(final String apiUrl) {
		this.apiUrl = apiUrl;
	}

}
