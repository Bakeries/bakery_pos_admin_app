package com.example.bakery_pos_admin_app.Models;

import java.io.Serializable;

public class Product implements Serializable {

    private long barcode;
    private String name, category, description;
    private double price;
    private boolean vegan;

    // Default Constructor.
    public Product() {}

    // Constructor for searching product via name.
    public Product(String name) {
        this.name = name;
    }

    public Product(long barcode, String name, String description, String category, boolean vegan, double price) {
        this.barcode = barcode;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.vegan = vegan;
    }

    // Classes Getters.
    public long getBarcode() {return barcode;}
    public String getName() {return name;}
    public String getCategory() {return category;}
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public boolean isVegan() {return vegan;}
}
