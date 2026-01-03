package com.university.finance.pattern.observer;

import com.university.finance.model.Transaction;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Observer qui journalise toutes les transactions pour l'audit.
 * Implémente le pattern Observer pour le système de logging.
 */
public class AuditLogger implements TransactionObserver {

    private static final String NAME = "AuditLogger";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final List<String> auditLog;
    private final String logFilePath;
    private boolean writeToFile;

    /**
     * Constructeur par défaut (log en mémoire uniquement).
     */
    public AuditLogger() {
        this.auditLog = new ArrayList<>();
        this.logFilePath = null;
        this.writeToFile = false;
    }

    /**
     * Constructeur avec chemin de fichier de log.
     *
     * @param logFilePath Chemin du fichier de log
     */
    public AuditLogger(String logFilePath) {
        this.auditLog = new ArrayList<>();
        this.logFilePath = logFilePath;
        this.writeToFile = logFilePath != null && !logFilePath.isEmpty();
    }

    @Override
    public void onTransaction(Transaction transaction) {
        String logEntry = formatLogEntry(transaction);
        auditLog.add(logEntry);

        // Afficher dans la console
        System.out.println("[AUDIT] " + logEntry);

        // Écrire dans le fichier si configuré
        if (writeToFile) {
            writeToLogFile(logEntry);
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    /**
     * Formate une entrée de log pour une transaction.
     *
     * @param transaction Transaction à logger
     * @return Entrée de log formatée
     */
    private String formatLogEntry(Transaction transaction) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(LocalDateTime.now().format(FORMATTER)).append("] ");
        sb.append("TX_ID: ").append(transaction.getId()).append(" | ");
        sb.append("TYPE: ").append(transaction.getType().getLabel()).append(" | ");
        sb.append("MONTANT: ").append(String.format("%.2f€", transaction.getAmount())).append(" | ");
        
        if (transaction.isTransfer()) {
            sb.append("DE: ").append(transaction.getFromAccount().getId()).append(" | ");
            sb.append("VERS: ").append(transaction.getToAccount().getId());
        } else {
            sb.append("COMPTE: ").append(transaction.getFromAccount().getId());
        }

        return sb.toString();
    }

    /**
     * Écrit une entrée dans le fichier de log.
     *
     * @param logEntry Entrée à écrire
     */
    private void writeToLogFile(String logEntry) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(logFilePath, true))) {
            writer.println(logEntry);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture dans le fichier de log: " + e.getMessage());
        }
    }

    /**
     * Retourne l'historique complet des logs.
     *
     * @return Liste des entrées de log
     */
    public List<String> getAuditLog() {
        return Collections.unmodifiableList(auditLog);
    }

    /**
     * Retourne le nombre de transactions loggées.
     *
     * @return Nombre de transactions
     */
    public int getLogCount() {
        return auditLog.size();
    }

    /**
     * Efface l'historique des logs en mémoire.
     */
    public void clearLog() {
        auditLog.clear();
    }

    /**
     * Active ou désactive l'écriture dans le fichier.
     *
     * @param writeToFile true pour activer
     */
    public void setWriteToFile(boolean writeToFile) {
        this.writeToFile = writeToFile && logFilePath != null;
    }
}

