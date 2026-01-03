package com.university.finance.pattern.observer;

import com.university.finance.model.Account;
import com.university.finance.model.Transaction;
import com.university.finance.model.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests unitaires pour le logger d'audit.
 */
public class AuditLoggerTest {

    private AuditLogger logger;
    private User owner;
    private Account account;

    @Before
    public void setUp() {
        logger = new AuditLogger();
        owner = new User("USR-001", "testuser", "password", "test@example.com");
        account = new Account("ACC-001", owner, 1000.0);
    }

    @Test
    public void testGetName() {
        assertEquals("AuditLogger", logger.getName());
    }

    @Test
    public void testOnTransactionDeposit() {
        Transaction tx = new Transaction("TX-001", Transaction.TransactionType.DEPOSIT, 
                100.0, account, "Test deposit");
        
        logger.onTransaction(tx);
        
        assertEquals(1, logger.getLogCount());
        assertTrue(logger.getAuditLog().get(0).contains("TX-001"));
        assertTrue(logger.getAuditLog().get(0).contains("Dépôt"));
    }

    @Test
    public void testOnTransactionTransfer() {
        User owner2 = new User("USR-002", "user2", "password", "user2@example.com");
        Account toAccount = new Account("ACC-002", owner2, 500.0);
        
        Transaction tx = new Transaction("TX-002", Transaction.TransactionType.TRANSFER, 
                200.0, account, toAccount, "Test transfer");
        
        logger.onTransaction(tx);
        
        assertEquals(1, logger.getLogCount());
        String logEntry = logger.getAuditLog().get(0);
        assertTrue(logEntry.contains("Transfert"));
        assertTrue(logEntry.contains("ACC-001"));
        assertTrue(logEntry.contains("ACC-002"));
    }

    @Test
    public void testMultipleTransactions() {
        Transaction tx1 = new Transaction("TX-001", Transaction.TransactionType.DEPOSIT, 
                100.0, account, "Deposit 1");
        Transaction tx2 = new Transaction("TX-002", Transaction.TransactionType.WITHDRAWAL, 
                50.0, account, "Withdrawal 1");
        
        logger.onTransaction(tx1);
        logger.onTransaction(tx2);
        
        assertEquals(2, logger.getLogCount());
    }

    @Test
    public void testClearLog() {
        Transaction tx = new Transaction("TX-001", Transaction.TransactionType.DEPOSIT, 
                100.0, account, "Test");
        
        logger.onTransaction(tx);
        assertEquals(1, logger.getLogCount());
        
        logger.clearLog();
        assertEquals(0, logger.getLogCount());
    }

    @Test
    public void testGetAuditLogReturnsUnmodifiableList() {
        Transaction tx = new Transaction("TX-001", Transaction.TransactionType.DEPOSIT, 
                100.0, account, "Test");
        logger.onTransaction(tx);
        
        try {
            logger.getAuditLog().add("Should not be added");
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testLogWithFilePath() {
        AuditLogger fileLogger = new AuditLogger("test-audit.log");
        fileLogger.setWriteToFile(false); // Disable for test
        
        Transaction tx = new Transaction("TX-001", Transaction.TransactionType.DEPOSIT, 
                100.0, account, "Test");
        fileLogger.onTransaction(tx);
        
        assertEquals(1, fileLogger.getLogCount());
    }
}

