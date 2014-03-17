package org.egbers.homeautomation.kitchen.upc.dao;

import java.util.List;

import org.egbers.homeautomation.kitchen.upc.domain.Item;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public class ItemLocalDAO extends HibernateDaoSupport {
	@Transactional(propagation=Propagation.REQUIRED)
	public Item save(Item transientInstance) {
		try {
			getHibernateTemplate().saveOrUpdate(transientInstance);
			return transientInstance;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public Item findById(Long id) {
		try {
			Item instance = (Item) getHibernateTemplate().get("org.egbers.homeautomation.kitchen.upc.domain.Item", id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public Item findByUPC(String upcCode){
		try {
			String queryString = "from org.egbers.homeautomation.kitchen.upc.domain.Item as model where model.upc= ?";
			Object[] values = {upcCode};
			List items = getHibernateTemplate().find(queryString, values);
			if(items.size() > 0) {
				return (Item) items.get(0);
			} else {
				return null;
			}
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void delete(Item transientInstance) {
		try {
			getHibernateTemplate().delete(transientInstance);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Item> findAll() {
		try {
			return getHibernateTemplate().find("from org.egbers.homeautomation.kitchen.upc.domain.Item as model order by model.name asc");
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Item> findCurrentInventory() {
		try {
			return getHibernateTemplate().find("from org.egbers.homeautomation.kitchen.upc.domain.Item as model where model.quantity > 0 order by model.name asc");
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Item> findCurrentShoppingList() {
		try {
			return getHibernateTemplate().find("from org.egbers.homeautomation.kitchen.upc.domain.Item as model where model.onList = true order by model.name asc");
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
