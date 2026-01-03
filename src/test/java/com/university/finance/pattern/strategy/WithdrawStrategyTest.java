package com.university.finance.pattern.strategy;

import com.university.finance.model.Account;
import com.university.finance.model.Transaction;
import com.university.finance.model.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests unitaires pour la stratégie de retrait.
 */
public class WithdrawStrategyTest {

    private WithdrawStrategy strategy;
    private User owner;
    private Account account;

    @Before
    public void setUp() {
        strategy = new WithdrawStrategy();
        owner = new User("USR-001", "testuser", "password", "test@example.com");
        account = new Account("ACC-001", owner, 1000.0);
    }

    @Test
    public void testGetType() {
        assertEquals("WITHDRAWAL", strategy.getType());
    }

    @Test
    public void testExecuteWithSufficientFunds() {
        Transaction tx = strategy.execute(account, 500.0);
        
        assertNotNull(tx);
        assertEquals(Transaction.TransactionType.WITHDRAWAL, tx.getType());
        assertEquals(500.0, tx.getAmount(), 0.001);
        assertEquals(500.0, account.getBalance(), 0.001);
        assertEquals(1, account.getTransactions().size());
    }

    @Test
    public void testExecuteEntireBalance() {
        Transaction tx = strategy.execute(account, 1000.0);
        
        assertNotNull(tx);
        assertEquals(0.0, account.getBalance(), 0.001);
    }

    @Test
    public void testExecuteWithInsufficientFunds() {
        Transaction tx = strategy.execute(account, 1500.0);
        
        assertNull(tx);
        assertEquals(1000.0, account.getBalance(), 0.001);
    }

    @Test
    public void testExecuteZeroAmount() {
        Transaction tx = strategy.execute(account, 0.0);
        
        assertNull(tx);
        assertEquals(1000.0, account.getBalance(), 0.001);
    }

    @Test
    public void testExecuteNegativeAmount() {
        Transaction tx = strategy.execute(account, -100.0);
        
        assertNull(tx);
        assertEquals(1000.0, account.getBalance(), 0.001);
    }

    @Test
    public void testExecuteWithNullAccount() {
        Transaction tx = strategy.execute(null, 100.0);
        
        assertNull(tx);
    }

    @Test
    public void testCanExecuteWithSufficientFunds() {
        assertTrue(strategy.canExecute(account, 500.0));
        assertTrue(strategy.canExecute(account, 1000.0));
    }

    @Test
    public void testCanExecuteWithInsufficientFunds() {
        assertFalse(strategy.canExecute(account, 1500.0));
    }

    @Test
    public void testCanExecuteWithNullAccount() {
        assertFalse(strategy.canExecute(null, 100.0));
    }

    @Test
    public void testMultipleWithdrawals() {
        strategy.execute(account, 200.0);
        strategy.execute(account, 300.0);
        
        assertEquals(500.0, account.getBalance(), 0.001);
        assertEquals(2, account.getTransactions().size());
    }
}

