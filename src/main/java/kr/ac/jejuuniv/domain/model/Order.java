package kr.ac.jejuuniv.domain.model;

import java.util.Collection;
import java.util.LinkedHashSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "orders")
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue
	private Long id;

	private String customer;

	@OneToMany(cascade = CascadeType.ALL)
	// , fetch=FetchType.EAGER
	@JoinColumn(name = "ORDER_ID")
	private Collection<Item> items = new LinkedHashSet<Item>();

	public Order() {
	}

	public Order(String customer, Collection<Item> items) {
		this.customer = customer;
		this.items = items;
		for (Item i : items)
			i.setOrder(this);
	}

	@Transient
	public double getTotalPrice() {
		double total = 0.0;
		for (Item item : items)
			total += item.getTotalPrice();
		return total;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", customer=" + customer + ", items=" + items + "]";
	}

}
