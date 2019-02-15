package com.renate.shop.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.renate.shop.generator.CategoryGenerator;
import com.renate.shop.generator.JSONConvertor;
import com.renate.shop.model.Category;
import com.renate.shop.service.command.CategoryCommand;
import com.renate.shop.service.query.CategoryQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CategoryController.class, secure = false)
public class CategoryControllerTest {

	@MockBean
	private CategoryCommand categoryCommand;

	@MockBean
	private CategoryQuery categoryQuery;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void createCategoryRequest_returnsHTTP201AndCreatedCategory() throws Exception {
		Category category = CategoryGenerator.generateCategory();
		given(this.categoryCommand.createCategory(any(Category.class))).willReturn(category);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/categories")
				.content(JSONConvertor.toJSON(category))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("name").value(category.getName()));
	}

	@Test
	public void updateCategoryRequest_returnsHTTP200AndUpdatedCategory() throws Exception {
		Category category = CategoryGenerator.generateCategory();
		given(this.categoryCommand.updateCategory(any(Category.class))).willReturn(category);

		this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/categories/" + category.getId())
				.content(JSONConvertor.toJSON(category))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(jsonPath("name").value(category.getName()));
	}

	@Test
	public void updateCategoryRequestWithMisMatchId_returnsHTTP400() throws Exception {
		Category category = CategoryGenerator.generateCategory();
		Long diffCategoryId = new Random().nextLong();
		given(this.categoryCommand.updateCategory(any(Category.class))).willReturn(category);

		this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/categories/" + diffCategoryId)
				.content(JSONConvertor.toJSON(category))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void getCategoriesWithPageParamsRequest_returnsAPageOfCategories() throws Exception {
		List<Category> categories = new ArrayList<>();
		categories.add(CategoryGenerator.generateCategory());
		categories.add(CategoryGenerator.generateCategory());
		Page<Category> pagedCategories = new PageImpl<>(categories);
		given(this.categoryQuery.getCategories(0, 2)).willReturn(pagedCategories);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories?page=0&size=2")
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(jsonPath("content").isArray())
			.andExpect(jsonPath("number").value(0));
	}

	@Test
	public void getCategoriesWithOutPageParamsRequest_returnsAPageOfCategories() throws Exception {
		List<Category> categories = new ArrayList<>();
		categories.add(CategoryGenerator.generateCategory());
		categories.add(CategoryGenerator.generateCategory());
		Page<Category> pagedCategories = new PageImpl<>(categories);
		given(this.categoryQuery.getCategories(null, null)).willReturn(pagedCategories);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories")
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(jsonPath("content").isArray())
			.andExpect(jsonPath("number").value(0));
	}

	@Test
	public void getCategoryRequest_returnsAnExistingCategory() throws Exception {
		Category category = CategoryGenerator.generateCategory();
		given(this.categoryQuery.getCategory(category.getId())).willReturn(category);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories/"+category.getId())
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(jsonPath("name").value(category.getName()));
	}

	@Test
	public void getNonExistingCategoryRequest_returnsHTTP404() throws Exception {
		Long categoryId = CategoryGenerator.generateCategory().getId();
		given(this.categoryQuery.getCategory(categoryId)).willReturn(null);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories/"+categoryId)
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isNotFound());
	}
}
