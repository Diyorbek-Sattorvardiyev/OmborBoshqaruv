package com.example.omborboshqaruv.Models;

public class ExpenseRequest {
    private String category;
    private double amount;
    private String description;
    private String expense_date;

    public ExpenseRequest(String category, double amount, String description, String expense_date) {
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.expense_date = expense_date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpense_date() {
        return expense_date;
    }

    public void setExpense_date(String expense_date) {
        this.expense_date = expense_date;
    }
}
