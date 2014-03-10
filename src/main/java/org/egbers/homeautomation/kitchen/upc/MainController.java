package org.egbers.homeautomation.kitchen.upc;

import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.egbers.homeautomation.kitchen.upc.domain.Item;
import org.egbers.homeautomation.kitchen.upc.output.UPCListDAO;
import org.egbers.homeautomation.kitchen.upc.service.ItemLookUpService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainController {
	static Logger LOG = Logger.getLogger(MainController.class.getName());
	
	@SuppressWarnings("resource")
	public static void main(final String[] args) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		ItemLookUpService service = (ItemLookUpService) context.getBean("itemLookUpService");
		context.getBean("fileBasedUPCListDAO");

		DataSource dataSource = (DataSource) context.getBean("dataSource");
		LOG.warn(dataSource);

		UPCListDAO derbyBasedUPCListDAO = (UPCListDAO) context.getBean("derbyBasedUPCListDAO");
		LOG.warn(derbyBasedUPCListDAO);

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
		Scanner scanner = new Scanner(System.in);
		while (true && !"exit".equalsIgnoreCase(upc)) {
			// "0028400034715"
			System.out.print("Please Enter a UPC: ");
			upc = scanner.nextLine();

			Item item = service.findItemByUPC(upc);
			if (item != null) {
				LOG.warn("Valid UPC");
				//upcListDAO.write(product);
				LOG.warn(item.getUpc()+":"+item.getName()+":"+item.getDescription());
			} else {
				LOG.warn("Invalid UPC");
			}
			
		}
		scanner.close();
		LOG.warn(upcListDAO.read());

	}

}
