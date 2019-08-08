package com.test.bank.application.service;

import com.test.bank.application.dto.AccountDto;
import com.test.bank.application.dto.TransactionDto;

/**
 * This interface forms service layer for the app
 * @author darshan.mehta
 *
 */
public interface BankService {
	
	public AccountDto addAccount(AccountDto accountDto);
	
	public double performTransaction(TransactionDto transactionDto);
	
	public AccountDto getAccount(Long accountNumber);

}
