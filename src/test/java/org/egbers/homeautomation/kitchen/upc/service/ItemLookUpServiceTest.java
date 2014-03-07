package org.egbers.homeautomation.kitchen.upc.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

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
	
	@Before
	public void setUp() {
		upcCode = "UPC Code";
		item = new Item();
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
}
