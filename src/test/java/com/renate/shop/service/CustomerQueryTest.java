package com.renate.shop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

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
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CustomerQueryTest {

	@TestConfiguration
	static class CustomerQueryTestConfigeuration {
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
	public void createCustomer_returnsNewlyCreatedCustomer() {
		Customer customer = CustomerGenerator.generateCustomer();
		given(this.customerRepository.save(customer)).willReturn(customer);

		Customer newCustomer = this.customerQuery.createCustomer(customer);

		assertThat(newCustomer).isEqualTo(customer);
	}

}
