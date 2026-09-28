package com.banking.banking.controller;

import com.banking.banking.model.Account;
import com.banking.banking.model.Loan;
import com.banking.banking.repository.AccountRepository;
import com.banking.banking.service.LoanService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;
    private final AccountRepository accountRepository;

    public LoanController(LoanService loanService,
                          AccountRepository accountRepository) {
        this.loanService = loanService;
        this.accountRepository = accountRepository;
    }

    // =========================
    // CUSTOMER - VIEW LOANS
    // =========================

    @GetMapping
    public String loans(
            @RequestParam("accountId") Long accountId,
            Authentication authentication,
            Model model) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getEmail().equals(authentication.getName())) {
            return "redirect:/accounts";
        }

        List<Loan> loans = loanService.getLoansByAccount(accountId);

        model.addAttribute("account", account);
        model.addAttribute("loans", loans);

        return "loans/list";
    }

    // =========================
    // CUSTOMER - APPLY LOAN
    // =========================

    @GetMapping("/apply")
    public String applyLoanPage(
            @RequestParam("accountId") Long accountId,
            Authentication authentication,
            Model model) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getEmail().equals(authentication.getName())) {
            return "redirect:/accounts";
        }

        model.addAttribute("account", account);

        return "loans/apply";
    }

    @PostMapping("/apply")
    public String applyLoan(
            @RequestParam("accountId") Long accountId,
            @RequestParam("loanType") String loanType,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("interestRate") BigDecimal interestRate,
            @RequestParam("tenure") Integer tenure,
            Authentication authentication,
            Model model) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getEmail().equals(authentication.getName())) {
            return "redirect:/accounts";
        }

        try {

            loanService.applyForLoan(
                    accountId,
                    loanType,
                    amount,
                    interestRate,
                    tenure
            );

            return "redirect:/loans?accountId=" + accountId;

        } catch (RuntimeException e) {

            model.addAttribute("account", account);
            model.addAttribute("error", e.getMessage());

            return "loans/apply";
        }
    }

    // =========================
    // STAFF / ADMIN - PENDING LOANS
    // =========================

    @GetMapping("/pending")
    public String pendingLoans(Model model) {

        List<Loan> loans = loanService.getPendingLoans();

        model.addAttribute("loans", loans);

        return "loans/pending";
    }

    // =========================
    // STAFF / ADMIN - APPROVE
    // =========================

    @PostMapping("/approve/{loanId}")
    public String approveLoan(
            @PathVariable Long loanId) {

        loanService.approveLoan(loanId);

        return "redirect:/loans/pending";
    }

    // =========================
    // STAFF / ADMIN - REJECT
    // =========================

    @PostMapping("/reject/{loanId}")
    public String rejectLoan(
            @PathVariable Long loanId) {

        loanService.rejectLoan(loanId);

        return "redirect:/loans/pending";
    }
}