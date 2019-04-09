package com.renate.shop.service.query;

import java.util.Optional;

import com.renate.shop.model.Item;
import org.springframework.data.domain.Page;

public interface ItemQuery {

	Page<Item> getItems(Integer page, Integer size);
	Optional<Item> getItem(Long id);

}
