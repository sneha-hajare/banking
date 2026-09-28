package com.banking.banking.service;

import com.banking.banking.model.Account;
import com.banking.banking.model.Transaction;
import com.banking.banking.repository.AccountRepository;
import com.banking.banking.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Transaction deposit(Long accountId, BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Deposit amount must be greater than zero");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!"ACTIVE".equals(account.getStatus())) {
            throw new RuntimeException("Account is not active");
        }

        BigDecimal newBalance = account.getBalance().add(amount);

        account.setBalance(newBalance);
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setTransactionType("DEPOSIT");
        transaction.setAmount(amount);
        transaction.setBalanceAfter(newBalance);
        transaction.setDescription("Cash deposit");

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction withdraw(Long accountId, BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Withdrawal amount must be greater than zero");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!"ACTIVE".equals(account.getStatus())) {
            throw new RuntimeException("Account is not active");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        BigDecimal newBalance = account.getBalance().subtract(amount);

        account.setBalance(newBalance);
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setTransactionType("WITHDRAWAL");
        transaction.setAmount(amount);
        transaction.setBalanceAfter(newBalance);
        transaction.setDescription("Cash withdrawal");

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactions(Long accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return transactionRepository
                .findByAccountOrderByTransactionDateDesc(account);
    }
}