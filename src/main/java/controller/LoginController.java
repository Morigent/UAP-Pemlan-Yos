package controller;

import service.LoginService;
import util.SessionManager;

public class LoginController {
    private LoginService authService = new LoginService();

    public boolean register(String username, String password, String confirmPassword) {
        try {
            // Validasi input
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Username tidak boleh kosong");
            }
            if (password == null || password.isEmpty()) {
                throw new IllegalArgumentException("Password tidak boleh kosong");
            }
            if (!password.equals(confirmPassword)) {
                throw new IllegalArgumentException("Password tidak cocok");
            }
            if (username.length() < 3) {
                throw new IllegalArgumentException("Username minimal 3 karakter");
            }
            if (password.length() < 4) {
                throw new IllegalArgumentException("Password minimal 4 karakter");
            }

            // Cek karakter spesial di username
            if (!username.matches("^[a-zA-Z0-9_]+$")) {
                throw new IllegalArgumentException("Username hanya boleh huruf, angka, dan underscore");
            }

            return authService.register(username, password);
        } catch (Exception e) {
            System.err.println("Register error: " + e.getMessage());
            throw e;
        }
    }

    public boolean login(String username, String password) {
        try {
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Username tidak boleh kosong");
            }
            if (password == null || password.isEmpty()) {
                throw new IllegalArgumentException("Password tidak boleh kosong");
            }

            boolean success = authService.login(username, password);
            if (success) {
                SessionManager.getInstance().login(username);
            }
            return success;
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            throw e;
        }
    }

    public void logout() {
        SessionManager.getInstance().logout();
    }

    public boolean isLoggedIn() {
        return SessionManager.getInstance().isLoggedIn();
    }

    public String getCurrentUser() {
        return SessionManager.getInstance().getCurrentUser();
    }

    // Method untuk change password
    public boolean changePassword(String currentPassword, String newPassword, String confirmPassword) {
        try {
            String username = getCurrentUser();
            if (username == null) {
                throw new IllegalStateException("User belum login");
            }

            // Validasi
            if (newPassword == null || newPassword.isEmpty()) {
                throw new IllegalArgumentException("Password baru tidak boleh kosong");
            }
            if (!newPassword.equals(confirmPassword)) {
                throw new IllegalArgumentException("Konfirmasi password tidak cocok");
            }
            if (newPassword.length() < 4) {
                throw new IllegalArgumentException("Password baru minimal 4 karakter");
            }

            // Cek password saat ini
            if (!authService.login(username, currentPassword)) {
                throw new IllegalArgumentException("Password saat ini salah");
            }

            return authService.updatePassword(username, newPassword);
        } catch (Exception e) {
            System.err.println("Change password error: " + e.getMessage());
            throw e;
        }
    }
}