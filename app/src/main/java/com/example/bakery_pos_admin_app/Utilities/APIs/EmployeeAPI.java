package com.example.bakery_pos_admin_app.Utilities.APIs;

import com.example.bakery_pos_admin_app.Models.Employee;
import com.example.bakery_pos_admin_app.Utilities.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EmployeeAPI {

    @GET(Constants.API_PATH_EMPLOYEES)
    Call<List<Employee>> getEmployees();

    @POST(Constants.API_PATH_EMPLOYEES_NEW)
    Call<Employee> addEmployee(@Body Employee employee);

    @PUT(Constants.API_PATH_EMPLOYEES_EDIT)
    Call<Employee> editEmployee(@Body Employee updated_employee);

    @DELETE(Constants.API_PATH_EMPLOYEES_DELETE)
    Call<Long> deleteEmployee(@Path("employee_id") long employee_id);
}
