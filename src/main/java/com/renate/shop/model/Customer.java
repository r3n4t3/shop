package com.renate.shop.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Customer {

	@Id
	@GeneratedValue
	private Long id;
	@NotNull
	private String firstName;
	@NotNull
	private String lastName;
	@NotNull
	private String telephone;
	private String address;
	private String email;

	public Customer() {
	}

	public Customer(@NotNull String firstName, @NotNull String lastName,
			@NotNull String telephone, String address, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.telephone = telephone;
		this.address = address;
		this.email = email;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
