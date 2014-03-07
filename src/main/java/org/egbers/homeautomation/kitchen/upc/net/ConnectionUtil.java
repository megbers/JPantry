package org.egbers.homeautomation.kitchen.upc.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionUtil {
	
	private URL serviceURL;
	
	public BufferedReader createBufferedReader(HttpURLConnection conn) throws IOException {
		return new BufferedReader(new InputStreamReader((conn.getInputStream())));
	}
	
	public HttpURLConnection createHttpUrlConnection(String upcCode) throws IOException {
		return (HttpURLConnection) new URL(serviceURL, upcCode).openConnection();
	}

	public void setServiceURL(URL serviceURL) {
		this.serviceURL = serviceURL;
	}
	
}
