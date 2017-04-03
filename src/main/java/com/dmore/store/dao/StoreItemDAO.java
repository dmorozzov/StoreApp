package com.dmore.store.dao;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dmore.store.entity.AbstractStoreItem;

@Repository
public class StoreItemDAO {

	@Autowired
	SessionFactory sessionFactory;

	public void save(AbstractStoreItem entity) throws Exception {
		Session session = sessionFactory.openSession();
		session.getTransaction().begin();
		session.save(entity);
		session.getTransaction().commit();
		session.close();
	}

	public void update(AbstractStoreItem entity) throws Exception {
		Session session = sessionFactory.openSession();
		session.getTransaction().begin();
		session.update(entity);
		session.getTransaction().commit();
		session.close();
	}

	public AbstractStoreItem findSame(AbstractStoreItem source) {
		Session session = sessionFactory.openSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

		CriteriaQuery<AbstractStoreItem> criteriaQuery = criteriaBuilder.createQuery(AbstractStoreItem.class);
		Root<AbstractStoreItem> c = criteriaQuery.from(AbstractStoreItem.class);
		ParameterExpression<Long> serialParameter = criteriaBuilder.parameter(Long.class);
		ParameterExpression<String> manufacturerParameter = criteriaBuilder.parameter(String.class);
		criteriaQuery.select(c).
			where(criteriaBuilder.equal(c.get("serialId"), serialParameter), criteriaBuilder.equal(c.get("manufacturer"), manufacturerParameter));

		TypedQuery<AbstractStoreItem> query = session.createQuery(criteriaQuery);
		query.setParameter(serialParameter, source.getSerialId());
		query.setParameter(manufacturerParameter, source.getManufacturer());
		List<AbstractStoreItem> result = query.getResultList();

		session.close();
		return !result.isEmpty() ? result.get(0) : null;
	}

	public AbstractStoreItem findOne(Long primaryKey) throws Exception {
		Session session = sessionFactory.openSession();
		AbstractStoreItem foundedBasicStoreItem = session.get(AbstractStoreItem.class, primaryKey);
		session.close();
		return foundedBasicStoreItem;
	}

	public List<AbstractStoreItem> findAll() {
		Session session = sessionFactory.openSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

		CriteriaQuery<AbstractStoreItem> criteriaQuery = criteriaBuilder.createQuery(AbstractStoreItem.class);
		Root<AbstractStoreItem> c = criteriaQuery.from(AbstractStoreItem.class);
		criteriaQuery.select(c);

		TypedQuery<AbstractStoreItem> query = session.createQuery(criteriaQuery);
		List<AbstractStoreItem> result = query.getResultList();

		session.close();
		return result;
	}

}
