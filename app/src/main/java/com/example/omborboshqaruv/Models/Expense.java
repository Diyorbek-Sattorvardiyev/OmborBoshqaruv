package com.example.omborboshqaruv.Models;

public class Expense {
    private int id;
    private String category;
    private double amount;
    private String description;
    private String expense_date;
    private String user_name;

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getExpense_date() {
        return expense_date;
    }

    public String getUser_name() {
        return user_name;
    }
}
