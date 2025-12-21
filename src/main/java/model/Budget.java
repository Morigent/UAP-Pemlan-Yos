package model;
public class Budget {
    private String username;
    private String category;
    private int month;
    private int year;
    private double limit;

    public Budget(String username, String category, int month, int year, double limit) {
        this.username = username;
        this.category = category;
        this.month = month;
        this.year = year;
        this.limit = limit;
    }

    public String getUsername() { return username; }
    public String getCategory() { return category; }
    public int getMonth() { return month; }
    public int getYear() { return year; }
    public double getLimit() { return limit; }
}
