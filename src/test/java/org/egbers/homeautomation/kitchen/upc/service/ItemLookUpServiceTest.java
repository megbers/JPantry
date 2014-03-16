package org.egbers.homeautomation.kitchen.upc.service;

import static org.egbers.homeautomation.kitchen.upc.domain.ItemHistory.INTAKE;
import static org.egbers.homeautomation.kitchen.upc.domain.ItemHistory.OUTBOUND;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egbers.homeautomation.kitchen.upc.dao.ItemExternalDAO;
import org.egbers.homeautomation.kitchen.upc.dao.ItemHistoryDAO;
import org.egbers.homeautomation.kitchen.upc.dao.ItemLocalDAO;
import org.egbers.homeautomation.kitchen.upc.domain.Item;
import org.egbers.homeautomation.kitchen.upc.domain.ItemHistory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ItemLookUpServiceTest {
	@Mock
	private ItemLocalDAO itemLocalDAO;
	@Mock
	private ItemExternalDAO itemExternalDAO;
	@Mock
	private ItemHistoryDAO itemHistoryDAO;
	@InjectMocks
	private ItemLookUpService service;
	private String upcCode;
	private Item item;
	private Integer quantity;
	private List<Item> itemList;
	private Boolean onList;
	
	@Before
	public void setUp() {
		upcCode = "UPC Code";
		item = new Item();
		quantity = 0;
		itemList = new ArrayList<Item>();
		onList = true;
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
		
		Item actual = service.saveItem(null, null);
		
		verify(itemLocalDAO, never()).save(null);
		assertNull(actual);
	}
	
	@Test
	public void saveShouldCallSaveOnDAOs() {
		when(itemLocalDAO.save(item)).thenReturn(item);
		
		Item actual = service.saveItem(item, null);
		
		verify(itemLocalDAO).save(item);
		verify(itemHistoryDAO).save(any(ItemHistory.class));
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
	public void itemIntakeShouldNotThrowExceptionAndSetNameToUpdateRequiredWhenItemIsNotFound() {
		when(itemLocalDAO.findByUPC(upcCode)).thenReturn(null);
		when(itemExternalDAO.findByUPC(upcCode)).thenReturn(null);
		item.setUpc(upcCode);
		when(itemLocalDAO.save(any(Item.class))).thenReturn(item);
		
		ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
		ArgumentCaptor<ItemHistory> itemHistoryCaptor = ArgumentCaptor.forClass(ItemHistory.class);
		
		service.itemIntake(upcCode, quantity);
		verify(itemLocalDAO).save(itemCaptor.capture());
		verify(itemHistoryDAO).save(itemHistoryCaptor.capture());
		
		assertEquals("N/A - Item not found", itemCaptor.getAllValues().get(0).getName());
		assertEquals(upcCode, itemCaptor.getAllValues().get(0).getUpc());
		
		assertEquals(upcCode, itemHistoryCaptor.getAllValues().get(0).getUpc());
		assertEquals(INTAKE, itemHistoryCaptor.getAllValues().get(0).getEvent());
	}
	
	@Test
	public void itemOutboundShouldUpdateQuantityWhenNonZero() {
		item.setQuantity(2);
		quantity = 1;
		when(itemLocalDAO.findByUPC(upcCode)).thenReturn(item);
		when(itemLocalDAO.save(item)).thenReturn(item);
		
		Item actual = service.itemOutbound(upcCode, quantity, onList);
		
		assertEquals(new Integer(1), actual.getQuantity());
		assertTrue(actual.getOnList());
	}
	
	@Test
	public void itemOutboundShouldSetToZeroWhenNegitive() {
		item.setQuantity(57);
		quantity = 100;
		when(itemLocalDAO.findByUPC(upcCode)).thenReturn(item);
		when(itemLocalDAO.save(item)).thenReturn(item);
		
		Item actual = service.itemOutbound(upcCode, quantity, onList);
		
		assertEquals(new Integer(0), actual.getQuantity());
		assertTrue(actual.getOnList());
	}
	 
	@Test
	public void itemOutboundShouldSetToZeroWhenItemQuantityIsNull() {
		item.setQuantity(null);
		quantity = 10;
		when(itemLocalDAO.findByUPC(upcCode)).thenReturn(item);
		when(itemLocalDAO.save(item)).thenReturn(item);
		
		Item actual = service.itemOutbound(upcCode, quantity, onList);
		
		assertEquals(new Integer(0), actual.getQuantity());
		assertTrue(actual.getOnList());
	}
	
	@Test
	public void itemOutboundShouldNotThrowExceptionAndSetNameToUpdateRequiredWhenItemIsNotFound() {
		when(itemLocalDAO.findByUPC(upcCode)).thenReturn(null);
		when(itemExternalDAO.findByUPC(upcCode)).thenReturn(null);
		item.setUpc(upcCode);
		when(itemLocalDAO.save(any(Item.class))).thenReturn(item);
		
		ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
		ArgumentCaptor<ItemHistory> itemHistoryCaptor = ArgumentCaptor.forClass(ItemHistory.class);
		
		service.itemOutbound(upcCode, quantity, onList);
		verify(itemLocalDAO).save(itemCaptor.capture());
		verify(itemHistoryDAO).save(itemHistoryCaptor.capture());
		
		assertEquals("N/A - Item not found", itemCaptor.getAllValues().get(0).getName());
		assertEquals(upcCode, itemCaptor.getAllValues().get(0).getUpc());
		
		assertEquals(upcCode, itemHistoryCaptor.getAllValues().get(0).getUpc());
		assertEquals(OUTBOUND, itemHistoryCaptor.getAllValues().get(0).getEvent());
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
