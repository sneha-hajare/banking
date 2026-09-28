package com.banking.banking.controller;

import com.banking.banking.model.Account;
import com.banking.banking.model.Transaction;
import com.banking.banking.repository.AccountRepository;
import com.banking.banking.service.TransactionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountRepository accountRepository;

    public TransactionController(TransactionService transactionService,
                                 AccountRepository accountRepository) {
        this.transactionService = transactionService;
        this.accountRepository = accountRepository;
    }

    @GetMapping
    public String transactions(
            @RequestParam("accountId") Long accountId,
            Authentication authentication,
            Model model) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Make sure the logged-in customer owns this account
        if (!account.getUser().getEmail().equals(authentication.getName())) {
            return "redirect:/accounts";
        }

        List<Transaction> transactions =
                transactionService.getTransactions(accountId);

        model.addAttribute("account", account);
        model.addAttribute("transactions", transactions);

        return "transactions/list";
    }

    @GetMapping("/deposit")
    public String depositPage(
            @RequestParam("accountId") Long accountId,
            Authentication authentication,
            Model model) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getEmail().equals(authentication.getName())) {
            return "redirect:/accounts";
        }

        model.addAttribute("account", account);

        return "transactions/deposit";
    }

    @PostMapping("/deposit")
    public String deposit(
            @RequestParam("accountId") Long accountId,
            @RequestParam("amount") BigDecimal amount,
            Authentication authentication) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getEmail().equals(authentication.getName())) {
            return "redirect:/accounts";
        }

        transactionService.deposit(accountId, amount);

        return "redirect:/transactions?accountId=" + accountId;
    }

    @GetMapping("/withdraw")
    public String withdrawPage(
            @RequestParam("accountId") Long accountId,
            Authentication authentication,
            Model model) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getEmail().equals(authentication.getName())) {
            return "redirect:/accounts";
        }

        model.addAttribute("account", account);

        return "transactions/withdraw";
    }

    @PostMapping("/withdraw")
    public String withdraw(
            @RequestParam("accountId") Long accountId,
            @RequestParam("amount") BigDecimal amount,
            Authentication authentication) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getEmail().equals(authentication.getName())) {
            return "redirect:/accounts";
        }

        transactionService.withdraw(accountId, amount);

        return "redirect:/transactions?accountId=" + accountId;
    }
}