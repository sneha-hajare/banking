package com.banking.banking.controller;

import com.banking.banking.model.Account;
import com.banking.banking.model.Transaction;
import com.banking.banking.repository.AccountRepository;
import com.banking.banking.service.TransactionService;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
public class StatementController {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    public StatementController(
            AccountRepository accountRepository,
            TransactionService transactionService) {

        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    @GetMapping("/statement")
    public String statement(
            @RequestParam("accountId") Long accountId,
            Authentication authentication,
            Model model) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        if (!account.getUser().getEmail()
                .equals(authentication.getName())) {

            return "redirect:/accounts";
        }

        List<Transaction> transactions =
                transactionService.getTransactions(accountId);

        model.addAttribute("account", account);
        model.addAttribute("transactions", transactions);

        return "statement";
    }

    @GetMapping("/statement/download")
    public void downloadStatement(
            @RequestParam("accountId") Long accountId,
            Authentication authentication,
            HttpServletResponse response) throws IOException {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        if (!account.getUser().getEmail()
                .equals(authentication.getName())) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Access denied"
            );

            return;
        }

        List<Transaction> transactions =
                transactionService.getTransactions(accountId);

        response.setContentType("application/pdf");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=account-statement.pdf"
        );

        Document document = new Document();

        PdfWriter.getInstance(
                document,
                response.getOutputStream()
        );

        document.open();

        document.add(
                new Paragraph("BANKCORE - ACCOUNT STATEMENT")
        );

        document.add(new Paragraph(" "));

        document.add(
                new Paragraph(
                        "Account Number: "
                                + account.getAccountNumber()
                )
        );

        document.add(
                new Paragraph(
                        "Account Type: "
                                + account.getAccountType()
                )
        );

        document.add(
                new Paragraph(
                        "Current Balance: ₹"
                                + account.getBalance()
                )
        );

        document.add(new Paragraph(" "));

        Table table = new Table(5);

        table.addCell(new Phrase("Date"));
        table.addCell(new Phrase("Type"));
        table.addCell(new Phrase("Amount"));
        table.addCell(new Phrase("Balance"));
        table.addCell(new Phrase("Description"));

        for (Transaction transaction : transactions) {

            table.addCell(
                    new Phrase(
                            String.valueOf(
                                    transaction.getTransactionDate()
                            )
                    )
            );

            table.addCell(
                    new Phrase(
                            transaction.getTransactionType()
                    )
            );

            table.addCell(
                    new Phrase(
                            "₹" + transaction.getAmount()
                    )
            );

            table.addCell(
                    new Phrase(
                            "₹" + transaction.getBalanceAfter()
                    )
            );

            table.addCell(
                    new Phrase(
                            transaction.getDescription() == null
                                    ? ""
                                    : transaction.getDescription()
                    )
            );
        }

        document.add(table);

        document.add(new Paragraph(" "));

        document.add(
                new Paragraph(
                        "Generated by BankCore Banking Management System"
                )
        );

        document.close();
    }
}