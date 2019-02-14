package com.renate.shop.generator;

import java.util.Random;

import com.renate.shop.model.Customer;
import com.renate.shop.model.Item;
import com.renate.shop.model.Transaction;
import com.renate.shop.model.User;

public class TransactionGenerator {

	public static Transaction generateTransaction() {
		Long id = new Random().nextLong();
		Integer quantity = 3;
		Item item = ItemGenerator.generateItem();
		Customer customer = CustomerGenerator.generateCustomer();
		User user = UserGenerator.generateUser();

		return new Transaction(id, quantity, item, customer, user);
	}
}
