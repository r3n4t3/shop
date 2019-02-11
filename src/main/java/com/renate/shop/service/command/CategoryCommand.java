package com.renate.shop.service.command;

import com.renate.shop.model.Category;

public interface CategoryCommand {

	Category createCategory(Category category);
	Category updateCategory(Category category);

}
