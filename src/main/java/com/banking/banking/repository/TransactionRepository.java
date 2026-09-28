package com.banking.banking.repository;

import com.banking.banking.model.Account;
import com.banking.banking.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountOrderByTransactionDateDesc(Account account);
}