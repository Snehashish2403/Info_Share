package com.crazydevstuff.infoshare.Models;

public class RegisterModel {
    public String name;
    public String email;
    public RegisterModel(){

    }
    public RegisterModel(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
