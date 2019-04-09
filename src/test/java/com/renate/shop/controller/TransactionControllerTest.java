package com.renate.shop.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.renate.shop.generator.JSONConvertor;
import com.renate.shop.generator.TransactionGenerator;
import com.renate.shop.model.Transaction;
import com.renate.shop.service.command.TransactionCommand;
import com.renate.shop.service.query.TransactionQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(value = TransactionController.class, secure = false)
public class TransactionControllerTest {

	@MockBean
	private TransactionCommand transactionCommand;

	@MockBean
	private TransactionQuery transactionQuery;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void createTransactionRequest_returnsHTTP201AndCreatedTransaction() throws Exception {
		Transaction transaction = TransactionGenerator.generateTransaction();
		given(this.transactionCommand.createTransaction(any(Transaction.class))).willReturn(transaction);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/transactions")
				.content(JSONConvertor.toJSON(transaction))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("quantity").value(transaction.getQuantity()));
	}

	@Test
	public void updateTransactionRequest_returnsHTTP200AndUpdatedTransaction() throws Exception {
		Transaction transaction = TransactionGenerator.generateTransaction();
		given(this.transactionCommand.updateTransaction(any(Transaction.class))).willReturn(transaction);

		this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/transactions/" + transaction.getId())
				.content(JSONConvertor.toJSON(transaction))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(jsonPath("quantity").value(transaction.getQuantity()));
	}


	@Test
	public void updateTransactionRequestWithMisMatchID_returnsHTTP400() throws Exception {
		Transaction transaction = TransactionGenerator.generateTransaction();
		Long diffTransId = TransactionGenerator.generateTransaction().getId();
		given(this.transactionCommand.updateTransaction(any(Transaction.class))).willReturn(transaction);

		this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/transactions/" + diffTransId)
				.content(JSONConvertor.toJSON(transaction))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void getTransactionsWithPageParams_returnsHTTP200AndAPageOfTransactions() throws Exception {
		List<Transaction> transactions = new ArrayList<>();
		transactions.add(TransactionGenerator.generateTransaction());
		transactions.add(TransactionGenerator.generateTransaction());
		Page<Transaction> pagedTransactions = new PageImpl<>(transactions);
		given(this.transactionQuery.getTransactions(0, 2)).willReturn(pagedTransactions);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/transactions?page=0&size=2")
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(jsonPath("content").isArray())
			.andExpect(jsonPath("number").value(0));
	}

	@Test
	public void getTransactionsWithOutPageParams_returnsHTTP200AndAPageOfTransactions() throws Exception {
		List<Transaction> transactions = new ArrayList<>();
		transactions.add(TransactionGenerator.generateTransaction());
		transactions.add(TransactionGenerator.generateTransaction());
		Page<Transaction> pagedTransactions = new PageImpl<>(transactions);
		given(this.transactionQuery.getTransactions(null, null)).willReturn(pagedTransactions);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/transactions")
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(jsonPath("content").isArray())
			.andExpect(jsonPath("number").value(0));
	}

	@Test
	public void getTransactionRequest_returnsHTTP200AndExistingTransaction() throws Exception {
		Transaction transaction = TransactionGenerator.generateTransaction();
		given(this.transactionQuery.getTransaction(transaction.getId()))
				.willReturn(Optional.of(transaction));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/transactions/" + transaction.getId())
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(jsonPath("quantity").value(transaction.getQuantity()));
	}

	@Test
	public void getNonExistingTransactionRequest_returnsHTTP404() throws Exception {
		Long transId = TransactionGenerator.generateTransaction().getId();
		given(this.transactionQuery.getTransaction(transId)).willReturn(Optional.empty());

		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/transactions/" + transId)
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isNotFound());
	}
}
