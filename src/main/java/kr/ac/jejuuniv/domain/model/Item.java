package kr.ac.jejuuniv.domain.model;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "items")
@Table(name = "items")
public class Item implements Cloneable {
	@Id
	@GeneratedValue
	private Long id;
	private String product;
	@ManyToOne
	private Order order;
	private double price;
	private int quantity;
	@Transient
	private double totalPrice;
	@Enumerated
	private ItemType type = ItemType.FOOD;

	public Item(String product, double price, int quantity) {
		this.product = product;
		this.price = price;
		this.quantity = quantity;
		this.setTotalPrice(price * quantity);
	}

	public Item() {
	}

	@Override
	public Item clone(){
		Object clone;
		try {
			clone = super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
		Item c = (Item) clone;
		c.price = price;
		c.quantity = quantity;
		c.type = type;
		return c;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", product=" + product + ", order=" + order.getId() + ", price=" + price + ", quantity="
				+ quantity + ", totalPrice=" + totalPrice + ", type=" + type + "]";
	}
	
	

}
