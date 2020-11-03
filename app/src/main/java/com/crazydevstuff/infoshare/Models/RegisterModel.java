package com.crazydevstuff.infoshare.Models;

public class RegisterModel {
    String name;
    String email;
    public RegisterModel(){

    }
    public RegisterModel(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
