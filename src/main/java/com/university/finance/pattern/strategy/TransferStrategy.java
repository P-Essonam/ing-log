package com.university.finance.pattern.strategy;

import com.university.finance.model.Account;
import com.university.finance.model.Transaction;
import com.university.finance.model.Transaction.TransactionType;

import java.util.UUID;

/**
 * Stratégie de transfert d'argent entre deux comptes.
 * Implémente le pattern Strategy pour les opérations de transfert.
 */
public class TransferStrategy implements TransactionStrategy {

    private static final String TYPE = "TRANSFER";

    @Override
    public Transaction execute(Account account, double amount) {
        // Les transferts nécessitent deux comptes
        throw new UnsupportedOperationException(
                "Utilisez execute(fromAccount, toAccount, amount) pour les transferts"
        );
    }

    @Override
    public Transaction execute(Account fromAccount, Account toAccount, double amount) {
        if (!canExecute(fromAccount, toAccount, amount)) {
            return null;
        }

        // Débiter le compte source
        boolean debitSuccess = fromAccount.debit(amount);
        if (!debitSuccess) {
            return null;
        }

        // Créditer le compte destination
        boolean creditSuccess = toAccount.credit(amount);
        if (!creditSuccess) {
            // Rollback: recréditer le compte source
            fromAccount.credit(amount);
            return null;
        }

        // Créer la transaction
        String transactionId = UUID.randomUUID().toString();
        Transaction transaction = new Transaction(
                transactionId,
                TransactionType.TRANSFER,
                amount,
                fromAccount,
                toAccount,
                "Transfert de " + fromAccount.getId() + " vers " + toAccount.getId()
        );

        // Ajouter à l'historique des deux comptes
        fromAccount.addTransaction(transaction);
        toAccount.addTransaction(transaction);

        return transaction;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean canExecute(Account account, double amount) {
        // Pour un transfert, on utilise la version à deux comptes
        return false;
    }

    /**
     * Vérifie si le transfert peut être exécuté.
     *
     * @param fromAccount Compte source
     * @param toAccount   Compte destination
     * @param amount      Montant du transfert
     * @return true si le transfert peut être exécuté
     */
    public boolean canExecute(Account fromAccount, Account toAccount, double amount) {
        return fromAccount != null 
                && toAccount != null 
                && !fromAccount.equals(toAccount)
                && amount > 0 
                && fromAccount.hasSufficientFunds(amount);
    }
}

