package com.crazydevstuff.infoshare.Models;

public class RegisterModel {
    private String name;
    private String email;
    private String phoneNumber;
    private String prof_pic;
    public RegisterModel(){

    }

    public RegisterModel(String name, String email,String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber=phoneNumber;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProf_pic() {
        return prof_pic;
    }

    public void setProf_pic(String prof_pic) {
        this.prof_pic = prof_pic;
    }

}
