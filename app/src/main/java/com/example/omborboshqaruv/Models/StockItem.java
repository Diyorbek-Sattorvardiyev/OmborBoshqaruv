package com.example.omborboshqaruv.Models;



public class StockItem {
    private int product_id;
    private String product_name;
    private String unit;
    private int current_stock;
    private int min_stock;
    private boolean low_stock;
    private double stock_value;
    private String owner;

    public int getProduct_id() { return product_id; }
    public String getProduct_name() { return product_name; }
    public String getUnit() { return unit; }
    public int getCurrent_stock() { return current_stock; }
    public int getMin_stock() { return min_stock; }
    public boolean isLow_stock() { return low_stock; }
    public double getStock_value() { return stock_value; }
    public String getOwner() { return owner; }
}
