package com.renate.shop.unitTest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.renate.shop.exception.BadRequestException;
import com.renate.shop.generator.CustomerGenerator;
import com.renate.shop.model.Customer;
import com.renate.shop.repository.CustomerRepository;
import com.renate.shop.service.query.CustomerQuery;
import com.renate.shop.service.query.implementation.CustomerQueryImplementation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CustomerQueryTest {

	@TestConfiguration
	static class CustomerQueryTestConfiguration {
		@Bean
		public CustomerQuery customerQuery() {
			return new CustomerQueryImplementation();
		}
	}

	@MockBean
	private CustomerRepository customerRepository;

	@Autowired
	private CustomerQuery customerQuery;

	@Test
	public void getCustomersWithPageParams_returnsAPageOfCustomers() {
		List<Customer> customers = new ArrayList<>();
		customers.add(CustomerGenerator.generateCustomer());
		customers.add(CustomerGenerator.generateCustomer());
		given(this.customerRepository.findAll(PageRequest.of(0,2)))
				.willReturn(new PageImpl<>(customers));

		Page<Customer> returnedCustomer = this.customerQuery.getCustomers(0, 2);

		assertThat(returnedCustomer).isEqualTo(new PageImpl<>(customers));
	}

	@Test
	public void getCustomersWithOutPageParams_returnsAPageOfCustomers() {
		List<Customer> customers = new ArrayList<>();
		customers.add(CustomerGenerator.generateCustomer());
		customers.add(CustomerGenerator.generateCustomer());
		given(this.customerRepository.findAll(PageRequest.of(0,10)))
				.willReturn(new PageImpl<>(customers));

		Page<Customer> returnedCustomer = this.customerQuery.getCustomers(null, null);

		assertThat(returnedCustomer).isEqualTo(new PageImpl<>(customers));
	}

	@Test(expected = BadRequestException.class)
	public void getCustomersWithInValidPageParams_throwsBadRequestException() {
		List<Customer> customers = new ArrayList<>();
		customers.add(CustomerGenerator.generateCustomer());
		customers.add(CustomerGenerator.generateCustomer());
		given(this.customerRepository.findAll(PageRequest.of(0,10)))
				.willReturn(new PageImpl<>(customers));

		this.customerQuery.getCustomers(-1, null);
	}

	@Test
	public void getCustomer_returnsExistingCustomer() {
		Customer customer = CustomerGenerator.generateCustomer();
		given(this.customerRepository.findById(customer.getId()))
				.willReturn(Optional.of(customer));

		Optional<Customer> gottenCustomer = this.customerQuery.getCustomer(customer.getId());

		assertThat(gottenCustomer.get()).isEqualTo(customer);
	}

	@Test
	public void getCustomerWithNotExistingAccount_returnsNull() {
		Customer customer = CustomerGenerator.generateCustomer();
		given(this.customerRepository.getOne(customer.getId())).willReturn(null);

		Optional<Customer> gottenCustomer = this.customerQuery.getCustomer(customer.getId());

		assertThat(gottenCustomer.isPresent()).isEqualTo(false);
	}
}
