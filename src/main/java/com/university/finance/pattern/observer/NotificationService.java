package com.university.finance.pattern.observer;

import com.university.finance.model.Account;
import com.university.finance.model.Transaction;
import com.university.finance.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Observer qui envoie des notifications aux utilisateurs lors des transactions.
 * Implémente le pattern Observer pour le système de notifications.
 */
public class NotificationService implements TransactionObserver {

    private static final String NAME = "NotificationService";

    private final List<String> sentNotifications;
    private boolean emailEnabled;
    private boolean smsEnabled;
    private boolean consoleEnabled;

    /**
     * Constructeur par défaut (notifications console uniquement).
     */
    public NotificationService() {
        this.sentNotifications = new ArrayList<>();
        this.emailEnabled = false;
        this.smsEnabled = false;
        this.consoleEnabled = true;
    }

    /**
     * Constructeur avec configuration des canaux.
     *
     * @param emailEnabled   Activer les notifications email
     * @param smsEnabled     Activer les notifications SMS
     * @param consoleEnabled Activer les notifications console
     */
    public NotificationService(boolean emailEnabled, boolean smsEnabled, boolean consoleEnabled) {
        this.sentNotifications = new ArrayList<>();
        this.emailEnabled = emailEnabled;
        this.smsEnabled = smsEnabled;
        this.consoleEnabled = consoleEnabled;
    }

    @Override
    public void onTransaction(Transaction transaction) {
        String notification = createNotificationMessage(transaction);
        sentNotifications.add(notification);

        // Envoyer via les différents canaux
        if (consoleEnabled) {
            sendConsoleNotification(notification);
        }
        if (emailEnabled) {
            sendEmailNotification(transaction, notification);
        }
        if (smsEnabled) {
            sendSmsNotification(transaction, notification);
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    /**
     * Crée le message de notification pour une transaction.
     *
     * @param transaction Transaction concernée
     * @return Message de notification
     */
    private String createNotificationMessage(Transaction transaction) {
        StringBuilder sb = new StringBuilder();
        sb.append("📧 Notification: ");
        
        switch (transaction.getType()) {
            case DEPOSIT:
                sb.append("Dépôt de ")
                  .append(String.format("%.2f€", transaction.getAmount()))
                  .append(" effectué sur votre compte ");
                sb.append(transaction.getFromAccount().getId());
                sb.append(". Nouveau solde: ")
                  .append(String.format("%.2f€", transaction.getFromAccount().getBalance()));
                break;
                
            case WITHDRAWAL:
                sb.append("Retrait de ")
                  .append(String.format("%.2f€", transaction.getAmount()))
                  .append(" effectué sur votre compte ");
                sb.append(transaction.getFromAccount().getId());
                sb.append(". Nouveau solde: ")
                  .append(String.format("%.2f€", transaction.getFromAccount().getBalance()));
                break;
                
            case TRANSFER:
                sb.append("Transfert de ")
                  .append(String.format("%.2f€", transaction.getAmount()))
                  .append(" de ")
                  .append(transaction.getFromAccount().getId())
                  .append(" vers ")
                  .append(transaction.getToAccount().getId());
                break;
        }
        
        return sb.toString();
    }

    /**
     * Envoie une notification à la console.
     *
     * @param message Message à afficher
     */
    private void sendConsoleNotification(String message) {
        System.out.println("[NOTIFICATION] " + message);
    }

    /**
     * Simule l'envoi d'un email de notification.
     *
     * @param transaction Transaction concernée
     * @param message     Message à envoyer
     */
    private void sendEmailNotification(Transaction transaction, String message) {
        Account account = transaction.getFromAccount();
        User owner = account.getOwner();
        System.out.println("[EMAIL -> " + owner.getEmail() + "] " + message);
    }

    /**
     * Simule l'envoi d'un SMS de notification.
     *
     * @param transaction Transaction concernée
     * @param message     Message à envoyer
     */
    private void sendSmsNotification(Transaction transaction, String message) {
        Account account = transaction.getFromAccount();
        User owner = account.getOwner();
        System.out.println("[SMS -> " + owner.getUsername() + "] " + message);
    }

    /**
     * Retourne la liste des notifications envoyées.
     *
     * @return Liste des notifications
     */
    public List<String> getSentNotifications() {
        return Collections.unmodifiableList(sentNotifications);
    }

    /**
     * Retourne le nombre de notifications envoyées.
     *
     * @return Nombre de notifications
     */
    public int getNotificationCount() {
        return sentNotifications.size();
    }

    /**
     * Efface l'historique des notifications.
     */
    public void clearNotifications() {
        sentNotifications.clear();
    }

    // Setters pour la configuration
    public void setEmailEnabled(boolean emailEnabled) {
        this.emailEnabled = emailEnabled;
    }

    public void setSmsEnabled(boolean smsEnabled) {
        this.smsEnabled = smsEnabled;
    }

    public void setConsoleEnabled(boolean consoleEnabled) {
        this.consoleEnabled = consoleEnabled;
    }

    // Getters pour la configuration
    public boolean isEmailEnabled() {
        return emailEnabled;
    }

    public boolean isSmsEnabled() {
        return smsEnabled;
    }

    public boolean isConsoleEnabled() {
        return consoleEnabled;
    }
}

