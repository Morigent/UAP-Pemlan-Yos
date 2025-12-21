package model;

import java.time.LocalDate;

public class Transaction {
    private String id;
    private String username;
    private String type;
    private String category;
    private double amount;
    private LocalDate date;
    private String description;

    public Transaction(String id, String username, String type,
                       String category, double amount,
                       LocalDate date, String description) {
        this.id = id;
        this.username = username;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public String getUsername() { return username; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public String getCategory() { return category; }
}
