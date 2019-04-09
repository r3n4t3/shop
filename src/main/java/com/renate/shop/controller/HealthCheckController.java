package com.renate.shop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

	@GetMapping(
			value = "/healthCheck",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<HttpStatus> healthCheck() {
		return new ResponseEntity(HttpStatus.OK);
	}

}
