package kr.ac.jejuuniv.domain.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface OrderRepository extends JpaRepository<Order, Long>, QueryDslPredicateExecutor<Order> {

}
