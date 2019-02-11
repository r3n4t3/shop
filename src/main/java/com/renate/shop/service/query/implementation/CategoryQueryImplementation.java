package com.renate.shop.service.query.implementation;

import java.util.Map;

import com.renate.shop.model.Category;
import com.renate.shop.repository.CategoryRepository;
import com.renate.shop.service.query.CategoryQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CategoryQueryImplementation implements CategoryQuery {

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Page<Category> getCategories(Integer page, Integer size) {
		Map<String, Integer> pageAndSize = PageValidator.validatePageAndSize(page, size);
		return this.categoryRepository
				.findAll(PageRequest.of(pageAndSize.get("page"), pageAndSize.get("size")));
	}

	@Override
	public Category getCategory(Long categoryId) {
		return this.categoryRepository.getOne(categoryId);
	}
}
