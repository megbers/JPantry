package org.egbers.homeautomation.kitchen.upc.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egbers.homeautomation.kitchen.upc.dao.ItemExternalDAO;
import org.egbers.homeautomation.kitchen.upc.dao.ItemLocalDAO;
import org.egbers.homeautomation.kitchen.upc.domain.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ItemLookUpServiceTest {
	@Mock
	private ItemLocalDAO itemLocalDAO;
	@Mock
	private ItemExternalDAO itemExternalDAO;
	@InjectMocks
	private ItemLookUpService service;
	private String upcCode;
	private Item item;
	private Integer quantity;
	private List<Item> itemList;
	
	@Before
	public void setUp() {
		upcCode = "UPC Code";
		item = new Item();
		quantity = 0;
		itemList = new ArrayList<Item>();
	}
	
	@Test
	public void shouldReturnItemWhenFoundLocally() {
		when(itemLocalDAO.findByUPC(upcCode)).thenReturn(item);
		
		Item actual = service.findItemByUPC(upcCode);
		assertEquals(item, actual);
	}
	
	@Test
	public void shouldReturnItemWhenNotFoundLocallyButFoundExternally() {
		when(itemLocalDAO.findByUPC(upcCode)).thenReturn(null);
		when(itemExternalDAO.findByUPC(upcCode)).thenReturn(item);
		when(itemLocalDAO.save(item)).thenReturn(item);
		
		Item actual = service.findItemByUPC(upcCode);
		assertEquals(item, actual);
	}
	
	@Test
	public void shouldReturnNullWhenNotFoundLocallyOrExternally() {
		when(itemLocalDAO.findByUPC(upcCode)).thenReturn(null);
		when(itemExternalDAO.findByUPC(upcCode)).thenReturn(null);
		
		Item actual = service.findItemByUPC(upcCode);
		assertNull(actual);
	}
	
	@Test
	public void shouldSaveItemWhenNotFoundLocallyButFoundExternally() {
		when(itemLocalDAO.findByUPC(upcCode)).thenReturn(null);
		when(itemExternalDAO.findByUPC(upcCode)).thenReturn(item);
		when(itemLocalDAO.save(item)).thenReturn(item);
		
		Item actual = service.findItemByUPC(upcCode);
		
		verify(itemLocalDAO).save(item);
		assertEquals(item, actual);
	}
	
	@Test
	public void saveShouldNotCallSaveWhenItemIsNull() {
		when(itemLocalDAO.save(null)).thenReturn(null);
		
		Item actual = service.saveItem(null);
		
		verify(itemLocalDAO, never()).save(null);
		assertNull(actual);
	}
	
	@Test
	public void saveShouldCallSaveOnDAO() {
		when(itemLocalDAO.save(item)).thenReturn(item);
		
		Item actual = service.saveItem(item);
		
		verify(itemLocalDAO).save(item);
		assertEquals(item, actual);
	}
	
	@Test
	public void itemIntakeShouldUpdateQuantityWhenNonZero() {
		item.setQuantity(0);
		quantity = 1;
		when(itemLocalDAO.findByUPC(upcCode)).thenReturn(item);
		when(itemLocalDAO.save(item)).thenReturn(item);
		
		Item actual = service.itemIntake(upcCode, quantity);
		
		assertEquals(new Integer(1), actual.getQuantity());
	}
	
	@Test
	public void itemIntakeShouldAddToExistingQuantity() {
		item.setQuantity(100);
		quantity = 57;
		when(itemLocalDAO.findByUPC(upcCode)).thenReturn(item);
		when(itemLocalDAO.save(item)).thenReturn(item);
		
		Item actual = service.itemIntake(upcCode, quantity);
		
		assertEquals(new Integer(157), actual.getQuantity());
	}
	
	@Test
	public void itemIntakeShouldNotThrowExceptionWhenItemQuantityIsNull() {
		item.setQuantity(null);
		quantity = 10;
		when(itemLocalDAO.findByUPC(upcCode)).thenReturn(item);
		when(itemLocalDAO.save(item)).thenReturn(item);
		
		Item actual = service.itemIntake(upcCode, quantity);
		
		assertEquals(new Integer(10), actual.getQuantity());
	}
	
	@Test
	public void itemOutboundShouldUpdateQuantityWhenNonZero() {
		item.setQuantity(2);
		quantity = 1;
		when(itemLocalDAO.findByUPC(upcCode)).thenReturn(item);
		when(itemLocalDAO.save(item)).thenReturn(item);
		
		Item actual = service.itemOutbound(upcCode, quantity);
		
		assertEquals(new Integer(1), actual.getQuantity());
	}
	
	@Test
	public void itemOutboundShouldSetToZeroWhenNegitive() {
		item.setQuantity(57);
		quantity = 100;
		when(itemLocalDAO.findByUPC(upcCode)).thenReturn(item);
		when(itemLocalDAO.save(item)).thenReturn(item);
		
		Item actual = service.itemOutbound(upcCode, quantity);
		
		assertEquals(new Integer(0), actual.getQuantity());
	}
	 
	@Test
	public void itemIntakeShouldSetToZeroWhenItemQuantityIsNull() {
		item.setQuantity(null);
		quantity = 10;
		when(itemLocalDAO.findByUPC(upcCode)).thenReturn(item);
		when(itemLocalDAO.save(item)).thenReturn(item);
		
		Item actual = service.itemOutbound(upcCode, quantity);
		
		assertEquals(new Integer(0), actual.getQuantity());
	}

	@Test
	public void getShoppingListShouldReturnListOfItems() throws Exception {
		when(itemLocalDAO.findCurrentShoppingList()).thenReturn(itemList);

		List<Item> actual = service.getShoppingList();

		verify(itemLocalDAO).findCurrentShoppingList();
		assertEquals(itemList, actual);
	}
	
	@Test
	public void getInventoryShouldReturnListOfItems() throws Exception {
		when(itemLocalDAO.findCurrentInventory()).thenReturn(itemList);

		List<Item> actual = service.getInventory();

		verify(itemLocalDAO).findCurrentInventory();
		assertEquals(itemList, actual);
	}
	
	@Test
	public void getAllItemsShouldReturnListOfItems() throws Exception {
		when(itemLocalDAO.findAll()).thenReturn(itemList);

		List<Item> actual = service.getAllItems();

		verify(itemLocalDAO).findAll();
		assertEquals(itemList, actual);
	}

}
