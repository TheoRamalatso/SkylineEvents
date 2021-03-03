package com.example.skylinestockservice.Model;

public class UserDef {
    private String name;
    private String contact;
    private String image;
    private String security;
    private String address;
    private String email;
    public UserDef(){

    }
    public UserDef(String name, String contact, String image, String security, String address, String email) {
        this.name = name;
        this.contact = contact;
        this.image = image;
        this.security = security;
        this.address = address;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}