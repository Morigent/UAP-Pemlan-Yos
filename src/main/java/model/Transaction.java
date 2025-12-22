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
    public String getId() {
        return id; }
    public String getUsername() {
        return username; }
    public String getType() {
        return type; }
    public double getAmount() {
        return amount; }
    public LocalDate getDate() {
        return date; }
    public String getCategory() {
        return category; }
    public String getDescription() {
        return description; }

    public void setId(String id) {
        this.id = id; }
    public void setType(String type) {
        this.type = type; }
    public void setCategory(String category) {
        this.category = category; }
    public void setAmount(double amount) {
        this.amount = amount; }
    public void setDate(LocalDate date) {
        this.date = date; }
    public void setDescription(String description) {
        this.description = description; }
}
