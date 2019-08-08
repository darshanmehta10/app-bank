package com.test.bank.application.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.bank.application.dto.AccountDto;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerIntegrationTest {
	
	private Random random = new Random();
	
	private final String ADD_ACCOUNT_PAYLOAD = "{\"name\" : \"%s\", \"balance\": %f}";
	
	private final String TRANSACTION_PAYLOAD = "{\"sourceAccountNumber\": %d, \"destinationAccountNumber\" : %d, \"amount\" : %f}";
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void addAccount() throws Exception {
		String name = "test";
		double balance = 10d;
		
		mockMvc.perform(post("/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(String.format(ADD_ACCOUNT_PAYLOAD, name, balance)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", is(name)))
			.andExpect(jsonPath("$.balance", is(balance)));
	}
	
	@Test
	public void addAccountWithNegativeBalance() throws Exception {
		String name = "test";
		double balance = -10d;
		
		mockMvc.perform(post("/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(String.format(ADD_ACCOUNT_PAYLOAD, name, balance)))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void performTransactionWithNonExistentAccount() throws Exception {
		double amount = 10d;
		Long sourceAccountNumber = random.nextLong();
		
		mockMvc.perform(post("/transfer")
				.contentType(MediaType.APPLICATION_JSON)
				.content(String.format(TRANSACTION_PAYLOAD, sourceAccountNumber, sourceAccountNumber, amount)))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void performTransactionWithSameAccount() throws Exception {
		double sourceAmount = 10d;
		String sourceName = "test1";
		final Long sourceAccountNumber;
		
		MvcResult addResultSource = mockMvc.perform(post("/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(String.format(ADD_ACCOUNT_PAYLOAD, sourceName, sourceAmount)))
				.andReturn();
		
		sourceAccountNumber = objectMapper.readValue(addResultSource.getResponse().getContentAsString(), AccountDto.class).getAccountNumber();

		mockMvc.perform(post("/transfer")
				.contentType(MediaType.APPLICATION_JSON)
				.content(String.format(TRANSACTION_PAYLOAD, sourceAccountNumber, sourceAccountNumber, sourceAmount)))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void performTransactionWithInsufficientBalance() throws Exception {
		double sourceAmount = 10d;
		String sourceName = "test1";
		final Long sourceAccountNumber;
		
		double destinationAmount = 10d;
		String destinationName = "test2";
		final Long destinationAccountNumber;
		
		double transferAmount = 15d;
		
		MvcResult addResultSource = mockMvc.perform(post("/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(String.format(ADD_ACCOUNT_PAYLOAD, sourceName, sourceAmount)))
				.andReturn();
		
		MvcResult addResultDestination = mockMvc.perform(post("/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(String.format(ADD_ACCOUNT_PAYLOAD, destinationName, destinationAmount)))
				.andReturn();
		
		sourceAccountNumber = objectMapper.readValue(addResultSource.getResponse().getContentAsString(), AccountDto.class).getAccountNumber();
		
		destinationAccountNumber = objectMapper.readValue(addResultDestination.getResponse().getContentAsString(), AccountDto.class).getAccountNumber();
		
		mockMvc.perform(post("/transfer")
				.contentType(MediaType.APPLICATION_JSON)
				.content(String.format(TRANSACTION_PAYLOAD, sourceAccountNumber, destinationAccountNumber, transferAmount)))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void performTransactionWithValidAccounts() throws Exception {
		double sourceAmount = 10d;
		String sourceName = "test1";
		final Long sourceAccountNumber;
		
		double destinationAmount = 10d;
		String destinationName = "test2";
		final Long destinationAccountNumber;
		
		double transferAmount = 5d;
		
		MvcResult addResultSource = mockMvc.perform(post("/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(String.format(ADD_ACCOUNT_PAYLOAD, sourceName, sourceAmount)))
				.andReturn();
		
		MvcResult addResultDestination = mockMvc.perform(post("/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(String.format(ADD_ACCOUNT_PAYLOAD, destinationName, destinationAmount)))
				.andReturn();
		
		sourceAccountNumber = objectMapper.readValue(addResultSource.getResponse().getContentAsString(), AccountDto.class).getAccountNumber();
		
		destinationAccountNumber = objectMapper.readValue(addResultDestination.getResponse().getContentAsString(), AccountDto.class).getAccountNumber();
		
		mockMvc.perform(post("/transfer")
				.contentType(MediaType.APPLICATION_JSON)
				.content(String.format(TRANSACTION_PAYLOAD, sourceAccountNumber, destinationAccountNumber, transferAmount)))
				.andExpect(status().isOk());
		
		mockMvc.perform(get("/get/" + sourceAccountNumber)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", is(sourceName)))
			.andExpect(jsonPath("$.balance", is(sourceAmount - transferAmount)));
	}
	
	@Test
	public void fetchNonExistentAccountDetails() throws Exception {
		mockMvc.perform(get("/get/" + random.nextLong())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}

}
