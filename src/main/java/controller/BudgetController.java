package controller;

import model.Budget;
import service.BudgetService;
import util.SessionManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class BudgetController {
    private BudgetService budgetService = new BudgetService();

    public void setBudget(String category, int month, int year, double limit) {
        String username = SessionManager.getInstance().getCurrentUser();
        if (username == null) throw new IllegalStateException("User belum login");

        try {
            if (limit <= 0) {
                throw new IllegalArgumentException("Budget harus lebih dari 0");
            }
            if (category == null || category.trim().isEmpty()) {
                throw new IllegalArgumentException("Kategori tidak boleh kosong");
            }
            if (month < 1 || month > 12) {
                throw new IllegalArgumentException("Bulan harus antara 1-12");
            }
            if (year < 2000 || year > 2100) {
                throw new IllegalArgumentException("Tahun tidak valid");
            }

            budgetService.setBudget(username, category, month, year, limit);
        } catch (Exception e) {
            System.err.println("Set budget error: " + e.getMessage());
            throw e;
        }
    }

    public List<Budget> getBudgets() {
        String username = SessionManager.getInstance().getCurrentUser();
        if (username == null) throw new IllegalStateException("User belum login");

        return budgetService.getBudgets(username);
    }

    public List<Budget> getBudgetsThisMonth() {
        String username = SessionManager.getInstance().getCurrentUser();
        if (username == null) throw new IllegalStateException("User belum login");

        LocalDate now = LocalDate.now();
        return budgetService.getBudgetsForMonth(username,
                now.getMonthValue(), now.getYear());
    }

    public void deleteBudget(String category, int month, int year) {
        String username = SessionManager.getInstance().getCurrentUser();
        if (username == null) throw new IllegalStateException("User belum login");

        budgetService.deleteBudget(username, category, month, year);
    }

    public Map<String, Double> getBudgetWarnings() {
        String username = SessionManager.getInstance().getCurrentUser();
        if (username == null) throw new IllegalStateException("User belum login");

        return budgetService.getBudgetWarnings(username);
    }
}