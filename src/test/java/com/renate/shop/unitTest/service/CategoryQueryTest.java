package com.renate.shop.unitTest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.renate.shop.exception.BadRequestException;
import com.renate.shop.generator.CategoryGenerator;
import com.renate.shop.model.Category;
import com.renate.shop.repository.CategoryRepository;
import com.renate.shop.service.query.CategoryQuery;
import com.renate.shop.service.query.implementation.CategoryQueryImplementation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CategoryQueryTest {

	@TestConfiguration
	static class CatgoryQueryTestConfiguration {
		@Bean
		public CategoryQuery categoryQuery() {
			return new CategoryQueryImplementation();
		}
	}

	@MockBean
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryQuery categoryQuery;

	@Test
	public void getCategoriesWithPageParams_returnsAPageOfCategories() {
		List<Category> categories = new ArrayList<>();
		categories.add(CategoryGenerator.generateCategory());
		categories.add(CategoryGenerator.generateCategory());
		Page<Category> pagedCategories = new PageImpl<>(categories);
		given(this.categoryRepository.findAll(PageRequest.of(0, 2)))
				.willReturn(pagedCategories);

		Page<Category> gottenCategories = this.categoryQuery.getCategories(0, 2);

		assertThat(gottenCategories).isEqualTo(pagedCategories);
	}

	@Test
	public void getCategoriesWithOutPageParams_returnsAPageOfCategories() {
		List<Category> categories = new ArrayList<>();
		categories.add(CategoryGenerator.generateCategory());
		categories.add(CategoryGenerator.generateCategory());
		Page<Category> pagedCategories = new PageImpl<>(categories);
		given(this.categoryRepository.findAll(PageRequest.of(0, 10)))
				.willReturn(pagedCategories);

		Page<Category> gottenCategories = this.categoryQuery.getCategories(null, null);

		assertThat(gottenCategories).isEqualTo(pagedCategories);
	}

	@Test(expected = BadRequestException.class)
	public void getCategoriesWithInValidtPageParams_throwsBadRequestException() {
		List<Category> categories = new ArrayList<>();
		categories.add(CategoryGenerator.generateCategory());
		categories.add(CategoryGenerator.generateCategory());
		Page<Category> pagedCategories = new PageImpl<>(categories);
		given(this.categoryRepository.findAll(PageRequest.of(0, 10)))
				.willReturn(pagedCategories);

		this.categoryQuery.getCategories(null, -1);
	}

	@Test
	public void getCategory_returnsExistingCategory() {
		Category category = CategoryGenerator.generateCategory();
		given(this.categoryRepository.findById(category.getId()))
				.willReturn(Optional.of(category));

		Optional<Category> gottenCategory = this.categoryQuery.getCategory(category.getId());

		assertThat(gottenCategory.get()).isEqualTo(category);
	}

	@Test
	public void getCategory_returnsNull() {
		Category category = CategoryGenerator.generateCategory();
		given(this.categoryRepository.findById(category.getId()))
				.willReturn(Optional.empty());

		Optional<Category> gottenCategory = this.categoryQuery.getCategory(category.getId());

		assertThat(gottenCategory.isPresent()).isEqualTo(false);
	}
}
