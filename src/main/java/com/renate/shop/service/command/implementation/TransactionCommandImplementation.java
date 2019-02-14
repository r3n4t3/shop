package com.renate.shop.service.command.implementation;

import java.util.Date;

import com.renate.shop.exception.NotFoundException;
import com.renate.shop.model.Transaction;
import com.renate.shop.repository.TransactionRepository;
import com.renate.shop.service.command.TransactionCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionCommandImplementation implements TransactionCommand {

	@Autowired
	private TransactionRepository transactionRepository;

	@Override
	public Transaction createTransaction(Transaction transaction) {
		transaction.setCreated(new Date());
		return this.transactionRepository.save(transaction);
	}

	@Override
	public Transaction updateTransaction(Transaction transaction) {
		Transaction existingTransaction = this.transactionRepository.getOne(transaction.getId());
		if (existingTransaction != null) {
			transaction.setCreated(existingTransaction.getCreated());
			return this.transactionRepository.save(transaction);
		}
		throw NotFoundException.create("Transaction does not exist");
	}
}
