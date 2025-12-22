package model;

public class User {
    private String username;
    private String password;


    public User(String username, String passwordHash, String salt) {
        this.username = username;
        this.password = passwordHash;

    }

    public String getUsername() {
        return username; }
    public String getPasswordHash() {
        return password; }
    public void setPassword(String password) {
        this.password = password; }
}