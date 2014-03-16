package org.egbers.homeautomation.kitchen.upc.dao;

import org.egbers.homeautomation.kitchen.upc.domain.ItemHistory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class ItemHistoryDAO extends HibernateDaoSupport {
	@Transactional(propagation=Propagation.REQUIRED)
	public ItemHistory save(ItemHistory transientInstance) {
		try {
			getHibernateTemplate().saveOrUpdate(transientInstance);
			return transientInstance;
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
