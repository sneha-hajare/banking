package com.banking.banking.controller;

import com.banking.banking.model.Account;
import com.banking.banking.repository.AccountRepository;
import com.banking.banking.service.FundTransferService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/transfers")
public class FundTransferController {

    private final FundTransferService fundTransferService;
    private final AccountRepository accountRepository;

    public FundTransferController(FundTransferService fundTransferService,
                                  AccountRepository accountRepository) {
        this.fundTransferService = fundTransferService;
        this.accountRepository = accountRepository;
    }

    @GetMapping("/create")
    public String transferPage(
            @RequestParam("accountId") Long accountId,
            Authentication authentication,
            Model model) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getEmail().equals(authentication.getName())) {
            return "redirect:/accounts";
        }

        model.addAttribute("account", account);

        return "transfers/create";
    }

    @PostMapping("/create")
    public String transfer(
            @RequestParam("fromAccountId") Long fromAccountId,
            @RequestParam("toAccountNumber") String toAccountNumber,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam(value = "remarks", required = false) String remarks,
            Authentication authentication,
            Model model) {

        Account account = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getEmail().equals(authentication.getName())) {
            return "redirect:/accounts";
        }

        try {
            fundTransferService.transfer(
                    fromAccountId,
                    toAccountNumber,
                    amount,
                    remarks
            );

            return "redirect:/transactions?accountId=" + fromAccountId;

        } catch (RuntimeException e) {

            model.addAttribute("account", account);
            model.addAttribute("error", e.getMessage());

            return "transfers/create";
        }
    }
}