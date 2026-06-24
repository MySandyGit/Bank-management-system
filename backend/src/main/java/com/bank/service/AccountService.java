package com.bank.service;

import com.bank.entity.Account;
import com.bank.entity.Customer;
import com.bank.exception.ResourceNotFoundException;
import com.bank.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerService customerService;

    public AccountService(AccountRepository accountRepository, CustomerService customerService) {
        this.accountRepository = accountRepository;
        this.customerService = customerService;
    }

    /** Gets the bank account that belongs to the currently logged-in username. */
    public Account getAccountByUsername(String username) {
        Customer customer = customerService.getProfileByUsername(username);
        return accountRepository.findByCustomer(customer)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }
}
