package com.renate.shop.service.query;

import com.renate.shop.model.Category;
import org.springframework.data.domain.Page;

public interface CategoryQuery {

	Page<Category> getCategories(Integer page, Integer size);
	Category getCategory(Long categoryId);

}
