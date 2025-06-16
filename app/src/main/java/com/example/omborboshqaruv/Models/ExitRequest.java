package com.example.omborboshqaruv.Models;

public class ExitRequest {
    private int product_id;
    private int quantity;
    private double unit_price;
    private String customer_name;

    public String getExit_date() {
        return exit_date;
    }

    public void setExit_date(String exit_date) {
        this.exit_date = exit_date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    private String notes;
    private String exit_date;

    public ExitRequest(int product_id, int quantity, double unit_price,
                       String customer_name, String notes, String exit_date) {
        this.product_id = product_id;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.customer_name = customer_name;
        this.notes = notes;
        this.exit_date = exit_date;
    }


}

