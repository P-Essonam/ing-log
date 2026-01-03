package com.university.finance.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Gestionnaire de configuration singleton.
 * Implémente le pattern Singleton pour centraliser la configuration de l'application.
 * Thread-safe avec initialisation paresseuse (lazy initialization).
 */
public class ConfigurationManager {

    // Instance unique (volatile pour la visibilité entre threads)
    private static volatile ConfigurationManager instance;

    // Propriétés de configuration
    private final Properties properties;

    // Valeurs par défaut
    private static final String DEFAULT_CURRENCY = "EUR";
    private static final double DEFAULT_MIN_BALANCE = 0.0;
    private static final double DEFAULT_MAX_TRANSFER = 10000.0;
    private static final boolean DEFAULT_NOTIFICATIONS_ENABLED = true;
    private static final boolean DEFAULT_AUDIT_ENABLED = true;

    /**
     * Constructeur privé (pattern Singleton).
     */
    private ConfigurationManager() {
        this.properties = new Properties();
        loadDefaultConfiguration();
    }

    /**
     * Retourne l'instance unique du gestionnaire de configuration.
     * Utilise le pattern Double-Checked Locking pour la thread-safety.
     *
     * @return Instance unique de ConfigurationManager
     */
    public static ConfigurationManager getInstance() {
        if (instance == null) {
            synchronized (ConfigurationManager.class) {
                if (instance == null) {
                    instance = new ConfigurationManager();
                }
            }
        }
        return instance;
    }

    /**
     * Charge la configuration par défaut.
     */
    private void loadDefaultConfiguration() {
        properties.setProperty("app.name", "Finance Refactoring App");
        properties.setProperty("app.version", "1.0.0");
        properties.setProperty("currency", DEFAULT_CURRENCY);
        properties.setProperty("min.balance", String.valueOf(DEFAULT_MIN_BALANCE));
        properties.setProperty("max.transfer", String.valueOf(DEFAULT_MAX_TRANSFER));
        properties.setProperty("notifications.enabled", String.valueOf(DEFAULT_NOTIFICATIONS_ENABLED));
        properties.setProperty("audit.enabled", String.valueOf(DEFAULT_AUDIT_ENABLED));
        properties.setProperty("date.format", "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Charge la configuration depuis un fichier.
     *
     * @param filePath Chemin du fichier de configuration
     * @throws IOException Si le fichier ne peut pas être lu
     */
    public void loadFromFile(String filePath) throws IOException {
        try (InputStream input = new FileInputStream(filePath)) {
            properties.load(input);
        }
    }

    /**
     * Charge la configuration depuis le classpath.
     *
     * @param resourceName Nom de la ressource
     * @throws IOException Si la ressource ne peut pas être lue
     */
    public void loadFromClasspath(String resourceName) throws IOException {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (input == null) {
                throw new IOException("Ressource non trouvée: " + resourceName);
            }
            properties.load(input);
        }
    }

    // Getters pour les propriétés courantes

    /**
     * Retourne le nom de l'application.
     *
     * @return Nom de l'application
     */
    public String getAppName() {
        return properties.getProperty("app.name");
    }

    /**
     * Retourne la version de l'application.
     *
     * @return Version de l'application
     */
    public String getAppVersion() {
        return properties.getProperty("app.version");
    }

    /**
     * Retourne la devise utilisée.
     *
     * @return Code de la devise
     */
    public String getCurrency() {
        return properties.getProperty("currency", DEFAULT_CURRENCY);
    }

    /**
     * Retourne le solde minimum autorisé.
     *
     * @return Solde minimum
     */
    public double getMinBalance() {
        return Double.parseDouble(properties.getProperty("min.balance", 
                String.valueOf(DEFAULT_MIN_BALANCE)));
    }

    /**
     * Retourne le montant maximum de transfert autorisé.
     *
     * @return Montant maximum
     */
    public double getMaxTransfer() {
        return Double.parseDouble(properties.getProperty("max.transfer", 
                String.valueOf(DEFAULT_MAX_TRANSFER)));
    }

    /**
     * Vérifie si les notifications sont activées.
     *
     * @return true si les notifications sont activées
     */
    public boolean isNotificationsEnabled() {
        return Boolean.parseBoolean(properties.getProperty("notifications.enabled", 
                String.valueOf(DEFAULT_NOTIFICATIONS_ENABLED)));
    }

    /**
     * Vérifie si l'audit est activé.
     *
     * @return true si l'audit est activé
     */
    public boolean isAuditEnabled() {
        return Boolean.parseBoolean(properties.getProperty("audit.enabled", 
                String.valueOf(DEFAULT_AUDIT_ENABLED)));
    }

    /**
     * Retourne le format de date configuré.
     *
     * @return Format de date
     */
    public String getDateFormat() {
        return properties.getProperty("date.format", "yyyy-MM-dd HH:mm:ss");
    }

    // Getters/Setters génériques

    /**
     * Retourne une propriété par son nom.
     *
     * @param key Clé de la propriété
     * @return Valeur de la propriété ou null
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Retourne une propriété avec une valeur par défaut.
     *
     * @param key          Clé de la propriété
     * @param defaultValue Valeur par défaut
     * @return Valeur de la propriété ou valeur par défaut
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Définit une propriété.
     *
     * @param key   Clé de la propriété
     * @param value Valeur de la propriété
     */
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    /**
     * Retourne une propriété sous forme d'entier.
     *
     * @param key          Clé de la propriété
     * @param defaultValue Valeur par défaut
     * @return Valeur entière
     */
    public int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Retourne une propriété sous forme de double.
     *
     * @param key          Clé de la propriété
     * @param defaultValue Valeur par défaut
     * @return Valeur double
     */
    public double getDoubleProperty(String key, double defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Retourne une propriété sous forme de booléen.
     *
     * @param key          Clé de la propriété
     * @param defaultValue Valeur par défaut
     * @return Valeur booléenne
     */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    /**
     * Réinitialise l'instance (pour les tests uniquement).
     * Note: Cette méthode ne devrait pas être utilisée en production.
     */
    public static void resetInstance() {
        synchronized (ConfigurationManager.class) {
            instance = null;
        }
    }
}

