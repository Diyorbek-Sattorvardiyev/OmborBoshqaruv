package com.example.omborboshqaruv.Models;

import java.util.List;

public class SalesResponse {
    private double total_sales;
    private int total_quantity;
    private String period;
    private double total_expense;
    private List<TopProduct> top_products;

    public double getTotal_sales() {
        return total_sales;
    }
    public double getTotal_expense() {
        return total_expense;
    }

    public int getTotal_quantity() {
        return total_quantity;
    }

    public String getPeriod() {
        return period;
    }

    public List<TopProduct> getTopProducts() {
        return top_products;
    }
}
