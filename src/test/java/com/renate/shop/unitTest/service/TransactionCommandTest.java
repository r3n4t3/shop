package com.renate.shop.unitTest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Date;

import com.renate.shop.exception.NotFoundException;
import com.renate.shop.generator.TransactionGenerator;
import com.renate.shop.model.Transaction;
import com.renate.shop.repository.TransactionRepository;
import com.renate.shop.service.command.TransactionCommand;
import com.renate.shop.service.command.implementation.TransactionCommandImplementation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TransactionCommandTest {

	@TestConfiguration
	static class TransactionCommandTestConfiguration {
		@Bean
		public TransactionCommand transactionCommand() {
			return new TransactionCommandImplementation();
		}
	}

	@MockBean
	public TransactionRepository transactionRepository;

	@Autowired
	public TransactionCommand transactionCommand;

	@Test
	public void createTransaction_returnsCreatedTransction() {
		Transaction transaction = TransactionGenerator.generateTransaction();
		given(this.transactionRepository.save(transaction)).willReturn(transaction);

		Transaction createdTransaction = this.transactionCommand.createTransaction(transaction);

		assertThat(createdTransaction).isEqualTo(transaction);
		assertThat(createdTransaction.getCreated()).isBeforeOrEqualsTo(new Date());
	}

	@Test
	public void updateTransaction_returnsUpdatedTransaction() {
		Transaction transaction = TransactionGenerator.generateTransaction();
		given(this.transactionRepository.getOne(transaction.getId())).willReturn(transaction);
		given(this.transactionRepository.save(transaction)).willReturn(transaction);

		Transaction updatedTransaction = this.transactionCommand.updateTransaction(transaction);

		assertThat(updatedTransaction).isEqualTo(transaction);
	}

	@Test(expected = NotFoundException.class)
	public void updateNonExistingTransaction_throwNonFoundException() {
		Transaction transaction = TransactionGenerator.generateTransaction();
		given(this.transactionRepository.getOne(transaction.getId())).willReturn(null);

		this.transactionCommand.updateTransaction(transaction);
	}
}
