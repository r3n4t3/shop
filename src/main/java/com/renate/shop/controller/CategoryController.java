package com.renate.shop.controller;

import java.util.Optional;

import com.renate.shop.model.Category;
import com.renate.shop.model.Customer;
import com.renate.shop.service.command.CategoryCommand;
import com.renate.shop.service.query.CategoryQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

	@Autowired
	private CategoryCommand categoryCommand;

	@Autowired
	private CategoryQuery categoryQuery;

	@PostMapping(
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Category> createCategory(@RequestBody Category category) {
		Category newCategory = this.categoryCommand.createCategory(category);
		return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
	}

	@PutMapping(
			value = "/{categoryId}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Category> updateCategory(@RequestBody Category category, @PathVariable("categoryId") Long id) {
		if (id.compareTo(category.getId()) == 0) {
			Category updatedCategory = this.categoryCommand.updateCategory(category);
			return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping(
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Page<Category>> getCategories(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false)Integer size) {
		Page<Category> categories = this.categoryQuery.getCategories(page, size);
		return new ResponseEntity<>(categories, HttpStatus.OK);
	}

	@GetMapping(
			value = "/{categoryId}",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Category> getCategories(@PathVariable("categoryId") Long id) {
		Optional<Category> category = this.categoryQuery.getCategory(id);
		if(category.isPresent()) {
			return new ResponseEntity<>(category.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
