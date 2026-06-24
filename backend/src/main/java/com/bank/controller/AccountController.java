package com.bank.controller;

import com.bank.entity.Account;
import com.bank.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /** Read: get the logged-in user's account number + current balance. */
    @GetMapping("/me")
    public ResponseEntity<Account> getMyAccount(Principal principal) {
        return ResponseEntity.ok(accountService.getAccountByUsername(principal.getName()));
    }
}
