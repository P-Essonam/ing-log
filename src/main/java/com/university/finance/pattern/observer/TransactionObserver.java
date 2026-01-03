package com.university.finance.pattern.observer;

import com.university.finance.model.Transaction;

/**
 * Interface Observer pour les notifications de transactions.
 * Implémente le pattern Observer pour permettre à différents
 * composants de réagir aux transactions.
 */
public interface TransactionObserver {

    /**
     * Appelé lorsqu'une transaction est effectuée.
     *
     * @param transaction La transaction effectuée
     */
    void onTransaction(Transaction transaction);

    /**
     * Retourne le nom de l'observer pour le logging.
     *
     * @return Nom de l'observer
     */
    String getName();
}

