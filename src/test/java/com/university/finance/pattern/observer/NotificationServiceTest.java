package com.university.finance.pattern.observer;

import com.university.finance.model.Account;
import com.university.finance.model.Transaction;
import com.university.finance.model.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests unitaires pour le service de notifications.
 */
public class NotificationServiceTest {

    private NotificationService service;
    private User owner;
    private Account account;

    @Before
    public void setUp() {
        service = new NotificationService();
        owner = new User("USR-001", "testuser", "password", "test@example.com");
        account = new Account("ACC-001", owner, 1000.0);
    }

    @Test
    public void testGetName() {
        assertEquals("NotificationService", service.getName());
    }

    @Test
    public void testDefaultConfiguration() {
        assertFalse(service.isEmailEnabled());
        assertFalse(service.isSmsEnabled());
        assertTrue(service.isConsoleEnabled());
    }

    @Test
    public void testCustomConfiguration() {
        NotificationService customService = new NotificationService(true, true, false);
        
        assertTrue(customService.isEmailEnabled());
        assertTrue(customService.isSmsEnabled());
        assertFalse(customService.isConsoleEnabled());
    }

    @Test
    public void testOnTransactionDeposit() {
        account.credit(500.0); // Update balance for notification message
        Transaction tx = new Transaction("TX-001", Transaction.TransactionType.DEPOSIT, 
                500.0, account, "Test deposit");
        
        service.onTransaction(tx);
        
        assertEquals(1, service.getNotificationCount());
        String notification = service.getSentNotifications().get(0);
        assertTrue(notification.contains("Dépôt"));
        assertTrue(notification.contains("500"));
    }

    @Test
    public void testOnTransactionWithdrawal() {
        Transaction tx = new Transaction("TX-002", Transaction.TransactionType.WITHDRAWAL, 
                200.0, account, "Test withdrawal");
        
        service.onTransaction(tx);
        
        assertEquals(1, service.getNotificationCount());
        assertTrue(service.getSentNotifications().get(0).contains("Retrait"));
    }

    @Test
    public void testOnTransactionTransfer() {
        User owner2 = new User("USR-002", "user2", "password", "user2@example.com");
        Account toAccount = new Account("ACC-002", owner2, 500.0);
        
        Transaction tx = new Transaction("TX-003", Transaction.TransactionType.TRANSFER, 
                300.0, account, toAccount, "Test transfer");
        
        service.onTransaction(tx);
        
        assertEquals(1, service.getNotificationCount());
        String notification = service.getSentNotifications().get(0);
        assertTrue(notification.contains("Transfert"));
        assertTrue(notification.contains("ACC-001"));
        assertTrue(notification.contains("ACC-002"));
    }

    @Test
    public void testClearNotifications() {
        Transaction tx = new Transaction("TX-001", Transaction.TransactionType.DEPOSIT, 
                100.0, account, "Test");
        
        service.onTransaction(tx);
        assertEquals(1, service.getNotificationCount());
        
        service.clearNotifications();
        assertEquals(0, service.getNotificationCount());
    }

    @Test
    public void testSettersAndGetters() {
        service.setEmailEnabled(true);
        service.setSmsEnabled(true);
        service.setConsoleEnabled(false);
        
        assertTrue(service.isEmailEnabled());
        assertTrue(service.isSmsEnabled());
        assertFalse(service.isConsoleEnabled());
    }

    @Test
    public void testGetSentNotificationsReturnsUnmodifiableList() {
        Transaction tx = new Transaction("TX-001", Transaction.TransactionType.DEPOSIT, 
                100.0, account, "Test");
        service.onTransaction(tx);
        
        try {
            service.getSentNotifications().add("Should not be added");
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
}

