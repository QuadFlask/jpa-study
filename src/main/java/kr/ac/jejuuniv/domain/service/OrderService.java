package kr.ac.jejuuniv.domain.service;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.ac.jejuuniv.domain.model.Order;
import kr.ac.jejuuniv.domain.model.OrderRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
	@PersistenceContext
	private EntityManager entityManager;

	@Resource
	private OrderRepository orderRepository;

	public List<Order> findAllOrders() {
		return orderRepository.findAll();
	}

	@Transactional
	public void addOrder(Order order) {
		orderRepository.save(order);
	}

	public List<Order> findOrderByCustomer(String customer) {
		return orderRepository.findOrderByCustomer(customer);
	}

	public List<Order> findOrderWhichContainsProduct(String product) {
		return orderRepository.findOrderWhichContainsProduct(product);
	}

}
