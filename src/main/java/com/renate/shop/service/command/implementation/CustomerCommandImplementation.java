package com.renate.shop.service.command.implementation;

import com.renate.shop.exception.ConflictException;
import com.renate.shop.exception.NotFoundException;
import com.renate.shop.model.Customer;
import com.renate.shop.repository.CustomerRepository;
import com.renate.shop.service.command.CustomerCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerCommandImplementation implements CustomerCommand {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public Customer createCustomer(Customer customer) {
		if (this.customerRepository.findByTelephone(customer.getTelephone()) == null) {
			return this.customerRepository.save(customer);
		}
		throw ConflictException.create("A customer exist with the give telephone");
	}

	@Override
	public Customer updateCustomer(Customer customer) {
		if (this.customerRepository.getOne(customer.getId()) != null) {
			return this.customerRepository.save(customer);
		}
		throw NotFoundException.create("Customer account does not exist");
	}
}
