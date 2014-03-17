package org.egbers.homeautomation.kitchen.upc.service;

import static org.egbers.homeautomation.kitchen.upc.domain.ItemHistory.INTAKE;
import static org.egbers.homeautomation.kitchen.upc.domain.ItemHistory.LOOKUP;
import static org.egbers.homeautomation.kitchen.upc.domain.ItemHistory.OUTBOUND;

import java.util.Date;
import java.util.List;

import org.egbers.homeautomation.kitchen.upc.dao.ItemExternalDAO;
import org.egbers.homeautomation.kitchen.upc.dao.ItemHistoryDAO;
import org.egbers.homeautomation.kitchen.upc.dao.ItemLocalDAO;
import org.egbers.homeautomation.kitchen.upc.domain.Item;
import org.egbers.homeautomation.kitchen.upc.domain.ItemHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class ItemLookUpService {
	@Autowired
	private ItemExternalDAO itemExternalDAO;
	@Autowired
	private ItemLocalDAO itemLocalDAO;
	@Autowired
	private ItemHistoryDAO itemHistoryDAO;
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public Item findItemByUPC(String upcCode) {
		Item item = itemLocalDAO.findByUPC(upcCode);
		if(item == null) {
			item = itemExternalDAO.findByUPC(upcCode);
			if(item == null) {
				item = initItem(upcCode, item);
			}
			item = saveItem(item, LOOKUP);
		}
		return item;
	}
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public Item saveItem(Item item, String event) {
		if(item != null) {
			item = itemLocalDAO.save(item);
			logHistory(item, event);
		}
		return item;
	}

	private void logHistory(Item item, String event) {
		ItemHistory history = new ItemHistory();
		history.setDate(new Date());
		history.setEvent(event);
		history.setUpc(item.getUpc());
		itemHistoryDAO.save(history);
	}
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public Item itemIntake(String upcCode, Integer quantity) {
		return updateQuantity(upcCode, quantity, false, 1, INTAKE);
	}
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public Item itemOutbound(String upcCode, Integer quantity, Boolean onList) {
		return updateQuantity(upcCode, quantity, onList, -1, OUTBOUND);
	}
	
	private Item updateQuantity(String upcCode, Integer quantity, Boolean onList, int sign, String event) {
		//TODO This will cause an extra DB update. Not a HUGE deal in this low volume application
		Item item = findItemByUPC(upcCode);//itemLocalDAO.findByUPC(upcCode);
		item = initItem(upcCode, item);
		item.setOnList(onList);
		Integer originalQuantity = item.getQuantity() == null ? 0 : item.getQuantity();
		Integer tempQuantity = (originalQuantity + (sign * quantity));
		Integer newQuantity = tempQuantity > 0 ? tempQuantity : 0; 
		item.setQuantity(newQuantity);
		
		return saveItem(item, event);
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
