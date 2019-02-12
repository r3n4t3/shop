package com.renate.shop.generator;

import java.util.Random;

import com.renate.shop.model.User;
import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {

	public static User generateUser() {
		Long id = new Random().nextLong();
		String firstName = RandomStringUtils.random(7, true, true);
		String lastName = RandomStringUtils.random(7, true, true);
		String email = RandomStringUtils.random(10, true, true).concat("@mail.cm");
		String telephone = RandomStringUtils.random(9, false, true);
		String username = RandomStringUtils.random(7, true, true);
		String password = RandomStringUtils.random(7, true, true);

		return new User(id, username, password, firstName, lastName, telephone, email);
	}
}
