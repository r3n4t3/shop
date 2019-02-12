package com.renate.shop.service.command;

import com.renate.shop.model.Item;

public interface ItemCommand {

	Item createItem(Item item);
	Item updateItem(Item item);
	void deleteItem(Long id);

}
