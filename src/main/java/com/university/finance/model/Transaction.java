package com.university.finance.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Représente une transaction bancaire.
 * Enregistre les détails d'une opération financière.
 */
public class Transaction {
    
    /**
     * Types de transactions possibles.
     */
    public enum TransactionType {
        DEPOSIT("Dépôt"),
        WITHDRAWAL("Retrait"),
        TRANSFER("Transfert");

        private final String label;

        TransactionType(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    private final String id;
    private final TransactionType type;
    private final double amount;
    private final LocalDateTime timestamp;
    private final Account fromAccount;
    private final Account toAccount;
    private final String description;

    /**
     * Constructeur pour les dépôts et retraits (compte unique).
     *
     * @param id          Identifiant unique de la transaction
     * @param type        Type de transaction
     * @param amount      Montant de la transaction
     * @param account     Compte concerné
     * @param description Description de la transaction
     */
    public Transaction(String id, TransactionType type, double amount, Account account, String description) {
        this(id, type, amount, account, account, description);
    }

    /**
     * Constructeur complet pour les transferts.
     *
     * @param id          Identifiant unique de la transaction
     * @param type        Type de transaction
     * @param amount      Montant de la transaction
     * @param fromAccount Compte source
     * @param toAccount   Compte destination
     * @param description Description de la transaction
     */
    public Transaction(String id, TransactionType type, double amount, 
                       Account fromAccount, Account toAccount, String description) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.description = description;
    }

    // Getters
    public String getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Formate le timestamp en chaîne lisible.
     *
     * @return Timestamp formaté
     */
    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return timestamp.format(formatter);
    }

    /**
     * Vérifie si c'est un transfert entre deux comptes différents.
     *
     * @return true si c'est un transfert
     */
    public boolean isTransfer() {
        return type == TransactionType.TRANSFER && 
               !Objects.equals(fromAccount, toAccount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFormattedTimestamp())
          .append(" - ")
          .append(type.getLabel())
          .append(": ");
        
        if (isTransfer()) {
            sb.append(fromAccount.getId())
              .append(" -> ")
              .append(toAccount.getId())
              .append(" ");
        }
        
        sb.append(String.format("%.2f€", amount));
        
        if (description != null && !description.isEmpty()) {
            sb.append(" (").append(description).append(")");
        }
        
        return sb.toString();
    }
}

