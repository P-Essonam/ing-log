package com.university.finance.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Représente un compte bancaire.
 * Contient le solde et l'historique des transactions.
 */
public class Account {
    private final String id;
    private final User owner;
    private double balance;
    private final List<Transaction> transactions;

    /**
     * Constructeur de Account.
     *
     * @param id            Identifiant unique du compte
     * @param owner         Propriétaire du compte
     * @param initialBalance Solde initial
     */
    public Account(String id, User owner, double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Le solde initial ne peut pas être négatif");
        }
        this.id = id;
        this.owner = owner;
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
    }

    // Getters
    public String getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    /**
     * Crédite le compte du montant spécifié.
     *
     * @param amount Montant à créditer (doit être positif)
     * @return true si l'opération a réussi
     */
    public boolean credit(double amount) {
        if (amount <= 0) {
            return false;
        }
        this.balance += amount;
        return true;
    }

    /**
     * Débite le compte du montant spécifié.
     *
     * @param amount Montant à débiter (doit être positif et <= solde)
     * @return true si l'opération a réussi
     */
    public boolean debit(double amount) {
        if (amount <= 0 || amount > this.balance) {
            return false;
        }
        this.balance -= amount;
        return true;
    }

    /**
     * Ajoute une transaction à l'historique.
     *
     * @param transaction Transaction à ajouter
     */
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    /**
     * Vérifie si le compte a suffisamment de fonds.
     *
     * @param amount Montant requis
     * @return true si le solde est suffisant
     */
    public boolean hasSufficientFunds(double amount) {
        return this.balance >= amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", owner=" + owner.getUsername() +
                ", balance=" + balance +
                '}';
    }
}

