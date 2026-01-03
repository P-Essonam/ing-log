package com.university.finance.pattern.factory;

import com.university.finance.model.Account;
import com.university.finance.model.User;

import java.util.UUID;

/**
 * Factory pour la création de comptes bancaires.
 * Implémente le pattern Factory pour centraliser la création d'objets Account
 * avec validation des données.
 */
public class AccountFactory {

    // Dépôt initial minimum
    private static final double MIN_INITIAL_DEPOSIT = 0.0;

    // Dépôt initial maximum
    private static final double MAX_INITIAL_DEPOSIT = 1000000.0;

    /**
     * Crée un nouveau compte avec un ID généré automatiquement.
     *
     * @param owner          Propriétaire du compte
     * @param initialDeposit Dépôt initial
     * @return Le nouveau compte créé
     * @throws IllegalArgumentException si les données sont invalides
     */
    public Account createAccount(User owner, double initialDeposit) {
        validateOwner(owner);
        validateInitialDeposit(initialDeposit);

        String id = generateAccountId();
        return new Account(id, owner, initialDeposit);
    }

    /**
     * Crée un nouveau compte avec un ID spécifique.
     *
     * @param id             Identifiant du compte
     * @param owner          Propriétaire du compte
     * @param initialDeposit Dépôt initial
     * @return Le nouveau compte créé
     * @throws IllegalArgumentException si les données sont invalides
     */
    public Account createAccount(String id, User owner, double initialDeposit) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID du compte ne peut pas être vide");
        }
        validateOwner(owner);
        validateInitialDeposit(initialDeposit);

        return new Account(id, owner, initialDeposit);
    }

    /**
     * Crée un compte avec un solde initial de zéro.
     *
     * @param owner Propriétaire du compte
     * @return Le nouveau compte créé
     */
    public Account createEmptyAccount(User owner) {
        validateOwner(owner);
        String id = generateAccountId();
        return new Account(id, owner, 0.0);
    }

    /**
     * Crée un compte premium avec des avantages spéciaux.
     * (Pour extensions futures)
     *
     * @param owner          Propriétaire du compte
     * @param initialDeposit Dépôt initial (minimum 1000€ pour compte premium)
     * @return Le nouveau compte premium créé
     * @throws IllegalArgumentException si le dépôt initial est insuffisant
     */
    public Account createPremiumAccount(User owner, double initialDeposit) {
        validateOwner(owner);
        if (initialDeposit < 1000.0) {
            throw new IllegalArgumentException(
                    "Un compte premium nécessite un dépôt initial d'au moins 1000€"
            );
        }
        validateInitialDeposit(initialDeposit);

        String id = "PRM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new Account(id, owner, initialDeposit);
    }

    /**
     * Valide le propriétaire du compte.
     *
     * @param owner Propriétaire à valider
     * @throws IllegalArgumentException si le propriétaire est invalide
     */
    private void validateOwner(User owner) {
        if (owner == null) {
            throw new IllegalArgumentException("Le propriétaire du compte ne peut pas être null");
        }
        if (owner.getId() == null || owner.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("Le propriétaire doit avoir un ID valide");
        }
    }

    /**
     * Valide le dépôt initial.
     *
     * @param initialDeposit Dépôt à valider
     * @throws IllegalArgumentException si le dépôt est invalide
     */
    private void validateInitialDeposit(double initialDeposit) {
        if (initialDeposit < MIN_INITIAL_DEPOSIT) {
            throw new IllegalArgumentException(
                    "Le dépôt initial ne peut pas être négatif"
            );
        }
        if (initialDeposit > MAX_INITIAL_DEPOSIT) {
            throw new IllegalArgumentException(
                    "Le dépôt initial ne peut pas dépasser " + MAX_INITIAL_DEPOSIT + "€"
            );
        }
    }

    /**
     * Génère un identifiant unique pour le compte.
     *
     * @return Identifiant unique
     */
    private String generateAccountId() {
        return "ACC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

