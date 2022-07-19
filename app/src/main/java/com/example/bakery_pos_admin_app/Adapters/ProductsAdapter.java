package com.example.bakery_pos_admin_app.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakery_pos_admin_app.Models.Product;
import com.example.bakery_pos_admin_app.Utilities.Listeners.ProductListener;
import com.example.bakery_pos_admin_app.databinding.ProductListContainerBinding;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {

    private final List<Product> products;
    private final ProductListener listener;

    public ProductsAdapter(List<Product> products, ProductListener listener) {
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductsViewHolder(ProductListContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        holder.setData(this.products.get(position));
    }

    @Override
    public int getItemCount() {
        return this.products.size();
    }

    protected class ProductsViewHolder extends RecyclerView.ViewHolder {
        private final ProductListContainerBinding binding;

        public ProductsViewHolder(ProductListContainerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        protected void setData(Product product) {
            binding.productName.setText(product.getName());
            binding.productVeganTag.setText(product.isVegan()? "VEGAN" : "NOT VEGAN");

            String price = String.valueOf(BigDecimal.valueOf(product.getPrice()).setScale(2, RoundingMode.CEILING));
            binding.productPriceLabel.setText(price);

            binding.productInfo.setOnClickListener(unused -> listener.onInfoClick(product));
            binding.editProductBtn.setOnClickListener(unused -> listener.productControl("Edit Product", product));
            binding.deleteProductBtn.setOnClickListener(unused -> listener.productDeleteControl(product));
        }
    }
}
