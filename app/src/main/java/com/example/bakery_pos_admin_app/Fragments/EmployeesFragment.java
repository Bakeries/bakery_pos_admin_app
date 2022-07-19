package com.example.bakery_pos_admin_app.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bakery_pos_admin_app.Adapters.EmployeesAdapter;
import com.example.bakery_pos_admin_app.Models.Employee;
import com.example.bakery_pos_admin_app.Utilities.APIs.EmployeeAPI;
import com.example.bakery_pos_admin_app.Utilities.Constants;
import com.example.bakery_pos_admin_app.Utilities.Listeners.EmployeeListener;
import com.example.bakery_pos_admin_app.Utilities.Services.RetrofitService;
import com.example.bakery_pos_admin_app.databinding.DeleteContainerBinding;
import com.example.bakery_pos_admin_app.databinding.FragmentEmployeesBinding;
import com.example.bakery_pos_admin_app.databinding.PopupEmployeeControlsContainerBinding;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeesFragment extends Fragment implements EmployeeListener {
    private FragmentEmployeesBinding binding;
    private Dialog general;

    private EmployeeAPI employeeAPI;

    private List<Employee> employees;

    public EmployeesFragment() {}

    public EmployeesFragment(List<Employee> employees) {this.employees = employees;}

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = FragmentEmployeesBinding.inflate(inflater, container, false);
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _init();
        _setListeners();
        _initAdapter(employees);
    }

    private void _init() {
        this.employeeAPI = new RetrofitService().getRetrofit().create(EmployeeAPI.class);
    }

    private void _setListeners() {
        binding.searchBtn.setOnClickListener(unused -> searchEmployee());
        binding.addHeaderLayout.setOnClickListener(unused -> employeeControls("Add Employee", null));
        binding.clearBtn.setOnClickListener(unused -> binding.searchBar.getText().clear());
    }

    private void _initAdapter(List<Employee> employeeList) {
        _showProgress(true);
        if (employees == null || employees.size() == 0) {
            _showError(true);
            return;
        }

        _showProgress(false);
        EmployeesAdapter adapter = new EmployeesAdapter(employeeList, this);
        adapter.notifyItemRangeChanged(0, employeeList.size());
        binding.employeesRecyclerView.setAdapter(adapter);
    }
    private void _showError(boolean showError) {

        if (showError) {
            binding.error.setText(Constants.KEY_EMPLOYEE_ERROR_MESSAGE_NULL);
            binding.error.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
            binding.employeesContainer.setVisibility(View.GONE);
            return;
        }

        binding.error.setVisibility(View.GONE);
    }
    private void _showProgress(boolean showBar) {

        _showError(false);
        if (showBar) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.employeesContainer.setVisibility(View.GONE);
            return;
        }

        binding.progressBar.setVisibility(View.GONE);
        binding.employeesContainer.setVisibility(View.VISIBLE);
    }

    private void searchEmployee() {
        String employee_search = binding.searchBar.getText().toString();
        if (employee_search.isEmpty()) return;

        List<Employee> searched = employees.stream().filter(employee -> employee.getEmployeeName().toLowerCase().contains(employee_search.toLowerCase())).collect(Collectors.toList());
        if (searched.size() == 0 && employee_search.contains("0123456789"))
            searched = employees.stream().filter(employee -> employee.getEmployeeID() == Integer.parseInt(employee_search)).collect(Collectors.toList());

        _initAdapter(searched);
    }

    private Employee checkInputsAndReturnEmployee(PopupEmployeeControlsContainerBinding popupBinding) {
        String _employeeID = popupBinding.employeeID.getText().toString();
        String employeeName = popupBinding.employeeName.getText().toString();
        String employeePassword = popupBinding.employeePassword.getText().toString();

        if (_employeeID.isEmpty()) {
            popupBinding.employeeID.setError("Employee's ID must not be empty.");
            popupBinding.employeeID.requestFocus();
            return null;
        }

        if (employeeName.isEmpty()) {
            popupBinding.employeeName.setError("Employee's name must not be empty.");
            popupBinding.employeeName.requestFocus();
            return null;
        }

        if (popupBinding.employeePassword.getVisibility() == View.VISIBLE) {
            if (employeePassword.isEmpty()) {
                popupBinding.employeePassword.setError("Product's name must not be empty.");
                popupBinding.employeePassword.requestFocus();
                return null;
            }

            if (employeePassword.length() < 8) {
                popupBinding.employeePassword.setError("Employee's password must be at least 8 characters long.");
                popupBinding.employeePassword.requestFocus();
                return null;
            }
        }

        long employeeID = Long.parseLong(_employeeID);

        return new Employee(employeeID, employeeName, employeePassword);
    }

    @Override
    public void employeeControls(String title, Employee employee) {
        PopupEmployeeControlsContainerBinding popupBinding = PopupEmployeeControlsContainerBinding.inflate(getLayoutInflater());
        popupBinding.employeeTitle.setText(title);

        general = new Dialog(getActivity());

        if (title.equals("Add Employee") && employee == null) {
            String btn_title = "ADD";
            popupBinding.saveEmployeeBtn.setText(btn_title);
            popupBinding.saveEmployeeBtn.setOnClickListener(unused -> addEmployee(checkInputsAndReturnEmployee(popupBinding)));

        } else if (title.equals("Edit Employee") && employee != null) {
            popupBinding.employeeID.setText(String.valueOf(employee.getEmployeeID()));
            popupBinding.employeeName.setText(employee.getEmployeeName());
            popupBinding.employeePassword.setVisibility(View.GONE);
            popupBinding.saveEmployeeBtn.setOnClickListener(unused -> editEmployee(employee, checkInputsAndReturnEmployee(popupBinding)));
        }

        general.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        general.setContentView(popupBinding.getRoot());
        general.show();
    }

    @Override
    public void employeeDeleteControl(Employee employee) {
        DeleteContainerBinding popupBinding = DeleteContainerBinding.inflate(getLayoutInflater());

        Dialog deletePopUp = new Dialog(getActivity());

        popupBinding.noBtn.setOnClickListener(unused -> deletePopUp.dismiss());
        popupBinding.yesBtn.setOnClickListener(unused -> {
            deletePopUp.dismiss();
            deleteEmployee(employee);
        });

        deletePopUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deletePopUp.setContentView(popupBinding.getRoot());
        deletePopUp.show();
    }

    @Override
    public void addEmployee(Employee employee) {

        if (employee == null) return;

        general.dismiss();
        employeeAPI.addEmployee(employee)
                .enqueue(new Callback<Employee>() {
                    @Override
                    public void onResponse(@NonNull Call<Employee> call, @NonNull Response<Employee> response) {
                        if (response.body() == null){
                            Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(getContext(), "Employee added.", Toast.LENGTH_SHORT).show();
                        employees.add(employee);
                        employees.sort(Comparator.comparing(Employee::getEmployeeName));
                        _initAdapter(employees);
                    }

                    @Override
                    public void onFailure(@NonNull Call<Employee> call, @NonNull Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void editEmployee(Employee original, Employee updated_employee) {
        if (updated_employee == null) return;

        general.dismiss();
        employeeAPI.editEmployee(updated_employee)
                .enqueue(new Callback<Employee>() {
                    @Override
                    public void onResponse(@NonNull Call<Employee> call, @NonNull Response<Employee> response) {
                        if (response.body() == null){
                            Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(getContext(), "Employee updated.", Toast.LENGTH_SHORT).show();
                        employees.set(employees.indexOf(original), updated_employee);
                        employees.sort(Comparator.comparing(Employee::getEmployeeName));
                        _initAdapter(employees);
                    }

                    @Override
                    public void onFailure(@NonNull Call<Employee> call, @NonNull Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void deleteEmployee(Employee employee) {
        employeeAPI.deleteEmployee(employee.getEmployeeID())
                .enqueue(new Callback<Long>() {
                    @Override
                    public void onResponse(@NonNull Call<Long> call, @NonNull Response<Long> response) {
                        if (response.body() == null){
                            Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(getContext(), "Employee " + employee.getEmployeeID() + " deleted.", Toast.LENGTH_SHORT).show();
                        employees.remove(employee);
                        _initAdapter(employees);
                    }

                    @Override
                    public void onFailure(@NonNull Call<Long> call, @NonNull Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}