package com.renate.shop.generator;

import java.util.Random;

import com.renate.shop.model.Customer;
import org.apache.commons.lang3.RandomStringUtils;

public class CustomerGenerator {

	public static Customer generateCustomer()  {
		Long id = new Random().nextLong();
		String firstName = RandomStringUtils.random(7, true, true);
		String lastName = RandomStringUtils.random(7, true, true);
		String telephone = RandomStringUtils.random(9, false, true);
		String address = RandomStringUtils.random(15, true, true);
		String email = RandomStringUtils.random(10, true, true).concat("@mail.com");
		Customer customer = new Customer(firstName, lastName, telephone, address, email);
		customer.setId(id);
		return customer;
	}

}
