package org.egbers.homeautomation.kitchen.upc;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ URL.class, UPCDatabaseConnectionService.class })
public class UPCDatabaseConnectionServiceTest {
	private HttpURLConnection mockedConnection;
	private URL mockedURL;
	private BufferedReader mockedReader;
	private UPCDatabaseConnectionService service;
	private String upcCode;

	@Before
	public void setUp() throws IOException {
		upcCode = "TEST UPC CODE";
		mockedConnection = mock(HttpURLConnection.class);
		mockedURL = PowerMockito.mock(URL.class);
		mockedReader = mock(BufferedReader.class);

		service = new UPCDatabaseConnectionService() {
			@Override
			BufferedReader createBufferedReader(final HttpURLConnection conn) throws IOException {
				return mockedReader;
			}

			@Override
			URL createURL(final String upcCode) throws MalformedURLException {
				return mockedURL;
			}
		};
		when(mockedURL.openConnection()).thenReturn(mockedConnection);
	}

	@Test(expected = RuntimeException.class)
	public void shouldThrowRuntimeExceptionWhenResponseCodeIsNot200() throws IOException {
		when(mockedConnection.getResponseCode()).thenReturn(404);
		service.lookUpUPC(upcCode);
	}

	@Test
	public void should__() throws Exception {
		when(mockedConnection.getResponseCode()).thenReturn(200);
		when(mockedReader.readLine()).thenReturn("{id: 123}", null);
		JSONObject obj = service.lookUpUPC(upcCode);
		assertEquals("123", obj.getString("id"));
	}

}
