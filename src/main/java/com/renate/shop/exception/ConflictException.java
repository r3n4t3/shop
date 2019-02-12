package com.renate.shop.exception;

import java.text.MessageFormat;

public class ConflictException extends RuntimeException {

	public ConflictException(String message) {
		super(message);
	}

	public static ConflictException create(String msg, Object ...args) {
		return new ConflictException(MessageFormat.format(msg, args));
	}

}
