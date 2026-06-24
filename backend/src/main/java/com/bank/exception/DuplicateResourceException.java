package com.bank.exception;

/** Thrown when registering with a username or email that already exists. */
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
