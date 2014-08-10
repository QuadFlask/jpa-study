package kr.ac.jejuuniv;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.ac.jejuuniv.config.WebMvcConfig;
import kr.ac.jejuuniv.domain.model.Item;
import kr.ac.jejuuniv.domain.model.Order;
import kr.ac.jejuuniv.domain.model.OrderRepository;
import kr.ac.jejuuniv.domain.model.QOrder;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsNot.*;
import static org.hamcrest.core.IsNull.*;
import static org.hamcrest.core.IsAnything.*;

import org.hibernate.Hibernate;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPAQuery;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebMvcConfig.class)
@WebAppConfiguration
@Transactional
public class OrderTest {

	@PersistenceContext
	private EntityManager entityManager;

	@Resource
	private OrderRepository orderRepository;

	QOrder qorder = QOrder.order;

	@Test
	public void order() {
		List<Item> items = new ArrayList<Item>();
		Item apple = new Item("apple", 100, 3);
		Item banana = new Item("banana", 20, 10);

		items.add(apple);
		items.add(banana);
		Order entity = new Order("flask", items);
		orderRepository.save(entity);

		List<Order> orders = orderRepository.findAll();
		assertEquals(1, orders.size());
		Order order = orders.get(0);
		assertEquals("flask", order.getCustomer());
		assertEquals(2, order.getItems().size());
		assertTrue(order.getItems().contains(apple));
		assertTrue(order.getItems().contains(banana));

		Item next2 = order.getItems().iterator().next();
		next2.setPrice(999999);
		orderRepository.save(order);

		order.getItems().add(new Item("mango", 200, 2));
		orders = orderRepository.findAll();
		order = orders.get(0);
		assertEquals(3, order.getItems().size());
	}

	@Test
	public void addOrderTest() {
		List<Item> items = new ArrayList<Item>();
		items.add(new Item("Apple", 100, 1));
		items.add(new Item("Banana", 150, 2));
		items.add(new Item("Guava", 400, 5));
		Order order = new Order("flask", items);
		orderRepository.save(order);

		Order insertedOrder = orderRepository.findOne(order.getId());
		assertThat(insertedOrder.getCustomer(), is("flask"));
		assertThat(insertedOrder.getItems().size(), is(3));
		ArrayList<Item> insertedItems = new ArrayList<Item>(insertedOrder.getItems());
		for (Item item : items)
			assertThat(insertedItems.contains(item), is(true));
	}

	@Test
	public void addAndChangeTest() {
		List<Item> items = new ArrayList<Item>();
		Item apple = new Item("Apple", 100, 1);
		items.add(apple);
		items.add(new Item("Banana", 150, 2));
		items.add(new Item("Guava", 400, 5));
		Order order = new Order("flask", items);
		orderRepository.save(order);

		apple.setPrice(200);
		apple.setQuantity(2);

		Order insertedOrder = orderRepository.findOne(order.getId());
		ArrayList<Item> insertedItems = new ArrayList<Item>(insertedOrder.getItems());
		Item insertedApple = null;
		for (Item item : insertedItems) {
			if (item.getProduct().equals("Apple")) {
				insertedApple = item;
				break;
			}
		}

		assertThat(insertedApple, is(notNullValue()));
		assertThat((int) insertedApple.getPrice(), is((int) 200));
		assertThat((int) insertedApple.getQuantity(), is(2));
	}

	@Test
	public void jpaFetchTest() {
		List<Item> items = new ArrayList<Item>();
		Item apple = new Item("Apple", 100, 1);
		items.add(apple);
		items.add(new Item("Banana", 150, 2));
		items.add(new Item("Guava", 400, 5));
		Order order = new Order("flask", items);
		orderRepository.save(order);

		JPAQuery jpaQuery = new JPAQuery(entityManager);
		Order insertedOrder = jpaQuery.from(qorder).where(qorder.items.size().gt(1)).uniqueResult(qorder);

		assertNotNull(insertedOrder);
		assertEquals(3, insertedOrder.getItems().size());
		Item insertedApple = null;
		for (Item item : insertedOrder.getItems()) {
			if (item.getProduct().equals("Apple")) {
				insertedApple = item;
				break;
			}
		}

		assertThat(insertedApple, is(notNullValue()));
		assertEquals(100, (int) insertedApple.getPrice());
	}
}
