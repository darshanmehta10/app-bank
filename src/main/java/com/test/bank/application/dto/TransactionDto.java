package com.test.bank.application.dto;

/**
 * This class is data transfer object for transaction
 * @author darshan.mehta
 *
 */
public class TransactionDto {
	
	private long sourceAccountNumber;
	private long destinationAccountNumber;
	private double amount;
	
	public long getSourceAccountNumber() {
		return sourceAccountNumber;
	}
	public void setSourceAccountNumber(long sourceAccountNumber) {
		this.sourceAccountNumber = sourceAccountNumber;
	}
	public long getDestinationAccountNumber() {
		return destinationAccountNumber;
	}
	public void setDestinationAccountNumber(long destinationAccountNumber) {
		this.destinationAccountNumber = destinationAccountNumber;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
}