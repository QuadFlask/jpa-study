package kr.ac.jejuuniv;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.ac.jejuuniv.config.WebMvcConfig;
import kr.ac.jejuuniv.domain.model.Item;
import kr.ac.jejuuniv.domain.model.Order;
import kr.ac.jejuuniv.domain.model.OrderRepository;
import kr.ac.jejuuniv.domain.model.QItem;
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

	private QOrder qorder = QOrder.order;

	private Item apple = new Item("Apple", 100, 1);
	private Item banana = new Item("Banana", 150, 2);
	private Item cucumber = new Item("Cucumber", 400, 5);

	@Test
	public void orderTotalPrice() {
		Order createSampleOrder = createSampleOrder();
		assertThat((int)createSampleOrder.getTotalPrice(), is(2400));
	}

	@Test
	public void addOrderTest() {
		List<Item> items = new LinkedList<Item>();
		items.add(apple.clone());
		items.add(banana.clone());
		items.add(cucumber.clone());
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
		Order order = createSampleOrder();
		Item durian = new Item("Durian", 1000, 2);
		Item clonedDurian = durian.clone();
		order.getItems().add(clonedDurian);
		orderRepository.save(order);

		clonedDurian.setPrice(1200);
		clonedDurian.setQuantity(3);

		Order insertedOrder = orderRepository.findOne(order.getId());
		Item insertedItem = findItemByProductName(insertedOrder, "Durian");

		assertNotNull(insertedItem);
		assertThat((int) insertedItem.getPrice(), is((int) 1200)); // ? WTF?
		assertThat((int) insertedItem.getQuantity(), is(3));
	}

	@Test
	public void queryDslTest() {
		Order order = createSampleOrder();
		orderRepository.save(order);

		JPAQuery jpaQuery = new JPAQuery(entityManager);
		Order insertedOrder = jpaQuery.from(qorder).where(qorder.items.size().gt(1)).uniqueResult(qorder);

		assertNotNull(insertedOrder);
		assertEquals(3, insertedOrder.getItems().size());
		Item insertedApple = findItemByProductName(insertedOrder, "Apple");

		assertNotNull(insertedApple);
		assertEquals(100, (int) insertedApple.getPrice());
	}

	@Test
	public void queryDslTest2() {
		Order order = createSampleOrder();
		orderRepository.save(order);
		List<Item> items = new ArrayList<Item>();
		items.add(apple.clone());
		items.add(banana.clone());
		orderRepository.save(new Order("flask", items));
		assertThat(orderRepository.findAll().size(), is(2));

		JPAQuery jpaQuery = new JPAQuery(entityManager);
		List<Order> insertedOrders = jpaQuery.from(qorder).where(qorder.items.any().product.eq("Cucumber"))
				.list(qorder);
		// items 에 Cucumber가 있는 Order 조회

		assertNotNull(insertedOrders);
		assertThat(insertedOrders.size(), is(1));
		assertEquals(3, insertedOrders.get(0).getItems().size());
		Item insertedCucumber = findItemByProductName(insertedOrders.get(0), "Cucumber");
		assertNotNull(insertedCucumber);
		assertEquals(400, (int) insertedCucumber.getPrice());

//		jpaQuery = new JPAQuery(entityManager);
//		List<Order> insertedOrders2 = jpaQuery.from(qorder).where(qorder.items.any().product.ne("Cucumber"))
//				.list(qorder);
		// items 에 Cucumber가 없는 Order 조회 가 아니지.
//		assertNotNull(insertedOrders2);
//		assertThat(insertedOrders2.size(), is(1));
//		assertEquals(2, insertedOrders2.get(0).getItems().size());
	}

	private Order createSampleOrder() {
		List<Item> items = new ArrayList<Item>();
		items.add(apple.clone());
		items.add(banana.clone());
		items.add(cucumber.clone());
		return new Order("flask", items);
	}

	private Item findItemByProductName(Order order, String product) {
		for (Item item : order.getItems())
			if (item.getProduct().equals(product))
				return item;
		return null;
	}

	// @Test
	// public void order() {
	// List<Item> items = new ArrayList<Item>();
	// Item apple = new Item("apple", 100, 3);
	// Item banana = new Item("banana", 20, 10);
	//
	// items.add(apple);
	// items.add(banana);
	// Order entity = new Order("flask", items);
	// orderRepository.save(entity);
	//
	// List<Order> orders = orderRepository.findAll();
	// assertEquals(1, orders.size());
	// Order order = orders.get(0);
	// assertEquals("flask", order.getCustomer());
	// assertEquals(2, order.getItems().size());
	// assertTrue(order.getItems().contains(apple));
	// assertTrue(order.getItems().contains(banana));
	//
	// Item next2 = order.getItems().iterator().next();
	// next2.setPrice(999999);
	// orderRepository.save(order);
	//
	// order.getItems().add(new Item("mango", 200, 2));
	// orders = orderRepository.findAll();
	// order = orders.get(0);
	// assertEquals(3, order.getItems().size());
	// }
}
