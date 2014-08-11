package kr.ac.jejuuniv;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kr.ac.jejuuniv.config.WebMvcConfig;
import kr.ac.jejuuniv.domain.model.Item;
import kr.ac.jejuuniv.domain.model.Order;
import kr.ac.jejuuniv.domain.service.OrderService;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsNot.*;
import static org.hamcrest.core.IsNull.*;
import static org.hamcrest.core.IsAnything.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebMvcConfig.class)
@WebAppConfiguration
public class OrderServiceTest {

	@Autowired
	private OrderService orderService;

	private Item apple = new Item("Apple", 100, 1);
	private Item banana = new Item("Banana", 150, 2);
	private Item cucumber = new Item("Cucumber", 400, 5);

	@Test
	@Transactional
	public void findAllOrders() {
		List<Order> orders = orderService.findAllOrders();
		assertNotNull(orders);
		assertTrue(orders.isEmpty());
	}

	@Test
	@Transactional
	public void addOrder() {
		Order order = createSampleOrder("flask");
		orderService.addOrder(order);

		List<Order> orders = orderService.findAllOrders();
		assertNotNull(orders);
		assertThat(orders.size(), is(1));
	}

	@Test
	@Transactional
	public void findOrderByCustomer() {
		List<Item> items2 = new ArrayList<Item>();
		items2.add(apple.clone());

		Order order1 = createSampleOrder("flask1");
		orderService.addOrder(order1);
		Order order2 = createSampleOrder("flask2");
		order2.getItems().add(banana.clone());

		orderService.addOrder(order2);

		List<Order> orders = orderService.findOrderByCustomer("flask2");
		assertThat(orders.size(), is(1));

		Order order = orders.get(0);
		List<Item> items = new ArrayList<Item>(order.getItems());
		assertThat(items.size(), is(2));
		assertThat(order.getCustomer(), is("flask2"));

		assertThat(items.contains(banana), is(true));
	}

	@Test
	@Transactional
	public void findOrderWhichContainsProduct() {
		Order order1 = createSampleOrder("flask1");
		Order order2 = createSampleOrder("flask2");
		order2.getItems().add(banana.clone());
		order2.getItems().add(cucumber.clone());

		orderService.addOrder(order1);
		orderService.addOrder(order2);

		List<Order> orders = orderService.findOrderWhichContainsProduct("banana");
		assertThat(orders.size(), is(1));

		Order order = orders.get(0);
		List<Item> items = new ArrayList<Item>(order.getItems());
		assertThat(items.size(), is(3));
		assertThat(order.getCustomer(), is("flask2"));

		assertThat(items.contains(banana), is(true));

		// /////////////

		orders = orderService.findOrderWhichContainsProduct("cucumber");
		assertThat(orders.size(), is(1));

		order = orders.get(0);
		items = new ArrayList<Item>(order.getItems());
		assertThat(items.size(), is(3));
		assertThat(order.getCustomer(), is("flask2"));

		assertThat(items.contains(cucumber), is(true));
	}

	private Order createSampleOrder(String customer) {
		List<Item> items = new ArrayList<Item>();
		items.add(apple.clone());
		return new Order(customer, items);
	}

}
