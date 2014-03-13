package org.egbers.homeautomation.kitchen.upc.service;

import java.util.List;

import org.egbers.homeautomation.kitchen.upc.dao.ItemExternalDAO;
import org.egbers.homeautomation.kitchen.upc.dao.ItemLocalDAO;
import org.egbers.homeautomation.kitchen.upc.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class ItemLookUpService {
	@Autowired
	private ItemExternalDAO itemExternalDAO;
	@Autowired
	private ItemLocalDAO itemLocalDAO; 
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public Item findItemByUPC(String upcCode) {
		Item item = itemLocalDAO.findByUPC(upcCode);
		if(item == null) {
			item = itemExternalDAO.findByUPC(upcCode);
			item = saveItem(item);
		}
		return item;
	}
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public Item saveItem(Item item) {
		if(item != null) {
			item = itemLocalDAO.save(item);
		}
		return item;
	}
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public Item itemIntake(String upcCode, Integer quantity) {
		return updateQuantity(upcCode, quantity, false, 1);
	}
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public Item itemOutbound(String upcCode, Integer quantity, Boolean onList) {
		return updateQuantity(upcCode, quantity, onList, -1);
	}
	
	private Item updateQuantity(String upcCode, Integer quantity, Boolean onList, int sign) {
		//TODO This will cause an extra DB update. Not a HUGE deal in this low volume application
		Item item = findItemByUPC(upcCode);//itemLocalDAO.findByUPC(upcCode);
		item = initItem(upcCode, item);
		item.setOnList(onList);
		Integer originalQuantity = item.getQuantity() == null ? 0 : item.getQuantity();
		Integer tempQuantity = (originalQuantity + (sign * quantity));
		Integer newQuantity = tempQuantity > 0 ? tempQuantity : 0; 
		item.setQuantity(newQuantity);
		
		return saveItem(item);
	}

	private Item initItem(String upcCode, Item item) {
		if(item == null) {
			item = new Item();
			item.setUpc(upcCode);
			item.setName("N/A - Item not found");
		}
		return item;
	}
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public List<Item> getShoppingList() {
		return itemLocalDAO.findCurrentShoppingList();
	}
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public List<Item> getInventory() {
		return itemLocalDAO.findCurrentInventory();
	}
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public List<Item> getAllItems() {
		return itemLocalDAO.findAll();
	}
}
