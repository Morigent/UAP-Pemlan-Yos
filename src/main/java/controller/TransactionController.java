package controller;

import model.Transaction;
import service.TransactionService;
import util.SessionManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class TransactionController {
    private TransactionService transactionService = new TransactionService();

    public void addTransaction(String type, String category,
                               double amount, LocalDate date, String description) {
        String username = SessionManager.getInstance().getCurrentUser();
        if (username == null) throw new IllegalStateException("User belum login");

        try {
            if (amount <= 0) {
                throw new IllegalArgumentException("Nominal harus lebih dari 0");
            }
            if (type == null || (!type.equals("INCOME") && !type.equals("EXPENSE"))) {
                throw new IllegalArgumentException("Tipe transaksi harus INCOME atau EXPENSE");
            }
            if (category == null || category.trim().isEmpty()) {
                throw new IllegalArgumentException("Kategori tidak boleh kosong");
            }

            Transaction transaction = new Transaction(
                    null, username, type, category, amount, date, description
            );
            transactionService.addTransaction(transaction);
        } catch (Exception e) {
            System.err.println("Add transaction error: " + e.getMessage());
            throw e;
        }
    }

    public List<Transaction> getTransactionsThisMonth() {
        String username = SessionManager.getInstance().getCurrentUser();
        if (username == null) throw new IllegalStateException("User belum login");

        LocalDate now = LocalDate.now();
        return transactionService.getTransactions(username,
                now.getMonthValue(), now.getYear());
    }

    public List<Transaction> getTransactionsByMonth(int month, int year) {
        String username = SessionManager.getInstance().getCurrentUser();
        if (username == null) throw new IllegalStateException("User belum login");

        return transactionService.getTransactions(username, month, year);
    }

    public List<Transaction> getTransactionsByDateRange(LocalDate start, LocalDate end) {
        String username = SessionManager.getInstance().getCurrentUser();
        if (username == null) throw new IllegalStateException("User belum login");

        return transactionService.getTransactionsByDateRange(username, start, end);
    }

    public void deleteTransaction(String transactionId) {
        String username = SessionManager.getInstance().getCurrentUser();
        if (username == null) throw new IllegalStateException("User belum login");

        transactionService.deleteTransaction(transactionId);
    }

    public Map<String, Double> getMonthlySummary(int month, int year) {
        String username = SessionManager.getInstance().getCurrentUser();
        if (username == null) throw new IllegalStateException("User belum login");

        return transactionService.getMonthlySummary(username, month, year);
    }
}