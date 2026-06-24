package com.bank.service;

import com.bank.dto.UpdateProfileRequest;
import com.bank.entity.Customer;
import com.bank.entity.User;
import com.bank.exception.ResourceNotFoundException;
import com.bank.repository.CustomerRepository;
import com.bank.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public CustomerService(CustomerRepository customerRepository, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    /** Finds the Customer profile that belongs to the currently logged-in username. */
    public Customer getProfileByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return customerRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found"));
    }

    /** Update operation (CRUD): the logged-in user edits their own profile details. */
    public Customer updateProfile(String username, UpdateProfileRequest request) {
        Customer customer = getProfileByUsername(username);
        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        return customerRepository.save(customer);
    }
}
