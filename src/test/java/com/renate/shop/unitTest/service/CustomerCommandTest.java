package com.renate.shop.unitTest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.renate.shop.exception.ConflictException;
import com.renate.shop.exception.NotFoundException;
import com.renate.shop.generator.CustomerGenerator;
import com.renate.shop.model.Customer;
import com.renate.shop.repository.CustomerRepository;
import com.renate.shop.service.command.CustomerCommand;
import com.renate.shop.service.command.implementation.CustomerCommandImplementation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CustomerCommandTest {

	@TestConfiguration
	static class CustomerCommandTestConfigeuration {
		@Bean
		public CustomerCommand customerQuery() {
			return new CustomerCommandImplementation();
		}
	}

	@MockBean
	private CustomerRepository customerRepository;

	@Autowired
	private CustomerCommand customerCommand;

	@Test
	public void createCustomer_returnsNewlyCreatedCustomer() {
		Customer customer = CustomerGenerator.generateCustomer();
		given(this.customerRepository.save(customer)).willReturn(customer);

		Customer newCustomer = this.customerCommand.createCustomer(customer);

		assertThat(newCustomer).isEqualTo(customer);
	}

	@Test(expected = ConflictException.class)
	public void createCustomerWithExistingTelephone_throwsConflictRequestException() {
		Customer customer = CustomerGenerator.generateCustomer();
		Customer existingCustomer = CustomerGenerator.generateCustomer();
		existingCustomer.setTelephone(customer.getTelephone());
		given(this.customerRepository.findByTelephone(anyString())).willReturn(existingCustomer);
		given(this.customerRepository.save(customer)).willReturn(customer);

		this.customerCommand.createCustomer(customer);
	}

	@Test
	public void updateCustomer_returnsUpdatedUser() {
		Customer customer = CustomerGenerator.generateCustomer();
		given(this.customerRepository.save(customer)).willReturn(customer);
		given(this.customerRepository.getOne(customer.getId())).willReturn(customer);

		Customer updatedCustomer = this.customerCommand.updateCustomer(customer);

		assertThat(updatedCustomer).isEqualTo(customer);
	}

	@Test(expected = NotFoundException.class)
	public void updateCustomerWithNonExistingAccount_throwsNotFoundRequestException() {
		Customer customer = CustomerGenerator.generateCustomer();
		given(this.customerRepository.getOne(customer.getId())).willReturn(null);

		this.customerCommand.updateCustomer(customer);
	}

}
