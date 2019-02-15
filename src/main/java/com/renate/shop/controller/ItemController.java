package com.renate.shop.controller;

import com.renate.shop.model.Item;
import com.renate.shop.service.command.ItemCommand;
import com.renate.shop.service.query.ItemQuery;
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
@RequestMapping("/api/v1/items")
public class ItemController {

	@Autowired
	private ItemCommand itemCommand;

	@Autowired
	private ItemQuery itemQuery;

	@PostMapping(
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Item> createItem(@RequestBody Item item) {
		Item createdItem = this.itemCommand.createItem(item);
		return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
	}

	@PutMapping(
			value = "/{itemId}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Item> updateItem(@RequestBody Item item, @PathVariable("itemId") Long id) {
		if (id.compareTo(item.getId()) == 0) {
			Item updatedItem = this.itemCommand.updateItem(item);
			return new ResponseEntity<>(updatedItem, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping(
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Page<Item>> getItems(@RequestParam(value = "page", required = false)Integer page,
			@RequestParam(value = "size", required = false)Integer size) {
		Page<Item> items = this.itemQuery.getItems(page, size);
		return new ResponseEntity<>(items, HttpStatus.OK);
	}

	@GetMapping(
			value = "/{itemId}",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Item> getItem(@PathVariable("itemId")Long id) {
		Item item = this.itemQuery.getItem(id);
		if (item == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(item, HttpStatus.OK);
	}
}
