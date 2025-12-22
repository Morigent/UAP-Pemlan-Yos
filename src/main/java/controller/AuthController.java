package controller;

import service.AuthService;
import util.SessionManager;

public class AuthController {
    private AuthService authService = new AuthService();

    public boolean register(String username, String password, String confirmPassword) {
        try {
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Username tidak boleh kosong");
            }
            if (password == null || password.isEmpty()) {
                throw new IllegalArgumentException("Password tidak boleh kosong");
            }
            if (!password.equals(confirmPassword)) {
                throw new IllegalArgumentException("Password tidak cocok");
            }
            if (password.length() < 6) {
                throw new IllegalArgumentException("Password minimal 6 karakter");
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
}