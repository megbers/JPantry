package org.egbers.homeautomation.kitchen.upc.dao;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.egbers.homeautomation.kitchen.upc.domain.ItemHistory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class ItemHistoryDAOTest {
	@Mock
	private HibernateTemplate template;
	@InjectMocks
	private ItemHistoryDAO dao;
	private ItemHistory itemHistory;
	
	@Before
	public void doBeforeEachTestCase() {
		itemHistory = new ItemHistory();
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void saveShouldUpdateTheDatabase() {
		ItemHistory actual = dao.save(itemHistory);
		verify(template).saveOrUpdate(itemHistory);
		assertEquals(itemHistory, actual);
	}

	@Test(expected = RuntimeException.class)
	public void saveShouldThrowExceptionIfRaised() {
		doThrow(new RuntimeException()).when(template).saveOrUpdate(itemHistory);
		dao.save(itemHistory);
	}
}
