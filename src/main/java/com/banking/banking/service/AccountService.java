package com.banking.banking.service;

import com.banking.banking.model.Account;
import com.banking.banking.model.User;
import com.banking.banking.repository.AccountRepository;
import com.banking.banking.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository,
                          UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Account createAccount(String email, String accountType) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = new Account();

        account.setAccountNumber(generateAccountNumber());
        account.setUser(user);
        account.setAccountType(accountType);
        account.setBalance(BigDecimal.ZERO);
        account.setStatus("ACTIVE");

        return accountRepository.save(account);
    }

    public List<Account> getAccountsByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return accountRepository.findByUser(user);
    }

    private String generateAccountNumber() {

        String accountNumber;

        do {
            accountNumber = "10" +
                    UUID.randomUUID()
                            .toString()
                            .replace("-", "")
                            .substring(0, 10);

        } while (accountRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }
}