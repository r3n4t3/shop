package com.renate.shop.service.query;

import com.renate.shop.model.User;
import org.springframework.data.domain.Page;

public interface UserQuery {

	Page<User> getUsers(Integer page, Integer size);
	User getUser(Long userId);

}
