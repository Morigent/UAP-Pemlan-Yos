package service;

import model.Transaction;

import java.time.LocalDate;
import java.util.List;

public class DashboardService {

    private TransactionService transactionService = new TransactionService();

    public double hitungSaldoBulanIni(String username) {
        LocalDate now = LocalDate.now();
        List<Transaction> list =
                transactionService.getTransactions(username, now.getMonthValue(), now.getYear());

        double pemasukan = list.stream()
                .filter(t -> t.getType().equals("INCOME"))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double pengeluaran = list.stream()
                .filter(t -> t.getType().equals("EXPENSE"))
                .mapToDouble(Transaction::getAmount)
                .sum();

        return pemasukan - pengeluaran;
    }
}
