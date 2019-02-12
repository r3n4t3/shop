package com.renate.shop.exception;

import java.text.MessageFormat;

public class NotFoundException extends RuntimeException {

	public NotFoundException(String message) {
		super(message);
	}

	public static NotFoundException create(String msg, Object ...args) {
		return new NotFoundException(MessageFormat.format(msg, args));
	}

}
