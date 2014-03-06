package org.egbers.homeautomation.kitchen.upc;

import org.codehaus.jettison.json.JSONObject;
import org.egbers.homeautomation.kitchen.upc.domain.Item;
import org.egbers.homeautomation.kitchen.upc.output.UPCListDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LocalDatabaseConnectionServiceTest {
	@Mock
	private UPCDatabaseConnectionService upcDatabaseConnectionService;
	@Mock
	private UPCListDAO upcListDAO;
	@InjectMocks
	private LocalDatabaseConnectionService service = new LocalDatabaseConnectionService();
	
	private String upcCode;
	
	@Before
	public void setUp() {
		upcCode = "Test UPC Code";
	}
	
	@Test
	@Ignore
	public void shouldReturnNull() throws Exception {
		Assert.assertNull(service.lookUpUPC(upcCode));
	}
	
	@Test
	public void shouldReturnItemFromLocalDBWhenPresent() throws Exception {
		Item item = new Item();
		Mockito.when(upcListDAO.find(upcCode)).thenReturn(item);
		
		JSONObject actual = service.lookUpUPC(upcCode);
		Assert.assertEquals(item, actual.get("item"));
	}
	
	@Test
	public void shouldReturnItemFromExternalServiceWhenNotFoundInDB() throws Exception {
		Mockito.when(upcListDAO.find(upcCode)).thenReturn(null);
		JSONObject jsonObject = new JSONObject();
		Mockito.when(upcDatabaseConnectionService.lookUpUPC(upcCode)).thenReturn(jsonObject);
		JSONObject actual = service.lookUpUPC(upcCode);
		Assert.assertEquals(jsonObject, actual.get("item"));
	}
	
}
