package com.university.finance.pattern.factory;

import com.university.finance.model.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests unitaires pour la factory d'utilisateurs.
 */
public class UserFactoryTest {

    private UserFactory factory;

    @Before
    public void setUp() {
        factory = new UserFactory();
    }

    @Test
    public void testCreateUserValid() {
        User user = factory.createUser("testuser", "password123", "test@example.com");
        
        assertNotNull(user);
        assertNotNull(user.getId());
        assertTrue(user.getId().startsWith("USR-"));
        assertEquals("testuser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    public void testCreateUserWithId() {
        User user = factory.createUser("CUSTOM-001", "testuser", "password123", "test@example.com");
        
        assertEquals("CUSTOM-001", user.getId());
    }

    @Test
    public void testCreateSimpleUser() {
        User user = factory.createSimpleUser("simpleuser", "password123");
        
        assertNotNull(user);
        assertEquals("simpleuser", user.getUsername());
        assertTrue(user.getEmail().contains("simpleuser"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUserNullUsername() {
        factory.createUser(null, "password123", "test@example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUserEmptyUsername() {
        factory.createUser("", "password123", "test@example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUserShortUsername() {
        factory.createUser("ab", "password123", "test@example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUserInvalidUsernameCharacters() {
        factory.createUser("test user!", "password123", "test@example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUserNullPassword() {
        factory.createUser("testuser", null, "test@example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUserEmptyPassword() {
        factory.createUser("testuser", "", "test@example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUserShortPassword() {
        factory.createUser("testuser", "abc", "test@example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUserNullEmail() {
        factory.createUser("testuser", "password123", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUserInvalidEmail() {
        factory.createUser("testuser", "password123", "invalidemail");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUserWithNullId() {
        factory.createUser(null, "testuser", "password123", "test@example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUserWithEmptyId() {
        factory.createUser("", "testuser", "password123", "test@example.com");
    }

    @Test
    public void testCreateUserUniqueIds() {
        User user1 = factory.createUser("user1", "password123", "user1@example.com");
        User user2 = factory.createUser("user2", "password123", "user2@example.com");
        
        assertNotEquals(user1.getId(), user2.getId());
    }

    @Test
    public void testValidUsernameWithUnderscore() {
        User user = factory.createUser("test_user_123", "password123", "test@example.com");
        assertEquals("test_user_123", user.getUsername());
    }
}

