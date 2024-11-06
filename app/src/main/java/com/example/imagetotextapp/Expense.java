package com.example.imagetotextapp;

public class Expense {
    private String itemName;
    private double price;
    private String category;

    public Expense(String itemName, double price, String category) {
        this.itemName = itemName;
        this.price = price;
        this.category = category;
    }

    public String getItemName() {
        return itemName;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }
}