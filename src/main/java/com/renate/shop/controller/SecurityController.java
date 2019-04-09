package com.renate.shop.controller;

import java.security.Principal;

import com.renate.shop.model.User;
import com.renate.shop.service.query.UserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

	@Autowired
	private UserQuery userQuery;

	@GetMapping(
			value = "/api/v1/users/me",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<User> getCurrentUser(Principal principal) {
		User user = this.userQuery.getUser(principal.getName());
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
}
