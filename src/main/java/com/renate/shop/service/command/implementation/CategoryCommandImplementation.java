package com.renate.shop.service.command.implementation;

import com.renate.shop.exception.BadRequestException;
import com.renate.shop.exception.ConflictException;
import com.renate.shop.exception.NotFoundException;
import com.renate.shop.model.Category;
import com.renate.shop.repository.CategoryRepository;
import com.renate.shop.service.command.CategoryCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryCommandImplementation implements CategoryCommand {

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Category createCategory(Category category) {
		if (this.categoryRepository.findByName(category.getName()) == null) {
			return this.categoryRepository.save(category);
		}
		throw ConflictException.create("Category exist");
	}

	@Override
	public Category updateCategory(Category category) {
		if (this.categoryRepository.getOne(category.getId()) != null) {
			return this.categoryRepository.save(category);
		}
		throw NotFoundException.create("Category does not exist");
	}
}
