package com.renate.shop.unitTest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.renate.shop.exception.BadRequestException;
import com.renate.shop.exception.ConflictException;
import com.renate.shop.exception.NotFoundException;
import com.renate.shop.generator.UserGenerator;
import com.renate.shop.model.User;
import com.renate.shop.service.command.UserCommand;
import com.renate.shop.repository.UserRepository;
import com.renate.shop.service.command.implementation.UserCommandImplementation;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class UserCommandTest {

	@TestConfiguration
	static class UserCommandTestConfiguration {
		@Bean
		public UserCommand userCommand() {
			return new UserCommandImplementation();
		}
	}

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserCommand userCommand;

	@Test
	public void createUser_returnsCreatedUser() {
		User user = UserGenerator.generateUser();
		given(this.userRepository.save(any(User.class))).willReturn(user);
		given(this.passwordEncoder.encode(user.getPassword())).willReturn(user.getPassword());

		User createdUser = this.userCommand.createUser(user);

		assertThat(createdUser).isEqualTo(user);
	}

	@Test(expected = ConflictException.class)
	public void createUserWithExistingUsername_throwsConflictException() {
		User user = UserGenerator.generateUser();
		User existingUser = UserGenerator.generateUser();
		existingUser.setUsername(user.getUsername());
		given(this.userRepository.findByUsername(user.getUsername())).willReturn(existingUser);

		this.userCommand.createUser(user);
	}

	@Test
	public void updateExistingUser_returnsUpdatedUser() {
		User user = UserGenerator.generateUser();
		given(this.userRepository.save(user)).willReturn(user);
		given(this.userRepository.getOne(user.getId())).willReturn(user);

		User updatedUser = this.userCommand.updateUser(user);

		assertThat(updatedUser).isEqualTo(user);
	}

	@Test(expected = NotFoundException.class)
	public void updateNonExistingUser_throwsNotFoundException() {
		User user = UserGenerator.generateUser();
		given(this.userRepository.save(user)).willReturn(user);
		given(this.userRepository.getOne(user.getId())).willReturn(null);

		this.userCommand.updateUser(user);
	}

	@Test
	public void updateExistingUser_returnsUpdatedUserWithSamePassword() {
		User user = UserGenerator.generateUser();
		User existingUser = UserGenerator.generateUser();
		existingUser.setId(user.getId());
		given(this.userRepository.save(user)).willReturn(user);
		given(this.userRepository.getOne(user.getId())).willReturn(existingUser);

		User updatedUser = this.userCommand.updateUser(user);

		assertThat(updatedUser.getPassword()).isEqualTo(existingUser.getPassword());
	}

	@Test
	public void updatePasswordWithCorrectPreviousPassword_returnsUserWithUpdatedPassword() {
		User user = UserGenerator.generateUser();
		String prevPassword = user.getPassword();
		User updatedPasswordUser = new User(user);
		String newPassword = RandomStringUtils.random(9, true, true);
		updatedPasswordUser.setPassword(newPassword);
		given(this.userRepository.getOne(user.getId())).willReturn(user);
		given(this.passwordEncoder.matches(prevPassword, user.getPassword())).willReturn(true);
		given(this.userRepository.save(any(User.class))).willReturn(updatedPasswordUser);
		given(this.passwordEncoder.encode(newPassword)).willReturn(newPassword);

		User updatedUser = this.userCommand.updatePassword(user.getId(), prevPassword, newPassword);

		assertThat(updatedUser.getPassword()).isEqualTo(newPassword);
	}

	@Test(expected = BadRequestException.class)
	public void updatePasswordWithInCorrectPreviousPassword_throwsBadRequestException() {
		User user = UserGenerator.generateUser();
		String prevPassword = RandomStringUtils.random(9, true, true);
		String newPassword = RandomStringUtils.random(9, true, true);
		given(this.userRepository.getOne(user.getId())).willReturn(user);
		given(this.passwordEncoder.matches(prevPassword, user.getPassword())).willReturn(false);

		this.userCommand.updatePassword(user.getId(), prevPassword, newPassword);
	}

}
