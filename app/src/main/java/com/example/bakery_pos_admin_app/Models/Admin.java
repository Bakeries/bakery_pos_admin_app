package com.example.bakery_pos_admin_app.Models;

import java.io.Serializable;

public class Admin implements Serializable {
    private long adminID;
    private String adminName, adminPassword = null;

    public Admin() {}


    public Admin(long adminID, String adminPassword) {
        this.adminID = adminID;
        this.adminPassword = adminPassword;
    }

    public Admin(long adminID, String adminName, String adminPassword) {
        this.adminID = adminID;
        this.adminName = adminName;
        this.adminPassword = adminPassword;
    }

    public long getAdminID() {return adminID;}
    public String getAdminName() {return adminName;}
    public String getAdminPassword() {return adminPassword;}
}
