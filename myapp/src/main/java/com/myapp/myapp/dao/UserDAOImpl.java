package com.myapp.myapp.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.myapp.myapp.entity.Concert;
import com.myapp.myapp.entity.Customer;
import com.myapp.myapp.entity.MyUser;
import com.myapp.myapp.entity.Order;

@Repository
public class UserDAOImpl implements UserDAO {

	private EntityManager em;
	
	@Autowired
	private OrderDAO oDAO;
	
	@Autowired
	public UserDAOImpl(EntityManager em) {
		this.em = em;
	}
	
	@Override
	@Transactional
	public List<MyUser> findAll() {
		Session currentSession = em.unwrap(Session.class);
		
		Query<MyUser> query = currentSession.createQuery("from User", MyUser.class);
		
		List<MyUser> users = query.getResultList();
		
		return users;
	}

	@Override
	@Transactional
	public MyUser getUserById(String id) {
		Session currentSession = em.unwrap(Session.class);
		
		MyUser user = currentSession.get(MyUser.class, id);
		
		return user;
	}

	@Override
	@Transactional
	public HashMap<Integer,Concert> getConcertsByUserId(String id) {
		
		System.out.println(id);
		
		MyUser user = getUserById(id);
		Customer customer = user.getCustomer();
		List<Order> orders = oDAO.getOrdersByCustomerId(customer.getId()); 	
		
		HashMap<Integer, Concert> orderConcertPair = new HashMap<>();
		
		orders.forEach(order -> {
			orderConcertPair.put(order.getId(), order.getConcert());
		});
		
		
		
		return orderConcertPair;
	}
	
	@Override
	@Transactional
	public void update(MyUser theUser) {
		Session currentSession = em.unwrap(Session.class);
		currentSession.update(theUser);
	}
	
	@Override
	@Transactional
	public void save(MyUser theUser) {
		Session currentSession = em.unwrap(Session.class);
		currentSession.save(theUser);
	}

}
