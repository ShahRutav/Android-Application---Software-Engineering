package com.example.weddingplanner.Models;

import java.util.Arrays;
import java.util.List;

public class Users {
    private String email,name, password,address;
    public Users()
    {

    }
    public Users(String email, String name, String password, String address, String list) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
