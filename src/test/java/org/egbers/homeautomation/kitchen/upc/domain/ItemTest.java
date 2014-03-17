package org.egbers.homeautomation.kitchen.upc.domain;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class ItemTest {
	@Test
	public void toStringShouldReturnStringRepresentationOfObject() {
		Item item = new Item();
		item.setId(1L);
		item.setName("Test Name");
		item.setDescription("Test Description");
		item.setQuantity(100);
		item.setOnList(true);
		item.setUpc("Test UPC");
		
		assertEquals("ID: 1| UPC:Test UPC| Name: Test Name| Description: Test Description| Quantity: 100| On List: true", item.toString());
	}
	
	
}
