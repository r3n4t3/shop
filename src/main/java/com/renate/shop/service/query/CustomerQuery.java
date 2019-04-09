package com.renate.shop.service.query;

import java.util.Optional;

import com.renate.shop.model.Customer;
import org.springframework.data.domain.Page;

public interface CustomerQuery {

	Page<Customer> getCustomers(Integer page, Integer size);
	Optional<Customer> getCustomer(Long customerId);

}
