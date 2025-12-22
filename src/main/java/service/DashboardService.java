package service;

import model.Transaction;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public class DashboardService {
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
        LocalDate now = LocalDate.now();
        List<Transaction> transactions = transactionService.getTransactions(
                username, now.getMonthValue(), now.getYear());

        return transactions.stream()
                .limit(limit)
                .collect(java.util.stream.Collectors.toList());
    }

    public Map<String, Double> getPengeluaranPerKategori(String username) {
        LocalDate now = LocalDate.now();
        return transactionService.getCategorySummary(username,
                now.getMonthValue(), now.getYear());
    }

    public Map<String, Object> getDashboardData(String username) {
        LocalDate now = LocalDate.now();
        Map<String, Object> dashboardData = new java.util.HashMap<>();

        // Data saldo
        dashboardData.put("saldo", getSaldoBulanIni(username));
        dashboardData.put("pemasukan", getTotalPemasukanBulanIni(username));
        dashboardData.put("pengeluaran", getTotalPengeluaranBulanIni(username));

        // Transaksi terbaru (5 terbaru)
        dashboardData.put("transaksi_terbaru", getTransaksiTerbaru(username, 5));

        // Ringkasan pengeluaran perkategori
        dashboardData.put("pengeluaran_per_kategori", getPengeluaranPerKategori(username));

        // Peringatan budget melebihi
        dashboardData.put("peringatan_budget", budgetService.getBudgetWarnings(username));

        return dashboardData;
    }
}