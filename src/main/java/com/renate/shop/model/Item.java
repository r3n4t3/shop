package com.renate.shop.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class Item {

	@Id
	@GeneratedValue
	private Long id;
	@NotNull
	@Column(unique = true, nullable = false)
	private String name;
	@NotNull
	private Integer quantity;
	@NotNull
	private Float price;
	@NotNull
	@ManyToOne
	@JoinColumn(name = "category_id", referencedColumnName = "id")
	private Category category;
	@OneToMany(mappedBy = "item")
	private Set<Transaction> transactions;

	public Item() {
	}

	public Item(Long id, @NotNull String name, @NotNull Integer quantity, @NotNull Float price,
			@NotNull Category category) {
		this.id = id;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
		this.category = category;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
}
