package com.renate.shop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import com.renate.shop.exception.BadRequestException;
import com.renate.shop.generator.TransactionGenerator;
import com.renate.shop.model.Transaction;
import com.renate.shop.repository.TransactionRepository;
import com.renate.shop.service.query.TransactionQuery;
import com.renate.shop.service.query.implementation.TransactionQueryImplementation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TransactionQueryTest {

	@TestConfiguration
	static class TransactionQueryTestConfiguration {
		@Bean
		public TransactionQuery transactionQuery() {
			return new TransactionQueryImplementation();
		}
	}

	@MockBean
	private TransactionRepository transactionRepository;

	@Autowired
	private TransactionQuery transactionQuery;

	@Test
	public void getTransactionsWithPageParams_returnsReturnsAPageOfTransaction() {
		List<Transaction> transactions = new ArrayList<>();
		transactions.add(TransactionGenerator.generateTransaction());
		transactions.add(TransactionGenerator.generateTransaction());
		Page<Transaction> pagedTransactions = new PageImpl<>(transactions);
		given(transactionRepository.findAll(PageRequest.of(0, 2))).willReturn(pagedTransactions);

		Page<Transaction> gottenTransactions = this.transactionQuery.getTransactions(0, 2);

		assertThat(gottenTransactions).isEqualTo(pagedTransactions);
	}

	@Test
	public void getTransactionsWithOutPageParams_returnsReturnsAPageOfTransaction() {
		List<Transaction> transactions = new ArrayList<>();
		transactions.add(TransactionGenerator.generateTransaction());
		transactions.add(TransactionGenerator.generateTransaction());
		Page<Transaction> pagedTransactions = new PageImpl<>(transactions);
		given(transactionRepository.findAll(PageRequest.of(0, 10))).willReturn(pagedTransactions);

		Page<Transaction> gottenTransactions = this.transactionQuery.getTransactions(0, null);

		assertThat(gottenTransactions).isEqualTo(pagedTransactions);
	}

	@Test(expected = BadRequestException.class)
	public void getTransactionsWithInValidPageParams_throwsBadRequestException() {
		List<Transaction> transactions = new ArrayList<>();
		transactions.add(TransactionGenerator.generateTransaction());
		transactions.add(TransactionGenerator.generateTransaction());
		Page<Transaction> pagedTransactions = new PageImpl<>(transactions);
		given(transactionRepository.findAll(PageRequest.of(0, 10))).willReturn(pagedTransactions);

		this.transactionQuery.getTransactions(0, -1);
	}

	@Test
	public void getTransaction_returnsAnExistingTransaction() {
		Transaction transaction = TransactionGenerator.generateTransaction();
		given(transactionRepository.getOne(transaction.getId())).willReturn(transaction);

		Transaction gottenTransaction = this.transactionQuery.getTransaction(transaction.getId());

		assertThat(gottenTransaction).isEqualTo(transaction);
	}

	@Test
	public void getTransaction_returnsNull() {
		Transaction transaction = TransactionGenerator.generateTransaction();
		given(transactionRepository.getOne(transaction.getId())).willReturn(null);

		Transaction gottenTransaction = this.transactionQuery.getTransaction(transaction.getId());

		assertThat(gottenTransaction).isEqualTo(null);
	}
}
