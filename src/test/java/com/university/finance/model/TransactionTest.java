package com.university.finance.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests unitaires pour la classe Transaction.
 */
public class TransactionTest {

    private User owner;
    private Account account;
    private Account targetAccount;

    @Before
    public void setUp() {
        owner = new User("USR-001", "testuser", "password123", "test@example.com");
        account = new Account("ACC-001", owner, 1000.0);
        
        User targetOwner = new User("USR-002", "targetuser", "password456", "target@example.com");
        targetAccount = new Account("ACC-002", targetOwner, 500.0);
    }

    @Test
    public void testDepositTransactionCreation() {
        Transaction tx = new Transaction("TX-001", Transaction.TransactionType.DEPOSIT, 
                100.0, account, "Test deposit");
        
        assertNotNull(tx);
        assertEquals("TX-001", tx.getId());
        assertEquals(Transaction.TransactionType.DEPOSIT, tx.getType());
        assertEquals(100.0, tx.getAmount(), 0.001);
        assertEquals(account, tx.getFromAccount());
        assertEquals(account, tx.getToAccount());
        assertEquals("Test deposit", tx.getDescription());
        assertNotNull(tx.getTimestamp());
    }

    @Test
    public void testTransferTransactionCreation() {
        Transaction tx = new Transaction("TX-002", Transaction.TransactionType.TRANSFER, 
                200.0, account, targetAccount, "Test transfer");
        
        assertEquals(Transaction.TransactionType.TRANSFER, tx.getType());
        assertEquals(account, tx.getFromAccount());
        assertEquals(targetAccount, tx.getToAccount());
        assertTrue(tx.isTransfer());
    }

    @Test
    public void testIsTransferForNonTransfer() {
        Transaction tx = new Transaction("TX-003", Transaction.TransactionType.DEPOSIT, 
                100.0, account, "Test deposit");
        
        assertFalse(tx.isTransfer());
    }

    @Test
    public void testIsTransferWithSameAccount() {
        Transaction tx = new Transaction("TX-004", Transaction.TransactionType.TRANSFER, 
                100.0, account, account, "Self transfer");
        
        assertFalse(tx.isTransfer());
    }

    @Test
    public void testGetFormattedTimestamp() {
        Transaction tx = new Transaction("TX-005", Transaction.TransactionType.DEPOSIT, 
                100.0, account, "Test");
        
        String formatted = tx.getFormattedTimestamp();
        assertNotNull(formatted);
        // Format: yyyy-MM-dd HH:mm:ss
        assertTrue(formatted.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    public void testTransactionTypeLabels() {
        assertEquals("Dépôt", Transaction.TransactionType.DEPOSIT.getLabel());
        assertEquals("Retrait", Transaction.TransactionType.WITHDRAWAL.getLabel());
        assertEquals("Transfert", Transaction.TransactionType.TRANSFER.getLabel());
    }

    @Test
    public void testEqualsWithSameId() {
        Transaction tx1 = new Transaction("TX-006", Transaction.TransactionType.DEPOSIT, 
                100.0, account, "Test");
        Transaction tx2 = new Transaction("TX-006", Transaction.TransactionType.WITHDRAWAL, 
                200.0, account, "Different");
        
        assertEquals(tx1, tx2);
    }

    @Test
    public void testEqualsWithDifferentId() {
        Transaction tx1 = new Transaction("TX-007", Transaction.TransactionType.DEPOSIT, 
                100.0, account, "Test");
        Transaction tx2 = new Transaction("TX-008", Transaction.TransactionType.DEPOSIT, 
                100.0, account, "Test");
        
        assertNotEquals(tx1, tx2);
    }

    @Test
    public void testToStringContainsInfo() {
        Transaction tx = new Transaction("TX-009", Transaction.TransactionType.DEPOSIT, 
                100.0, account, "Test description");
        
        String str = tx.toString();
        assertTrue(str.contains("Dépôt"));
        assertTrue(str.contains("100"));
    }
}

