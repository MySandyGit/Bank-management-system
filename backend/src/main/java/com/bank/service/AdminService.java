package com.bank.service;

import com.bank.dto.RegisterRequest;
import com.bank.dto.UpdateProfileRequest;
import com.bank.entity.*;
import com.bank.exception.DuplicateResourceException;
import com.bank.exception.ResourceNotFoundException;
import com.bank.repository.AccountRepository;
import com.bank.repository.CustomerRepository;
import com.bank.repository.TransactionRepository;
import com.bank.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * All the admin-only CRUD operations on customers/accounts.
 * (Create, Read, Update, Delete)
 */
@Service
public class AdminService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(UserRepository userRepository,
                         CustomerRepository customerRepository,
                         AccountRepository accountRepository,
                         TransactionRepository transactionRepository,
                         PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ---------- READ ----------

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
    }

    public Account getAccountByCustomerId(Long customerId) {
        Customer customer = getCustomerById(customerId);
        return accountRepository.findByCustomer(customer)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    // ---------- CREATE ----------

    /** Admin manually creates a brand-new customer (with login, profile and account). */
    @Transactional
    public Customer createCustomer(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already taken");
        }
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered");
        }

        User user = new User(request.getUsername(), passwordEncoder.encode(request.getPassword()), Role.USER);
        userRepository.save(user);

        Customer customer = new Customer(request.getFullName(), request.getEmail(),
                request.getPhone(), request.getAddress(), user);
        customerRepository.save(customer);

        String accountNumber = String.valueOf(ThreadLocalRandom.current().nextLong(1_000_000_000L, 9_999_999_999L));
        Account account = new Account(accountNumber, "SAVINGS", 0.0, customer);
        accountRepository.save(account);

        return customer;
    }

    // ---------- UPDATE ----------

    public Customer updateCustomer(Long id, UpdateProfileRequest request) {
        Customer customer = getCustomerById(id);
        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        return customerRepository.save(customer);
    }

    // ---------- DELETE ----------

    /** Deletes the customer along with their account, transactions and login. */
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        Account account = accountRepository.findByCustomer(customer).orElse(null);

        if (account != null) {
            transactionRepository.deleteAll(transactionRepository.findByAccountOrderByTransactionDateDesc(account));
            accountRepository.delete(account);
        }

        User user = customer.getUser();
        customerRepository.delete(customer);

        if (user != null) {
            userRepository.delete(user);
        }
    }

    public List<Transaction> getAllTransactionsForCustomer(Long customerId) {
        Account account = getAccountByCustomerId(customerId);
        return transactionRepository.findByAccountOrderByTransactionDateDesc(account);
    }
}
