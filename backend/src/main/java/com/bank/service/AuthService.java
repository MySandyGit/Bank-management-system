package com.bank.service;

import com.bank.dto.JwtResponse;
import com.bank.dto.LoginRequest;
import com.bank.dto.RegisterRequest;
import com.bank.entity.*;
import com.bank.exception.DuplicateResourceException;
import com.bank.repository.AccountRepository;
import com.bank.repository.CustomerRepository;
import com.bank.repository.UserRepository;
import com.bank.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                        CustomerRepository customerRepository,
                        AccountRepository accountRepository,
                        PasswordEncoder passwordEncoder,
                        AuthenticationManager authenticationManager,
                        JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Self sign-up for a new customer:
     * creates a User (login), a Customer (profile) and an Account (balance 0)
     * all in one go, then logs them straight in by returning a JWT token.
     */
    public JwtResponse register(RegisterRequest request) {
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

        Account account = new Account(generateAccountNumber(), "SAVINGS", 0.0, customer);
        accountRepository.save(account);

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return new JwtResponse(token, user.getUsername(), user.getRole().name());
    }

    /** Verifies username/password and returns a JWT token if correct. */
    public JwtResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid username or password");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return new JwtResponse(token, user.getUsername(), user.getRole().name());
    }

    /** Simple 10-digit account number generator, e.g. 4839201756 */
    private String generateAccountNumber() {
        long number = ThreadLocalRandom.current().nextLong(1_000_000_000L, 9_999_999_999L);
        return String.valueOf(number);
    }
}
