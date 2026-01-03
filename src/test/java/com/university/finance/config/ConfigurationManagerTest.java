package com.university.finance.config;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests unitaires pour le gestionnaire de configuration (Singleton).
 */
public class ConfigurationManagerTest {

    @Before
    public void setUp() {
        // Reset l'instance pour chaque test
        ConfigurationManager.resetInstance();
    }

    @Test
    public void testSingletonInstance() {
        ConfigurationManager instance1 = ConfigurationManager.getInstance();
        ConfigurationManager instance2 = ConfigurationManager.getInstance();
        
        assertSame(instance1, instance2);
    }

    @Test
    public void testDefaultConfiguration() {
        ConfigurationManager config = ConfigurationManager.getInstance();
        
        assertEquals("Finance Refactoring App", config.getAppName());
        assertEquals("1.0.0", config.getAppVersion());
        assertEquals("EUR", config.getCurrency());
        assertEquals(0.0, config.getMinBalance(), 0.001);
        assertEquals(10000.0, config.getMaxTransfer(), 0.001);
        assertTrue(config.isNotificationsEnabled());
        assertTrue(config.isAuditEnabled());
    }

    @Test
    public void testSetProperty() {
        ConfigurationManager config = ConfigurationManager.getInstance();
        
        config.setProperty("custom.property", "custom.value");
        assertEquals("custom.value", config.getProperty("custom.property"));
    }

    @Test
    public void testGetPropertyWithDefault() {
        ConfigurationManager config = ConfigurationManager.getInstance();
        
        assertEquals("default", config.getProperty("nonexistent", "default"));
    }

    @Test
    public void testGetIntProperty() {
        ConfigurationManager config = ConfigurationManager.getInstance();
        
        config.setProperty("test.int", "42");
        assertEquals(42, config.getIntProperty("test.int", 0));
    }

    @Test
    public void testGetIntPropertyDefault() {
        ConfigurationManager config = ConfigurationManager.getInstance();
        
        assertEquals(99, config.getIntProperty("nonexistent.int", 99));
    }

    @Test
    public void testGetIntPropertyInvalid() {
        ConfigurationManager config = ConfigurationManager.getInstance();
        
        config.setProperty("invalid.int", "not-a-number");
        assertEquals(99, config.getIntProperty("invalid.int", 99));
    }

    @Test
    public void testGetDoubleProperty() {
        ConfigurationManager config = ConfigurationManager.getInstance();
        
        config.setProperty("test.double", "3.14");
        assertEquals(3.14, config.getDoubleProperty("test.double", 0.0), 0.001);
    }

    @Test
    public void testGetDoublePropertyDefault() {
        ConfigurationManager config = ConfigurationManager.getInstance();
        
        assertEquals(9.99, config.getDoubleProperty("nonexistent.double", 9.99), 0.001);
    }

    @Test
    public void testGetBooleanProperty() {
        ConfigurationManager config = ConfigurationManager.getInstance();
        
        config.setProperty("test.bool", "true");
        assertTrue(config.getBooleanProperty("test.bool", false));
        
        config.setProperty("test.bool2", "false");
        assertFalse(config.getBooleanProperty("test.bool2", true));
    }

    @Test
    public void testGetBooleanPropertyDefault() {
        ConfigurationManager config = ConfigurationManager.getInstance();
        
        assertTrue(config.getBooleanProperty("nonexistent.bool", true));
        assertFalse(config.getBooleanProperty("nonexistent.bool", false));
    }

    @Test
    public void testGetDateFormat() {
        ConfigurationManager config = ConfigurationManager.getInstance();
        
        assertEquals("yyyy-MM-dd HH:mm:ss", config.getDateFormat());
    }

    @Test
    public void testResetInstance() {
        ConfigurationManager instance1 = ConfigurationManager.getInstance();
        instance1.setProperty("test", "value");
        
        ConfigurationManager.resetInstance();
        ConfigurationManager instance2 = ConfigurationManager.getInstance();
        
        assertNotSame(instance1, instance2);
        assertNull(instance2.getProperty("test"));
    }
}

