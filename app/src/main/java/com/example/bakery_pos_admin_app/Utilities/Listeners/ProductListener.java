package com.example.bakery_pos_admin_app.Utilities.Listeners;

import com.example.bakery_pos_admin_app.Models.Product;

public interface ProductListener {
    void onInfoClick(Product product);
    void productControl(String title, Product product);
    void productDeleteControl(Product product);
    void addProduct(Product product);
    void editProduct(Product original_product, Product updated_product);
    void deleteProduct(Product product);
}
