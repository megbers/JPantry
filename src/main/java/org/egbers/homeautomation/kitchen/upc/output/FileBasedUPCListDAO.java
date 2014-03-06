package org.egbers.homeautomation.kitchen.upc.output;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.codehaus.jettison.json.JSONObject;
import org.egbers.homeautomation.kitchen.upc.domain.Item;
import org.springframework.beans.factory.annotation.Value;

public class FileBasedUPCListDAO implements UPCListDAO {

	private String fileName;
	private FileWriter fileOut;
	private FileReader fileIn;

	public void write(final JSONObject product) throws Exception {
		fileOut.append(product.optString("itemname") + " - " + product.optString("description") + "\n");
		fileOut.flush();
	}

	public String read() throws Exception {
		StringBuffer buffer = new StringBuffer();
		char[] a = new char[10000];
		fileIn.read(a); // reads the content to the array
		for (char c : a) {
			buffer.append(c);
		}
		return buffer.toString();
	}

	public void reset() throws Exception {
		// TODO Auto-generated method stub
	}

	@Value("#{properties.listFileName}")
	public void setFileName(final String fileName) throws IOException {
		this.fileName = fileName;
		fileOut = new FileWriter(this.fileName, true);
		fileIn = new FileReader(this.fileName);
	}

	public Item find(String upcCode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
}
