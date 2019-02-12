package com.renate.shop.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class Category {

	@Id
	@GeneratedValue
	private Long id;
	@NotNull
	@Column(nullable = false, unique = true)
	private String name;
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private Set<Item> items;

	public Category() {
	}

	public Category(Long id, @NotNull String name) {
		this.id = id;
		this.name = name;
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
}
