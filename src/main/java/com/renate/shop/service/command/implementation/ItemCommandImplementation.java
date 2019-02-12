package com.renate.shop.service.command.implementation;

import com.renate.shop.exception.ConflictException;
import com.renate.shop.exception.NotFoundException;
import com.renate.shop.model.Item;
import com.renate.shop.repository.ItemRepository;
import com.renate.shop.service.command.ItemCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemCommandImplementation implements ItemCommand {

	@Autowired
	private ItemRepository itemRepository;

	@Override
	public Item createItem(Item item) {
		if (this.itemRepository.findByName(item.getName()) == null) {
			return this.itemRepository.save(item);
		}
		throw ConflictException.create("Bad Request: Item {0} exist", item.getName());
	}

	@Override
	public Item updateItem(Item item) {
		if (this.itemRepository.getOne(item.getId()) != null) {
			return this.itemRepository.save(item);
		}
		throw NotFoundException.create("Bad Request: Item does not exist");
	}

	@Override
	public void deleteItem(Long id) {

	}
}
