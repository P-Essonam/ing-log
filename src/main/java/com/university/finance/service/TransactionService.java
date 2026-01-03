package com.university.finance.service;

import com.university.finance.model.Account;
import com.university.finance.model.Transaction;
import com.university.finance.pattern.observer.TransactionObserver;
import com.university.finance.pattern.strategy.DepositStrategy;
import com.university.finance.pattern.strategy.TransactionStrategy;
import com.university.finance.pattern.strategy.TransferStrategy;
import com.university.finance.pattern.strategy.WithdrawStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Service de gestion des transactions.
 * Implémente le pattern Subject pour notifier les observers.
 * Utilise les stratégies de transaction pour exécuter les opérations.
 */
public class TransactionService {

    // Liste des observers (pattern Observer)
    private final List<TransactionObserver> observers;

    // Stratégies de transaction (pattern Strategy)
    private final DepositStrategy depositStrategy;
    private final WithdrawStrategy withdrawStrategy;
    private final TransferStrategy transferStrategy;

    /**
     * Constructeur par défaut.
     */
    public TransactionService() {
        this.observers = new ArrayList<>();
        this.depositStrategy = new DepositStrategy();
        this.withdrawStrategy = new WithdrawStrategy();
        this.transferStrategy = new TransferStrategy();
    }

    // ==================== Gestion des Observers ====================

    /**
     * Ajoute un observer pour les notifications de transactions.
     *
     * @param observer Observer à ajouter
     */
    public void addObserver(TransactionObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Supprime un observer.
     *
     * @param observer Observer à supprimer
     */
    public void removeObserver(TransactionObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifie tous les observers d'une transaction.
     *
     * @param transaction Transaction à notifier
     */
    private void notifyObservers(Transaction transaction) {
        for (TransactionObserver observer : observers) {
            try {
                observer.onTransaction(transaction);
            } catch (Exception e) {
                System.err.println("Erreur lors de la notification de l'observer " 
                        + observer.getName() + ": " + e.getMessage());
            }
        }
    }

    /**
     * Retourne le nombre d'observers enregistrés.
     *
     * @return Nombre d'observers
     */
    public int getObserverCount() {
        return observers.size();
    }

    // ==================== Opérations de Transaction ====================

    /**
     * Effectue un dépôt sur un compte.
     *
     * @param account Compte sur lequel effectuer le dépôt
     * @param amount  Montant à déposer
     * @return La transaction créée, ou null si l'opération a échoué
     */
    public Transaction deposit(Account account, double amount) {
        Transaction transaction = depositStrategy.execute(account, amount);
        if (transaction != null) {
            notifyObservers(transaction);
        }
        return transaction;
    }

    /**
     * Effectue un retrait sur un compte.
     *
     * @param account Compte sur lequel effectuer le retrait
     * @param amount  Montant à retirer
     * @return La transaction créée, ou null si l'opération a échoué
     */
    public Transaction withdraw(Account account, double amount) {
        Transaction transaction = withdrawStrategy.execute(account, amount);
        if (transaction != null) {
            notifyObservers(transaction);
        }
        return transaction;
    }

    /**
     * Effectue un transfert entre deux comptes.
     *
     * @param fromAccount Compte source
     * @param toAccount   Compte destination
     * @param amount      Montant à transférer
     * @return La transaction créée, ou null si l'opération a échoué
     */
    public Transaction transfer(Account fromAccount, Account toAccount, double amount) {
        Transaction transaction = transferStrategy.execute(fromAccount, toAccount, amount);
        if (transaction != null) {
            notifyObservers(transaction);
        }
        return transaction;
    }

    // ==================== Vérifications ====================

    /**
     * Vérifie si un dépôt peut être effectué.
     *
     * @param account Compte concerné
     * @param amount  Montant du dépôt
     * @return true si le dépôt est possible
     */
    public boolean canDeposit(Account account, double amount) {
        return depositStrategy.canExecute(account, amount);
    }

    /**
     * Vérifie si un retrait peut être effectué.
     *
     * @param account Compte concerné
     * @param amount  Montant du retrait
     * @return true si le retrait est possible
     */
    public boolean canWithdraw(Account account, double amount) {
        return withdrawStrategy.canExecute(account, amount);
    }

    /**
     * Vérifie si un transfert peut être effectué.
     *
     * @param fromAccount Compte source
     * @param toAccount   Compte destination
     * @param amount      Montant du transfert
     * @return true si le transfert est possible
     */
    public boolean canTransfer(Account fromAccount, Account toAccount, double amount) {
        return transferStrategy.canExecute(fromAccount, toAccount, amount);
    }

    // ==================== Getters pour les stratégies ====================

    /**
     * Retourne la stratégie de dépôt.
     *
     * @return Stratégie de dépôt
     */
    public TransactionStrategy getDepositStrategy() {
        return depositStrategy;
    }

    /**
     * Retourne la stratégie de retrait.
     *
     * @return Stratégie de retrait
     */
    public TransactionStrategy getWithdrawStrategy() {
        return withdrawStrategy;
    }

    /**
     * Retourne la stratégie de transfert.
     *
     * @return Stratégie de transfert
     */
    public TransactionStrategy getTransferStrategy() {
        return transferStrategy;
    }
}

