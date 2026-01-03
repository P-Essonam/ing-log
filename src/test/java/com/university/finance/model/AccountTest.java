package com.university.finance.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests unitaires pour la classe Account.
 */
public class AccountTest {

    private User owner;
    private Account account;

    @Before
    public void setUp() {
        owner = new User("USR-001", "testuser", "password123", "test@example.com");
        account = new Account("ACC-001", owner, 1000.0);
    }

    @Test
    public void testAccountCreation() {
        assertNotNull(account);
        assertEquals("ACC-001", account.getId());
        assertEquals(owner, account.getOwner());
        assertEquals(1000.0, account.getBalance(), 0.001);
        assertTrue(account.getTransactions().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAccountCreationWithNegativeBalance() {
        new Account("ACC-002", owner, -100.0);
    }

    @Test
    public void testCreditPositiveAmount() {
        assertTrue(account.credit(500.0));
        assertEquals(1500.0, account.getBalance(), 0.001);
    }

    @Test
    public void testCreditZeroAmount() {
        assertFalse(account.credit(0.0));
        assertEquals(1000.0, account.getBalance(), 0.001);
    }

    @Test
    public void testCreditNegativeAmount() {
        assertFalse(account.credit(-100.0));
        assertEquals(1000.0, account.getBalance(), 0.001);
    }

    @Test
    public void testDebitValidAmount() {
        assertTrue(account.debit(500.0));
        assertEquals(500.0, account.getBalance(), 0.001);
    }

    @Test
    public void testDebitEntireBalance() {
        assertTrue(account.debit(1000.0));
        assertEquals(0.0, account.getBalance(), 0.001);
    }

    @Test
    public void testDebitInsufficientFunds() {
        assertFalse(account.debit(1500.0));
        assertEquals(1000.0, account.getBalance(), 0.001);
    }

    @Test
    public void testDebitZeroAmount() {
        assertFalse(account.debit(0.0));
        assertEquals(1000.0, account.getBalance(), 0.001);
    }

    @Test
    public void testDebitNegativeAmount() {
        assertFalse(account.debit(-100.0));
        assertEquals(1000.0, account.getBalance(), 0.001);
    }

    @Test
    public void testHasSufficientFunds() {
        assertTrue(account.hasSufficientFunds(500.0));
        assertTrue(account.hasSufficientFunds(1000.0));
        assertFalse(account.hasSufficientFunds(1500.0));
    }

    @Test
    public void testAddTransaction() {
        Transaction tx = new Transaction("TX-001", Transaction.TransactionType.DEPOSIT, 
                100.0, account, "Test deposit");
        account.addTransaction(tx);
        
        assertEquals(1, account.getTransactions().size());
        assertEquals(tx, account.getTransactions().get(0));
    }

    @Test
    public void testEqualsWithSameId() {
        Account sameAccount = new Account("ACC-001", owner, 500.0);
        assertEquals(account, sameAccount);
    }

    @Test
    public void testEqualsWithDifferentId() {
        Account differentAccount = new Account("ACC-002", owner, 1000.0);
        assertNotEquals(account, differentAccount);
    }
}

