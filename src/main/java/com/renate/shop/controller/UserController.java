package com.renate.shop.controller;

import java.util.Optional;

import com.renate.shop.model.User;
import com.renate.shop.service.command.UserCommand;
import com.renate.shop.service.query.UserQuery;
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
@RequestMapping("/api/v1/users")
public class UserController {

	@Autowired
	private UserCommand userCommand;

	@Autowired
	private UserQuery userQuery;

	@PostMapping(
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<User> createUser(@RequestBody User user) {
		User newUser = this.userCommand.createUser(user);
		return new ResponseEntity<>(newUser, HttpStatus.CREATED);
	}

	@PutMapping(
			value = "/{userId}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable("userId") Long userId) {
		if (user.getId().compareTo(userId) == 0) {
			User updatedUser = this.userCommand.updateUser(user);
			return new ResponseEntity<>(updatedUser, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping(
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Page<User>> getUsers(@RequestParam(value = "size", required = false)Integer size,
			@RequestParam(value = "page", required = false)Integer page ) {
		Page<User> users = this.userQuery.getUsers(page, size);
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@GetMapping(
			value = "/{userId}",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<User> getUser(@PathVariable("userId") Long userId) {
		Optional<User> user = this.userQuery.getUser(userId);
		if (user.isPresent()) {
			return new ResponseEntity<>(user.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
