package com.bank.entity;

/**
 * Two roles in this simple bank system:
 * ADMIN - can manage all customers/accounts and view all transactions
 * USER  - a normal bank customer, can manage only their own account
 */
public enum Role {
    ADMIN,
    USER
}
