package com.test.bank.application.dto;

/**
 * This class is data transfer object for account
 * @author darshan.mehta
 *
 */
public class AccountDto {
	
	private Long accountNumber;
	private String name;
	private double balance;
	
	public Long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
}