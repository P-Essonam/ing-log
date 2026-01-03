package com.university.finance.pattern.strategy;

import com.university.finance.model.Account;
import com.university.finance.model.Transaction;

/**
 * Interface Strategy pour les différents types de transactions.
 * Implémente le pattern Strategy pour permettre de changer
 * le comportement des transactions de manière flexible.
 */
public interface TransactionStrategy {

    /**
     * Exécute la transaction sur le compte spécifié.
     *
     * @param account Compte sur lequel effectuer la transaction
     * @param amount  Montant de la transaction
     * @return La transaction créée, ou null si l'opération a échoué
     */
    Transaction execute(Account account, double amount);

    /**
     * Exécute un transfert entre deux comptes.
     *
     * @param fromAccount Compte source
     * @param toAccount   Compte destination
     * @param amount      Montant du transfert
     * @return La transaction créée, ou null si l'opération a échoué
     */
    default Transaction execute(Account fromAccount, Account toAccount, double amount) {
        throw new UnsupportedOperationException("Cette stratégie ne supporte pas les transferts");
    }

    /**
     * Retourne le type de transaction géré par cette stratégie.
     *
     * @return Le type de transaction sous forme de chaîne
     */
    String getType();

    /**
     * Vérifie si la transaction peut être exécutée.
     *
     * @param account Compte à vérifier
     * @param amount  Montant de la transaction
     * @return true si la transaction peut être exécutée
     */
    boolean canExecute(Account account, double amount);
}

