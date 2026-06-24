package com.bank.repository;

import com.bank.entity.Customer;
import com.bank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUser(User user);
    boolean existsByEmail(String email);
}
