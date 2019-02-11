package com.renate.shop.exception;

import java.text.MessageFormat;

public class BadRequestException extends RuntimeException {

	public BadRequestException(String message) {
		super(message);
	}

	public static BadRequestException create(String msg, String ...args) {
		return new BadRequestException(MessageFormat.format(msg, args));
	}

}
