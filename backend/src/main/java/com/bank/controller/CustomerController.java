package com.bank.controller;

import com.bank.dto.UpdateProfileRequest;
import com.bank.entity.Customer;
import com.bank.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /** Read: get the logged-in user's own profile. */
    @GetMapping("/me")
    public ResponseEntity<Customer> getMyProfile(Principal principal) {
        return ResponseEntity.ok(customerService.getProfileByUsername(principal.getName()));
    }

    /** Update: edit the logged-in user's own profile. */
    @PutMapping("/me")
    public ResponseEntity<Customer> updateMyProfile(Principal principal,
                                                      @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(customerService.updateProfile(principal.getName(), request));
    }
}
