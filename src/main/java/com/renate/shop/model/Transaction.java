package com.renate.shop.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Transaction {

	@Id
	@GeneratedValue
	Long id;
	@NotNull
	private Integer quantity;
	@NotNull
	@ManyToOne
	@JoinColumn(name = "item_id", nullable = false)
	Item item;
	@NotNull
	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	Customer customer;
	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	User user;
	Date created;

	public Transaction() {
	}

	public Transaction(Long id, @NotNull Integer quantity, @NotNull Item item,
			@NotNull Customer customer, @NotNull User user) {
		this.id = id;
		this.quantity = quantity;
		this.item = item;
		this.customer = customer;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
}
