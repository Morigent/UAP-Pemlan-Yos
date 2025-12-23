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
            System.err.println("tambah transaksi error: " + e.getMessage());
            throw new RuntimeException("gagal tambah transaksi: " + e.getMessage(), e);
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
            throw new IllegalArgumentException("kategori tidak boleh kosong");
        }
        return transactionService.getTransactionsByCategory(getCurrentUsername(), category);
    }

    public List<Transaction> getTransactionsByType(String type) {
        validateSession();
        if (!"INCOME".equalsIgnoreCase(type) && !"EXPENSE".equalsIgnoreCase(type)) {
            throw new IllegalArgumentException("Tipe  income atau expense");
        }
        return transactionService.getTransactionsByType(getCurrentUsername(), type.toUpperCase());
    }

    public Transaction getTransactionById(String transactionId) {
        validateSession();
        if (transactionId == null || transactionId.trim().isEmpty()) {
            throw new IllegalArgumentException("transaksi id tidak boleh kosong");
        }

        Transaction transaction = transactionService.getTransactionById(
                getCurrentUsername(), transactionId);

        if (transaction == null) {
            throw new RuntimeException("transaksi tidak ditemukan");
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
            System.err.println("Update transaksi error: " + e.getMessage());
            throw new RuntimeException("gagal update transaksi: " + e.getMessage(), e);
        }
    }

    // DELETE OPERATIONS
    public boolean deleteTransaction(String transactionId) {
        validateSession();

        try {
            if (transactionId == null || transactionId.trim().isEmpty()) {
                throw new IllegalArgumentException("Transaksi ID tidak boleh kosong");
            }

            return transactionService.deleteTransaction(getCurrentUsername(), transactionId);

        } catch (Exception e) {
            System.err.println("transaksi error: " + e.getMessage());
            throw new RuntimeException("gagal menghapus transaksi: " + e.getMessage(), e);
        }
    }

    public boolean deleteTransactionsByCategory(String category) {
        validateSession();

        try {
            if (category == null || category.trim().isEmpty()) {
                throw new IllegalArgumentException("kategori tidak boleh kosong");
            }

            return transactionService.deleteTransactionsByCategory(getCurrentUsername(), category);

        } catch (Exception e) {
            System.err.println("hapus transaksi error: " + e.getMessage());
            throw new RuntimeException("gagal hapus transaksi: " + e.getMessage(), e);
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
                throw new IllegalArgumentException("Jumlah transfer harus positif");
            }
            if (fromCategory == null || fromCategory.trim().isEmpty()) {
                throw new IllegalArgumentException("Kategori sumber tidak boleh kosong");
            }
            if (toCategory == null || toCategory.trim().isEmpty()) {
                throw new IllegalArgumentException("Kategori tujuan tidak boleh kosong");
            }
            if (fromCategory.equalsIgnoreCase(toCategory)) {
                throw new IllegalArgumentException("Kategori sumber dan tujuan tidak bisa sama");
            }

            return transactionService.transferBalance(
                    getCurrentUsername(),
                    amount,
                    fromCategory,
                    toCategory,
                    description
            );

        } catch (Exception e) {
            System.err.println("transfer error: " + e.getMessage());
            throw new RuntimeException("Failed transfer: " + e.getMessage(), e);
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
            throw new IllegalArgumentException("tipe INCOME / EXPENSE");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("kategori tidak boleh kosong");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Jumlah harus lebih besar dari 0");
        }
        if (date == null) {
            throw new IllegalArgumentException("tanggal tidak boleh kosong");
        }
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("tanggal tidak valid");
        }
    }

    private void validateMonthYear(int month, int year) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Bulan harus antara 1 dan 12");
        }
        if (year < 2000 || year > LocalDate.now().getYear() + 1) {
            throw new IllegalArgumentException("tahun tidak valid");
        }
    }

    private void validateDateRange(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Tanggal mulai dan berakhir tidak boleh kosong ");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Tanggal mulai harus sebelum tanggal akhir");
        }
        if (end.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Tanggal akhir tidak valid");
        }
    }

    private void validateYear(int year) {
        if (year < 2000 || year > LocalDate.now().getYear() + 1) {
            throw new IllegalArgumentException("tahun tidak valid");
        }
    }
}