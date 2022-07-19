package com.example.bakery_pos_admin_app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.bakery_pos_admin_app.Models.Admin;
import com.example.bakery_pos_admin_app.Utilities.APIs.AdminAPI;
import com.example.bakery_pos_admin_app.Utilities.Constants;
import com.example.bakery_pos_admin_app.Utilities.Managers.PreferenceManager;
import com.example.bakery_pos_admin_app.Utilities.Services.RetrofitService;
import com.example.bakery_pos_admin_app.databinding.ActivityAdminLoginBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminLoginActivity extends AppCompatActivity {

    private ActivityAdminLoginBinding binding;
    private PreferenceManager manager;
    private AdminAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _init();
        _setListeners();
        _checkForLoggedInAdmin();

        setContentView(this.binding.getRoot());
    }

    private void _init() {
        this.binding = ActivityAdminLoginBinding.inflate(getLayoutInflater());
        this.manager = new PreferenceManager(getApplicationContext());
        this.api = new RetrofitService().getRetrofit().create(AdminAPI.class);
    }

    private void _setListeners() {
        binding.loginBtn.setOnClickListener(unused -> _checkInputs());
    }

    private void _checkForLoggedInAdmin() {
        String adminID = manager.getString(Constants.KEY_ADMIN_ID);
        String adminName = manager.getString(Constants.KEY_ADMIN_NAME);

        if (adminID == null || adminName == null || adminID.isEmpty() || adminName.isEmpty()) return;
        _openMainActivity(new Admin(Long.parseLong(adminID), adminName));
    }

    private void _checkInputs() {
        _showProgressBar(true);

        String adminID = binding.adminID.getText().toString();
        String adminPassword = binding.adminPassword.getText().toString();

        if (adminID.isEmpty() || adminPassword.isEmpty()) _showProgressBar(false);

        if (adminID.isEmpty()) {
            binding.adminID.setError("Must not be empty!");
            binding.adminID.requestFocus();
            return;
        }
        
        if (adminPassword.isEmpty()) {
            binding.adminPassword.setError("Must not be empty!");
            binding.adminPassword.requestFocus();
            return;
        }

        api.loginAdmin(new Admin(Long.parseLong(adminID), adminPassword))
                .enqueue(new Callback<Admin>() {
                    @Override
                    public void onResponse(@NonNull Call<Admin> call, @NonNull Response<Admin> response) {
                        if (response.body() == null){
                            Toast.makeText(getApplicationContext(), "Invalid credentials.", Toast.LENGTH_SHORT).show();
                            _showProgressBar(false);
                            return;
                        }
                        _openMainActivity(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<Admin> call, @NonNull Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        _showProgressBar(false);
                    }
                });
    }

    private void _showProgressBar(boolean showBar) {
        // Method that enables/disables progress bar.
        if (showBar){
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.loginBtn.setVisibility(View.GONE);
            return;
        }

        binding.progressBar.setVisibility(View.GONE);
        binding.loginBtn.setVisibility(View.VISIBLE);
    }

    private void _openMainActivity(Admin admin) {

        manager.putString(Constants.KEY_ADMIN_ID, String.valueOf(admin.getAdminID()));
        manager.putString(Constants.KEY_ADMIN_NAME, admin.getAdminName());

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.KEY_ADMIN, admin);
        startActivity(intent);
        finish();
    }
}