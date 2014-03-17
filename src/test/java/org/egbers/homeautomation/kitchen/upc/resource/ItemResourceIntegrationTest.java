package org.egbers.homeautomation.kitchen.upc.resource;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.ws.rs.core.Response;

import org.egbers.homeautomation.kitchen.upc.dao.ItemLocalDAO;
import org.egbers.homeautomation.kitchen.upc.domain.Item;
import org.egbers.homeautomation.kitchen.upc.integration.IntegrationTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations="classpath:applicationContext-Test.xml") 
@TransactionConfiguration(defaultRollback=true,transactionManager="transactionManager") 
@Category(IntegrationTest.class)
public class ItemResourceIntegrationTest {
	@Autowired
	private ItemResource itemResource;
	@Autowired
	private ItemLocalDAO itemLocalDAO;
	
	@Test
	public void itemResourceLookUpShouldReturnItemAlreadyInDatabase() throws Exception {
		List<Item> originalItems = itemLocalDAO.findAll();
		int itemCount = originalItems.size();
		
		String upcCode = "1234567890";
		Response response = itemResource.findByUPC(upcCode);
		Item returnedItem = (Item) response.getEntity();
		assertEquals("1234567890", returnedItem.getUpc());
		assertEquals("Test Name", returnedItem.getName());
		
		List<Item> items = itemLocalDAO.findAll();
		assertEquals(itemCount, items.size());
	}
	
	@Test
	public void itemResourceLookUpShouldAddAndReturnItemFromExternalDAO() throws Exception {
		List<Item> originalItems = itemLocalDAO.findAll();
		int itemCount = originalItems.size();
		
		String upcCode = "02838423";
		Response response = itemResource.findByUPC(upcCode);
		Item returnedItem = (Item) response.getEntity();
		assertEquals("02838423", returnedItem.getUpc());
		assertEquals("Marlboro Lights", returnedItem.getName());
		
		List<Item> items = itemLocalDAO.findAll();
		assertEquals(itemCount+1, items.size());
	}
	
	@Test
	public void itemResourceLookUpShouldReturnDefaultItemWhenNoItemsFoundLocallyOrExternally() throws Exception {
		List<Item> originalItems = itemLocalDAO.findAll();
		int itemCount = originalItems.size();
		
		String upcCode = "02266901";
		Response response = itemResource.findByUPC(upcCode);
		Item returnedItem = (Item) response.getEntity();
		assertEquals("02266901", returnedItem.getUpc());
		assertEquals("N/A - Item not found", returnedItem.getName());
		
		List<Item> items = itemLocalDAO.findAll();
		assertEquals(itemCount+1, items.size());
	}
	
}
