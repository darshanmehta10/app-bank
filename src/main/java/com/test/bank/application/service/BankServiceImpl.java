package com.test.bank.application.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.bank.application.dto.AccountDto;
import com.test.bank.application.dto.TransactionDto;
import com.test.bank.application.model.Account;
import com.test.bank.application.model.Transaction;
import com.test.bank.application.repository.AccountRepository;
import com.test.bank.application.repository.TransactionRepository;

/**
 * This class implements service interface methods and talks to DAO layer (repositories) for different operations
 * @author darshan.mehta
 *
 */
@Service
public class BankServiceImpl implements BankService {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;

	@Override
	@Transactional
	public AccountDto addAccount(AccountDto accountDto) {
		validateAccount(accountDto);
		Account account = toAccount(accountDto);
		accountRepository.saveAndFlush(account);
		return toDto(account);
	}
	
	@Override
	@Transactional
	public double performTransaction(TransactionDto transactionDto) {
		validateTransaction(transactionDto);
		Account sourceAccount = accountRepository.findById(transactionDto.getSourceAccountNumber()).get();
		Account destinationAccount = accountRepository.findById(transactionDto.getDestinationAccountNumber()).get();
		sourceAccount.setBalance(sourceAccount.getBalance() - transactionDto.getAmount());
		destinationAccount.setBalance(destinationAccount.getBalance() + transactionDto.getAmount());
		accountRepository.saveAndFlush(sourceAccount);
		accountRepository.saveAndFlush(destinationAccount);
		transactionRepository.save(toTransaction(transactionDto, sourceAccount, destinationAccount));
		return sourceAccount.getBalance();
	}
	
	@Override
	public AccountDto getAccount(Long accountNumber) {
		Optional<Account> account = accountRepository.findById(accountNumber);
		if(!account.isPresent()) {
			throw new NoSuchElementException("Account not found with number : " + accountNumber);
		}
		return toDto(account.get());
	}
	
	private Account toAccount(AccountDto accountDto) {
		Account account = new Account();
		account.setBalance(accountDto.getBalance());
		account.setName(accountDto.getName());
		return account;
	}
	
	private Transaction toTransaction(TransactionDto transactionDto, Account sourceAccount, Account destinationAccount) {
		Transaction transaction = new Transaction();
		transaction.setSourceAccount(sourceAccount);
		transaction.setDestinationAccount(destinationAccount);
		transaction.setAmount(transactionDto.getAmount());
		return transaction;
	}
	
	private void validateAccount(AccountDto accountDto) {
		if(accountDto.getName() == null 
				|| accountDto.getName().isEmpty()
				|| accountDto.getBalance() < 0) {
			throw new IllegalArgumentException("Account must have name and non negative balance");
		}
	}
	
	private void validateTransaction(TransactionDto transactionDto) {
		Optional<Account> sourceAccount = accountRepository.findById(transactionDto.getSourceAccountNumber());
		Optional<Account> destinationAccount = accountRepository.findById(transactionDto.getDestinationAccountNumber());
		if(!sourceAccount.isPresent() || !destinationAccount.isPresent()) {
			throw new NoSuchElementException("Invalid source or destination account number. Account doesn't exist.");
		}else if(sourceAccount.get().getAccountNumber().equals(destinationAccount.get().getAccountNumber())) {
			throw new IllegalArgumentException("Source and Destination account numbers can't be samne.");
		}else if(sourceAccount.get().getBalance() < transactionDto.getAmount()) {
			throw new IllegalArgumentException("Source account doesn't have suffient funds to perform the transaction.");
		}
	}
	
	private AccountDto toDto(Account account) {
		AccountDto accountDto = new AccountDto();
		accountDto.setAccountNumber(account.getAccountNumber());
		accountDto.setName(account.getName());
		accountDto.setBalance(account.getBalance());
		return accountDto;
	}

}