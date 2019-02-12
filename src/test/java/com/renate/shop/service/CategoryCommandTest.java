package com.renate.shop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.renate.shop.exception.ConflictException;
import com.renate.shop.exception.NotFoundException;
import com.renate.shop.generator.CategoryGenerator;
import com.renate.shop.model.Category;
import com.renate.shop.repository.CategoryRepository;
import com.renate.shop.service.command.CategoryCommand;
import com.renate.shop.service.command.implementation.CategoryCommandImplementation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CategoryCommandTest {

	@TestConfiguration
	static class CategoryCommandTestConfiguration {
		@Bean
		public CategoryCommand categoryCommand() {
			return new CategoryCommandImplementation();
		}
	}

	@MockBean
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryCommand categoryCommand;

	@Test
	public void createCategory_returnsCreatedCategory() {
		Category category = CategoryGenerator.generateCategory();
		given(this.categoryRepository.save(category)).willReturn(category);

		Category newCategory = this.categoryCommand.createCategory(category);

		assertThat(newCategory).isEqualTo(category);
	}

	@Test(expected = ConflictException.class)
	public void createCategoryWithExistingName_throwsConflictException() {
		Category category = CategoryGenerator.generateCategory();
		Category existingCategory = CategoryGenerator.generateCategory();
		existingCategory.setName(category.getName());
		given(this.categoryRepository.save(category)).willReturn(category);
		given(this.categoryRepository.findByName(category.getName())).willReturn(existingCategory);

		this.categoryCommand.createCategory(category);
	}

	@Test
	public void updateCategory_returnsUpdatedCategory() {
		Category category = CategoryGenerator.generateCategory();
		given(this.categoryRepository.save(category)).willReturn(category);
		given(this.categoryRepository.getOne(category.getId())).willReturn(category);

		Category updatedCategory = this.categoryCommand.updateCategory(category);

		assertThat(updatedCategory).isEqualTo(category);
	}

	@Test(expected = NotFoundException.class)
	public void updateCategoryWithNonExistingCategory_throwsNotFoundException() {
		Category category = CategoryGenerator.generateCategory();
		given(this.categoryRepository.getOne(category.getId())).willReturn(null);
		given(this.categoryRepository.save(category)).willReturn(category);

		this.categoryCommand.updateCategory(category);
	}

}
