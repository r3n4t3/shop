package com.renate.shop.controller;

import com.renate.shop.model.Transaction;
import com.renate.shop.service.command.TransactionCommand;
import com.renate.shop.service.query.TransactionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

	@Autowired
	private TransactionCommand transactionCommand;

	@Autowired
	private TransactionQuery transactionQuery;

	@PostMapping(
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
		Transaction newTransaction = this.transactionCommand.createTransaction(transaction);
		return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
	}

	@PutMapping(
			value = "/{transId}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Transaction> updateTransaction(@RequestBody Transaction transaction,
			@PathVariable("transId")Long id) {
		if (id.compareTo(transaction.getId()) == 0) {
			Transaction updatedTransaction = this.transactionCommand.updateTransaction(transaction);
			return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping(
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Page<Transaction>> getTransactions(@RequestParam(value = "page", required = false)Integer page,
			@RequestParam(value = "size", required = false)Integer size) {
		Page<Transaction> transactions = this.transactionQuery.getTransactions(page, size);
		return new ResponseEntity<>(transactions, HttpStatus.OK);
	}

	@GetMapping(
			value = "/{transId}",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Transaction> getTransaction(@PathVariable("transId")Long id) {
		Transaction transaction = this.transactionQuery.getTransaction(id);
		if (transaction == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(transaction, HttpStatus.OK);
	}
}
