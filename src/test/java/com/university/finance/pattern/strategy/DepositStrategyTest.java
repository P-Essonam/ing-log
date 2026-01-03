package com.university.finance.pattern.strategy;

import com.university.finance.model.Account;
import com.university.finance.model.Transaction;
import com.university.finance.model.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests unitaires pour la stratégie de dépôt.
 */
public class DepositStrategyTest {

    private DepositStrategy strategy;
    private User owner;
    private Account account;

    @Before
    public void setUp() {
        strategy = new DepositStrategy();
        owner = new User("USR-001", "testuser", "password", "test@example.com");
        account = new Account("ACC-001", owner, 1000.0);
    }

    @Test
    public void testGetType() {
        assertEquals("DEPOSIT", strategy.getType());
    }

    @Test
    public void testExecutePositiveAmount() {
        Transaction tx = strategy.execute(account, 500.0);
        
        assertNotNull(tx);
        assertEquals(Transaction.TransactionType.DEPOSIT, tx.getType());
        assertEquals(500.0, tx.getAmount(), 0.001);
        assertEquals(1500.0, account.getBalance(), 0.001);
        assertEquals(1, account.getTransactions().size());
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
    public void testCanExecuteValid() {
        assertTrue(strategy.canExecute(account, 100.0));
    }

    @Test
    public void testCanExecuteWithNullAccount() {
        assertFalse(strategy.canExecute(null, 100.0));
    }

    @Test
    public void testCanExecuteWithZeroAmount() {
        assertFalse(strategy.canExecute(account, 0.0));
    }

    @Test
    public void testCanExecuteWithNegativeAmount() {
        assertFalse(strategy.canExecute(account, -50.0));
    }

    @Test
    public void testMultipleDeposits() {
        strategy.execute(account, 100.0);
        strategy.execute(account, 200.0);
        strategy.execute(account, 300.0);
        
        assertEquals(1600.0, account.getBalance(), 0.001);
        assertEquals(3, account.getTransactions().size());
    }
}

