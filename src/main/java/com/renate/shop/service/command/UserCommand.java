package com.renate.shop.service.command;

import com.renate.shop.model.User;

public interface UserCommand {

	User createUser(User user);
	User updateUser(User user);
	User updatePassword(Long userId, String prevPassword, String newPassword);

}
