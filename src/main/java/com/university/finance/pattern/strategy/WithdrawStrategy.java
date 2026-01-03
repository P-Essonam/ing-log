package com.university.finance.pattern.strategy;

import com.university.finance.model.Account;
import com.university.finance.model.Transaction;
import com.university.finance.model.Transaction.TransactionType;

import java.util.UUID;

/**
 * Stratégie de retrait d'argent d'un compte.
 * Implémente le pattern Strategy pour les opérations de retrait.
 */
public class WithdrawStrategy implements TransactionStrategy {

    private static final String TYPE = "WITHDRAWAL";

    @Override
    public Transaction execute(Account account, double amount) {
        if (!canExecute(account, amount)) {
            return null;
        }

        // Débiter le compte
        boolean success = account.debit(amount);
        if (!success) {
            return null;
        }

        // Créer la transaction
        String transactionId = UUID.randomUUID().toString();
        Transaction transaction = new Transaction(
                transactionId,
                TransactionType.WITHDRAWAL,
                amount,
                account,
                "Retrait de " + String.format("%.2f", amount) + "€"
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
        // Un retrait est valide si le montant est positif et le solde suffisant
        return account != null && amount > 0 && account.hasSufficientFunds(amount);
    }
}

