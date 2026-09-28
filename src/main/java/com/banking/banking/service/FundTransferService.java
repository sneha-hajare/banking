package com.banking.banking.service;

import com.banking.banking.model.Account;
import com.banking.banking.model.FundTransfer;
import com.banking.banking.repository.AccountRepository;
import com.banking.banking.repository.FundTransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class FundTransferService {

    private final AccountRepository accountRepository;
    private final FundTransferRepository fundTransferRepository;

    public FundTransferService(AccountRepository accountRepository,
                               FundTransferRepository fundTransferRepository) {
        this.accountRepository = accountRepository;
        this.fundTransferRepository = fundTransferRepository;
    }

    @Transactional
    public FundTransfer transfer(Long fromAccountId,
                                 String toAccountNumber,
                                 BigDecimal amount,
                                 String remarks) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Transfer amount must be greater than zero");
        }

        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Source account not found"));

        Account toAccount = accountRepository
                .findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new RuntimeException("Destination account not found"));

        if (!"ACTIVE".equals(fromAccount.getStatus()) ||
                !"ACTIVE".equals(toAccount.getStatus())) {
            throw new RuntimeException("Both accounts must be active");
        }

        if (fromAccount.getId().equals(toAccount.getId())) {
            throw new RuntimeException("Cannot transfer to the same account");
        }

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        fromAccount.setBalance(
                fromAccount.getBalance().subtract(amount)
        );

        toAccount.setBalance(
                toAccount.getBalance().add(amount)
        );

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        FundTransfer transfer = new FundTransfer();

        transfer.setFromAccount(fromAccount);
        transfer.setToAccount(toAccount);
        transfer.setAmount(amount);
        transfer.setStatus("SUCCESS");
        transfer.setRemarks(remarks);

        return fundTransferRepository.save(transfer);
    }
}