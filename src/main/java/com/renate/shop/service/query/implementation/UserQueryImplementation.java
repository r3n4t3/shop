package com.renate.shop.service.query.implementation;

import java.util.Map;

import com.renate.shop.model.User;
import com.renate.shop.repository.UserRepository;
import com.renate.shop.service.query.UserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class  UserQueryImplementation implements UserQuery {

	@Autowired
	private UserRepository userRepository;

	@Override
	public Page<User> getUsers(Integer page, Integer size) {
		Map<String, Integer> pageAndSize = PageValidator.validatePageAndSize(page, size);
		return this.userRepository
				.findAll(PageRequest.of(pageAndSize.get("page"), pageAndSize.get("size")));
	}

	@Override
	public User getUser(Long userId) {
		return this.userRepository.getOne(userId);
	}
}
