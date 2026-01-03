package com.university.finance.service;

import com.university.finance.config.ConfigurationManager;
import com.university.finance.model.Account;
import com.university.finance.model.Transaction;
import com.university.finance.model.User;
import com.university.finance.pattern.factory.AccountFactory;
import com.university.finance.pattern.factory.UserFactory;
import com.university.finance.pattern.observer.AuditLogger;
import com.university.finance.pattern.observer.NotificationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service principal de gestion bancaire.
 * Orchestre les opérations en utilisant les différents patterns implémentés.
 */
public class BankingService {

    // Stockage des utilisateurs et comptes
    private final Map<String, User> users;
    private final Map<String, Account> accounts;

    // Factories
    private final UserFactory userFactory;
    private final AccountFactory accountFactory;

    // Service de transaction
    private final TransactionService transactionService;

    // Observers
    private final AuditLogger auditLogger;
    private final NotificationService notificationService;

    // Configuration
    private final ConfigurationManager config;

    /**
     * Constructeur par défaut.
     * Initialise tous les composants avec les observers par défaut.
     */
    public BankingService() {
        this.users = new HashMap<>();
        this.accounts = new HashMap<>();
        this.userFactory = new UserFactory();
        this.accountFactory = new AccountFactory();
        this.transactionService = new TransactionService();
        this.config = ConfigurationManager.getInstance();

        // Initialiser les observers
        this.auditLogger = new AuditLogger();
        this.notificationService = new NotificationService();

        // Enregistrer les observers si activés dans la configuration
        if (config.isAuditEnabled()) {
            transactionService.addObserver(auditLogger);
        }
        if (config.isNotificationsEnabled()) {
            transactionService.addObserver(notificationService);
        }
    }

    /**
     * Constructeur avec observers personnalisés.
     *
     * @param auditLogger         Logger d'audit
     * @param notificationService Service de notifications
     */
    public BankingService(AuditLogger auditLogger, NotificationService notificationService) {
        this.users = new HashMap<>();
        this.accounts = new HashMap<>();
        this.userFactory = new UserFactory();
        this.accountFactory = new AccountFactory();
        this.transactionService = new TransactionService();
        this.config = ConfigurationManager.getInstance();

        this.auditLogger = auditLogger;
        this.notificationService = notificationService;

        if (auditLogger != null) {
            transactionService.addObserver(auditLogger);
        }
        if (notificationService != null) {
            transactionService.addObserver(notificationService);
        }
    }

    // ==================== Gestion des Utilisateurs ====================

    /**
     * Crée un nouvel utilisateur.
     *
     * @param username Nom d'utilisateur
     * @param password Mot de passe
     * @param email    Adresse email
     * @return L'utilisateur créé
     * @throws IllegalArgumentException si les données sont invalides ou l'utilisateur existe
     */
    public User createUser(String username, String password, String email) {
        if (findUserByUsername(username) != null) {
            throw new IllegalArgumentException("Un utilisateur avec ce nom existe déjà");
        }

        User user = userFactory.createUser(username, password, email);
        users.put(user.getId(), user);
        return user;
    }

    /**
     * Trouve un utilisateur par son ID.
     *
     * @param userId ID de l'utilisateur
     * @return L'utilisateur ou null
     */
    public User findUserById(String userId) {
        return users.get(userId);
    }

    /**
     * Trouve un utilisateur par son nom d'utilisateur.
     *
     * @param username Nom d'utilisateur
     * @return L'utilisateur ou null
     */
    public User findUserByUsername(String username) {
        return users.values().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Authentifie un utilisateur.
     *
     * @param username Nom d'utilisateur
     * @param password Mot de passe
     * @return L'utilisateur si l'authentification réussit, null sinon
     */
    public User authenticate(String username, String password) {
        User user = findUserByUsername(username);
        if (user != null && user.checkPassword(password)) {
            return user;
        }
        return null;
    }

    /**
     * Retourne tous les utilisateurs.
     *
     * @return Liste des utilisateurs
     */
    public List<User> getAllUsers() {
        return List.copyOf(users.values());
    }

    // ==================== Gestion des Comptes ====================

    /**
     * Crée un nouveau compte pour un utilisateur.
     *
     * @param user           Propriétaire du compte
     * @param initialDeposit Dépôt initial
     * @return Le compte créé
     */
    public Account createAccount(User user, double initialDeposit) {
        Account account = accountFactory.createAccount(user, initialDeposit);
        accounts.put(account.getId(), account);
        return account;
    }

    /**
     * Crée un utilisateur et un compte associé en une seule opération.
     *
     * @param username       Nom d'utilisateur
     * @param password       Mot de passe
     * @param email          Adresse email
     * @param initialDeposit Dépôt initial
     * @return Le compte créé
     */
    public Account createUserWithAccount(String username, String password, 
                                          String email, double initialDeposit) {
        User user = createUser(username, password, email);
        return createAccount(user, initialDeposit);
    }

    /**
     * Trouve un compte par son ID.
     *
     * @param accountId ID du compte
     * @return Le compte ou null
     */
    public Account findAccountById(String accountId) {
        return accounts.get(accountId);
    }

    /**
     * Trouve les comptes d'un utilisateur.
     *
     * @param user Utilisateur
     * @return Liste des comptes de l'utilisateur
     */
    public List<Account> findAccountsByUser(User user) {
        return accounts.values().stream()
                .filter(a -> a.getOwner().equals(user))
                .collect(Collectors.toList());
    }

    /**
     * Retourne le solde d'un compte.
     *
     * @param accountId ID du compte
     * @return Solde du compte
     * @throws IllegalArgumentException si le compte n'existe pas
     */
    public double getBalance(String accountId) {
        Account account = findAccountById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Compte non trouvé: " + accountId);
        }
        return account.getBalance();
    }

