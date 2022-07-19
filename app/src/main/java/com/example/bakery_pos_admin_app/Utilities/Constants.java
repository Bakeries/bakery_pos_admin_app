package com.example.bakery_pos_admin_app.Utilities;

import com.example.bakery_pos_admin_app.R;

public class Constants {
    // Key paths for api.
    // Product API paths
    public final static String API_PATH_PRODUCTS = "/api/v1/products";
    public final static String API_PATH_PRODUCTS_NAME = API_PATH_PRODUCTS + "/name";
    public final static String API_PATH_PRODUCTS_CATEGORY = API_PATH_PRODUCTS + "/category/{category}";
    public final static String API_PATH_PRODUCTS_DELETE = API_PATH_PRODUCTS + "/delete/{product_id}";
    public static final String API_PATH_PRODUCTS_ADD = API_PATH_PRODUCTS + "/new";
    public static final String API_PATH_PRODUCTS_EDIT = API_PATH_PRODUCTS + "/edit";
    // Employee API paths
    public final static String API_PATH_EMPLOYEES = "/api/v1/employees";
    public final static String API_PATH_EMPLOYEES_NEW = API_PATH_EMPLOYEES+ "/new";
    public final static String API_PATH_EMPLOYEES_EDIT = API_PATH_EMPLOYEES+ "/edit";
    public final static String API_PATH_EMPLOYEES_DELETE = API_PATH_EMPLOYEES+ "/delete/{employee_id}";
    // Admin API paths
    public final static String API_PATH_ADMIN_LOGIN = "/api/v1/admins/login";

    // Basic app keys.
    public final static String KEY_PRODUCT_ERROR_MESSAGE_NULL = "Unable to find products";
    public final static String KEY_EMPLOYEE_ERROR_MESSAGE_NULL = "Unable to find employees";
    public final static String KEY_PREFERENCES_NAME = "posPreferences";
    public final static String KEY_PRODUCTS_BREAD = "bread";
    public final static String KEY_PRODUCTS_DRINK = "drink";
    public final static String KEY_PRODUCTS_SWEET = "sweet";
    public final static String KEY_PRODUCTS_SALTY = "salty";
    public final static String KEY_ADMIN = "admin";
    public static final String KEY_ADMIN_ID = "adminID";
    public final static String KEY_ADMIN_NAME = "adminName";

    // Basic app ids.
    public final static int KEY_FRAGMENT_CONTAINER = R.id.fragmentContainer;
    public final static int KEY_PRODUCTS_FRAGMENT = R.id.products;
    public final static int KEY_EMPLOYEES_FRAGMENT = R.id.employees;
    public final static int KEY_ALL_PRODUCTS_NAV_BAR = R.id.allProducts;
    public final static int KEY_BREAD_NAV_BAR = R.id.breads;
    public final static int KEY_SALTY_NAV_BAR = R.id.salty;
    public final static int KEY_SWEET_NAV_BAR = R.id.sweets;
    public final static int KEY_DRINK_NAV_BAR = R.id.drinks;
    public static final String[] KEY_PRODUCTS_LIST = {"bread", "drink", "sweet", "salty"};

    // Keys for layout
    public final static int SPAN_COUNT = 5;
}
