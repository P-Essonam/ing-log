package com.university.finance;

import com.university.finance.config.ConfigurationManager;
import com.university.finance.model.Account;
import com.university.finance.model.Transaction;
import com.university.finance.model.User;
import com.university.finance.service.BankingService;

import java.util.List;
import java.util.Scanner;

/**
 * Application principale du système bancaire.
 * Version refactorisée utilisant les design patterns:
 * - Strategy: pour les différents types de transactions
 * - Factory: pour la création d'utilisateurs et comptes
 * - Observer: pour les notifications et l'audit
 * - Singleton: pour la configuration
 */
public class MainApp {

    private final BankingService bankingService;
    private final Scanner scanner;
    private final ConfigurationManager config;

    private User currentUser;
    private Account currentAccount;

    /**
     * Constructeur de l'application.
     */
    public MainApp() {
        this.bankingService = new BankingService();
        this.scanner = new Scanner(System.in);
        this.config = ConfigurationManager.getInstance();
        this.currentUser = null;
        this.currentAccount = null;

        // Initialiser des données de démonstration
        initializeDemoData();
    }

    /**
     * Initialise des données de démonstration.
     */
    private void initializeDemoData() {
        try {
            // Créer des utilisateurs de démonstration
            Account account1 = bankingService.createUserWithAccount(
                    "user1", "password1", "user1@example.com", 1000.0);
            Account account2 = bankingService.createUserWithAccount(
                    "user2", "password2", "user2@example.com", 500.0);

            System.out.println("Données de démonstration créées:");
            System.out.println("  - " + account1.getOwner().getUsername() + 
                    " (Compte: " + account1.getId() + ", Solde: " + account1.getBalance() + "€)");
            System.out.println("  - " + account2.getOwner().getUsername() + 
                    " (Compte: " + account2.getId() + ", Solde: " + account2.getBalance() + "€)");
            System.out.println();
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation: " + e.getMessage());
        }
    }

    /**
     * Démarre l'application.
     */
    public void start() {
        printWelcome();
        boolean running = true;

        while (running) {
            if (currentUser == null) {
                running = showLoginMenu();
            } else {
                running = showMainMenu();
            }
        }

        cleanup();
        System.out.println("Au revoir!");
    }

    /**
     * Affiche le message de bienvenue.
     */
    private void printWelcome() {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║    " + config.getAppName() + "    ║");
        System.out.println("║           Version " + config.getAppVersion() + "              ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println();
    }

    /**
     * Affiche le menu de connexion.
     *
     * @return false si l'utilisateur veut quitter
     */
    private boolean showLoginMenu() {
        System.out.println("=== Menu de Connexion ===");
        System.out.println("1. Se connecter");
        System.out.println("2. Créer un compte");
        System.out.println("0. Quitter");
        System.out.print("Choix: ");

        int choice = readInt();

        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            case 0:
                return false;
            default:
                System.out.println("Choix invalide!");
        }
        System.out.println();
        return true;
    }

    /**
     * Affiche le menu principal.
     *
     * @return false si l'utilisateur veut quitter
     */
    private boolean showMainMenu() {
        System.out.println("=== Système Bancaire ===");
        System.out.println("Utilisateur: " + currentUser.getUsername());
        if (currentAccount != null) {
            System.out.println("Compte actif: " + currentAccount.getId() + 
                    " (Solde: " + String.format("%.2f", currentAccount.getBalance()) + "€)");
        }
        System.out.println("------------------------");
        System.out.println("1. Afficher solde");
        System.out.println("2. Déposer argent");
        System.out.println("3. Retirer argent");
        System.out.println("4. Transfert");
        System.out.println("5. Historique des transactions");
        System.out.println("6. Changer de compte");
        System.out.println("7. Créer un nouveau compte");
        System.out.println("8. Se déconnecter");
        System.out.println("0. Quitter");
        System.out.print("Choix: ");

        int choice = readInt();

        switch (choice) {
            case 1:
                showBalance();
                break;
            case 2:
                deposit();
                break;
            case 3:
                withdraw();
                break;
            case 4:
                transfer();
                break;
            case 5:
                showHistory();
                break;
            case 6:
                selectAccount();
                break;
            case 7:
                createNewAccount();
                break;
            case 8:
                logout();
                break;
            case 0:
                return false;
            default:
                System.out.println("Choix invalide!");
        }
        System.out.println();
        return true;
    }

    /**
     * Connexion d'un utilisateur.
     */
    private void login() {
        System.out.print("Nom d'utilisateur: ");
        String username = scanner.next();
        System.out.print("Mot de passe: ");
        String password = scanner.next();

        currentUser = bankingService.authenticate(username, password);
        if (currentUser != null) {
            System.out.println("Connexion réussie! Bienvenue " + currentUser.getUsername());
            
            // Sélectionner automatiquement le premier compte
            List<Account> userAccounts = bankingService.findAccountsByUser(currentUser);
            if (!userAccounts.isEmpty()) {
                currentAccount = userAccounts.get(0);
            }
        } else {
            System.out.println("Identifiants incorrects!");
        }
    }

