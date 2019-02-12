package com.renate.shop.service.command.implementation;

import java.util.Date;

import com.renate.shop.exception.BadRequestException;
import com.renate.shop.exception.ConflictException;
import com.renate.shop.exception.NotFoundException;
import com.renate.shop.model.User;
import com.renate.shop.service.command.UserCommand;
import com.renate.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserCommandImplementation implements UserCommand {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User createUser(User user) {
		if (this.userRepository.findByUsername(user.getUsername()) == null) {
			user.setCreated(new Date());
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			return this.userRepository.save(user);
		}
		throw ConflictException.create("Username exist");
	}

	@Override
	public User updateUser(User user) {
		User existingUser = this.userRepository.getOne(user.getId());
		if (existingUser != null) {
			user.setPassword(existingUser.getPassword());
			return this.userRepository.save(user);
		}
		throw NotFoundException.create("User account does not exist");
	}

	@Override
	public User updatePassword(Long userId, String prevPassword, String newPassword) {
		User existingUser = this.userRepository.getOne(userId);
		if (passwordEncoder.matches(prevPassword, existingUser.getPassword())) {
			existingUser
					.setPassword(this.passwordEncoder.encode(newPassword));
			return this.userRepository.save(existingUser);
		}
		throw BadRequestException.create("Incorrect previous password");
	}
}
