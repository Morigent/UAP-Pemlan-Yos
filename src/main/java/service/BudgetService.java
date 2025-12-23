package service;

import model.Budget;
import repository.CSVRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class BudgetService {
    private CSVRepository repo = new CSVRepository();

    // Method baru: Update existing budget
    public boolean updateBudget(String username, String category,
                                int month, int year, double newLimit) {
        if (newLimit <= 0) {
            throw new IllegalArgumentException("Budget harus > 0");
        }

        List<String[]> allBudgets = repo.read("budgets.csv");
        boolean found = false;

        for (int i = 0; i < allBudgets.size(); i++) {
            String[] row = allBudgets.get(i);
            if (row.length >= 5 &&
                    row[0].equals(username) &&
                    row[1].equals(category) &&
                    row[2].equals(String.valueOf(month)) &&
                    row[3].equals(String.valueOf(year))) {

                allBudgets.set(i, new String[]{
                        username, category,
                        String.valueOf(month),
                        String.valueOf(year),
                        String.valueOf(newLimit)
                });
                found = true;
                break;
            }
        }

        if (found) {
            repo.writeAll("budgets.csv", allBudgets);
        }

        return found;
    }

    // Method baru: Get specific budget
    public Budget getBudget(String username, String category, int month, int year) {
        return getBudgets(username).stream()
                .filter(b -> b.getCategory().equals(category) &&
                        b.getMonth() == month &&
                        b.getYear() == year)
                .findFirst()
                .orElse(null);
    }

    // Method baru: Check if budget exists
    public boolean budgetExists(String username, String category, int month, int year) {
        return getBudget(username, category, month, year) != null;
    }

    // Method baru: Get total budget for month
    public double getTotalBudgetForMonth(String username, int month, int year) {
        return getBudgetsForMonth(username, month, year).stream()
                .mapToDouble(Budget::getLimit)
                .sum();
    }

    // Method baru: Get categories with budget
    public List<String> getBudgetCategories(String username, int month, int year) {
        return getBudgetsForMonth(username, month, year).stream()
                .map(Budget::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }

    // Method yang sudah ada (perbaikan)
    public void setBudget(String username, String category,
                          int month, int year, double limit) {
        if (limit <= 0) throw new IllegalArgumentException("Budget harus > 0");
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Kategori tidak boleh kosong");
        }

        // Cek dulu apakah sudah ada
        if (budgetExists(username, category, month, year)) {
            updateBudget(username, category, month, year, limit);
        } else {
            repo.append("budgets.csv",
                    username + "," + category + "," + month + "," + year + "," + limit);
        }
    }

    // Restore methods expected by controllers
    public List<Budget> getBudgets(String username) {
        return repo.read("budgets.csv").stream()
                .filter(r -> r.length >= 5 && r[0].equals(username))
                .map(r -> new Budget(r[0], r[1], Integer.parseInt(r[2]), Integer.parseInt(r[3]), Double.parseDouble(r[4])))
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

        List<Budget> budgets = getBudgetsForMonth(username, now.getMonthValue(), now.getYear());

        for (Budget budget : budgets) {
            double spent = transactionService.getTransactionsByCategory(username, budget.getCategory()).stream()
                    .filter(t -> "EXPENSE".equalsIgnoreCase(t.getType()))
                    .filter(t -> t.getDate().getMonthValue() == now.getMonthValue() && t.getDate().getYear() == now.getYear())
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