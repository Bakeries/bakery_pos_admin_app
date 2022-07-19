package com.example.bakery_pos_admin_app.Utilities.APIs;

import com.example.bakery_pos_admin_app.Models.Admin;
import com.example.bakery_pos_admin_app.Utilities.Constants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AdminAPI {

    @POST(Constants.API_PATH_ADMIN_LOGIN)
    Call<Admin> loginAdmin(@Body Admin admin);
}
