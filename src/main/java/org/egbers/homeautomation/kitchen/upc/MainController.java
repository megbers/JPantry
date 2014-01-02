package org.egbers.homeautomation.kitchen.upc;

import java.util.Scanner;

import javax.sql.DataSource;

import org.codehaus.jettison.json.JSONObject;
import org.egbers.homeautomation.kitchen.upc.output.UPCListDAO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainController {

	public static void main(final String[] args) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		ConnectionService service = (ConnectionService) context.getBean("upcDatabaseConnectionService");
		context.getBean("fileBasedUPCListDAO");

		DataSource dataSource = (DataSource) context.getBean("dataSource");
		System.out.println(dataSource);

		UPCListDAO derbyBasedUPCListDAO = (UPCListDAO) context.getBean("derbyBasedUPCListDAO");
		System.out.println(derbyBasedUPCListDAO);

		// UPCLookUpGUI gui = new UPCLookUpGUI();
		//
		// String lastUPCCode = "";
		// while (true) {
		// String upc = gui.getUPCCode();
		// if (!lastUPCCode.equals(upc)) {
		// lastUPCCode = upc;
		// JSONObject product = service.lookUpUPC(upc);
		// System.out.println(product.toString());
		// gui.addProductToList(product.getString("itemname") +
		// " - " + product.getString("description"));
		// }
		// }

		UPCListDAO upcListDAO = derbyBasedUPCListDAO;

		String upc = "";
		while (true && !"exit".equalsIgnoreCase(upc)) {
			// "0028400034715"
			System.out.print("Please Enter a UPC: ");
			Scanner scanner = new Scanner(System.in);
			upc = scanner.nextLine();

			JSONObject product = service.lookUpUPC(upc);
			if (product.optBoolean("valid")) {
				System.out.println("Valid UPC");
				upcListDAO.write(product);
			} else {
				System.out.println("Invalid UPC");
			}
			System.out.println(product.toString());
		}

		System.out.println(upcListDAO.read());

	}

}
