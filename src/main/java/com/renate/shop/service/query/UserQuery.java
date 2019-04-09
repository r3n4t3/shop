package com.renate.shop.service.query;

import java.util.Optional;

import com.renate.shop.model.User;
import org.springframework.data.domain.Page;

public interface UserQuery {

	Page<User> getUsers(Integer page, Integer size);
	Optional<User> getUser(Long userId);
	User getUser(String username);

}
