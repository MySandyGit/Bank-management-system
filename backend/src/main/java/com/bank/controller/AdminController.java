package com.bank.controller;

import com.bank.dto.MessageResponse;
import com.bank.dto.RegisterRequest;
import com.bank.dto.UpdateProfileRequest;
import com.bank.entity.Account;
import com.bank.entity.Customer;
import com.bank.entity.Transaction;
import com.bank.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Every endpoint here is restricted to ROLE_ADMIN by SecurityConfig
 * (any path starting with /api/admin/).
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ---------- Read ----------

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(adminService.getAllCustomers());
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getCustomerById(id));
    }

    @GetMapping("/customers/{id}/account")
    public ResponseEntity<Account> getCustomerAccount(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getAccountByCustomerId(id));
    }

    @GetMapping("/customers/{id}/transactions")
    public ResponseEntity<List<Transaction>> getCustomerTransactions(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getAllTransactionsForCustomer(id));
    }

    // ---------- Create ----------

    @PostMapping("/customers")
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(adminService.createCustomer(request));
    }

    // ---------- Update ----------

    @PutMapping("/customers/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id,
                                                     @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(adminService.updateCustomer(id, request));
    }

    // ---------- Delete ----------

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<MessageResponse> deleteCustomer(@PathVariable Long id) {
        adminService.deleteCustomer(id);
        return ResponseEntity.ok(new MessageResponse("Customer deleted successfully"));
    }
}
