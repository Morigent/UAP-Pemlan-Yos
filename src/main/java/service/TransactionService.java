package service;

import model.Transaction;
import repository.CSVRepository;
import util.DateUtil;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionService {
    private CSVRepository repo = new CSVRepository();

    public void addTransaction(Transaction t) {
        if (t.getAmount() <= 0) throw new IllegalArgumentException("Nominal harus > 0");
        if (t.getId() == null || t.getId().isEmpty()) {
            t.setId(UUID.randomUUID().toString());
        }

        repo.append("transactions.csv",
                String.join(",",
                        t.getId(),
                        t.getUsername(),
                        t.getType(),
                        t.getCategory(),
                        String.valueOf(t.getAmount()),
                        t.getDate().format(DateUtil.FORMATTER),
                        t.getDescription()));
    }

    public List<Transaction> getTransactions(String username, int month, int year) {
        return repo.read("transactions.csv").stream()
                .filter(r -> r.length >= 7 && r[1].equals(username))
                .map(this::parseTransaction)
                .filter(t -> t.getDate().getMonthValue() == month &&
                        t.getDate().getYear() == year)
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByDateRange(String username, LocalDate start, LocalDate end) {
        return repo.read("transactions.csv").stream()
                .filter(r -> r.length >= 7 && r[1].equals(username))
                .map(this::parseTransaction)
                .filter(t -> !t.getDate().isBefore(start) && !t.getDate().isAfter(end))
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByCategory(String username, String category) {
        return repo.read("transactions.csv").stream()
                .filter(r -> r.length >= 7 && r[1].equals(username) && r[3].equals(category))
                .map(this::parseTransaction)
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .collect(Collectors.toList());
    }

    public void deleteTransaction(String transactionId) {
        repo.deleteById("transactions.csv", transactionId);
    }

    public Map<String, Double> getMonthlySummary(String username, int month, int year) {
        List<Transaction> transactions = getTransactions(username, month, year);

        double totalIncome = transactions.stream()
                .filter(t -> "INCOME".equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalExpense = transactions.stream()
                .filter(t -> "EXPENSE".equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        Map<String, Double> summary = new HashMap<>();
        summary.put("income", totalIncome);
        summary.put("expense", totalExpense);
        summary.put("balance", totalIncome - totalExpense);

        return summary;
    }

    public Map<String, Double> getCategorySummary(String username, int month, int year) {
        return getTransactions(username, month, year).stream()
                .filter(t -> "EXPENSE".equals(t.getType()))
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    private Transaction parseTransaction(String[] row) {
        return new Transaction(
                row[0], row[1], row[2], row[3],
                Double.parseDouble(row[4]),
                DateUtil.parse(row[5]),
                row[6]
        );
    }
}