package com.banking.banking.service;

import com.banking.banking.model.Account;
import com.banking.banking.model.Loan;
import com.banking.banking.repository.AccountRepository;
import com.banking.banking.repository.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final AccountRepository accountRepository;

    public LoanService(LoanRepository loanRepository,
                       AccountRepository accountRepository) {
        this.loanRepository = loanRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Loan applyForLoan(Long accountId,
                             String loanType,
                             BigDecimal amount,
                             BigDecimal interestRate,
                             Integer tenure) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Loan amount must be greater than zero");
        }

        if (tenure == null || tenure <= 0) {
            throw new RuntimeException("Tenure must be greater than zero");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!"ACTIVE".equals(account.getStatus())) {
            throw new RuntimeException("Account is not active");
        }

        Loan loan = new Loan();

        loan.setAccount(account);
        loan.setLoanType(loanType);
        loan.setAmount(amount);
        loan.setInterestRate(interestRate);
        loan.setTenure(tenure);
        loan.setStatus("PENDING");

        return loanRepository.save(loan);
    }

    public List<Loan> getLoansByAccount(Long accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return loanRepository.findByAccount(account);
    }

    public List<Loan> getPendingLoans() {
        return loanRepository.findByStatus("PENDING");
    }

    @Transactional
    public Loan approveLoan(Long loanId) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        loan.setStatus("APPROVED");
        loan.setApprovedDate(LocalDate.now());

        loan.setDueDate(
                LocalDate.now().plusMonths(loan.getTenure())
        );

        return loanRepository.save(loan);
    }

    @Transactional
    public Loan rejectLoan(Long loanId) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        loan.setStatus("REJECTED");

        return loanRepository.save(loan);
    }
}