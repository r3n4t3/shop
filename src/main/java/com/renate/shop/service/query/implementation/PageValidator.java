package com.renate.shop.service.query.implementation;

import java.util.HashMap;
import java.util.Map;

import com.renate.shop.exception.BadRequestException;

public class PageValidator {

	public static Map<String, Integer> validatePageAndSize(Integer page, Integer size) {
		page = page == null ? 0: page;
		size = size == null ? 10: size;
		if (page >= 0 && size >= 0) {
			Map<String, Integer> pageAndSize = new HashMap<>();
			pageAndSize.put("page", page);
			pageAndSize.put("size", size);
			return pageAndSize;
		}
		throw BadRequestException.create("Invalid page parameters");
	}
}
