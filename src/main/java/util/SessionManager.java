package util;

public class SessionManager {
    private static SessionManager instance;
    private String currentUser;

    private SessionManager() {
        // Private constructor
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(String username) {
        this.currentUser = username;
        System.out.println("User " + username + " logged in");
    }

    public void logout() {
        System.out.println("User " + currentUser + " logged out");
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public String getCurrentUser() {
        return currentUser;
    }
}