package service;

import model.Budget;
import repository.CSVRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class BudgetService {
    private CSVRepository repo = new CSVRepository();

    public void setBudget(String username, String category,
                          int month, int year, double limit) {
        if (limit <= 0) throw new IllegalArgumentException("Budget harus > 0");

        // Hapus budget lama jika ada
        deleteBudget(username, category, month, year);

        // Tambah budget baru
        repo.append("budgets.csv",
                username + "," + category + "," + month + "," + year + "," + limit);
    }

    public List<Budget> getBudgets(String username) {
        return repo.read("budgets.csv").stream()
                .filter(r -> r.length >= 5 && r[0].equals(username))
                .map(r -> new Budget(r[0], r[1],
                        Integer.parseInt(r[2]),
                        Integer.parseInt(r[3]),
                        Double.parseDouble(r[4])))
                .collect(Collectors.toList());
    }

    public List<Budget> getBudgetsForMonth(String username, int month, int year) {
        return getBudgets(username).stream()
                .filter(b -> b.getMonth() == month && b.getYear() == year)
                .collect(Collectors.toList());
    }

    public void deleteBudget(String username, String category, int month, int year) {
        List<String[]> allBudgets = repo.read("budgets.csv");
        List<String[]> newBudgets = new ArrayList<>();

        for (String[] row : allBudgets) {
            if (row.length >= 5) {
                boolean isMatch = row[0].equals(username) &&
                        row[1].equals(category) &&
                        row[2].equals(String.valueOf(month)) &&
                        row[3].equals(String.valueOf(year));
                if (!isMatch) {
                    newBudgets.add(row);
                }
            }
        }

        repo.writeAll("budgets.csv", newBudgets);
    }

    public Map<String, Double> getBudgetWarnings(String username) {
        LocalDate now = LocalDate.now();
        TransactionService transactionService = new TransactionService();
        Map<String, Double> warnings = new HashMap<>();

        List<Budget> budgets = getBudgetsForMonth(username,
                now.getMonthValue(), now.getYear());

        for (Budget budget : budgets) {
            double spent = transactionService.getTransactionsByCategory(username,
                            budget.getCategory()).stream()
                    .filter(t -> "EXPENSE".equals(t.getType()))
                    .filter(t -> t.getDate().getMonthValue() == now.getMonthValue() &&
                            t.getDate().getYear() == now.getYear())
                    .mapToDouble(t -> t.getAmount())
                    .sum();

            if (spent > budget.getLimit() * 0.8) {
                double percentage = (spent / budget.getLimit()) * 100;
                warnings.put(budget.getCategory(), percentage);
            }
        }

        return warnings;
    }
}