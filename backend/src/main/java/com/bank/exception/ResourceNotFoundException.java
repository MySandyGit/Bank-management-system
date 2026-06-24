package com.bank.exception;

/** Thrown when a customer/account/user is not found in the database. */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