    /**
     * Inscription d'un nouvel utilisateur.
     */
    private void register() {
        System.out.print("Nom d'utilisateur: ");
        String username = scanner.next();
        System.out.print("Mot de passe: ");
        String password = scanner.next();
        System.out.print("Email: ");
        String email = scanner.next();
        System.out.print("Dépôt initial: ");
        double initialDeposit = readDouble();

        try {
            currentAccount = bankingService.createUserWithAccount(
                    username, password, email, initialDeposit);
            currentUser = currentAccount.getOwner();
            System.out.println("Compte créé avec succès!");
            System.out.println("ID du compte: " + currentAccount.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    /**
     * Affiche le solde du compte courant.
     */
    private void showBalance() {
        if (currentAccount == null) {
            System.out.println("Aucun compte sélectionné!");
            return;
        }
        System.out.println("Solde du compte " + currentAccount.getId() + ": " + 
                String.format("%.2f", currentAccount.getBalance()) + "€");
    }

    /**
     * Effectue un dépôt.
     */
    private void deposit() {
        if (currentAccount == null) {
            System.out.println("Aucun compte sélectionné!");
            return;
        }

        System.out.print("Montant à déposer: ");
        double amount = readDouble();

        Transaction tx = bankingService.deposit(currentAccount.getId(), amount);
        if (tx != null) {
            System.out.println("Dépôt réussi!");
            System.out.println("Nouveau solde: " + String.format("%.2f", currentAccount.getBalance()) + "€");
        } else {
            System.out.println("Échec du dépôt!");
        }
    }

    /**
     * Effectue un retrait.
     */
    private void withdraw() {
        if (currentAccount == null) {
            System.out.println("Aucun compte sélectionné!");
            return;
        }

        System.out.print("Montant à retirer: ");
        double amount = readDouble();

        Transaction tx = bankingService.withdraw(currentAccount.getId(), amount);
        if (tx != null) {
            System.out.println("Retrait réussi!");
            System.out.println("Nouveau solde: " + String.format("%.2f", currentAccount.getBalance()) + "€");
        } else {
            System.out.println("Échec du retrait! Vérifiez votre solde.");
        }
    }

    /**
     * Effectue un transfert.
     */
    private void transfer() {
        if (currentAccount == null) {
            System.out.println("Aucun compte sélectionné!");
            return;
        }

        // Afficher les comptes disponibles
        System.out.println("Comptes disponibles:");
        for (Account account : bankingService.getAllAccounts()) {
            if (!account.equals(currentAccount)) {
                System.out.println("  - " + account.getId() + 
                        " (" + account.getOwner().getUsername() + ")");
            }
        }

        System.out.print("ID du compte destinataire: ");
        String toAccountId = scanner.next();
        System.out.print("Montant à transférer: ");
        double amount = readDouble();

        try {
            Transaction tx = bankingService.transfer(
                    currentAccount.getId(), toAccountId, amount);
            if (tx != null) {
                System.out.println("Transfert réussi!");
                System.out.println("Nouveau solde: " + 
                        String.format("%.2f", currentAccount.getBalance()) + "€");
            } else {
                System.out.println("Échec du transfert!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    /**
     * Affiche l'historique des transactions.
     */
    private void showHistory() {
        if (currentAccount == null) {
            System.out.println("Aucun compte sélectionné!");
            return;
        }

        List<Transaction> transactions = bankingService.getTransactionHistory(
                currentAccount.getId());
        
        if (transactions.isEmpty()) {
            System.out.println("Aucune transaction pour ce compte.");
            return;
        }

        System.out.println("=== Historique des transactions ===");
        for (Transaction tx : transactions) {
            System.out.println(tx);
        }
    }

    /**
     * Sélectionne un autre compte de l'utilisateur.
     */
    private void selectAccount() {
        List<Account> userAccounts = bankingService.findAccountsByUser(currentUser);
        
        if (userAccounts.size() <= 1) {
            System.out.println("Vous n'avez qu'un seul compte.");
            return;
        }

        System.out.println("Vos comptes:");
        for (int i = 0; i < userAccounts.size(); i++) {
            Account acc = userAccounts.get(i);
            System.out.println((i + 1) + ". " + acc.getId() + 
                    " (Solde: " + String.format("%.2f", acc.getBalance()) + "€)");
        }

        System.out.print("Numéro du compte: ");
        int choice = readInt();

        if (choice >= 1 && choice <= userAccounts.size()) {
            currentAccount = userAccounts.get(choice - 1);
            System.out.println("Compte " + currentAccount.getId() + " sélectionné.");
        } else {
            System.out.println("Choix invalide!");
        }
    }

    /**
     * Crée un nouveau compte pour l'utilisateur connecté.
     */
    private void createNewAccount() {
        System.out.print("Dépôt initial: ");
        double initialDeposit = readDouble();

        try {
            Account newAccount = bankingService.createAccount(currentUser, initialDeposit);
            currentAccount = newAccount;
            System.out.println("Nouveau compte créé: " + newAccount.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    /**
     * Déconnecte l'utilisateur.
     */
    private void logout() {
        currentUser = null;
        currentAccount = null;
        System.out.println("Déconnexion réussie.");
    }

    /**
     * Lit un entier depuis la console.
     *
     * @return L'entier lu ou -1 en cas d'erreur
     */
    private int readInt() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine(); // Vider le buffer
            return -1;
        }
    }

    /**
     * Lit un double depuis la console.
     *
     * @return Le double lu ou 0 en cas d'erreur
     */
    private double readDouble() {
        try {
            return scanner.nextDouble();
        } catch (Exception e) {
            scanner.nextLine(); // Vider le buffer
            return 0.0;
        }
    }

    /**
     * Nettoie les ressources.
     */
    private void cleanup() {
        scanner.close();
    }

    /**
     * Point d'entrée de l'application.
     *
     * @param args Arguments de la ligne de commande
     */
    public static void main(String[] args) {
        MainApp app = new MainApp();
        app.start();
    }
}

