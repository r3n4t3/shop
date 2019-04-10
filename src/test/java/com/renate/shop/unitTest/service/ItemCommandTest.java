package com.renate.shop.unitTest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.renate.shop.exception.ConflictException;
import com.renate.shop.exception.NotFoundException;
import com.renate.shop.generator.ItemGenerator;
import com.renate.shop.model.Item;
import com.renate.shop.repository.ItemRepository;
import com.renate.shop.service.command.ItemCommand;
import com.renate.shop.service.command.implementation.ItemCommandImplementation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ItemCommandTest {

	@TestConfiguration
	static class ItemCommandTestConfiguration {
		@Bean
		public ItemCommand itemCommand() {
			return new ItemCommandImplementation();
		}
	}

	@MockBean
	private ItemRepository itemRepository;

	@Autowired
	private ItemCommand itemCommand;

	@Test
	public void createItem_returnsCreatedItem() {
		Item item = ItemGenerator.generateItem();
		given(this.itemRepository.save(item)).willReturn(item);

		Item newItem = this.itemCommand.createItem(item);

		assertThat(newItem).isEqualTo(item);
	}

	@Test(expected = ConflictException.class)
	public void createItemWithExistingName_throwsConflictRequestException() {
		Item item = ItemGenerator.generateItem();
		Item existingItem = ItemGenerator.generateItem();
		existingItem.setName(item.getName());
		given(this.itemRepository.findByName(item.getName())).willReturn(existingItem);

		this.itemCommand.createItem(item);
	}

	@Test
	public void updateItem_returnsUpdatedItem() {
		Item item = ItemGenerator.generateItem();
		given(this.itemRepository.getOne(item.getId())).willReturn(item);
		given(this.itemRepository.save(item)).willReturn(item);

		Item updatedItem = this.itemCommand.updateItem(item);

		assertThat(updatedItem).isEqualTo(item);
	}

	@Test(expected = NotFoundException.class)
	public void updateNonExistingItem_throwsNotFoundRequestException() {
		Item item = ItemGenerator.generateItem();
		given(this.itemRepository.getOne(item.getId())).willReturn(null);

		this.itemCommand.updateItem(item);
	}
}
