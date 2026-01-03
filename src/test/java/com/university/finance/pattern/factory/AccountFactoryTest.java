package com.university.finance.pattern.factory;

import com.university.finance.model.Account;
import com.university.finance.model.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests unitaires pour la factory de comptes.
 */
public class AccountFactoryTest {

    private AccountFactory factory;
    private User owner;

    @Before
    public void setUp() {
        factory = new AccountFactory();
        owner = new User("USR-001", "testuser", "password123", "test@example.com");
    }

    @Test
    public void testCreateAccountValid() {
        Account account = factory.createAccount(owner, 1000.0);
        
        assertNotNull(account);
        assertNotNull(account.getId());
        assertTrue(account.getId().startsWith("ACC-"));
        assertEquals(owner, account.getOwner());
        assertEquals(1000.0, account.getBalance(), 0.001);
    }

    @Test
    public void testCreateAccountWithId() {
        Account account = factory.createAccount("CUSTOM-ACC-001", owner, 500.0);
        
        assertEquals("CUSTOM-ACC-001", account.getId());
    }

    @Test
    public void testCreateEmptyAccount() {
        Account account = factory.createEmptyAccount(owner);
        
        assertNotNull(account);
        assertEquals(0.0, account.getBalance(), 0.001);
    }

    @Test
    public void testCreatePremiumAccount() {
        Account account = factory.createPremiumAccount(owner, 5000.0);
        
        assertNotNull(account);
        assertTrue(account.getId().startsWith("PRM-"));
        assertEquals(5000.0, account.getBalance(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountNullOwner() {
        factory.createAccount(null, 1000.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountNegativeBalance() {
        factory.createAccount(owner, -100.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountExceedsMaximum() {
        factory.createAccount(owner, 2000000.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountWithNullId() {
        factory.createAccount(null, owner, 1000.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountWithEmptyId() {
        factory.createAccount("", owner, 1000.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatePremiumAccountInsufficientDeposit() {
        factory.createPremiumAccount(owner, 500.0);
    }

    @Test
    public void testCreateAccountZeroBalance() {
        Account account = factory.createAccount(owner, 0.0);
        
        assertNotNull(account);
        assertEquals(0.0, account.getBalance(), 0.001);
    }

    @Test
    public void testCreateAccountUniqueIds() {
        Account account1 = factory.createAccount(owner, 100.0);
        Account account2 = factory.createAccount(owner, 200.0);
        
        assertNotEquals(account1.getId(), account2.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAccountOwnerWithNullId() {
        User invalidOwner = new User(null, "test", "password", "test@example.com");
        factory.createAccount(invalidOwner, 1000.0);
    }
}

