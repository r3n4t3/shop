package com.renate.shop.generator;

import java.util.Random;

import com.renate.shop.model.Category;
import org.apache.commons.lang3.RandomStringUtils;

public class CategoryGenerator {

	public static Category generateCategory() {
		Long id = new Random().nextLong();
		String name = RandomStringUtils.random(10, true, false);
		return new Category(id, name);
	}

}
