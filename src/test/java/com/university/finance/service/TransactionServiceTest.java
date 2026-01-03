package com.university.finance.service;

import com.university.finance.model.Account;
import com.university.finance.model.Transaction;
import com.university.finance.model.User;
import com.university.finance.pattern.observer.TransactionObserver;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests unitaires pour le service de transactions.
 */
public class TransactionServiceTest {

    private TransactionService service;
    private User owner1;
    private User owner2;
    private Account account1;
    private Account account2;

    @Before
    public void setUp() {
        service = new TransactionService();
        owner1 = new User("USR-001", "user1", "password", "user1@example.com");
        owner2 = new User("USR-002", "user2", "password", "user2@example.com");
        account1 = new Account("ACC-001", owner1, 1000.0);
        account2 = new Account("ACC-002", owner2, 500.0);
    }

    @Test
    public void testDeposit() {
        Transaction tx = service.deposit(account1, 500.0);
        
        assertNotNull(tx);
        assertEquals(1500.0, account1.getBalance(), 0.001);
    }

    @Test
    public void testDepositInvalidAmount() {
        Transaction tx = service.deposit(account1, -100.0);
        
        assertNull(tx);
        assertEquals(1000.0, account1.getBalance(), 0.001);
    }

    @Test
    public void testWithdraw() {
        Transaction tx = service.withdraw(account1, 300.0);
        
        assertNotNull(tx);
        assertEquals(700.0, account1.getBalance(), 0.001);
    }

    @Test
    public void testWithdrawInsufficientFunds() {
        Transaction tx = service.withdraw(account1, 1500.0);
        
        assertNull(tx);
        assertEquals(1000.0, account1.getBalance(), 0.001);
    }

    @Test
    public void testTransfer() {
        Transaction tx = service.transfer(account1, account2, 300.0);
        
        assertNotNull(tx);
        assertEquals(700.0, account1.getBalance(), 0.001);
        assertEquals(800.0, account2.getBalance(), 0.001);
    }

    @Test
    public void testTransferInsufficientFunds() {
        Transaction tx = service.transfer(account1, account2, 1500.0);
        
        assertNull(tx);
        assertEquals(1000.0, account1.getBalance(), 0.001);
        assertEquals(500.0, account2.getBalance(), 0.001);
    }

    @Test
    public void testCanDeposit() {
        assertTrue(service.canDeposit(account1, 100.0));
        assertFalse(service.canDeposit(account1, -100.0));
        assertFalse(service.canDeposit(null, 100.0));
    }

    @Test
    public void testCanWithdraw() {
        assertTrue(service.canWithdraw(account1, 500.0));
        assertFalse(service.canWithdraw(account1, 1500.0));
        assertFalse(service.canWithdraw(null, 100.0));
    }

    @Test
    public void testCanTransfer() {
        assertTrue(service.canTransfer(account1, account2, 500.0));
        assertFalse(service.canTransfer(account1, account2, 1500.0));
        assertFalse(service.canTransfer(account1, account1, 100.0));
    }

    @Test
    public void testAddObserver() {
        TestObserver observer = new TestObserver();
        service.addObserver(observer);
        
        assertEquals(1, service.getObserverCount());
    }

    @Test
    public void testRemoveObserver() {
        TestObserver observer = new TestObserver();
        service.addObserver(observer);
        service.removeObserver(observer);
        
        assertEquals(0, service.getObserverCount());
    }

    @Test
    public void testObserverNotification() {
        TestObserver observer = new TestObserver();
        service.addObserver(observer);
        
        service.deposit(account1, 100.0);
        
        assertEquals(1, observer.getNotificationCount());
    }

    @Test
    public void testMultipleObservers() {
        TestObserver observer1 = new TestObserver();
        TestObserver observer2 = new TestObserver();
        service.addObserver(observer1);
        service.addObserver(observer2);
        
        service.deposit(account1, 100.0);
        
        assertEquals(1, observer1.getNotificationCount());
        assertEquals(1, observer2.getNotificationCount());
    }

    @Test
    public void testObserverNotCalledOnFailedTransaction() {
        TestObserver observer = new TestObserver();
        service.addObserver(observer);
        
        service.withdraw(account1, 2000.0); // Should fail
        
        assertEquals(0, observer.getNotificationCount());
    }

    @Test
    public void testGetStrategies() {
        assertNotNull(service.getDepositStrategy());
        assertNotNull(service.getWithdrawStrategy());
        assertNotNull(service.getTransferStrategy());
    }

    /**
     * Observer de test pour vérifier les notifications.
     */
    private static class TestObserver implements TransactionObserver {
        private final List<Transaction> notifications = new ArrayList<>();

        @Override
        public void onTransaction(Transaction transaction) {
            notifications.add(transaction);
        }

        @Override
        public String getName() {
            return "TestObserver";
        }

        public int getNotificationCount() {
            return notifications.size();
        }
    }
}

