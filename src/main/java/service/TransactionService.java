package service;

import model.Transaction;
import repository.CSVRepository;
import util.DateUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionService {
    private CSVRepository repo = new CSVRepository();
    private static final String TRANSACTIONS_FILE = "transactions.csv";

    // ========== CREATE ==========
    public void addTransaction(Transaction t) {
        validateTransaction(t);

        if (t.getId() == null || t.getId().isEmpty()) {
            t.setId(generateTransactionId());
        }

        String transactionData = String.join(",",
                t.getId(),
                t.getUsername(),
                t.getType(),
                t.getCategory(),
                String.valueOf(t.getAmount()),
                DateUtil.format(t.getDate()),
                t.getDescription() != null ? t.getDescription() : ""
        );

        repo.append(TRANSACTIONS_FILE, transactionData);
    }

    // ========== READ ==========
    public List<Transaction> getAllTransactions(String username) {
        return repo.read(TRANSACTIONS_FILE).stream()
                .filter(r -> r.length >= 7 && r[1].equals(username))
                .map(this::parseTransaction)
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactions(String username, int month, int year) {
        return getAllTransactions(username).stream()
                .filter(t -> t.getDate().getMonthValue() == month && t.getDate().getYear() == year)
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByDateRange(String username, LocalDate start, LocalDate end) {
        return getAllTransactions(username).stream()
                .filter(t -> !t.getDate().isBefore(start) && !t.getDate().isAfter(end))
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByCategory(String username, String category) {
        return getAllTransactions(username).stream()
                .filter(t -> t.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByType(String username, String type) {
        return getAllTransactions(username).stream()
                .filter(t -> t.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    public Transaction getTransactionById(String username, String transactionId) {
        return getAllTransactions(username).stream()
                .filter(t -> t.getId().equals(transactionId))
                .findFirst()
                .orElse(null);
    }

    public List<Transaction> getRecentTransactions(String username, int limit) {
        return getAllTransactions(username).stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    // ========== UPDATE ==========
    public boolean updateTransaction(String username, Transaction updatedTransaction) {
        try {
            validateTransaction(updatedTransaction);

            Transaction existing = getTransactionById(username, updatedTransaction.getId());
            if (existing == null) {
                return false;
            }

            String[] newData = {
                    updatedTransaction.getId(),
                    updatedTransaction.getUsername(),
                    updatedTransaction.getType(),
                    updatedTransaction.getCategory(),
                    String.valueOf(updatedTransaction.getAmount()),
                    DateUtil.format(updatedTransaction.getDate()),
                    updatedTransaction.getDescription() != null ? updatedTransaction.getDescription() : ""
            };

            repo.update(TRANSACTIONS_FILE, updatedTransaction.getId(), newData);
            return true;

        } catch (Exception e) {
            System.err.println("Error updating transaction: " + e.getMessage());
            return false;
        }
    }

    // ========== DELETE ==========
    public boolean deleteTransaction(String username, String transactionId) {
        try {
            Transaction transaction = getTransactionById(username, transactionId);
            if (transaction == null) {
                return false;
            }

            repo.deleteById(TRANSACTIONS_FILE, transactionId);
            return true;

        } catch (Exception e) {
            System.err.println("Error deleting transaction: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteTransactionsByCategory(String username, String category) {
        try {
            List<Transaction> transactions = getTransactionsByCategory(username, category);
            if (transactions.isEmpty()) {
                return false;
            }

            for (Transaction t : transactions) {
                deleteTransaction(username, t.getId());
            }
            return true;

        } catch (Exception e) {
            System.err.println("Error deleting transactions by category: " + e.getMessage());
            return false;
        }
    }

    // ========== QUERIES & REPORTS ==========
    public Map<String, Double> getMonthlySummary(String username, int month, int year) {
        List<Transaction> transactions = getTransactions(username, month, year);

        double totalIncome = transactions.stream()
                .filter(t -> "INCOME".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalExpense = transactions.stream()
                .filter(t -> "EXPENSE".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        Map<String, Double> summary = new HashMap<>();
        summary.put("income", totalIncome);
        summary.put("expense", totalExpense);
        summary.put("balance", totalIncome - totalExpense);
        summary.put("transaction_count", (double) transactions.size());

        return summary;
    }

    public Map<String, Double> getCategorySummary(String username, int month, int year) {
        return getTransactions(username, month, year).stream()
                .filter(t -> "EXPENSE".equalsIgnoreCase(t.getType()))
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    public Map<String, Double> getIncomeCategorySummary(String username, int month, int year) {
        return getTransactions(username, month, year).stream()
                .filter(t -> "INCOME".equalsIgnoreCase(t.getType()))
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    public Map<String, Double> getDateRangeSummary(String username, LocalDate start, LocalDate end) {
        List<Transaction> transactions = getTransactionsByDateRange(username, start, end);

        double totalIncome = transactions.stream()
                .filter(t -> "INCOME".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalExpense = transactions.stream()
                .filter(t -> "EXPENSE".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        Map<String, Double> summary = new HashMap<>();
        summary.put("income", totalIncome);
        summary.put("expense", totalExpense);
        summary.put("balance", totalIncome - totalExpense);
        summary.put("transaction_count", (double) transactions.size());
        long days = Math.max(1, start.until(end).getDays());
        summary.put("average_daily_income", totalIncome / days);
        summary.put("average_daily_expense", totalExpense / days);

        return summary;
    }

    public List<String> getAllCategories(String username) {
        return getAllTransactions(username).stream()
                .map(Transaction::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<String> getIncomeCategories(String username) {
        return getAllTransactions(username).stream()
                .filter(t -> "INCOME".equalsIgnoreCase(t.getType()))
                .map(Transaction::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<String> getExpenseCategories(String username) {
        return getAllTransactions(username).stream()
                .filter(t -> "EXPENSE".equalsIgnoreCase(t.getType()))
                .map(Transaction::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public Map<String, Map<String, Double>> getYearlySummary(String username, int year) {
        Map<String, Map<String, Double>> yearlySummary = new TreeMap<>();

        for (int month = 1; month <= 12; month++) {
            yearlySummary.put(String.format("%02d", month), getMonthlySummary(username, month, year));
        }

        return yearlySummary;
    }

    // ========== VALIDATION & UTILITY ==========
    private void validateTransaction(Transaction t) {
        if (t == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }
        if (t.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        if (t.getType() == null || (!"INCOME".equalsIgnoreCase(t.getType()) && !"EXPENSE".equalsIgnoreCase(t.getType()))) {
            throw new IllegalArgumentException("Type must be INCOME or EXPENSE");
        }
        if (t.getCategory() == null || t.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        if (t.getDate() == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (t.getDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be in the future");
        }
        if (t.getUsername() == null || t.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
    }

    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private Transaction parseTransaction(String[] row) {
        try {
            return new Transaction(
                    row[0], // id
                    row[1], // username
                    row[2], // type
                    row[3], // category
                    Double.parseDouble(row[4]), // amount
                    DateUtil.parse(row[5]), // date
                    row.length > 6 ? row[6] : "" // description
            );
        } catch (Exception e) {
            System.err.println("Error parsing transaction row: " + Arrays.toString(row));
            throw e;
        }
    }

    // ========== BULK OPERATIONS ==========
    public boolean importTransactions(String username, List<Transaction> transactions) {
        try {
            for (Transaction t : transactions) {
                t.setUsername(username); // Pastikan username sesuai
                addTransaction(t);
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error importing transactions: " + e.getMessage());
            return false;
        }
    }

    public boolean transferBalance(String username, double amount, String fromCategory,
                                   String toCategory, String description) {
        try {
            if (amount <= 0) {
                throw new IllegalArgumentException("Transfer amount must be positive");
            }

            // Buat transaksi pengeluaran dari kategori asal
            Transaction expense = new Transaction(
                    generateTransactionId(),
                    username,
                    "EXPENSE",
                    fromCategory,
                    amount,
                    LocalDate.now(),
                    description != null ? description : "Transfer to " + toCategory
            );
            addTransaction(expense);

            // Buat transaksi pemasukan ke kategori tujuan
            Transaction income = new Transaction(
                    generateTransactionId(),
                    username,
                    "INCOME",
                    toCategory,
                    amount,
                    LocalDate.now(),
                    description != null ? description : "Transfer from " + fromCategory
            );
            addTransaction(income);

            return true;

        } catch (Exception e) {
            System.err.println("Error in balance transfer: " + e.getMessage());
            return false;
        }
    }
}