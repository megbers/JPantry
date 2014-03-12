package org.egbers.homeautomation.kitchen.upc.dao;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egbers.homeautomation.kitchen.upc.domain.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.orm.hibernate3.HibernateTemplate;

@RunWith(MockitoJUnitRunner.class)
public class ItemLocalDAOTest {
	@Mock
	private HibernateTemplate template;
	@InjectMocks
	private ItemLocalDAO dao;
	private Item item;
	private Long id;
	
	@Before
	public void doBeforeEachTestCase() {
		item = new Item();
		id = 1001L;
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void saveShouldUpdateTheDatabase() {
		Item actual = dao.save(item);
		verify(template).saveOrUpdate(item);
		assertEquals(item, actual);
	}

	@Test(expected = RuntimeException.class)
	public void saveShouldThrowExceptionIfRaised() {
		doThrow(new RuntimeException()).when(template).saveOrUpdate(item);
		dao.save(item);
	}

	@Test
	public void findByIdShouldFindAUser() {
		when(template.get("org.egbers.homeautomation.kitchen.upc.domain.Item", id)).thenReturn(item);
		Item actual = dao.findById(id);
		assertEquals(item, actual);
	}

	@Test(expected = RuntimeException.class)
	public void findByIdShouldThrowExcpetionIfRaised() {
		doThrow(new RuntimeException()).when(template).get("org.egbers.homeautomation.kitchen.upc.domain.Item", id);
		dao.findById(id);
	}
	
	@Test
	public void findByUPCShouldReturnAnItem() {
		String upcCode = "upcCode";
		List<Item> list = new ArrayList<Item>();
		list.add(item);
		Object[] values = { upcCode };
		when(template.find("from org.egbers.homeautomation.kitchen.upc.domain.Item as model where model.upc= ?", values)).thenReturn(list);
		Item actual = dao.findByUPC(upcCode);
		verify(template).find("from org.egbers.homeautomation.kitchen.upc.domain.Item as model where model.upc= ?", values);
		assertEquals(actual, item);
	}
	
	@Test
	public void findByUPCShouldReturnWhenNoneAreFound() {
		String upcCode = "upcCode";
		List<Item> list = new ArrayList<Item>();
		Object[] values = { upcCode };
		when(template.find("from org.egbers.homeautomation.kitchen.upc.domain.Item as model where model.upc= ?", values)).thenReturn(list);
		Item actual = dao.findByUPC(upcCode);
		assertNull(actual);
	}

	@Test(expected = RuntimeException.class)
	public void findByUPCShouldThrowExceptionIfRaised() {
		String upcCode = "upcCode";
		Object[] values = { upcCode };
		doThrow(new RuntimeException()).when(template).find("from org.egbers.homeautomation.kitchen.upc.domain.Item as model where model.upc= ?", values);
		dao.findByUPC(upcCode);
	}	
	
	@Test
	public void deleteNoteShouldDeleteNote() {
		dao.delete(item);
		verify(template).delete(item);
	}

	@Test(expected = RuntimeException.class)
	public void deleteNoteShouldThrowExceptionIfRaised() {
		doThrow(new RuntimeException()).when(template).delete(item);
		dao.delete(item);
	}

	@Test
	public void findAllShouldFindItems() {
		List<Item> list = new ArrayList<Item>();
		when(template.find("from org.egbers.homeautomation.kitchen.upc.domain.Item")).thenReturn(list);
		List<Item> actual = dao.findAll();
		verify(template).find("from org.egbers.homeautomation.kitchen.upc.domain.Item");
		assertEquals(actual, list);
	}

	@Test(expected = RuntimeException.class)
	public void findAllShouldThrowExceptionIfRaised() {
		doThrow(new RuntimeException()).when(template).find("from org.egbers.homeautomation.kitchen.upc.domain.Item");
		dao.findAll();
	}
	
}
