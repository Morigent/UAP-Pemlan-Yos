package service;

import model.Transaction;
import repository.CSVRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardService {
    private CSVRepository repo = new CSVRepository();
    private TransactionService transactionService = new TransactionService();
    private BudgetService budgetService = new BudgetService();

    public double getSaldoBulanIni(String username) {
        LocalDate now = LocalDate.now();
        Map<String, Double> summary = transactionService.getMonthlySummary(username,
                now.getMonthValue(), now.getYear());
        return summary.get("balance");
    }

    public double getTotalPemasukanBulanIni(String username) {
        LocalDate now = LocalDate.now();
        Map<String, Double> summary = transactionService.getMonthlySummary(username,
                now.getMonthValue(), now.getYear());
        return summary.get("income");
    }

    public double getTotalPengeluaranBulanIni(String username) {
        LocalDate now = LocalDate.now();
        Map<String, Double> summary = transactionService.getMonthlySummary(username,
                now.getMonthValue(), now.getYear());
        return summary.get("expense");
    }

    public List<Transaction> getTransaksiTerbaru(String username, int limit) {
        return repo.read("transactions.csv").stream()
                .filter(r -> r.length >= 7 && r[1].equals(username))
                .map(row -> new Transaction(
                        row[0], row[1], row[2], row[3],
                        Double.parseDouble(row[4]),
                        LocalDate.parse(row[5]),
                        row[6]
                ))
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Map<String, Double> getPengeluaranPerKategori(String username) {
        LocalDate now = LocalDate.now();
        List<Transaction> transactions = transactionService.getTransactions(username,
                now.getMonthValue(), now.getYear());

        return transactions.stream()
                .filter(t -> "EXPENSE".equals(t.getType()))
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    public Map<String, Object> getDashboardData(String username) {
        Map<String, Object> dashboard = new HashMap<>();

        dashboard.put("saldo", getSaldoBulanIni(username));
        dashboard.put("totalPemasukan", getTotalPemasukanBulanIni(username));
        dashboard.put("totalPengeluaran", getTotalPengeluaranBulanIni(username));
        dashboard.put("transaksiTerbaru", getTransaksiTerbaru(username, 5));
        dashboard.put("pengeluaranPerKategori", getPengeluaranPerKategori(username));
        dashboard.put("budgetWarnings", budgetService.getBudgetWarnings(username));

        return dashboard;
    }
}