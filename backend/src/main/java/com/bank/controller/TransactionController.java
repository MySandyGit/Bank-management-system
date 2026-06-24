package com.bank.controller;

import com.bank.dto.DepositRequest;
import com.bank.dto.WithdrawRequest;
import com.bank.entity.Transaction;
import com.bank.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(Principal principal, @Valid @RequestBody DepositRequest request) {
        return ResponseEntity.ok(transactionService.deposit(principal.getName(), request.getAmount()));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(Principal principal, @Valid @RequestBody WithdrawRequest request) {
        return ResponseEntity.ok(transactionService.withdraw(principal.getName(), request.getAmount()));
    }

    /** Transaction History: every deposit/withdraw the logged-in user has made. */
    @GetMapping("/history")
    public ResponseEntity<List<Transaction>> history(Principal principal) {
        return ResponseEntity.ok(transactionService.getHistory(principal.getName()));
    }
}
