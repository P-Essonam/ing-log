package com.university.finance.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests unitaires pour la classe User.
 */
public class UserTest {

    private User user;

    @Before
    public void setUp() {
        user = new User("USR-001", "testuser", "password123", "test@example.com");
    }

    @Test
    public void testUserCreation() {
        assertNotNull(user);
        assertEquals("USR-001", user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    public void testCheckPasswordValid() {
        assertTrue(user.checkPassword("password123"));
    }

    @Test
    public void testCheckPasswordInvalid() {
        assertFalse(user.checkPassword("wrongpassword"));
    }

    @Test
    public void testSetUsername() {
        user.setUsername("newusername");
        assertEquals("newusername", user.getUsername());
    }

    @Test
    public void testSetPassword() {
        user.setPassword("newpassword");
        assertEquals("newpassword", user.getPassword());
        assertTrue(user.checkPassword("newpassword"));
    }

    @Test
    public void testSetEmail() {
        user.setEmail("newemail@example.com");
        assertEquals("newemail@example.com", user.getEmail());
    }

    @Test
    public void testEqualsWithSameId() {
        User sameUser = new User("USR-001", "different", "different", "diff@example.com");
        assertEquals(user, sameUser);
    }

    @Test
    public void testEqualsWithDifferentId() {
        User differentUser = new User("USR-002", "testuser", "password123", "test@example.com");
        assertNotEquals(user, differentUser);
    }

    @Test
    public void testHashCode() {
        User sameUser = new User("USR-001", "different", "different", "diff@example.com");
        assertEquals(user.hashCode(), sameUser.hashCode());
    }

    @Test
    public void testToString() {
        String str = user.toString();
        assertTrue(str.contains("USR-001"));
        assertTrue(str.contains("testuser"));
        assertTrue(str.contains("test@example.com"));
        // Le mot de passe ne doit pas apparaître dans toString pour la sécurité
        assertFalse(str.contains("password123"));
    }
}

