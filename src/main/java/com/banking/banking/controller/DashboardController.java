package com.banking.banking.controller;

import com.banking.banking.model.Account;
import com.banking.banking.model.Transaction;
import com.banking.banking.repository.AccountRepository;
import com.banking.banking.service.LoanService;
import com.banking.banking.service.TransactionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final AccountRepository accountRepository;
    private final LoanService loanService;
    private final TransactionService transactionService;

    public DashboardController(
            AccountRepository accountRepository,
            LoanService loanService,
            TransactionService transactionService) {

        this.accountRepository = accountRepository;
        this.loanService = loanService;
        this.transactionService = transactionService;
    }

    // =========================
    // MAIN DASHBOARD REDIRECT
    // =========================

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {

        String role = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        if (role.equals("ROLE_ADMIN")) {
            return "redirect:/admin/dashboard";
        }

        if (role.equals("ROLE_STAFF")) {
            return "redirect:/staff/dashboard";
        }

        return "redirect:/customer/dashboard";
    }

    // =========================
    // CUSTOMER DASHBOARD
    // =========================

    @GetMapping("/customer/dashboard")
    public String customerDashboard(
            Authentication authentication,
            Model model) {

        String email = authentication.getName();

        // Get customer's accounts
        List<Account> accounts = accountRepository.findAll()
                .stream()
                .filter(account ->
                        account.getUser().getEmail().equals(email))
                .toList();

        // Total accounts
        model.addAttribute("accounts", accounts);
        model.addAttribute("totalAccounts", accounts.size());

        // Calculate total balance
        java.math.BigDecimal totalBalance = accounts.stream()
                .map(Account::getBalance)
                .reduce(
                        java.math.BigDecimal.ZERO,
                        java.math.BigDecimal::add
                );

        model.addAttribute("totalBalance", totalBalance);

        // Get recent transactions
        List<Transaction> recentTransactions = accounts.stream()
                .flatMap(account ->
                        transactionService
                                .getTransactions(account.getId())
                                .stream())
                .limit(5)
                .toList();

        model.addAttribute(
                "recentTransactions",
                recentTransactions
        );

        return "dashboard/customer";
    }

    // =========================
    // STAFF DASHBOARD
    // =========================

    @GetMapping("/staff/dashboard")
    public String staffDashboard() {
        return "dashboard/staff";
    }

    // =========================
    // ADMIN DASHBOARD
    // =========================

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "dashboard/admin";
    }
}