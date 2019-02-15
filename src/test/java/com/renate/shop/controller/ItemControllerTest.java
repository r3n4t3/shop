package com.renate.shop.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.renate.shop.generator.ItemGenerator;
import com.renate.shop.generator.JSONConvertor;
import com.renate.shop.model.Item;
import com.renate.shop.service.command.ItemCommand;
import com.renate.shop.service.query.ItemQuery;
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
@WebMvcTest(value = ItemController.class, secure = false)
public class ItemControllerTest {

	@MockBean
	private ItemCommand itemCommand;

	@MockBean
	private ItemQuery itemQuery;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void createItemRequest_returnsHTTP201AndCreateItem() throws Exception {
		Item item = ItemGenerator.generateItem();
		given(this.itemCommand.createItem(any(Item.class))).willReturn(item);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/items")
				.content(JSONConvertor.toJSON(item))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("name").value(item.getName()));
	}

	@Test
	public void updateItemRequest_returnsHTTP200AndUpdatedItem() throws Exception {
		Item item = ItemGenerator.generateItem();
		given(this.itemCommand.updateItem(any(Item.class))).willReturn(item);

		this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/items/" + item.getId())
				.content(JSONConvertor.toJSON(item))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(jsonPath("name").value(item.getName()));
	}

	@Test
	public void updateItemRequestWithMisMatchId_returnsHTTP400() throws Exception {
		Item item = ItemGenerator.generateItem();
		Long diffItemId = new Random().nextLong();
		given(this.itemCommand.updateItem(any(Item.class))).willReturn(item);

		this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/items/" + diffItemId)
				.content(JSONConvertor.toJSON(item))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void getItemsRequestWithPageParams_returnsHTTP200AndAPageOfItems() throws Exception {
		List<Item> items = new ArrayList<>();
		items.add(ItemGenerator.generateItem());
		items.add(ItemGenerator.generateItem());
		Page<Item> pagedItems = new PageImpl<>(items);
		given(this.itemQuery.getItems(0, 2)).willReturn(pagedItems);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/items?page=0&size=2")
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(jsonPath("content").isArray())
			.andExpect(jsonPath("number").value(0));
	}

	@Test
	public void getItemsRequestWithOutPageParams_returnsHTTP200AndAPageOfItems() throws Exception {
		List<Item> items = new ArrayList<>();
		items.add(ItemGenerator.generateItem());
		items.add(ItemGenerator.generateItem());
		Page<Item> pagedItems = new PageImpl<>(items);
		given(this.itemQuery.getItems(null, null)).willReturn(pagedItems);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/items")
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(jsonPath("content").isArray())
			.andExpect(jsonPath("number").value(0));
	}

	@Test
	public void getItemRequest_returnsHTTP200AndAnExistingItem() throws Exception {
		Item item = ItemGenerator.generateItem();
		given(this.itemQuery.getItem(item.getId())).willReturn(item);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/items/" + item.getId())
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(jsonPath("name").value(item.getName()));
	}

	@Test
	public void getNonExistingItemRequest_returnsHTTP404() throws Exception {
		Long itemId = ItemGenerator.generateItem().getId();
		given(this.itemQuery.getItem(itemId)).willReturn(null);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/items/" + itemId)
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isNotFound());
	}
}
