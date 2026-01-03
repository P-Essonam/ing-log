package com.university.finance.pattern.strategy;

import com.university.finance.model.Account;
import com.university.finance.model.Transaction;
import com.university.finance.model.Transaction.TransactionType;

import java.util.UUID;

/**
 * Stratégie de dépôt d'argent sur un compte.
 * Implémente le pattern Strategy pour les opérations de dépôt.
 */
public class DepositStrategy implements TransactionStrategy {

    private static final String TYPE = "DEPOSIT";

    @Override
    public Transaction execute(Account account, double amount) {
        if (!canExecute(account, amount)) {
            return null;
        }

        // Créditer le compte
        boolean success = account.credit(amount);
        if (!success) {
            return null;
        }

        // Créer la transaction
        String transactionId = UUID.randomUUID().toString();
        Transaction transaction = new Transaction(
                transactionId,
                TransactionType.DEPOSIT,
                amount,
                account,
                "Dépôt de " + String.format("%.2f", amount) + "€"
        );

        // Ajouter à l'historique du compte
        account.addTransaction(transaction);

        return transaction;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean canExecute(Account account, double amount) {
        // Un dépôt est valide si le montant est positif et le compte existe
        return account != null && amount > 0;
    }
}

