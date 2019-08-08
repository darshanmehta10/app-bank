package com.test.bank.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.bank.application.model.Account;

/**
 * This interface provides CRUD functionalities for account object
 * @author darshan.mehta
 *
 */
public interface AccountRepository extends JpaRepository<Account, Long>{

}
