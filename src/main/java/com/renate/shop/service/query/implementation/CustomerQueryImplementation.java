package com.renate.shop.service.query.implementation;

import com.renate.shop.model.Customer;
import com.renate.shop.repository.CustomerRepository;
import com.renate.shop.service.query.CustomerQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerQueryImplementation implements CustomerQuery {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public Customer createCustomer(Customer customer) {
		return this.customerRepository.save(customer);
	}
}
