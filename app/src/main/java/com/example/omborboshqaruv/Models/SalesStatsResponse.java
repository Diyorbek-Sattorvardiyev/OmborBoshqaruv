package com.example.omborboshqaruv.Models;

import java.util.List;

public class SalesStatsResponse {
    private double total_sales;
    private int total_quantity;
    private String period;
    private List<TopProduct> top_products;

    public double getTotal_sales() { return total_sales; }
    public int getTotal_quantity() { return total_quantity; }
    public String getPeriod() { return period; }
    public List<TopProduct> getTop_products() { return top_products; }
}


