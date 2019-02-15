package com.renate.shop.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.renate.shop.generator.CustomerGenerator;
import com.renate.shop.generator.JSONConvertor;
import com.renate.shop.model.Customer;
import com.renate.shop.repository.UserRepository;
import com.renate.shop.service.command.CustomerCommand;
import com.renate.shop.service.query.CustomerQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CustomerController.class, secure = false)
public class CustomerControllerTest {

	@MockBean
	private CustomerCommand customerCommand;

	@MockBean
	private CustomerQuery customerQuery;

	@MockBean
	private UserRepository userRepository;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void createCustomerRequest_returnsHTTP201AndCreatedCustomer() throws Exception {
		Customer customer = CustomerGenerator.generateCustomer();
		given(this.customerCommand.createCustomer(any(Customer.class))).willReturn(customer);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customers")
				.content(JSONConvertor.toJSON(customer))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("telephone").value(customer.getTelephone()));
	}

	@Test
	public void updateCustomerRequest_returnsHTTP200AndUpdatedCustomer() throws Exception {
		Customer customer = CustomerGenerator.generateCustomer();
		given(this.customerCommand.updateCustomer(any(Customer.class))).willReturn(customer);

		this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/customers/" + customer.getId())
				.content(JSONConvertor.toJSON(customer))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(jsonPath("id").value(customer.getId()))
			.andExpect(jsonPath("telephone").value(customer.getTelephone()));
	}

	@Test
	public void updateCustomerRequestWithMisMatchId_returnsHTTP404() throws Exception {
		Customer customer = CustomerGenerator.generateCustomer();
		Long diffCustomerId = new Random().nextLong();
		given(this.customerCommand.updateCustomer(any(Customer.class))).willReturn(customer);

		this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/customers/" + diffCustomerId)
				.content(JSONConvertor.toJSON(customer))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isNotFound());
	}

	@Test
	public void getCustomersRequestWithPageParams_returnsHTTP200AndAPageOfCustomers() throws Exception {
		List<Customer> customers = new ArrayList<>();
		customers.add(CustomerGenerator.generateCustomer());
		customers.add(CustomerGenerator.generateCustomer());
		Page<Customer> pagedCustomers = new PageImpl<>(customers);
		given(this.customerQuery.getCustomers(0, 2)).willReturn(pagedCustomers);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers?page=0&size=2")
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(jsonPath("content").isArray())
			.andExpect(jsonPath("number").value(0));
	}

	@Test
	public void getCustomersRequestWithNoPageParams_returnsHTTP200AndAPageOfCustomers() throws Exception {
		List<Customer> customers = new ArrayList<>();
		customers.add(CustomerGenerator.generateCustomer());
		customers.add(CustomerGenerator.generateCustomer());
		Page<Customer> pagedCustomers = new PageImpl<>(customers);
		given(this.customerQuery.getCustomers(null, null)).willReturn(pagedCustomers);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers")
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(jsonPath("content").isArray())
			.andExpect(jsonPath("numberOfElements").value(2));
	}

	@Test
	public void getCustomerRequest_returnsHTTP200AndACustomer() throws Exception {
		Customer customer = CustomerGenerator.generateCustomer();
		given(this.customerQuery.getCustomer(customer.getId())).willReturn(customer);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/" + customer.getId())
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(jsonPath("id").value(customer.getId()))
			.andExpect(jsonPath("telephone").value(customer.getTelephone()));
	}

	@Test
	public void getNonExistingCustomerRequest_returnsHTTP404() throws Exception {
		Long customerId = new Random().nextLong();
		given(this.customerQuery.getCustomer(customerId)).willReturn(null);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/" + customerId)
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isNotFound());
	}
}
