package com.test.bank.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.bank.application.model.Transaction;

/**
 * This interface provides CRUD functionalities for transaction object
 * @author darshan.mehta
 *
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long>{

}
