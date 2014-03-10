package org.egbers.homeautomation.kitchen.upc.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
}
