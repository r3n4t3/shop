package com.renate.shop.unitTest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.renate.shop.controller.UserController;
import com.renate.shop.generator.JSONConvertor;
import com.renate.shop.generator.UserGenerator;
import com.renate.shop.model.User;
import com.renate.shop.service.command.UserCommand;
import com.renate.shop.service.query.UserQuery;
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
@WebMvcTest(value = UserController.class, secure = false)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserCommand userCommand;

	@MockBean
	private UserQuery userQuery;

	@Test
	public void createUserRequest_returnsHttp201AndCreatedItem() throws Exception {
		User user = UserGenerator.generateUser();
		given(this.userCommand.createUser(any(User.class))).willReturn(user);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(JSONConvertor.toJSON(user)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("username").value(user.getUsername()));
	}

	@Test
	public void updateUserRequest_returnsHttp200AndUpdatedUser() throws Exception {
		User user = UserGenerator.generateUser();
		given(this.userCommand.updateUser(any(User.class))).willReturn(user);

		this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/" + user.getId())
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(JSONConvertor.toJSON(user)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("username").value(user.getUsername()))	;
	}

	@Test
	public void updateUserRequestWithMisMatchID_returnsHttp200AndUpdatedUser() throws Exception {
		User user = UserGenerator.generateUser();
		Long id = UserGenerator.generateUser().getId();
		given(this.userCommand.updateUser(any(User.class))).willReturn(user);

		this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/" + id)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(JSONConvertor.toJSON(user)))
				.andExpect(status().isBadRequest())	;
	}

	@Test
	public void getUsersRequestWithPageParams_returnsHttp200AndPageofUsers() throws Exception {
		List<User> users = new ArrayList<>();
		users.add(UserGenerator.generateUser());
		users.add(UserGenerator.generateUser());
		Page<User> pagedUsers = new PageImpl<>(users);
		given(this.userQuery.getUsers(0, 2)).willReturn(pagedUsers);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users?page=0&size=2")
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(jsonPath("content").isArray())
			.andExpect(jsonPath("number").value(0));
	}

	@Test
	public void getUsersRequestWithoutPageParams_returnsHttp200AndPageofUsers() throws Exception {
		List<User> users = new ArrayList<>();
		users.add(UserGenerator.generateUser());
		users.add(UserGenerator.generateUser());
		Page<User> pagedUsers = new PageImpl<>(users);
		given(this.userQuery.getUsers( null, null)).willReturn(pagedUsers);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users")
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(jsonPath("content").isArray())
			.andExpect(jsonPath("number").value(0));
	}

	@Test
	public void getExistingUserRequest_returnsAnExistingUserAndHttp200() throws Exception {
		User user = UserGenerator.generateUser();
		given(this.userQuery.getUser(user.getId())).willReturn(Optional.of(user));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/" + user.getId())
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(jsonPath("username").value(user.getUsername()));
	}

	@Test
	public void getNonExistingUserRequest_returnsHttp404() throws Exception {
		Long id = UserGenerator.generateUser().getId();
		given(this.userQuery.getUser(id)).willReturn(Optional.empty());

		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/" + id)
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isNotFound());
	}
}