    /**
     * Retourne tous les comptes.
     *
     * @return Liste des comptes
     */
    public List<Account> getAllAccounts() {
        return List.copyOf(accounts.values());
    }

    // ==================== Opérations Bancaires ====================

    /**
     * Effectue un dépôt sur un compte.
     *
     * @param accountId ID du compte
     * @param amount    Montant à déposer
     * @return La transaction créée
     * @throws IllegalArgumentException si le compte n'existe pas
     */
    public Transaction deposit(String accountId, double amount) {
        Account account = findAccountById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Compte non trouvé: " + accountId);
        }
        return transactionService.deposit(account, amount);
    }

    /**
     * Effectue un retrait sur un compte.
     *
     * @param accountId ID du compte
     * @param amount    Montant à retirer
     * @return La transaction créée
     * @throws IllegalArgumentException si le compte n'existe pas
     */
    public Transaction withdraw(String accountId, double amount) {
        Account account = findAccountById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Compte non trouvé: " + accountId);
        }
        return transactionService.withdraw(account, amount);
    }

    /**
     * Effectue un transfert entre deux comptes.
     *
     * @param fromAccountId ID du compte source
     * @param toAccountId   ID du compte destination
     * @param amount        Montant à transférer
     * @return La transaction créée
     * @throws IllegalArgumentException si un compte n'existe pas
     */
    public Transaction transfer(String fromAccountId, String toAccountId, double amount) {
        Account fromAccount = findAccountById(fromAccountId);
        Account toAccount = findAccountById(toAccountId);

        if (fromAccount == null) {
            throw new IllegalArgumentException("Compte source non trouvé: " + fromAccountId);
        }
        if (toAccount == null) {
            throw new IllegalArgumentException("Compte destination non trouvé: " + toAccountId);
        }

        // Vérifier la limite de transfert
        if (amount > config.getMaxTransfer()) {
            throw new IllegalArgumentException(
                    "Le montant dépasse la limite de transfert de " + config.getMaxTransfer() + "€"
            );
        }

        return transactionService.transfer(fromAccount, toAccount, amount);
    }

    /**
     * Retourne l'historique des transactions d'un compte.
     *
     * @param accountId ID du compte
     * @return Liste des transactions
     * @throws IllegalArgumentException si le compte n'existe pas
     */
    public List<Transaction> getTransactionHistory(String accountId) {
        Account account = findAccountById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Compte non trouvé: " + accountId);
        }
        return account.getTransactions();
    }

    // ==================== Getters pour les composants ====================

    /**
     * Retourne le service de transactions.
     *
     * @return Service de transactions
     */
    public TransactionService getTransactionService() {
        return transactionService;
    }

    /**
     * Retourne le logger d'audit.
     *
     * @return Logger d'audit
     */
    public AuditLogger getAuditLogger() {
        return auditLogger;
    }

    /**
     * Retourne le service de notifications.
     *
     * @return Service de notifications
     */
    public NotificationService getNotificationService() {
        return notificationService;
    }

    /**
     * Retourne la factory d'utilisateurs.
     *
     * @return Factory d'utilisateurs
     */
    public UserFactory getUserFactory() {
        return userFactory;
    }

    /**
     * Retourne la factory de comptes.
     *
     * @return Factory de comptes
     */
    public AccountFactory getAccountFactory() {
        return accountFactory;
    }

    /**
     * Retourne le nombre d'utilisateurs.
     *
     * @return Nombre d'utilisateurs
     */
    public int getUserCount() {
        return users.size();
    }

    /**
     * Retourne le nombre de comptes.
     *
     * @return Nombre de comptes
     */
    public int getAccountCount() {
        return accounts.size();
    }
}

