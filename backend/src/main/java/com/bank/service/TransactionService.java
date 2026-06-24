package com.bank.service;

import com.bank.entity.Account;
import com.bank.entity.Transaction;
import com.bank.exception.InsufficientBalanceException;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    public TransactionService(TransactionRepository transactionRepository,
                               AccountRepository accountRepository,
                               AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    @Transactional
    public Transaction deposit(String username, Double amount) {
        Account account = accountService.getAccountByUsername(username);

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        Transaction transaction = new Transaction("DEPOSIT", amount, account.getBalance(), account);
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction withdraw(String username, Double amount) {
        Account account = accountService.getAccountByUsername(username);

        if (account.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance for this withdrawal");
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        Transaction transaction = new Transaction("WITHDRAW", amount, account.getBalance(), account);
        return transactionRepository.save(transaction);
    }

    /** Transaction History for the logged-in user, most recent first. */
    public List<Transaction> getHistory(String username) {
        Account account = accountService.getAccountByUsername(username);
        return transactionRepository.findByAccountOrderByTransactionDateDesc(account);
    }
}
