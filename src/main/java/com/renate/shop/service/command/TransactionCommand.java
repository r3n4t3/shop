package com.renate.shop.service.command;

import com.renate.shop.model.Transaction;

public interface TransactionCommand {

	Transaction createTransaction(Transaction transaction);
	Transaction updateTransaction(Transaction transaction);

}
