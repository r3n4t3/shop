package com.renate.shop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import com.renate.shop.exception.BadRequestException;
import com.renate.shop.generator.ItemGenerator;
import com.renate.shop.model.Item;
import com.renate.shop.repository.ItemRepository;
import com.renate.shop.service.query.ItemQuery;
import com.renate.shop.service.query.implementation.ItemQueryImplementation;
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
public class ItemQueryTest {

	@TestConfiguration
	static class ItemQueryTestConfiguration {
		@Bean
		public ItemQuery itemQuery() {
			return new ItemQueryImplementation();
		}
	}

	@MockBean
	private ItemRepository itemRepository;

	@Autowired
	private ItemQuery itemQuery;

	@Test
	public void getItemsWithPageParams_returnsAPageofItems() {
		List<Item> items = new ArrayList<>();
		items.add(ItemGenerator.generateItem());
		items.add(ItemGenerator.generateItem());
		Page<Item> pagedItems = new PageImpl<>(items);
		given(this.itemRepository.findAll(PageRequest.of(0, 2))).willReturn(pagedItems);

		Page<Item> gottenItem = this.itemQuery.getItems(0, 2);

		assertThat(gottenItem).isEqualTo(pagedItems);
	}

	@Test
	public void getItemsWithOutPageParams_returnsAPageofItems() {
		List<Item> items = new ArrayList<>();
		items.add(ItemGenerator.generateItem());
		items.add(ItemGenerator.generateItem());
		Page<Item> pagedItems = new PageImpl<>(items);
		given(this.itemRepository.findAll(PageRequest.of(0, 10))).willReturn(pagedItems);

		Page<Item> gottenItem = this.itemQuery.getItems(null, null);

		assertThat(gottenItem).isEqualTo(pagedItems);
	}

	@Test(expected = BadRequestException.class)
	public void getItemsWithInValidPageParams_returnsAPageofItems() {
		List<Item> items = new ArrayList<>();
		items.add(ItemGenerator.generateItem());
		items.add(ItemGenerator.generateItem());
		Page<Item> pagedItems = new PageImpl<>(items);
		given(this.itemRepository.findAll(PageRequest.of(0, 10))).willReturn(pagedItems);

		this.itemQuery.getItems(null, -1);
	}

	@Test
	public void getItem_returnsExistingItem() {
		Item item = ItemGenerator.generateItem();
		given(this.itemRepository.getOne(item.getId())).willReturn(item);

		Item gottenItem = this.itemQuery.getItem(item.getId());

		assertThat(gottenItem).isEqualTo(item);
	}

	@Test
	public void getItem_returnsNull() {
		Item item = ItemGenerator.generateItem();
		given(this.itemRepository.getOne(item.getId())).willReturn(null);

		Item gottenItem = this.itemQuery.getItem(item.getId());

		assertThat(gottenItem).isEqualTo(null);
	}
}
