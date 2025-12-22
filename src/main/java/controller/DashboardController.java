package controller;

import model.Transaction;
import service.DashboardService;
import util.SessionManager;

import java.util.List;
import java.util.Map;

public class DashboardController {
    private DashboardService dashboardService = new DashboardService();

    public double getSaldoBulanIni() {
        String username = SessionManager.getInstance().getCurrentUser();
        if (username == null) throw new IllegalStateException("User belum login");
        return dashboardService.getSaldoBulanIni(username);
    }

    public double getTotalPemasukanBulanIni() {
        String username = SessionManager.getInstance().getCurrentUser();
        if (username == null) throw new IllegalStateException("User belum login");
        return dashboardService.getTotalPemasukanBulanIni(username);
    }

    public double getTotalPengeluaranBulanIni() {
        String username = SessionManager.getInstance().getCurrentUser();
        if (username == null) throw new IllegalStateException("User belum login");
        return dashboardService.getTotalPengeluaranBulanIni(username);
    }

    public List<Transaction> getTransaksiTerbaru() {
        String username = SessionManager.getInstance().getCurrentUser();
        if (username == null) throw new IllegalStateException("User belum login");
        return dashboardService.getTransaksiTerbaru(username, 10);
    }

    public Map<String, Double> getPengeluaranPerKategori() {
        String username = SessionManager.getInstance().getCurrentUser();
        if (username == null) throw new IllegalStateException("User belum login");
        return dashboardService.getPengeluaranPerKategori(username);
    }

    public Map<String, Object> getDashboardData() {
        String username = SessionManager.getInstance().getCurrentUser();
        if (username == null) throw new IllegalStateException("User belum login");
        return dashboardService.getDashboardData(username);
    }
}