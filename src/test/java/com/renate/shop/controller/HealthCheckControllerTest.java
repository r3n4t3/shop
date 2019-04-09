package com.renate.shop.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(value = HealthCheckController.class, secure = false)
public class HealthCheckControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void whenHealthCheckGetRequestIsMade_Http200IsReturned() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/healthCheck")
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk());
	}

}
