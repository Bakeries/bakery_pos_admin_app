package com.example.bakery_pos_admin_app.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.bakery_pos_admin_app.Adapters.ProductsAdapter;
import com.example.bakery_pos_admin_app.Models.Product;
import com.example.bakery_pos_admin_app.R;
import com.example.bakery_pos_admin_app.Utilities.APIs.ProductAPI;
import com.example.bakery_pos_admin_app.Utilities.Constants;
import com.example.bakery_pos_admin_app.Utilities.Listeners.ProductListener;
import com.example.bakery_pos_admin_app.Utilities.Services.RetrofitService;
import com.example.bakery_pos_admin_app.databinding.DeleteContainerBinding;
import com.example.bakery_pos_admin_app.databinding.FragmentProductsBinding;
import com.example.bakery_pos_admin_app.databinding.PopupInfoProductContainerBinding;
import com.example.bakery_pos_admin_app.databinding.PopupProductControlsContainerBinding;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsFragment extends Fragment implements ProductListener {

    private FragmentProductsBinding binding;
    private Dialog general;
    private ProductAPI productAPI;

    private List<Product> products = null;
    private List<Product> breads = null;
    private List<Product> salty = null;
    private List<Product> sweets = null;
    private List<Product> drinks = null;

    public ProductsFragment() {}

    public ProductsFragment(List<Product> products) {
        this.products = products;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = FragmentProductsBinding.inflate(inflater, container, false);
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _showProgress(true);
        _showError(false);

        if (products == null) {
            _showError(true);
            return;
        }

        this.productAPI = new RetrofitService().getRetrofit().create(ProductAPI.class);
        _initLists();
        _setListeners();
        _initAdapter(products);
    }

    private void _initLists() {
        for (String category : Constants.KEY_PRODUCTS_LIST){
            List<Product> categoryList = products.stream().filter(product -> category.equals(product.getCategory())).collect(Collectors.toList());

            switch (category){
                case Constants.KEY_PRODUCTS_BREAD:
                    this.breads = categoryList;
                    break;
                case Constants.KEY_PRODUCTS_DRINK:
                    this.drinks = categoryList;
                    break;
                case Constants.KEY_PRODUCTS_SWEET:
                    this.sweets = categoryList;
                    break;
                case Constants.KEY_PRODUCTS_SALTY:
                    this.salty = categoryList;
                    break;
            }
        }
    }

    private void _setListeners() {
        binding.categories.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case Constants.KEY_BREAD_NAV_BAR:
                    _initAdapter(breads);
                    break;
                case Constants.KEY_SALTY_NAV_BAR:
                    _initAdapter(salty);
                    break;
                case Constants.KEY_SWEET_NAV_BAR:
                    _initAdapter(sweets);
                    break;
                case Constants.KEY_DRINK_NAV_BAR:
                    _initAdapter(drinks);
                    break;
                default:
                    _initAdapter(products);
                    break;
            }
            return true;
        });
        binding.addHeaderLayout.setOnClickListener(unused -> productControl("Add Product", null));
        binding.searchBtn.setOnClickListener(unused -> searchProducts());
        binding.clearBtn.setOnClickListener(unused -> binding.searchBar.getText().clear());
    }

    private void _initAdapter(List<Product> productList) {
        if (productList == null || productList.size() == 0) {
            _showError(true);
            return;
        }

        _showProgress(false);
        ProductsAdapter adapter = new ProductsAdapter(productList, this);
        adapter.notifyItemRangeChanged(0, productList.size());
        binding.productsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), Constants.SPAN_COUNT));
        binding.productsRecyclerView.setAdapter(adapter);
    }

    private void _showError(boolean showError) {

        if (showError) {
            binding.error.setText(Constants.KEY_PRODUCT_ERROR_MESSAGE_NULL);
            binding.error.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
            binding.productsRecyclerView.setVisibility(View.GONE);
            return;
        }

        binding.error.setVisibility(View.GONE);
    }

    private void _showProgress(boolean showBar) {

        _showError(false);
        if (showBar) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.productsRecyclerView.setVisibility(View.GONE);
            return;
        }

        binding.progressBar.setVisibility(View.GONE);
        binding.productsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void searchProducts() {
        String product_search = binding.searchBar.getText().toString();
        if (product_search.isEmpty()) return;
        binding.categories.setSelectedItemId(Constants.KEY_ALL_PRODUCTS_NAV_BAR);

        List<Product> searched = products.stream().filter(product -> product.getName().toLowerCase().contains(product_search.toLowerCase())).collect(Collectors.toList());
        if (searched.size() == 0)
            searched = products.stream().filter(product -> product.getCategory().toLowerCase().contains(product_search.toLowerCase())).collect(Collectors.toList());

        _initAdapter(searched);
    }

    private Product checkInputsAndReturnProduct(PopupProductControlsContainerBinding popupBinding) {
        String _productBarcode = popupBinding.productBarcode.getText().toString();
        String productName = popupBinding.productName.getText().toString();
        String productDesc = popupBinding.productDescription.getText().toString();
        String _productVegan = popupBinding.veganTagMenu.getSelectedItem().toString();
        String productCategory = popupBinding.categoryMenu.getSelectedItem().toString();
        String _productPrice = popupBinding.productPrice.getText().toString();

        if (_productBarcode.length() == 0) {
            popupBinding.productBarcode.setError("Product's barcode must not be empty.");
            popupBinding.productBarcode.requestFocus();
            return null;
        }

        if (productName.length() == 0) {
            popupBinding.productName.setError("Product's name must not be empty.");
            popupBinding.productName.requestFocus();
            return null;
        }

        if (productDesc.length() == 0) {
            popupBinding.productDescription.setError("Product's description must not be empty.");
            popupBinding.productDescription.requestFocus();
            return null;
        }

        if (_productPrice.length() == 0 || _productPrice.equals("0.00")) {
            popupBinding.productPrice.setError("Product's price must not be empty.");
            popupBinding.productPrice.requestFocus();
            return null;
        }

        long productBarcode = Long.parseLong(_productBarcode);
        boolean productIsVegan = _productVegan.equals("Yes");
        double productPrice = Double.parseDouble(_productPrice);

        return new Product(productBarcode, productName, productDesc, productCategory, productIsVegan, productPrice);
    }

    @Override
    public void productControl(String title, Product product) {
        PopupProductControlsContainerBinding popupBinding = PopupProductControlsContainerBinding.inflate(getLayoutInflater());
        general = new Dialog(getActivity());

        popupBinding.productTitle.setText(title);

        ArrayAdapter<CharSequence> veganAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.veganTags, android.R.layout.simple_spinner_item);
        veganAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        popupBinding.veganTagMenu.setAdapter(veganAdapter);
        popupBinding.veganTagMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String veganSelection = adapterView.getItemAtPosition(i).toString();
                popupBinding.veganSelection.setText(veganSelection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> categoryAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Constants.KEY_PRODUCTS_LIST);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        popupBinding.categoryMenu.setAdapter(categoryAdapter);
        popupBinding.categoryMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String categorySelection = adapterView.getItemAtPosition(i).toString();
                popupBinding.categorySelection.setText(categorySelection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (title.equals("Add Product") && product == null){
            String btn_title = "ADD";
            popupBinding.saveProductBtn.setText(btn_title);
            popupBinding.saveProductBtn.setOnClickListener(unused -> addProduct(checkInputsAndReturnProduct(popupBinding)));
        }
        else if (title.equals("Edit Product") && product != null) {
            popupBinding.productBarcode.setText(String.valueOf(product.getBarcode()));
            popupBinding.productName.setText(product.getName());
            popupBinding.productDescription.setText(product.getDescription());

            popupBinding.veganTagMenu.setSelection(product.isVegan()? 0: 1);
            popupBinding.categoryMenu.setSelection(
                    Constants.KEY_PRODUCTS_LIST[0].equals(product.getCategory()) ?
                            0: Constants.KEY_PRODUCTS_LIST[1].equals(product.getCategory())?
                            1: Constants.KEY_PRODUCTS_LIST[2].equals(product.getCategory())?
                            2: Constants.KEY_PRODUCTS_LIST[3].equals(product.getCategory())?
                            3: Constants.KEY_PRODUCTS_LIST[4].equals(product.getCategory())?
                            4: 0);
            popupBinding.productPrice.setText(String.valueOf(product.getPrice()));

            popupBinding.saveProductBtn.setOnClickListener(unused -> editProduct(product, checkInputsAndReturnProduct(popupBinding)));
        }

        general.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        general.setContentView(popupBinding.getRoot());
        general.show();
    }

    @Override
    public void productDeleteControl(Product product) {
        DeleteContainerBinding popupBinding = DeleteContainerBinding.inflate(getLayoutInflater());

        Dialog deletePopUp = new Dialog(getActivity());

        popupBinding.noBtn.setOnClickListener(unused -> deletePopUp.dismiss());
        popupBinding.yesBtn.setOnClickListener(unused -> {
            deletePopUp.dismiss();
            deleteProduct(product);
        });

        deletePopUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deletePopUp.setContentView(popupBinding.getRoot());
        deletePopUp.show();
    }

    @Override
    public void onInfoClick(Product product) {
        PopupInfoProductContainerBinding popupBinding = PopupInfoProductContainerBinding.inflate(getLayoutInflater());
        String notVegan = "NOT VEGAN";
        String isVegan = "VEGAN";

        popupBinding.productNameLabel.setText(product.getName());
        popupBinding.productDescriptionLabel.setText(product.getDescription());
        if (product.isVegan())
            popupBinding.productVeganTag.setText(isVegan);
        else
            popupBinding.productVeganTag.setText(notVegan);

        String price = String.valueOf(BigDecimal.valueOf(product.getPrice()).setScale(3, RoundingMode.CEILING));
        popupBinding.productPriceLabel.setText(price);

        Dialog info = new Dialog(getActivity());
        info.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        info.setContentView(popupBinding.getRoot());
        info.show();
    }

    @Override
    public void addProduct(Product product) {

        if (product == null) return;

        general.dismiss();
        productAPI.addNewProduct(product)
                .enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                        if (response.body() == null){
                            Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(getContext(), "Product added.", Toast.LENGTH_SHORT).show();
                        products.add(product);
                        products.sort(Comparator.comparing(Product::getName));
                        _initLists();
                        _initAdapter(products);
                    }

                    @Override
                    public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void editProduct(Product original, Product updated_product) {

        if (updated_product == null) return;

        general.dismiss();
        productAPI.editProduct(updated_product)
                .enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                        if (response.body() == null){
                            Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(getContext(), "Product updated.", Toast.LENGTH_SHORT).show();
                        products.set(products.indexOf(original), updated_product);
                        products.sort(Comparator.comparing(Product::getName));
                        _initLists();
                        _initAdapter(products);
                    }

                    @Override
                    public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void deleteProduct(Product product) {
        productAPI.deleteSpecificProduct(product.getBarcode())
                .enqueue(new Callback<Long>() {
                    @Override
                    public void onResponse(@NonNull Call<Long> call, @NonNull Response<Long> response) {
                        if (response.body() == null){
                            Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(getContext(), "Product " + product.getBarcode() + " deleted.", Toast.LENGTH_SHORT).show();
                        products.remove(product);
                        _initLists();
                        _initAdapter(products);
                    }

                    @Override
                    public void onFailure(@NonNull Call<Long> call, @NonNull Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}