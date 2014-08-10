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

@Entity
@Table(name = "T_ORDER")
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public Collection<Item> getItems() {
		return items;
	}

	public void setItems(Collection<Item> items) {
		this.items = items;
	}

}
