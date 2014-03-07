package org.egbers.homeautomation.kitchen.upc.service;

import org.egbers.homeautomation.kitchen.upc.dao.ItemExternalDAO;
import org.egbers.homeautomation.kitchen.upc.dao.ItemLocalDAO;
import org.egbers.homeautomation.kitchen.upc.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;

public class ItemLookUpService {
	@Autowired
	private ItemExternalDAO itemExternalDAO;
	@Autowired
	private ItemLocalDAO itemLocalDAO; 
	
	public Item findItemByUPC(String upcCode) {
		Item item = itemLocalDAO.findByUPC(upcCode);
		if(item == null) {
			item = itemExternalDAO.findByUPC(upcCode);
		}
		return item;
	}
}
