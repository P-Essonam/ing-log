package com.university.finance.pattern.factory;

import com.university.finance.model.User;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Factory pour la création d'utilisateurs.
 * Implémente le pattern Factory pour centraliser la création d'objets User
 * avec validation des données.
 */
public class UserFactory {

    // Pattern pour valider l'email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );

    // Longueur minimale du mot de passe
    private static final int MIN_PASSWORD_LENGTH = 4;

    // Longueur minimale du nom d'utilisateur
    private static final int MIN_USERNAME_LENGTH = 3;

    /**
     * Crée un nouvel utilisateur avec un ID généré automatiquement.
     *
     * @param username Nom d'utilisateur
     * @param password Mot de passe
     * @param email    Adresse email
     * @return Le nouvel utilisateur créé
     * @throws IllegalArgumentException si les données sont invalides
     */
    public User createUser(String username, String password, String email) {
        validateUsername(username);
        validatePassword(password);
        validateEmail(email);

        String id = generateUserId();
        return new User(id, username, password, email);
    }

    /**
     * Crée un nouvel utilisateur avec un ID spécifique.
     *
     * @param id       Identifiant de l'utilisateur
     * @param username Nom d'utilisateur
     * @param password Mot de passe
     * @param email    Adresse email
     * @return Le nouvel utilisateur créé
     * @throws IllegalArgumentException si les données sont invalides
     */
    public User createUser(String id, String username, String password, String email) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID ne peut pas être vide");
        }
        validateUsername(username);
        validatePassword(password);
        validateEmail(email);

        return new User(id, username, password, email);
    }

    /**
     * Crée un utilisateur avec des données minimales (sans email).
     *
     * @param username Nom d'utilisateur
     * @param password Mot de passe
     * @return Le nouvel utilisateur créé
     */
    public User createSimpleUser(String username, String password) {
        validateUsername(username);
        validatePassword(password);

        String id = generateUserId();
        return new User(id, username, password, username + "@default.com");
    }

    /**
     * Valide le nom d'utilisateur.
     *
     * @param username Nom d'utilisateur à valider
     * @throws IllegalArgumentException si le nom d'utilisateur est invalide
     */
    private void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom d'utilisateur ne peut pas être vide");
        }
        if (username.length() < MIN_USERNAME_LENGTH) {
            throw new IllegalArgumentException(
                    "Le nom d'utilisateur doit contenir au moins " + MIN_USERNAME_LENGTH + " caractères"
            );
        }
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException(
                    "Le nom d'utilisateur ne peut contenir que des lettres, chiffres et underscores"
            );
        }
    }

    /**
     * Valide le mot de passe.
     *
     * @param password Mot de passe à valider
     * @throws IllegalArgumentException si le mot de passe est invalide
     */
    private void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être vide");
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException(
                    "Le mot de passe doit contenir au moins " + MIN_PASSWORD_LENGTH + " caractères"
            );
        }
    }

    /**
     * Valide l'adresse email.
     *
     * @param email Email à valider
     * @throws IllegalArgumentException si l'email est invalide
     */
    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("L'email ne peut pas être vide");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Format d'email invalide");
        }
    }

    /**
     * Génère un identifiant unique pour l'utilisateur.
     *
     * @return Identifiant unique
     */
    private String generateUserId() {
        return "USR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

