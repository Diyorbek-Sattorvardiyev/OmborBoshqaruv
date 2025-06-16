package com.example.omborboshqaruv.Models;

public class NotificationModel {
    private String type;
    private String title;
    private String message;
    private int product_id;
    private String priority;
    private Integer days_left;

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    private boolean isRead = false;

    public String getType() { return type; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public int getProduct_id() { return product_id; }
    public String getPriority() { return priority; }
    public Integer getDays_left() { return days_left; }
}
