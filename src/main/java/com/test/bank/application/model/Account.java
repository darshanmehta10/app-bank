package com.test.bank.application.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * This is model class for Account
 * @author darshan.mehta
 *
 */
@Entity
public class Account {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
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