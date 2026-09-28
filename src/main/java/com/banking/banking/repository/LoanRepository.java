package com.banking.banking.repository;

import com.banking.banking.model.Loan;
import com.banking.banking.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByAccount(Account account);

    List<Loan> findByStatus(String status);
}