package controller;

import model.Transaction;
import service.TransactionService;
import util.SessionManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class TransactionController {
    private TransactionService transactionService = new TransactionService();

    // CREATE OPERATIONS
    public String addTransaction(String type, String category, double amount,
                                 LocalDate date, String description) {
        validateSession();

        try {
            validateTransactionData(type, category, amount, date);

            Transaction transaction = new Transaction(
                    null,
                    SessionManager.getInstance().getCurrentUser(),
                    type.toUpperCase(),
                    category,
                    amount,
                    date,
                    description
            );

            transactionService.addTransaction(transaction);
            return transaction.getId();

        } catch (Exception e) {
            System.err.println("Add transaction error: " + e.getMessage());
            throw new RuntimeException("Failed to add transaction: " + e.getMessage(), e);
        }
    }

    // READ OPERATIONS
    public List<Transaction> getAllTransactions() {
        validateSession();
        return transactionService.getAllTransactions(getCurrentUsername());
    }

    public List<Transaction> getTransactionsThisMonth() {
        validateSession();
        LocalDate now = LocalDate.now();
        return transactionService.getTransactions(getCurrentUsername(),
                now.getMonthValue(), now.getYear());
    }

    public List<Transaction> getTransactionsByMonth(int month, int year) {
        validateSession();
        validateMonthYear(month, year);
        return transactionService.getTransactions(getCurrentUsername(), month, year);
    }

    public List<Transaction> getTransactionsByDateRange(LocalDate start, LocalDate end) {
        validateSession();
        validateDateRange(start, end);
        return transactionService.getTransactionsByDateRange(getCurrentUsername(), start, end);
    }

    public List<Transaction> getTransactionsByCategory(String category) {
        validateSession();
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        return transactionService.getTransactionsByCategory(getCurrentUsername(), category);
    }

    public List<Transaction> getTransactionsByType(String type) {
        validateSession();
        if (!"INCOME".equalsIgnoreCase(type) && !"EXPENSE".equalsIgnoreCase(type)) {
            throw new IllegalArgumentException("Type must be INCOME or EXPENSE");
        }
        return transactionService.getTransactionsByType(getCurrentUsername(), type.toUpperCase());
    }

    public Transaction getTransactionById(String transactionId) {
        validateSession();
        if (transactionId == null || transactionId.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction ID cannot be empty");
        }

        Transaction transaction = transactionService.getTransactionById(
                getCurrentUsername(), transactionId);

        if (transaction == null) {
            throw new RuntimeException("Transaction not found or access denied");
        }

        return transaction;
    }

    public List<Transaction> getRecentTransactions(int limit) {
        validateSession();
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit ");
        }
        return transactionService.getRecentTransactions(getCurrentUsername(), limit);
    }

    //  UPDATE OPERATIONS
    public boolean updateTransaction(String transactionId, String type, String category,
                                     double amount, LocalDate date, String description) {
        validateSession();

        try {
            validateTransactionData(type, category, amount, date);

            Transaction existing = getTransactionById(transactionId);
            if (existing == null) {
                return false;
            }

            Transaction updated = new Transaction(
                    transactionId,
                    getCurrentUsername(),
                    type.toUpperCase(),
                    category,
                    amount,
                    date,
                    description
            );

            return transactionService.updateTransaction(getCurrentUsername(), updated);

        } catch (Exception e) {
            System.err.println("Update transaction error: " + e.getMessage());
            throw new RuntimeException("Failed to update transaction: " + e.getMessage(), e);
        }
    }

    // DELETE OPERATIONS
    public boolean deleteTransaction(String transactionId) {
        validateSession();

        try {
            if (transactionId == null || transactionId.trim().isEmpty()) {
                throw new IllegalArgumentException("Transaction ID cannot be empty");
            }

            return transactionService.deleteTransaction(getCurrentUsername(), transactionId);

        } catch (Exception e) {
            System.err.println("Delete transaction error: " + e.getMessage());
            throw new RuntimeException("Failed to delete transaction: " + e.getMessage(), e);
        }
    }

    public boolean deleteTransactionsByCategory(String category) {
        validateSession();

        try {
            if (category == null || category.trim().isEmpty()) {
                throw new IllegalArgumentException("Category cannot be empty");
            }

            return transactionService.deleteTransactionsByCategory(getCurrentUsername(), category);

        } catch (Exception e) {
            System.err.println("Delete transactions by category error: " + e.getMessage());
            throw new RuntimeException("Failed to delete transactions: " + e.getMessage(), e);
        }
    }

    // ========== REPORT OPERATIONS ==========
    public Map<String, Double> getMonthlySummary(int month, int year) {
        validateSession();
        validateMonthYear(month, year);
        return transactionService.getMonthlySummary(getCurrentUsername(), month, year);
    }

    public Map<String, Double> getCurrentMonthSummary() {
        validateSession();
        LocalDate now = LocalDate.now();
        return getMonthlySummary(now.getMonthValue(), now.getYear());
    }

    public Map<String, Double> getCategorySummary(int month, int year) {
        validateSession();
        validateMonthYear(month, year);
        return transactionService.getCategorySummary(getCurrentUsername(), month, year);
    }

    public Map<String, Double> getIncomeCategorySummary(int month, int year) {
        validateSession();
        validateMonthYear(month, year);
        return transactionService.getIncomeCategorySummary(getCurrentUsername(), month, year);
    }

    public Map<String, Double> getDateRangeSummary(LocalDate start, LocalDate end) {
        validateSession();
        validateDateRange(start, end);
        return transactionService.getDateRangeSummary(getCurrentUsername(), start, end);
    }

    public Map<String, Map<String, Double>> getYearlySummary(int year) {
        validateSession();
        validateYear(year);
        return transactionService.getYearlySummary(getCurrentUsername(), year);
    }

    // UTILITY METHODS
    public List<String> getAllCategories() {
        validateSession();
        return transactionService.getAllCategories(getCurrentUsername());
    }

    public List<String> getIncomeCategories() {
        validateSession();
        return transactionService.getIncomeCategories(getCurrentUsername());
    }

    public List<String> getExpenseCategories() {
        validateSession();
        return transactionService.getExpenseCategories(getCurrentUsername());
    }

    public boolean transferBalance(double amount, String fromCategory,
                                   String toCategory, String description) {
        validateSession();

        try {
            if (amount <= 0) {
                throw new IllegalArgumentException("Transfer amount must be positive");
            }
            if (fromCategory == null || fromCategory.trim().isEmpty()) {
                throw new IllegalArgumentException("Source category cannot be empty");
            }
            if (toCategory == null || toCategory.trim().isEmpty()) {
                throw new IllegalArgumentException("Destination category cannot be empty");
            }
            if (fromCategory.equalsIgnoreCase(toCategory)) {
                throw new IllegalArgumentException("Source and destination categories cannot be the same");
            }

            return transactionService.transferBalance(
                    getCurrentUsername(),
                    amount,
                    fromCategory,
                    toCategory,
                    description
            );

        } catch (Exception e) {
            System.err.println("Balance transfer error: " + e.getMessage());
            throw new RuntimeException("Failed to transfer balance: " + e.getMessage(), e);
        }
    }

    public double getTotalIncome() {
        validateSession();
        return getCurrentMonthSummary().getOrDefault("income", 0.0);
    }

    public double getTotalExpense() {
        validateSession();
        return getCurrentMonthSummary().getOrDefault("expense", 0.0);
    }

    public double getCurrentBalance() {
        validateSession();
        return getCurrentMonthSummary().getOrDefault("balance", 0.0);
    }

    // ========== PRIVATE HELPER METHODS ==========
    private void validateSession() {
        if (!SessionManager.getInstance().isLoggedIn()) {
            throw new IllegalStateException("User not logged in");
        }
    }

    private String getCurrentUsername() {
        return SessionManager.getInstance().getCurrentUser();
    }

    private void validateTransactionData(String type, String category, double amount, LocalDate date) {
        if (type == null || (!"INCOME".equalsIgnoreCase(type) && !"EXPENSE".equalsIgnoreCase(type))) {
            throw new IllegalArgumentException("Type must be INCOME or EXPENSE");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be in the future");
        }
    }

    private void validateMonthYear(int month, int year) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }
        if (year < 2000 || year > LocalDate.now().getYear() + 1) {
            throw new IllegalArgumentException("Year is not valid");
        }
    }

    private void validateDateRange(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end dates cannot be null");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        if (end.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("End date cannot be in the future");
        }
    }

    private void validateYear(int year) {
        if (year < 2000 || year > LocalDate.now().getYear() + 1) {
            throw new IllegalArgumentException("Year is not valid");
        }
    }
}