package com.bank.exception;

/** Thrown when a withdrawal amount is greater than the available balance. */
public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
