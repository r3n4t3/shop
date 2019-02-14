package com.renate.shop.service.query.implementation;

import java.util.Map;

import com.renate.shop.model.Transaction;
import com.renate.shop.repository.TransactionRepository;
import com.renate.shop.service.query.TransactionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class TransactionQueryImplementation implements TransactionQuery {

	@Autowired
	private TransactionRepository transactionRepository;

	@Override
	public Page<Transaction> getTransactions(Integer page, Integer size) {
		Map<String, Integer> pageAndSize = PageValidator.validatePageAndSize(page, size);
		return this.transactionRepository
				.findAll(PageRequest.of(pageAndSize.get("page"), pageAndSize.get("size")));
	}

	@Override
	public Transaction getTransaction(Long transactionId) {
		return this.transactionRepository.getOne(transactionId);
	}
}
