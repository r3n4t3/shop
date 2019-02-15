package com.renate.shop.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONConvertor {

	public static String toJSON(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(object);
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
