package controller;

import model.Transaction;
import service.DashboardService;
import util.SessionManager;

import java.util.List;
import java.util.Map;
import java.util.Collections;

public class DashboardController {
    private DashboardService dashboardService = new DashboardService();

    public double getSaldoBulanIni() {
        validateSession();
        String username = getCurrentUsername();
        return dashboardService.getSaldoBulanIni(username);
    }

    public double getTotalPemasukanBulanIni() {
        validateSession();
        String username = getCurrentUsername();
        return dashboardService.getTotalPemasukanBulanIni(username);
    }

    public double getTotalPengeluaranBulanIni() {
        validateSession();
        String username = getCurrentUsername();
        return dashboardService.getTotalPengeluaranBulanIni(username);
    }

    public List<Transaction> getTransaksiTerbaru() {
        return getTransaksiTerbaru(10);
    }

    public List<Transaction> getTransaksiTerbaru(int limit) {
        validateSession();
        if (limit <= 0) throw new IllegalArgumentException("transaksi > 0");
        String username = getCurrentUsername();
        return dashboardService.getTransaksiTerbaru(username, limit);
    }

    public Map<String, Double> getPengeluaranPerKategori() {
        validateSession();
        String username = getCurrentUsername();
        Map<String, Double> result = dashboardService.getPengeluaranPerKategori(username);
        return result != null ? result : Collections.emptyMap();
    }

    public Map<String, Object> getDashboardData() {
        validateSession();
        String username = getCurrentUsername();
        Map<String, Object> data = dashboardService.getDashboardData(username);
        return data != null ? data : Collections.emptyMap();
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