package com.example.bakery_pos_admin_app.Models;

import java.io.Serializable;

public class Employee implements Serializable {

    private long employeeID; // Will be the ID number of passport or id.

    private String employeeName;
    private String password;


    public Employee() {}

    public Employee(String employeeName) {
        this.employeeName = employeeName;
    }

    public Employee(long employeeID) {
        this.employeeID = employeeID;
    }

    // Constructor for login
    public Employee(long employeeID, String password){
        this.employeeID = employeeID;
        this.password = password;
    }

    // Create employee
    public Employee(long employeeID, String employeeName, String password) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.password = password;
    }

    // Getters
    public long getEmployeeID() {return employeeID;}
    public String getEmployeeName() {return employeeName;}
    public String getPassword() {return password;}
}
