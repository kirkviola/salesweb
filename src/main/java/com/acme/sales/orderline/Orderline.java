package com.acme.sales.orderline;

import javax.persistence.*;

import com.acme.sales.order.Order;

@Entity(name="orderlines")
public class Orderline {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int id;
	@Column(length=30, nullable=false)
	String product;
	@Column(nullable=false)
	int quantity;
	@Column(nullable=false)
	int price;
	
	@ManyToOne
	@JoinColumn(name="orderId")
	public Order order;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	} 
	
	
}
