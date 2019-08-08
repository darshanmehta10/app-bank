package com.test.bank.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.test.bank.application.dto.AccountDto;
import com.test.bank.application.dto.TransactionDto;
import com.test.bank.application.service.BankService;

/**
 * This class is entrypoint for all the requests 
 * @author darshan.mehta
 *
 */
@Controller
public class AccountController {
	
	@Autowired
	private BankService bankService;
	
	@RequestMapping(method = RequestMethod.POST, value = "/add")
	public ResponseEntity<AccountDto> addAccount(@RequestBody AccountDto accountDto) {
		return new ResponseEntity<>(bankService.addAccount(accountDto), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/transfer")
	public ResponseEntity<String> performTransaction(@RequestBody TransactionDto transactionDto) {
		return new ResponseEntity<>("Sucessfully performed funds transfer. New balance in source account : " + bankService.performTransaction(transactionDto), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/get/{accountNumber}")
	public ResponseEntity<AccountDto> getAccount(@PathVariable("accountNumber") Long accountNumber) {
		return new ResponseEntity<>(bankService.getAccount(accountNumber), HttpStatus.OK);
	}

}
