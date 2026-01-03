package com.university.finance.service;

import com.university.finance.config.ConfigurationManager;
import com.university.finance.model.Account;
import com.university.finance.model.Transaction;
import com.university.finance.model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests unitaires pour le service bancaire principal.
 */
public class BankingServiceTest {

    private BankingService service;

    @Before
    public void setUp() {
        // Reset la configuration singleton pour les tests
        ConfigurationManager.resetInstance();
        service = new BankingService();
    }

    @Test
    public void testCreateUser() {
        User user = service.createUser("testuser", "password123", "test@example.com");
        
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDuplicateUser() {
        service.createUser("testuser", "password123", "test1@example.com");
        service.createUser("testuser", "password456", "test2@example.com");
    }

    @Test
    public void testFindUserById() {
        User user = service.createUser("testuser", "password123", "test@example.com");
        User found = service.findUserById(user.getId());
        
        assertEquals(user, found);
    }

    @Test
    public void testFindUserByUsername() {
        service.createUser("testuser", "password123", "test@example.com");
        User found = service.findUserByUsername("testuser");
        
        assertNotNull(found);
        assertEquals("testuser", found.getUsername());
    }

    @Test
    public void testAuthenticate() {
        service.createUser("testuser", "password123", "test@example.com");
        
        User authenticated = service.authenticate("testuser", "password123");
        assertNotNull(authenticated);
        
        User failed = service.authenticate("testuser", "wrongpassword");
        assertNull(failed);
    }

    @Test
    public void testCreateAccount() {
        User user = service.createUser("testuser", "password123", "test@example.com");
        Account account = service.createAccount(user, 1000.0);
        
        assertNotNull(account);
        assertEquals(user, account.getOwner());
        assertEquals(1000.0, account.getBalance(), 0.001);
    }

    @Test
    public void testCreateUserWithAccount() {
        Account account = service.createUserWithAccount("testuser", "password123", 
                "test@example.com", 500.0);
        
        assertNotNull(account);
        assertNotNull(account.getOwner());
        assertEquals("testuser", account.getOwner().getUsername());
        assertEquals(500.0, account.getBalance(), 0.001);
    }

    @Test
    public void testFindAccountById() {
        User user = service.createUser("testuser", "password123", "test@example.com");
        Account account = service.createAccount(user, 1000.0);
        
        Account found = service.findAccountById(account.getId());
        assertEquals(account, found);
    }

    @Test
    public void testFindAccountsByUser() {
        User user = service.createUser("testuser", "password123", "test@example.com");
        service.createAccount(user, 1000.0);
        service.createAccount(user, 500.0);
        
        List<Account> accounts = service.findAccountsByUser(user);
        assertEquals(2, accounts.size());
    }

    @Test
    public void testGetBalance() {
        User user = service.createUser("testuser", "password123", "test@example.com");
        Account account = service.createAccount(user, 1000.0);
        
        assertEquals(1000.0, service.getBalance(account.getId()), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBalanceNonexistentAccount() {
        service.getBalance("NONEXISTENT");
    }

    @Test
    public void testDeposit() {
        User user = service.createUser("testuser", "password123", "test@example.com");
        Account account = service.createAccount(user, 1000.0);
        
        Transaction tx = service.deposit(account.getId(), 500.0);
        
        assertNotNull(tx);
        assertEquals(1500.0, service.getBalance(account.getId()), 0.001);
    }

    @Test
    public void testWithdraw() {
        User user = service.createUser("testuser", "password123", "test@example.com");
        Account account = service.createAccount(user, 1000.0);
        
        Transaction tx = service.withdraw(account.getId(), 300.0);
        
        assertNotNull(tx);
        assertEquals(700.0, service.getBalance(account.getId()), 0.001);
    }

    @Test
    public void testTransfer() {
        Account account1 = service.createUserWithAccount("user1", "password123", 
                "user1@example.com", 1000.0);
        Account account2 = service.createUserWithAccount("user2", "password456", 
                "user2@example.com", 500.0);
        
        Transaction tx = service.transfer(account1.getId(), account2.getId(), 300.0);
        
        assertNotNull(tx);
        assertEquals(700.0, service.getBalance(account1.getId()), 0.001);
        assertEquals(800.0, service.getBalance(account2.getId()), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferNonexistentFromAccount() {
        Account account = service.createUserWithAccount("user1", "password123", 
                "user1@example.com", 1000.0);
        service.transfer("NONEXISTENT", account.getId(), 100.0);
    }

    @Test
    public void testGetTransactionHistory() {
        User user = service.createUser("testuser", "password123", "test@example.com");
        Account account = service.createAccount(user, 1000.0);
        
        service.deposit(account.getId(), 100.0);
        service.withdraw(account.getId(), 50.0);
        
        List<Transaction> history = service.getTransactionHistory(account.getId());
        assertEquals(2, history.size());
    }

    @Test
    public void testGetAllUsers() {
        service.createUser("user1", "password123", "user1@example.com");
        service.createUser("user2", "password456", "user2@example.com");
        
        assertEquals(2, service.getAllUsers().size());
    }

    @Test
    public void testGetAllAccounts() {
        service.createUserWithAccount("user1", "password123", "user1@example.com", 100.0);
        service.createUserWithAccount("user2", "password456", "user2@example.com", 200.0);
        
        assertEquals(2, service.getAllAccounts().size());
    }

    @Test
    public void testUserCount() {
        assertEquals(0, service.getUserCount());
        service.createUser("testuser", "password123", "test@example.com");
        assertEquals(1, service.getUserCount());
    }

    @Test
    public void testAccountCount() {
        assertEquals(0, service.getAccountCount());
        service.createUserWithAccount("testuser", "password123", "test@example.com", 100.0);
        assertEquals(1, service.getAccountCount());
    }
}

