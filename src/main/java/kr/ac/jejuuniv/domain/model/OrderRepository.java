package kr.ac.jejuuniv.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface OrderRepository extends JpaRepository<Order, Long>, QueryDslPredicateExecutor<Order> {

	@Query("SELECT o FROM orders o WHERE o.customer=?1")
	public List<Order> findOrderByCustomer(String customer);

	@Query("SELECT o FROM orders o join o.items i where i.product=?1")
	public List<Order> findOrderWhichContainsProduct(String product);

}
