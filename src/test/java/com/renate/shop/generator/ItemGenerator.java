package com.renate.shop.generator;

import java.util.Random;

import com.renate.shop.model.Category;
import com.renate.shop.model.Item;
import org.apache.commons.lang3.RandomStringUtils;

public class ItemGenerator {

	public static Item generateItem() {
		Long id = new Random().nextLong();
		String name = RandomStringUtils.random(7, true, true);
		Integer quantity = new Random().nextInt(1);
		Float price = new Random().nextFloat();
		Category category = CategoryGenerator.generateCategory();
		return new Item(id, name, quantity, price, category);
	}
}
