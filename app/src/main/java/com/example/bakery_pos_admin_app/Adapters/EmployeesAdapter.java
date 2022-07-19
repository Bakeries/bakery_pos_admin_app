package com.example.bakery_pos_admin_app.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakery_pos_admin_app.Models.Employee;
import com.example.bakery_pos_admin_app.Utilities.Listeners.EmployeeListener;
import com.example.bakery_pos_admin_app.databinding.EmployeeListContainerBinding;

import java.util.List;

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.EmployeesViewHolder> {

    private final List<Employee> employees;
    private final EmployeeListener listener;

    public EmployeesAdapter(List<Employee> employees, EmployeeListener listener) {
        this.employees = employees;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EmployeesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmployeesViewHolder(EmployeeListContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeesViewHolder holder, int position) {
        holder.setData(employees.get(position));
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    protected class EmployeesViewHolder extends RecyclerView.ViewHolder {

        private final EmployeeListContainerBinding binding;

        protected EmployeesViewHolder(EmployeeListContainerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        protected void setData(Employee employee) {
            binding.employeeID.setText(String.valueOf(employee.getEmployeeID()));
            binding.employeeName.setText(employee.getEmployeeName());
            binding.editEmployeeBtn.setOnClickListener(unused -> listener.employeeControls("Edit Employee", employee));
            binding.deleteEmployeeBtn.setOnClickListener(unused -> listener.employeeDeleteControl(employee));
        }
    }
}
