package org.egbers.homeautomation.kitchen.upc.resource;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.egbers.homeautomation.kitchen.upc.domain.Item;
import org.egbers.homeautomation.kitchen.upc.service.ItemLookUpService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ItemResourceTest {
	@Mock
	private ItemLookUpService itemLookUpService;
	@InjectMocks
	private ItemResource resource;
	private String upcCode;
	private Item item;
	private Integer quantity;

	@Before
	public void setUp() {
		upcCode = "1234567890";
		quantity = 0;
		item = new Item();
	}

	@Test
	public void findByUPCShouldReturnResponse() throws Exception {
		when(itemLookUpService.findItemByUPC(upcCode)).thenReturn(item);

		Response response = resource.findByUPC(upcCode);

		assertEquals(item, response.getEntity());
	}

	// TODO Come up with some good error handling
	@Test(expected = RuntimeException.class)
	public void findByUPCShouldThrowExceptionWhenServiceThrowsException() throws Exception {
		when(itemLookUpService.findItemByUPC(upcCode)).thenThrow(new RuntimeException());

		resource.findByUPC(upcCode);
	}

	@Test
	public void intakeItemShouldReturnResponse() throws Exception {
		when(itemLookUpService.itemIntake(upcCode, quantity)).thenReturn(item);

		Response response = resource.itemIntake(upcCode, quantity);

		assertEquals(item, response.getEntity());
	}

	// TODO Come up with some good error handling
	@Test(expected = RuntimeException.class)
	public void intakeItemShouldThrowExceptionWhenServiceThrowsException() throws Exception {
		when(itemLookUpService.itemIntake(upcCode, quantity)).thenThrow(new RuntimeException());

		resource.itemIntake(upcCode, quantity);
	}
	
	@Test
	public void outboundItemShouldReturnResponse() throws Exception {
		when(itemLookUpService.itemOutbound(upcCode, quantity)).thenReturn(item);

		Response response = resource.itemOutbound(upcCode, quantity);

		assertEquals(item, response.getEntity());
	}

	// TODO Come up with some good error handling
	@Test(expected = RuntimeException.class)
	public void outboundItemShouldThrowExceptionWhenServiceThrowsException() throws Exception {
		when(itemLookUpService.itemOutbound(upcCode, quantity)).thenThrow(new RuntimeException());

		resource.itemOutbound(upcCode, quantity);
	}
}
