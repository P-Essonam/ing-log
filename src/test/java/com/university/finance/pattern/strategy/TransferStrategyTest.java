package com.university.finance.pattern.strategy;

import com.university.finance.model.Account;
import com.university.finance.model.Transaction;
import com.university.finance.model.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests unitaires pour la stratégie de transfert.
 */
public class TransferStrategyTest {

    private TransferStrategy strategy;
    private User owner1;
    private User owner2;
    private Account fromAccount;
    private Account toAccount;

    @Before
    public void setUp() {
        strategy = new TransferStrategy();
        owner1 = new User("USR-001", "user1", "password", "user1@example.com");
        owner2 = new User("USR-002", "user2", "password", "user2@example.com");
        fromAccount = new Account("ACC-001", owner1, 1000.0);
        toAccount = new Account("ACC-002", owner2, 500.0);
    }

    @Test
    public void testGetType() {
        assertEquals("TRANSFER", strategy.getType());
    }

    @Test
    public void testExecuteValidTransfer() {
        Transaction tx = strategy.execute(fromAccount, toAccount, 300.0);
        
        assertNotNull(tx);
        assertEquals(Transaction.TransactionType.TRANSFER, tx.getType());
        assertEquals(300.0, tx.getAmount(), 0.001);
        assertEquals(700.0, fromAccount.getBalance(), 0.001);
        assertEquals(800.0, toAccount.getBalance(), 0.001);
    }

    @Test
    public void testExecuteTransferRecordsInBothAccounts() {
        strategy.execute(fromAccount, toAccount, 200.0);
        
        assertEquals(1, fromAccount.getTransactions().size());
        assertEquals(1, toAccount.getTransactions().size());
    }

    @Test
    public void testExecuteWithInsufficientFunds() {
        Transaction tx = strategy.execute(fromAccount, toAccount, 1500.0);
        
        assertNull(tx);
        assertEquals(1000.0, fromAccount.getBalance(), 0.001);
        assertEquals(500.0, toAccount.getBalance(), 0.001);
    }

    @Test
    public void testExecuteWithNullFromAccount() {
        Transaction tx = strategy.execute(null, toAccount, 100.0);
        
        assertNull(tx);
    }

    @Test
    public void testExecuteWithNullToAccount() {
        Transaction tx = strategy.execute(fromAccount, null, 100.0);
        
        assertNull(tx);
    }

    @Test
    public void testExecuteWithSameAccount() {
        Transaction tx = strategy.execute(fromAccount, fromAccount, 100.0);
        
        assertNull(tx);
    }

    @Test
    public void testExecuteWithZeroAmount() {
        Transaction tx = strategy.execute(fromAccount, toAccount, 0.0);
        
        assertNull(tx);
    }

    @Test
    public void testExecuteWithNegativeAmount() {
        Transaction tx = strategy.execute(fromAccount, toAccount, -100.0);
        
        assertNull(tx);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExecuteSingleAccountThrowsException() {
        strategy.execute(fromAccount, 100.0);
    }

    @Test
    public void testCanExecuteValid() {
        assertTrue(strategy.canExecute(fromAccount, toAccount, 500.0));
    }

    @Test
    public void testCanExecuteWithInsufficientFunds() {
        assertFalse(strategy.canExecute(fromAccount, toAccount, 1500.0));
    }

    @Test
    public void testCanExecuteWithNullFromAccount() {
        assertFalse(strategy.canExecute(null, toAccount, 100.0));
    }

    @Test
    public void testCanExecuteWithNullToAccount() {
        assertFalse(strategy.canExecute(fromAccount, null, 100.0));
    }

    @Test
    public void testCanExecuteWithSameAccount() {
        assertFalse(strategy.canExecute(fromAccount, fromAccount, 100.0));
    }

    @Test
    public void testCanExecuteSingleAccount() {
        assertFalse(strategy.canExecute(fromAccount, 100.0));
    }
}

