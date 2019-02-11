package com.renate.shop.service.command;

import com.renate.shop.model.Customer;

public interface CustomerCommand {

	Customer createCustomer(Customer customer);
	Customer updateCustomer(Customer customer);

}
