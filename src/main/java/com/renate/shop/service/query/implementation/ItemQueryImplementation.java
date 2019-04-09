package com.renate.shop.service.query.implementation;

import java.util.Map;
import java.util.Optional;

import com.renate.shop.model.Item;
import com.renate.shop.repository.ItemRepository;
import com.renate.shop.service.query.ItemQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ItemQueryImplementation implements ItemQuery {

	@Autowired
	private ItemRepository itemRepository;

	@Override
	public Page<Item> getItems(Integer page, Integer size) {
		Map<String, Integer> pageAndSize = PageValidator.validatePageAndSize(page, size);
		return this.itemRepository
				.findAll(PageRequest.of(pageAndSize.get("page"), pageAndSize.get("size")));
	}

	@Override
	public Optional<Item> getItem(Long id) {
		return this.itemRepository.findById(id);
	}
}
