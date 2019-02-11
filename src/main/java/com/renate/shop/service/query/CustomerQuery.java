package com.renate.shop.service.query;

import com.renate.shop.model.Customer;
import org.springframework.data.domain.Page;

public interface CustomerQuery {

	Page<Customer> getCustomers(Integer page, Integer size);
	Customer getCustomer(Long customerId);

}
