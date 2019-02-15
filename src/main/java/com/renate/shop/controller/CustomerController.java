package com.renate.shop.controller;

import com.renate.shop.model.Customer;
import com.renate.shop.service.command.CustomerCommand;
import com.renate.shop.service.query.CustomerQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

	@Autowired
	private CustomerCommand customerCommand;

	@Autowired
	private CustomerQuery customerQuery;

	@PostMapping(
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
		Customer newCustomer = this.customerCommand.createCustomer(customer);
		return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
	}

	@PutMapping(
			path = "/{customerId}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Customer> putCustomer(@RequestBody Customer customer,
			@PathVariable("customerId") Long id) {
		if (id.compareTo(customer.getId()) == 0) {
			Customer updatedCustomer = this.customerCommand.updateCustomer(customer);
			return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping(
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Page<Customer>> getCustomers(@RequestParam(value="page", required = false)Integer page,
			@RequestParam(value="size", required = false)Integer size) {
		Page<Customer> customers = this.customerQuery.getCustomers(page, size);
		return new ResponseEntity<>(customers, HttpStatus.OK);
	}

	@GetMapping(
			value = "/{customerId}",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Customer> getCustomer(@PathVariable("customerId") Long id) {
		Customer customer = this.customerQuery.getCustomer(id);
		if (customer != null) {
			return new ResponseEntity<>(customer, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
