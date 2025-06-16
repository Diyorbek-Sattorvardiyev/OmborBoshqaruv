package com.example.omborboshqaruv.Models;

public class TopProduct {
    private String name;
    private int quantity;
    private double revenue;

    public TopProduct(String name, int quantity, double revenue) {
        this.name = name;
        this.quantity = quantity;
        this.revenue = revenue;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
}
