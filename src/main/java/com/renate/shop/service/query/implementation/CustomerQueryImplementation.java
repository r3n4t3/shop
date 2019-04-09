package com.renate.shop.service.query.implementation;

import java.util.Map;
import java.util.Optional;

import com.renate.shop.model.Customer;
import com.renate.shop.repository.CustomerRepository;
import com.renate.shop.service.query.CustomerQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CustomerQueryImplementation implements CustomerQuery {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public Page<Customer> getCustomers(Integer page, Integer size) {
		Map<String, Integer> pageAndSize = PageValidator.validatePageAndSize(page, size);
		return this.customerRepository
				.findAll(PageRequest.of(pageAndSize.get("page"), pageAndSize.get("size")));
	}

	@Override
	public Optional<Customer> getCustomer(Long customerId) {
		return this.customerRepository.findById(customerId);
	}
}
