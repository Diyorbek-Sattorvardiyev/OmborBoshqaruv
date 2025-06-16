package com.example.omborboshqaruv.Models;

public class Product {

    private int id;
    private String name;
    private String code;
    private String barcode;
    private String unit;
    private double purchase_price;
    private double selling_price;
    private int min_stock;
    private int current_stock;
    private boolean low_stock;
    private String image_path;
    private String expiry_date;
    private String description;
    private String owner;


    private static final String BASE_IMAGE_URL = "http://192.168.86.217:5000/uploads/";


    public String getImage_url() {
        if (image_path == null || image_path.isEmpty()) {
            return null;
        }
        return BASE_IMAGE_URL + image_path;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getUnit() {
        return unit;
    }

    public double getPurchase_price() {
        return purchase_price;
    }

    public double getSelling_price() {
        return selling_price;
    }

    public int getMin_stock() {
        return min_stock;
    }

    public int getCurrent_stock() {
        return current_stock;
    }

    public boolean isLow_stock() {
        return low_stock;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public String getDescription() {
        return description;
    }

    public String getOwner() {
        return owner;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setPurchase_price(double purchase_price) {
        this.purchase_price = purchase_price;
    }

    public void setSelling_price(double selling_price) {
        this.selling_price = selling_price;
    }

    public void setMin_stock(int min_stock) {
        this.min_stock = min_stock;
    }

    public void setCurrent_stock(int current_stock) {
        this.current_stock = current_stock;
    }

    public void setLow_stock(boolean low_stock) {
        this.low_stock = low_stock;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
