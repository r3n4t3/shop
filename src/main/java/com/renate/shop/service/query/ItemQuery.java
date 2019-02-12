package com.renate.shop.service.query;

import com.renate.shop.model.Item;
import org.springframework.data.domain.Page;

public interface ItemQuery {

	Page<Item> getItems(Integer page, Integer size);
	Item getItem(Long id);

}
