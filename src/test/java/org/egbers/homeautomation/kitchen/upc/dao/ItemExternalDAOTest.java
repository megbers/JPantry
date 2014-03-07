package org.egbers.homeautomation.kitchen.upc.dao;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;

import org.egbers.homeautomation.kitchen.upc.domain.Item;
import org.egbers.homeautomation.kitchen.upc.net.ConnectionUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ItemExternalDAOTest {
	@Mock
	private HttpURLConnection mockedConnection;
	@Mock
	private BufferedReader mockedReader;
	@Mock
	private ConnectionUtil connectionUtil;
	@InjectMocks
	private ItemExternalDAO service;
	private String upcCode;
	
	@Before
	public void setUp() throws IOException {
		upcCode = "TEST UPC CODE";
		when(connectionUtil.createHttpUrlConnection(upcCode)).thenReturn(mockedConnection);
		when(connectionUtil.createBufferedReader(mockedConnection)).thenReturn(mockedReader);
	}

	@Test
	public void shouldReturnNullWhenResponseCodeIsNot200() throws IOException {
		when(mockedConnection.getResponseCode()).thenReturn(404);
		assertNull(service.findByUPC(upcCode));
	}
	
	@Test
	public void shouldReturnNullWhenJSONException() throws IOException {
		when(mockedConnection.getResponseCode()).thenReturn(200);
		when(mockedReader.readLine()).thenReturn("INVALID JSON", (String) null);
		assertNull(service.findByUPC(upcCode));
	}
	
	@Test
	public void shouldReturnNullWhenIOExceptionOnReader() throws IOException {
		when(mockedConnection.getResponseCode()).thenReturn(200);
		when(mockedReader.readLine()).thenThrow(new IOException());
		assertNull(service.findByUPC(upcCode));
	}

	@Test
	public void shouldReturnAnItemWhenSuccessfull() throws Exception {
		when(mockedConnection.getResponseCode()).thenReturn(200);
		when(mockedReader.readLine()).thenReturn("{number: \"123\", itemname: \"Product Name\", description: \"Product Description\"}", (String) null);
		Item item = service.findByUPC(upcCode);
		assertEquals("123", item.getUpc());
		assertEquals("Product Name", item.getName());
		assertEquals("Product Description", item.getDescription());
	}

}
