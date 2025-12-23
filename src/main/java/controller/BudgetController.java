package Controller;

import model.Budget;
import service.BudgetService;
import util.SessionManager;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BudgetController {
    private BudgetService budgetService = new BudgetService();

    public void setBudget(String category, int month, int year, double limit) {
        validateSession();
        String username = getCurrentUsername();

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
            System.err.println("budget error: " + e.getMessage());
            throw e;
        }
    }

    public List<Budget> getBudgets() {
        validateSession();
        String username = getCurrentUsername();
        try {
            return budgetService.getBudgets(username);
        } catch (Exception e) {
            System.err.println("budgets error: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Budget> getBudgetsThisMonth() {
        validateSession();
        String username = getCurrentUsername();
        try {
            LocalDate now = LocalDate.now();
            return budgetService.getBudgetsForMonth(username,
                    now.getMonthValue(), now.getYear());
        } catch (Exception e) {
            System.err.println("error: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public void deleteBudget(String category, int month, int year) {
        validateSession();
        String username = getCurrentUsername();

        try {
            if (category == null || category.trim().isEmpty()) {
                throw new IllegalArgumentException("Kategori tidak boleh kosong");
            }
            if (month < 1 || month > 12) {
                throw new IllegalArgumentException("Bulan harus antara 1-12");
            }
            if (year < 2000 || year > 2100) {
                throw new IllegalArgumentException("Tahun tidak valid");
            }

            budgetService.deleteBudget(username, category, month, year);
        } catch (Exception e) {
            System.err.println("hapus budget error: " + e.getMessage());
            throw e;
        }
    }

    public Map<String, Double> getBudgetWarnings() {
        validateSession();
        String username = getCurrentUsername();
        try {
            return budgetService.getBudgetWarnings(username);
        } catch (Exception e) {
            System.err.println("warnings error: " + e.getMessage());
            return Collections.emptyMap();
        }
    }


    private void validateSession() {
        if (!SessionManager.getInstance().isLoggedIn()) {
            throw new IllegalStateException("User belum login");
        }
    }

    private String getCurrentUsername() {
        return SessionManager.getInstance().getCurrentUser();
    }
}