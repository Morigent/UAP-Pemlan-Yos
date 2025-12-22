package util;

public class SessionManager {
    private static SessionManager instance;
    private String currentUser;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(String username) {
        this.currentUser = username;
        System.out.println("User logged in: " + username);
    }

    public void logout() {
        this.currentUser = null;
        System.out.println("User logged out");
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}