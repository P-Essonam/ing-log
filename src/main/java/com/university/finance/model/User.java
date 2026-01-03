package com.university.finance.model;

import java.util.Objects;

/**
 * Représente un utilisateur du système bancaire.
 * Contient les informations d'identification et de contact.
 */
public class User {
    private final String id;
    private String username;
    private String password;
    private String email;

    /**
     * Constructeur de User.
     *
     * @param id       Identifiant unique de l'utilisateur
     * @param username Nom d'utilisateur
     * @param password Mot de passe
     * @param email    Adresse email
     */
    public User(String id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Vérifie si le mot de passe fourni correspond.
     *
     * @param password Mot de passe à vérifier
     * @return true si le mot de passe correspond
     */
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

