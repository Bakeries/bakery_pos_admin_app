package com.example.bakery_pos_admin_app.Utilities.APIs;

import com.example.bakery_pos_admin_app.Models.Product;
import com.example.bakery_pos_admin_app.Utilities.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProductAPI {

    @GET(Constants.API_PATH_PRODUCTS)       // Path to all products.
    Call<List<Product>> getAllProducts();   // Getting all products.

    @GET(Constants.API_PATH_PRODUCTS_CATEGORY)
    Call<List<Product>> getCategory(@Path("category") String category);

    @GET(Constants.API_PATH_PRODUCTS_NAME)                            // Path to specific products.
    Call<List<Product>> getSpecificProducts(@Body Product product);   // Getting specific products.

    @POST(Constants.API_PATH_PRODUCTS_ADD)
    Call<Product> addNewProduct(@Body Product product);

    @PUT(Constants.API_PATH_PRODUCTS_EDIT)
    Call<Product> editProduct(@Body Product product);

    @DELETE(Constants.API_PATH_PRODUCTS_DELETE)
    Call<Long> deleteSpecificProduct(@Path("product_id") long product_id);

}
