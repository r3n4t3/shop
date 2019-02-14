package com.renate.shop.service.query;

import com.renate.shop.model.Transaction;
import org.springframework.data.domain.Page;

public interface TransactionQuery {

	Page<Transaction> getTransactions(Integer page, Integer size);
	Transaction getTransaction(Long transactionId);

}
