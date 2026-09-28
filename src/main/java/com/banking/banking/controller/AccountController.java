package com.banking.banking.controller;

import com.banking.banking.model.Account;
import com.banking.banking.service.AccountService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public String accounts(Authentication authentication, Model model) {

        String email = authentication.getName();

        List<Account> accounts =
                accountService.getAccountsByEmail(email);

        model.addAttribute("accounts", accounts);

        return "accounts/list";
    }

    @GetMapping("/create")
    public String createAccountPage() {
        return "accounts/create";
    }

    @PostMapping("/create")
    public String createAccount(
            @RequestParam("accountType") String accountType,
            Authentication authentication) {

        String email = authentication.getName();

        accountService.createAccount(email, accountType);

        return "redirect:/accounts";
    }
}