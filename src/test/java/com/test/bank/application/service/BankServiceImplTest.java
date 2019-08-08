package com.test.bank.application.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.test.bank.application.dto.AccountDto;
import com.test.bank.application.dto.TransactionDto;
import com.test.bank.application.model.Account;
import com.test.bank.application.repository.AccountRepository;
import com.test.bank.application.repository.TransactionRepository;


@RunWith(MockitoJUnitRunner.class)
public class BankServiceImplTest {
	
	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();
	
	@Mock
	private AccountRepository accountRepository;
	
	@Mock
	private TransactionRepository transactionRepository;
	
	@InjectMocks
	BankService testObj = new BankServiceImpl();

	@Test
	public void addShouldThrowExeptionWithNoName() {
		exceptionRule.expect(IllegalArgumentException.class);
		testObj.addAccount(new AccountDto());
	}
	
	@Test
	public void addShouldThrowExceptionWithNegativeBalance() {
		AccountDto accountDto = new AccountDto();
		accountDto.setName("test");
		accountDto.setBalance(-1);
		exceptionRule.expect(IllegalArgumentException.class);
		testObj.addAccount(accountDto);
	}
	
	@Test
	public void addShouldPersistTheAccount() {
		final Long ACCOUNT_NUMBER = 1l;
		AccountDto accountDto = new AccountDto();
		accountDto.setName("test");
		accountDto.setBalance(10);
		
		Mockito.when(accountRepository.saveAndFlush(Mockito.any(Account.class))).thenAnswer(invocation -> {
				((Account)invocation.getArgument(0)).setAccountNumber(ACCOUNT_NUMBER);
				return null;
				});
		Assert.assertEquals(testObj.addAccount(accountDto).getAccountNumber(), ACCOUNT_NUMBER);
	}
	
	@Test
	public void transactionShouldThrowAnExcetptionForInvalidAccount() {
		final Long SOURCE_ACCOUNT = 2l;
		final Long DESTINATION_ACCOUNT = 3l;
		Mockito.when(accountRepository.findById(SOURCE_ACCOUNT)).thenReturn(Optional.ofNullable(null));
		TransactionDto transactionDto = new TransactionDto();
		transactionDto.setSourceAccountNumber(SOURCE_ACCOUNT);
		transactionDto.setDestinationAccountNumber(DESTINATION_ACCOUNT);
		transactionDto.setAmount(20);
		exceptionRule.expect(NoSuchElementException.class);
		testObj.performTransaction(transactionDto);
	}
	
	@Test
	public void transactionShouldThrowAnExcetptionForInvalidAmount() {
		Random random = new Random();
		final Long SOURCE_ACCOUNT = 2l;
		final Long DESTINATION_ACCOUNT = 3l;
		Account sourceAccount = new Account();
		sourceAccount.setAccountNumber(random.nextLong());
		
		Account destinationAccount = new Account();
		destinationAccount.setAccountNumber(random.nextLong());
		Mockito.when(accountRepository.findById(SOURCE_ACCOUNT)).thenReturn(Optional.of(sourceAccount));
		Mockito.when(accountRepository.findById(DESTINATION_ACCOUNT)).thenReturn(Optional.of(destinationAccount));
		TransactionDto transactionDto = new TransactionDto();
		transactionDto.setSourceAccountNumber(SOURCE_ACCOUNT);
		transactionDto.setDestinationAccountNumber(DESTINATION_ACCOUNT);
		transactionDto.setAmount(20);
		exceptionRule.expect(IllegalArgumentException.class);
		testObj.performTransaction(transactionDto);
	}
	
	@Test
	public void transactionShouldDeductAmount() {
		final Long SOURCE_ACCOUNT = 2l;
		final Long DESTINATION_ACCOUNT = 3l;
		final double sourceBalance = 10;
		final double destinationBalance = 5;
		final double transactionAmount = 5;
		
		Account sourceAccount = new Account();
		Account destinationAccount = new Account();
		
		sourceAccount.setBalance(sourceBalance);
		sourceAccount.setAccountNumber(SOURCE_ACCOUNT);
		
		destinationAccount.setBalance(destinationBalance);
		destinationAccount.setAccountNumber(DESTINATION_ACCOUNT);
		
		Mockito.when(accountRepository.findById(SOURCE_ACCOUNT)).thenReturn(Optional.of(sourceAccount));
		Mockito.when(accountRepository.findById(DESTINATION_ACCOUNT)).thenReturn(Optional.of(destinationAccount));
		
		TransactionDto transactionDto = new TransactionDto();
		transactionDto.setSourceAccountNumber(SOURCE_ACCOUNT);
		transactionDto.setDestinationAccountNumber(DESTINATION_ACCOUNT);
		transactionDto.setAmount(transactionAmount);
		
		testObj.performTransaction(transactionDto);
		
		Assert.assertEquals(sourceAccount.getBalance(), sourceBalance - transactionAmount, 0.0);
		Assert.assertEquals(destinationAccount.getBalance(), destinationBalance + transactionAmount, 0.0);
	}
	
	@Test
	public void getAccountShouldThrowExceptionForInvalidNumber() {
		final Long ACCOUNT_NUMBER = 5l;
		exceptionRule.expect(NoSuchElementException.class);
		testObj.getAccount(ACCOUNT_NUMBER);
	}
	
	@Test
	public void getAccountShouldReturnCorrectInfo() {
		final Long ACCOUNT_NUMBER = 4l;
		final double BALANCE = 10d;
		final String NAME = "test";
		
		Account account = new Account();
		account.setAccountNumber(ACCOUNT_NUMBER);
		account.setBalance(BALANCE);
		account.setName(NAME);
		
		Mockito.when(accountRepository.findById(ACCOUNT_NUMBER)).thenReturn(Optional.of(account));
		
		AccountDto accountDto = testObj.getAccount(ACCOUNT_NUMBER);
		
		Assert.assertEquals(accountDto.getBalance(), account.getBalance(), 0.0);
		Assert.assertEquals(accountDto.getName(), account.getName());
	}

}
