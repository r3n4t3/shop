package com.renate.shop.unitTest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.renate.shop.exception.BadRequestException;
import com.renate.shop.generator.UserGenerator;
import com.renate.shop.model.User;
import com.renate.shop.repository.UserRepository;
import com.renate.shop.service.query.UserQuery;
import com.renate.shop.service.query.implementation.CustomerUserDetailService;
import com.renate.shop.service.query.implementation.UserQueryImplementation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class UserQueryTest {

	@TestConfiguration
	static class UserQueryTestConfiguration {
		@Bean
		public UserQuery userQuery() {
			return new UserQueryImplementation();
		}

		@Bean
		public UserDetailsService userDetailsService() {
			return new CustomerUserDetailService();
		}
	}

	@MockBean
	private UserRepository userRepository;

	@Autowired
	private UserQuery userQuery;

	@Autowired
	private UserDetailsService userDetailsService;

	@Test
	public void getUsersWithPageParams_returnsAPageOfUsers() {
		List<User> users = new ArrayList<>();
		users.add(UserGenerator.generateUser());
		users.add(UserGenerator.generateUser());
		Page<User> pagedUsers = new PageImpl<>(users);
		given(this.userRepository.findAll(PageRequest.of(0, 2))).willReturn(pagedUsers);

		Page<User> gottenUsers = this.userQuery.getUsers(0, 2);

		assertThat(gottenUsers).isEqualTo(pagedUsers);
	}

	@Test
	public void getUsersWithOutPageParams_returnsAPageOfUsers() {
		List<User> users = new ArrayList<>();
		users.add(UserGenerator.generateUser());
		users.add(UserGenerator.generateUser());
		Page<User> pagedUsers = new PageImpl<>(users);
		given(this.userRepository.findAll(PageRequest.of(0,10))).willReturn(pagedUsers);

		Page<User> gottenUsers = this.userQuery.getUsers(null, null);

		assertThat(gottenUsers).isEqualTo(pagedUsers);
	}

	@Test(expected = BadRequestException.class)
	public void getUsersWithInValidPageParams_throwsBadRequestException() {
		List<User> users = new ArrayList<>();
		users.add(UserGenerator.generateUser());
		users.add(UserGenerator.generateUser());
		Page<User> pagedUsers = new PageImpl<>(users);
		given(this.userRepository.findAll(PageRequest.of(0,10))).willReturn(pagedUsers);

		this.userQuery.getUsers(null, -5);
	}

	@Test
	public void getUserById_returnsGottenUser() {
		User user = UserGenerator.generateUser();
		given(this.userRepository.findById(user.getId()))
				.willReturn(Optional.of(user));

		Optional<User> gottenUser = this.userQuery.getUser(user.getId());

		assertThat(gottenUser.get()).isEqualTo(user);
	}

	@Test
	public void getUserByUsername_returnsGottenUser() {
		User user = UserGenerator.generateUser();
		given(this.userRepository.findByUsername(user.getUsername())).willReturn(user);

		User gottenUser = this.userQuery.getUser(user.getUsername());

		assertThat(gottenUser).isEqualTo(user);
	}


	@Test
	public void getNonExistingUserByUsername_returnsGottenUser() {
		String username = UserGenerator.generateUser().getUsername();
		given(this.userRepository.findByUsername(username)).willReturn(null);

		User gottenUser = this.userQuery.getUser(username);

		assertThat(gottenUser).isEqualTo(null);
	}
	@Test
	public void getNonExistingUser_returnsNull() {
		User user = UserGenerator.generateUser();
		given(this.userRepository.findById(user.getId())).willReturn(Optional.empty());

		Optional<User> gottenUser = this.userQuery.getUser(user.getId());

		assertThat(gottenUser.isPresent()).isEqualTo(false);
	}

	@Test
	public void loadUserByUserName_returnsGottenUser() {
		User user = UserGenerator.generateUser();
		List<GrantedAuthority> authorities = new ArrayList<>();
		UserDetails userDetails = new org.springframework.security.core.userdetails
				.User(user.getUsername(), user.getPassword(), user.getEnabled(), true,
				true, true, authorities);
		given(this.userRepository.findByUsername(user.getUsername())).willReturn(user);

		UserDetails gottenUser = this.userDetailsService.loadUserByUsername(user.getUsername());

		assertThat(gottenUser).isEqualTo(userDetails);
	}

	@Test(expected = UsernameNotFoundException.class)
	public void getNonExistingUserByUserName_throwNotFoundException() {
		User user = UserGenerator.generateUser();
		given(this.userRepository.findByUsername(user.getUsername())).willReturn(null);

		this.userDetailsService.loadUserByUsername(user.getUsername());
	}
}
