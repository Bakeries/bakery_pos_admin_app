package com.example.bakery_pos_admin_app.Utilities.Listeners;

import com.example.bakery_pos_admin_app.Models.Employee;

public interface EmployeeListener {
    void employeeControls(String title, Employee employee);
    void employeeDeleteControl(Employee employee);
    void addEmployee(Employee employee);
    void editEmployee(Employee original, Employee updated_employee);
    void deleteEmployee(Employee employee);
}
