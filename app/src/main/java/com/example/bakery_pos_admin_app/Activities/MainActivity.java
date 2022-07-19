package com.example.bakery_pos_admin_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.bakery_pos_admin_app.Fragments.EmployeesFragment;
import com.example.bakery_pos_admin_app.Fragments.ProductsFragment;
import com.example.bakery_pos_admin_app.Models.Employee;
import com.example.bakery_pos_admin_app.Models.Product;
import com.example.bakery_pos_admin_app.Utilities.APIs.EmployeeAPI;
import com.example.bakery_pos_admin_app.Utilities.APIs.ProductAPI;
import com.example.bakery_pos_admin_app.Utilities.Constants;
import com.example.bakery_pos_admin_app.Utilities.Managers.PreferenceManager;
import com.example.bakery_pos_admin_app.Utilities.Services.RetrofitService;
import com.example.bakery_pos_admin_app.databinding.ActivityMainBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private PreferenceManager manager;
    private ProductAPI productAPI;
    private EmployeeAPI employeeAPI;

    private ProductsFragment productsFragment;
    private EmployeesFragment employeesFragment;

    private List<Product> products = null;
    private List<Employee> employees = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _init();
        _loadLists();
        _setListeners();
        setContentView(binding.getRoot());
    }

    private void _init() {
        this.binding = ActivityMainBinding.inflate(getLayoutInflater());
        this.manager = new PreferenceManager(getApplicationContext());

        RetrofitService retrofitService = new RetrofitService();
        this.productAPI = retrofitService.getRetrofit().create(ProductAPI.class);
        this.employeeAPI = retrofitService.getRetrofit().create(EmployeeAPI.class);
    }

    private void _setListeners() {
        binding.logout.setOnClickListener(unused -> {
            manager.clear();
            startActivity(new Intent(this, AdminLoginActivity.class));
            finish();
        });
        binding.bottomNavBar.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case Constants.KEY_PRODUCTS_FRAGMENT:
                    _setFragments(productsFragment);
                    break;
                case Constants.KEY_EMPLOYEES_FRAGMENT:
                    _setFragments(employeesFragment);
                    break;
            }
            return true;
        });
    }

    private void _setFragments(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(Constants.KEY_FRAGMENT_CONTAINER, fragment).commit();
    }

    private void _showProgressBar(boolean showBar) {
        if (showBar) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.fragmentContainer.setVisibility(View.GONE);
            return;
        }

        binding.progressBar.setVisibility(View.GONE);
        binding.fragmentContainer.setVisibility(View.VISIBLE);
    }

    private void _loadLists() {
        _showProgressBar(true);
        productAPI.getAllProducts()
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                        if (response.body() == null) {
                            Toast.makeText(getApplicationContext(), "Unable to load products", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        products = response.body();
                        productsFragment = new ProductsFragment(products);
                        _setFragments(productsFragment);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        employeeAPI.getEmployees()
                .enqueue(new Callback<List<Employee>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Employee>> call, @NonNull Response<List<Employee>> response) {
                        if (response.body() == null) {
                            Toast.makeText(getApplicationContext(), "Unable to load employees", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        employees = response.body();
                        employeesFragment = new EmployeesFragment(employees);

                        _showProgressBar(false);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Employee>> call, @NonNull Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}